package org.witness.informacam.utils;

import java.util.ArrayList;

import org.bouncycastle.util.encoders.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.witness.informacam.R;
import org.witness.informacam.app.mods.Selections;
import org.witness.informacam.utils.Constants.AddressBook;
import org.witness.informacam.utils.Constants.App;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.util.Log;

public class AddressBookUtil {
	public interface AddressBookListener {
		public void receiveNewAddresses(ArrayList<Selections> s);
		public void receiveNewAddresses(JSONObject address);
	}
	
	
	public static void getAddressList(final Activity c, String[] emailAddresses) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<Selections> s = new ArrayList<Selections>();
				String[] projection = new String[] {
						Email.DATA,
						Contacts.DISPLAY_NAME,
						Email.CONTACT_ID
				};
				
				Cursor a = c.getContentResolver().query(Email.CONTENT_URI, projection, null, null, Contacts.DISPLAY_NAME + " ASC");
				
				
				if(a != null) {
					Log.d(App.LOG, "TOTAL: " + a.getCount());
					
					int batchNum = 1;
					int dispatchNum = 0;
					int threshold = 10;
					if(a.getCount() >= threshold)
						batchNum = Math.round(a.getCount()/threshold) + 1;
					
					a.moveToFirst();
					while(a.moveToNext()) {
						if(dispatchNum != threshold) {
							boolean isSelected = false;
							String contactEmail = a.getString(a.getColumnIndex(Email.DATA));
							String contactDisplayName = a.getString(a.getColumnIndex(Contacts.DISPLAY_NAME));
							long contactId = a.getLong(a.getColumnIndex(Email.CONTACT_ID));
														
							byte[] contactPhoto = Base64.encode(IOUtility.getBytesFromBitmap(((BitmapDrawable) c.getResources().getDrawable(R.drawable.ic_blank_person)).getBitmap(), 10)); 
							
							try {
								String photoQuery = Data.MIMETYPE + "='" + Photo.CONTENT_ITEM_TYPE + "'";
								Cursor b = c.getContentResolver().query(Data.CONTENT_URI, null, photoQuery, null, null);
								
								if(b != null) {
									if(b.moveToFirst()) {
										Uri contact = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);										
										Uri photoUri = Uri.withAppendedPath(contact, Contacts.Photo.CONTENT_DIRECTORY);
										
										Cursor p = c.getContentResolver().query(photoUri, new String[] {Contacts.Photo.PHOTO}, null, null, null);
										if(p != null) {
											if(p.moveToFirst())
												contactPhoto = Base64.encode(p.getBlob(0));
											p.close();
										}
									}
									b.close();
								}
								
								
								JSONObject contactExtras = new JSONObject();
								contactExtras.put(AddressBook.Keys.CONTACT_EMAIL, contactEmail);
								contactExtras.put(AddressBook.Keys.CONTACT_NAME, contactDisplayName.equals(contactEmail) ? "" : contactDisplayName);
								contactExtras.put(AddressBook.Keys.CONTACT_PHOTO, new String(contactPhoto));
								
								s.add(new Selections(contactDisplayName + "\n" + contactEmail, isSelected, contactExtras));
							} catch(JSONException e) {}
							dispatchNum++;
						} else {
							// dispatch some results
							((AddressBookListener) c).receiveNewAddresses(s);
							s.clear();
							dispatchNum = 0;
						}
							
					}
					a.close();
				}
				
			}
			
		}).start();
	}
	
	public static void getAddressList(Activity c) {
		getAddressList(c, null);
	}
}
