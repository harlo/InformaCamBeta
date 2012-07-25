package org.witness.informacam.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.http.client.ClientProtocolException;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.witness.informacam.storage.DatabaseHelper;
import org.witness.informacam.transport.HttpUtility;
import org.witness.informacam.utils.AddressBookUtil.AddressBookDisplay;
import org.witness.informacam.utils.Constants.AddressBook;
import org.witness.informacam.utils.Constants.Crypto;
import org.witness.informacam.utils.Constants.Settings;
import org.witness.informacam.utils.Constants.TrustedDestination;
import org.witness.informacam.utils.Constants.Crypto.PGP;
import org.witness.informacam.utils.Constants.Settings.Device;
import org.witness.informacam.utils.Constants.Storage.Tables;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.Base64;
import android.util.Log;

public class KeyUtility {
	public interface KeyFoundListener {
		public void onKeyFound(KeyServerResponse keyServerResponse);
	}
	
	public final static class KeyServerResponse extends JSONObject {
		public KeyServerResponse(PGPPublicKey key, String displayName, String email) {
			if(email == null) {
				Log.d(Crypto.LOG, "no display name or email");
				Iterator<String> uIt = key.getUserIDs();
				while(uIt.hasNext()) {
					String[] id = uIt.next().split("<");
					displayName = id[0].substring(0, id[0].indexOf(" ("));
					email = id[1].substring(0, id[1].length() - 1);
				}
			}
			
			try {
				this.put(PGP.Keys.PGP_DISPLAY_NAME, displayName);
				this.put(PGP.Keys.PGP_EMAIL_ADDRESS, email);
				this.put(PGP.Keys.PGP_CREATION_DATE, key.getCreationTime().getTime());
				this.put(PGP.Keys.PGP_EXPIRY_DATE, key.getCreationTime().getTime() + key.getValidSeconds());
				this.put(PGP.Keys.PGP_FINGERPRINT, new String(Hex.encode(key.getFingerprint())));
				this.put(PGP.Keys.PGP_KEY_ID, key.getKeyID());
				this.put(Crypto.Keyring.Keys.PUBLIC_KEY, new String(key.getEncoded()));
			} catch (JSONException e) {}
			catch (IOException e) {}
		}
		
		public KeyServerResponse(PGPPublicKey key) {
			this(key, null, null);
		}
	}
	
	private static String parseKeyserverQuery(String rawdata) {
		String keyblock = null;
		String bodyContents = rawdata.substring(rawdata.indexOf("<body>"), rawdata.indexOf("</html>"));
		
		if(bodyContents.contains(PGP.beginKeyBlock[0])) {
			keyblock = bodyContents.substring(bodyContents.indexOf(PGP.beginKeyBlock[0]), bodyContents.indexOf(PGP.endKeyBlock[0]) + PGP.endKeyBlock[0].length());
		}
		return keyblock;
	}
	
	public static void queryKeyserverByEmail(Activity c, String displayName, String email) {
		try {
			String keyblock = parseKeyserverQuery(HttpUtility.executeHttpGet(PGP.keyserverUrl[0] + email));
			if(keyblock != null) {
				PGPPublicKey key = extractPublicKeyFromBytes(keyblock.getBytes());
				KeyServerResponse keyServerResponse = new KeyServerResponse(key, displayName, email);
				((KeyFoundListener) c).onKeyFound(keyServerResponse);
			}
			
			
		} catch (ClientProtocolException e) {}
		catch (URISyntaxException e) {}
		catch (IOException e) {}
		catch (InterruptedException e) {}
		catch (ExecutionException e) {}
		catch (PGPException e) {}
	}
	
	@SuppressWarnings("unchecked")
	public static PGPPublicKey extractPublicKeyFromBytes(byte[] keyBlock) throws IOException, PGPException {
		PGPPublicKeyRingCollection keyringCol = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(new ByteArrayInputStream(keyBlock)));
		PGPPublicKey key = null;
		Iterator<PGPPublicKeyRing> rIt = keyringCol.getKeyRings();
		while(key == null && rIt.hasNext()) {
			PGPPublicKeyRing keyring = (PGPPublicKeyRing) rIt.next();
			Iterator<PGPPublicKey> kIt = keyring.getPublicKeys();
			while(key == null && kIt.hasNext()) {
				PGPPublicKey k = (PGPPublicKey) kIt.next();
				if(k.isEncryptionKey())
					key = k;
			}
		}
		if(key == null)
			throw new IllegalArgumentException("there isn't an encryption key here.");
		
		return key;
	}
	
	public static boolean installNewKey(Activity c, KeyServerResponse ksr, AddressBookDisplay abd) {
		ContentValues key = new ContentValues();
		Iterator<String> kIt = ksr.keys();
		while(kIt.hasNext()) {
			String k = kIt.next();
			try {
				key.put(k, ksr.getString(k));
			} catch(ClassCastException e) {
				try {
					key.put(k, ksr.getLong(k));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		DatabaseHelper dh = new DatabaseHelper(c);
		SQLiteDatabase db = dh.getWritableDatabase(PreferenceManager.getDefaultSharedPreferences(c).getString(Settings.Keys.CURRENT_LOGIN, ""));
		
		dh.setTable(db, Tables.Keys.KEYRING);
		db.insert(dh.getTable(), null, key);
		
		if(abd != null) {
			dh.setTable(db, Tables.Keys.TRUSTED_DESTINATIONS);
			ContentValues td = new ContentValues();
			try {
				td.put(TrustedDestination.Keys.DISPLAY_NAME, key.getAsString(PGP.Keys.PGP_DISPLAY_NAME));
				td.put(TrustedDestination.Keys.EMAIL, key.getAsString(PGP.Keys.PGP_EMAIL_ADDRESS));
				td.put(TrustedDestination.Keys.KEYRING_ID, key.getAsLong(PGP.Keys.PGP_KEY_ID));
				td.put(TrustedDestination.Keys.CONTACT_PHOTO, abd.getString(AddressBook.Keys.CONTACT_PHOTO));
			
				Log.d(Crypto.LOG, td.toString());
				db.insert(dh.getTable(), null, td);
			} catch(JSONException e){}
		}
		
		db.close();
		dh.close();
		return true;
	}
	
	private static String generatePassword(byte[] baseImage) throws NoSuchAlgorithmException {
		// initialize random bytes
		byte[] randomBytes = new byte[baseImage.length];
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.nextBytes(randomBytes);
		
		// xor by baseImage
		byte[] product = new byte[baseImage.length];
		for(int b = 0; b < baseImage.length; b++) {
			product[b] = (byte) (baseImage[b] ^ randomBytes[b]);
		}
		
		// digest to SHA1 string, voila password.
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		return Base64.encodeToString(md.digest(product), Base64.DEFAULT);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean generateNewKeyFromImage(Activity c) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
		DatabaseHelper dh = new DatabaseHelper(c);
		SQLiteDatabase db = dh.getWritableDatabase(sp.getString(Settings.Keys.CURRENT_LOGIN, ""));
		
		byte[] baseImage;
		
		dh.setTable(db, Tables.Keys.SETUP);
		Cursor b = dh.getValue(db, new String[] {Device.Keys.BASE_IMAGE}, BaseColumns._ID, 1);
		if(b != null && b.moveToFirst()) {
			baseImage = b.getBlob(0);
			b.close();
			
			Security.addProvider(new BouncyCastleProvider());
			KeyPairGenerator kpg;
			
			try {
				String pwd = generatePassword(baseImage);
				kpg = KeyPairGenerator.getInstance("RSA","BC");
				kpg.initialize(4096);
				KeyPair keyPair = kpg.generateKeyPair();
				
				PGPSignatureSubpacketGenerator hashedGen = new PGPSignatureSubpacketGenerator();
				hashedGen.setKeyFlags(true, KeyFlags.ENCRYPT_STORAGE);
				hashedGen.setPreferredCompressionAlgorithms(false, new int[] {
					CompressionAlgorithmTags.ZLIB,
					CompressionAlgorithmTags.ZIP
				});
				hashedGen.setPreferredHashAlgorithms(false, new int[] {
					HashAlgorithmTags.SHA256,
					HashAlgorithmTags.SHA384,
					HashAlgorithmTags.SHA512
				});
				hashedGen.setPreferredSymmetricAlgorithms(false, new int[] {
					SymmetricKeyAlgorithmTags.AES_256,
					SymmetricKeyAlgorithmTags.AES_192,
					SymmetricKeyAlgorithmTags.AES_128,
					SymmetricKeyAlgorithmTags.CAST5,
					SymmetricKeyAlgorithmTags.DES
				});
				
				PGPSecretKey secret = new PGPSecretKey(
						PGPSignature.DEFAULT_CERTIFICATION,
						PublicKeyAlgorithmTags.RSA_GENERAL,
						keyPair.getPublic(),
						keyPair.getPrivate(),
						new Date(),
						"InformaCam OpenPGP Key",
						SymmetricKeyAlgorithmTags.AES_256,
						pwd.toCharArray(),
						hashedGen.generate(),
						null,
						new SecureRandom(),
						"BC");
				
				ContentValues cv = new ContentValues();
				cv.put(Settings.Device.Keys.SECRET_KEY, secret.getEncoded());
				cv.put(Settings.Device.Keys.AUTH_KEY, pwd);
				cv.put(Settings.Device.Keys.KEYRING_ID, secret.getPublicKey().getKeyID());
				
				Log.d(Crypto.LOG, cv.toString());

				db.update(dh.getTable(), cv, BaseColumns._ID + " = ?", new String[] {Integer.toString(1)});
				db.close();
				dh.close();
				
				KeyServerResponse ksr = new KeyServerResponse(
						secret.getPublicKey(), 
						sp.getString(Settings.Keys.DISPLAY_NAME, sp.getString(Settings.Keys.DISPLAY_NAME, "")), 
						sp.getString(Settings.Keys.DISPLAY_NAME, sp.getString(Settings.Keys.DISPLAY_NAME, ""))
				);
				
				installNewKey(c, ksr, null);
				
				return true;
			} catch (NoSuchAlgorithmException e) {
				Log.e(Crypto.LOG, "key error: " + e.toString());
				db.close();
				dh.close();
				return false;
			} catch (NoSuchProviderException e) {
				Log.e(Crypto.LOG, "key error: " + e.toString());
				db.close();
				dh.close();
				return false;
			} catch (PGPException e) {
				Log.e(Crypto.LOG, "key error: " + e.toString());
				db.close();
				dh.close();
				return false;
			} catch (IOException e) {
				Log.e(Crypto.LOG, "key error: " + e.toString());
				db.close();
				dh.close();
				return false;
			}
		}
		
		db.close();
		dh.close();
		return false;
	}
}
