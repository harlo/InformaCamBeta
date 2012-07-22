package org.witness.informacam.utils;

import android.media.ExifInterface;
import android.os.Environment;
import android.provider.BaseColumns;

public class Constants {
	public final static class App {
		public static final String LOG = "**************** InformaCam:UI Thread ****************";
		
		public final static class Eula {
			
		}
		
		public final static class Wizard {
			public final static int FROM_BASE_IMAGE_CAPTURE = 884;
			public final static int FROM_ADDRESSBOOK_CHOOSER = 885;
		}
		
		public final static class LogIn {
			
		}
		
		public final static class Main {
			public final static int FROM_MEDIA_CAPTURE = 774;
		}
		
		public final static class MediaManager {
			
		}
		
		public final static class AddressBook {
			public final static int FROM_CONTACT_CHOOSER = 674;
		}
	}
	
	public final static class Mods {
		public final static class Selections {
			public final static String SELECT_ONE = "select_one";
			public final static String SELECT_MULTI = "select_multi";
		}
	}
	
	public final static class AddressBook {
		public final static class Keys {
			public final static String CONTACT_PHOTO = "contactPhoto";
			public final static String CONTACT_NAME = "contactName";
			public final static String CONTACT_EMAIL = "contactEmail";
			public final static String CONTACT_ID = "contactID";
		}
	}
	
	public final static class TrustedDestination {
		public final static class Keys {
			public final static String EMAIL = "trustedDestinationEmail";
			public final static String KEYRING_ID = Crypto.Keyring.Keys.ID;
			public final static String URL = "trustedDestinationURL";
			public final static String DISPLAY_NAME = "trustedDestinationDisplayName";
		}
	}
	
	public final static class Media {
		public final static class Keys {
			public final static String TYPE = "mediaType";
			public final static String ORIGINAL_HASH = "originalHash";
			public final static String ANNOTATED_HASH = "annotatedHash";
			public final static String METADATA = "informaCamMetadata";
			public final static String LOCATION_OF_ORIGINAL = "locationOfOriginalMedia";
			public final static String LOCATION_OF_SENT = "locationOfSentMedia";
			public final static String TRUSTED_DESTINATION_ID = "trustedDestinationId";
			public final static String TRUSTED_DESTINATION_URL = TrustedDestination.Keys.URL;
			public final static String TRUSTED_DESTINATION_KEYRING_ID = TrustedDestination.Keys.KEYRING_ID;
			public final static String TRUSTED_DESTINATION_EMAIL = TrustedDestination.Keys.EMAIL;
			public final static String SHARE_VECTOR = "shareVector";
			public final static String MESSAGE_URL = "messageUrl";
			public final static String STATUS = "transitStatus";
			public final static String AUTH_TOKEN = "authToken";
			public final static String ALIAS = "mediaAlias";
		}
		
		public final static class Type {
			public final static String MP4 = ".mp4";
			public final static String MKV = ".mkv";
			public final static String JEPG = ".jpg";
		}
	}
	
	public final static class Settings {
		public static final String LOG = "**************** InformaCam:Settings ****************";
		public static final class Keys {
			public final static String CURRENT_LOGIN = "currentLogIn";
			public final static String EULA_ACCEPTED = "eulaAccepted";
			public final static String SETTINGS_VIEWED = "settingsViewed";
			public final static String LOGIN_CACHE_TIME = "loginCacheTime";
			public final static String DEFAULT_IMAGE_HANDLING = "defaultImageHandling";
			public final static String USE_ENCRYPTION = "useEncryption";
			public final static String USE_PROXY = "useProxy";
		}
		
		public final static class Device {
			public static final class Keys {
				public final static String KEYRING_ID = Crypto.PGP.Keys.PGP_KEY_ID;
				public final static String PRIVATE_KEY = "devicePrivateKey";
				public final static String AUTH_KEY = "deviceAuthKey";
				public final static String BASE_IMAGE_PATH = "deviceBaseImagePath";
			}
		}
		
		public final static class Login {
			public final static String PW_EXPIRY = "1839****PW_is139Expired(*)@";
		}
		
		public final static class LoginCache {
			public final static int ALWAYS = 200;
			public final static int AFTER_SAVE = 201;
			public final static int ON_CLOSE = 202;
		}
		
		public final static class OriginalImageHandling {
			public final static int LEAVE_ORIGINAL_ALONE = 300;
			public final static int ENCRYPT_ORIGINAL = 301;
			public final static int DELETE_ORIGINAL = 302;
		}
	}
	
	public final static class Suckers {
		public static final String LOG = "**************** InformaCam:Suckers ****************";
		
		public final static class LogRate {
			public final static long ACC = 500L;
			public final static long PHONE = 5000L;
			public final static long GEO = 10000L;
		}
		
		public static final class Broadcasts {
			public final static String START = "startSuckerService";
			public final static String LOCK_LOGS = "lockLogs";
			public final static String UNLOCK_LOGS = "unlockLogs";
		}
	}
	
	public final static class Informa {
		public static final String LOG = "**************** InformaCam:Informa ****************";
		
		public final static class Consent {
			public final static int GENERAL = 101;
		}
		
		public final static class Device {
			public final static int IS_SELF = -1;
			public final static int IS_NEIGHBOR = 1;
			public final static String SELF = "_self";
		}
		
		public final static class Genealogy {
			public final static class MediaOrigin {
				public final static int IMPORT = 400;
				public final static int FROM_INFORMA = 401;
			}
		}
		
		public final static class Owner {
			public final static int INDIVIDUAL = 400;
		}
		
		public final static class VideoRegions {
			public final static class Parent {
				public final static int SELF = -1;
			}
		}
		
		public static final class Keys {
			public final static String INTENT = "intent";
			public final static String GENEALOGY = "genealogy";
			public final static String DATA = "data";
			
			public static final class CaptureEvent {
				public final static String TYPE = "captureEventType";
				public final static String MATCH_TIMESTAMP = "captureEventMatchTimestamp";
				public final static String TIMESTAMP = "captureEventTimestamp";
				public final static String ON_VIDEO_START = "timestampOnVideoStart";
				public final static String MEDIA_CAPTURE_COMPLETE = "mediaCapturedComplete";
			}
			
			public static final class Intent {
				public final static String TRUSTED_DESTINATION = TrustedDestination.Keys.EMAIL;
			}
			
			public static final class Genealogy {
				public final static String LOCAL_MEDIA_PATH = Media.Keys.LOCATION_OF_ORIGINAL;
				public final static String DATE_CREATED = "dateCreated";
				public final static String MEDIA_ORIGIN = "mediaOrigin";
			}
			
			public static final class Device {
				public final static String LOCAL_TIMESTAMP = "deviceLocalTimestamp";
				public final static String PUBLIC_TIMESTAMP = "devicePublicTimestamp";
				public final static String DISPLAY_NAME = Keys.Owner.DISPLAY_NAME;
			}
			
			public static final class Owner {
				public final static String DISPLAY_NAME = "ownerName";
			}
			
			public static final class Data {
				public static final class Description {
					public final static String IMAGE_REGIONS = "imageRegions";
					public final static String VIDEO_REGIONS = "videoRegions";
					public final static String EVENTS = "events";
					public final static String ORIGINAL_HASH = Media.Keys.ORIGINAL_HASH;
					public final static String ANNOTATED_HASH = Media.Keys.ANNOTATED_HASH;
					public final static String EXIF = "exif";
					public final static String LOCATIONS = "location";
					public final static String CORROBORATION = "corroboration";
					public final static String CAPTURE_TIMESTAMPS = "captureTimestamp";
					public final static String MEDIA_TYPE = Media.Keys.TYPE;
				}
				
				public final static class Exif {
					public final static String MAKE = ExifInterface.TAG_MAKE;
					public final static String MODEL = ExifInterface.TAG_MODEL;
					public final static String APERTURE = ExifInterface.TAG_APERTURE;
					public final static String FLASH = ExifInterface.TAG_FLASH;
					public final static String EXPOSURE = ExifInterface.TAG_EXPOSURE_TIME;
					public final static String FOCAL_LENGTH = ExifInterface.TAG_FOCAL_LENGTH;
					public final static String IMAGE_WIDTH = ExifInterface.TAG_IMAGE_WIDTH;
					public final static String IMAGE_LENGTH = ExifInterface.TAG_IMAGE_LENGTH;
					public final static String ISO = ExifInterface.TAG_ISO;
					public final static String ORIENTATION = ExifInterface.TAG_ORIENTATION;
					public final static String WHITE_BALANCE = ExifInterface.TAG_WHITE_BALANCE;
					public final static String TIMESTAMP = ExifInterface.TAG_DATETIME;
					public final static String DURATION = "duration";
				}
				
				public static final class ImageRegion {
					public final static String INDEX = "regionIndex";
					public final static String THUMBNAIL = "regionThumbnail";
					public static final String DATA = "region_data";
					public final static String TIMESTAMP = "timestampOnGeneration";
					public final static String LOCATION = "locationOnGeneration";
					public final static String TAGGER_RETURN = "taggerReturned";
					public final static String FILTER = "obfuscationType";
					public final static String COORDINATES = "regionCoordinates";
					public final static String DIMENSIONS = "regionDimensions";
					public final static String WIDTH = "region_width";
					public final static String HEIGHT = "region_height";
					public final static String TOP = "region_top";
					public final static String LEFT = "region_left";
					public final static String UNREDACTED_DATA = "unredactedRegionData";
					public final static String BASE = "base";
					
					public final static class Subject {
						public final static String PSEUDONYM = "subject_pseudonym";
						public final static String INFORMED_CONSENT_GIVEN = "subject_informedConsentGiven";
						public final static String PERSIST_FILTER = "subject_persistFilter";
					}
				}
				
				public static final class VideoRegion {
					public final static String INDEX = Informa.Keys.Data.ImageRegion.INDEX;
					public final static String THUMBNAIL = Informa.Keys.Data.ImageRegion.THUMBNAIL;
					public static final String DATA = Informa.Keys.Data.ImageRegion.DATA;
					public final static String TIMESTAMP = Informa.Keys.Data.ImageRegion.TIMESTAMP;
					public final static String LOCATION = Informa.Keys.Data.ImageRegion.LOCATION;
					public final static String TAGGER_RETURN = Informa.Keys.Data.ImageRegion.TAGGER_RETURN;
					public final static String FILTER = Informa.Keys.Data.ImageRegion.FILTER;
					public final static String UNREDACTED_DATA = Informa.Keys.Data.ImageRegion.UNREDACTED_DATA;
					public final static String BASE = Informa.Keys.Data.ImageRegion.BASE;
					
					public final static class Child {
						public final static String COORDINATES = Informa.Keys.Data.ImageRegion.COORDINATES;
						public final static String DIMENSIONS = Informa.Keys.Data.ImageRegion.DIMENSIONS;
						public final static String WIDTH = Informa.Keys.Data.ImageRegion.WIDTH;
						public final static String HEIGHT = Informa.Keys.Data.ImageRegion.HEIGHT;
						public final static String TOP = Informa.Keys.Data.ImageRegion.TOP;
						public final static String LEFT = Informa.Keys.Data.ImageRegion.LEFT;
					}
					
					public final static class Subject {
						public final static String PSEUDONYM = Informa.Keys.Data.ImageRegion.Subject.PSEUDONYM;
						public final static String INFORMED_CONSENT_GIVEN = Informa.Keys.Data.ImageRegion.Subject.INFORMED_CONSENT_GIVEN;
						public final static String PERSIST_FILTER = Informa.Keys.Data.ImageRegion.Subject.PERSIST_FILTER;
					}
				}
			}
		}
	}
	
	public final static class Storage {
		public static final String LOG = "**************** InformaCam:Storage ****************";
		
		public final static class Tables {
			public final static class Keys {
				public static final String MEDIA = "informaCamMedia";
				public static final String CONTACTS = "informaCamContacts";
				public static final String SETUP = "informaCamSetup";
				public static final String TRUSTED_DESTINATIONS = "informaCamTrustedDestinations";
				public static final String KEYRING = "informaCamKeyring";
				public static final String KEYSTORE = "informaCamKeystore";
			}
		}
		
		public final static class FileIO {
			public final static String DUMP_FOLDER = Environment.getExternalStorageDirectory() + "/InformaCam";
			public final static String IMAGE_TMP = "informa_tmp.jpg";
			public final static String VIDEO_TMP = "informa_tmp.mp4";
		}
	}
	
	public final static class Uploader {
		public static final String LOG = "**************** InformaCam:Uploader ****************";
		
		public static final class Broadcasts {
			public final static String AVAILABLE = "uploaderAvailable";
			public final static String START = "startUploaderService";
		}
	}
	
	public final static class Crypto {
		public static final String LOG = "**************** InformaCam:Crypto ****************";
		
		public final static class Keyring {
			public final static class Keys {
				public final static String ID = PGP.Keys.PGP_KEY_ID;
				public final static String PUBLIC_KEY = "trustedDestinationPublicKey";
				public final static String FINGERPRINT = PGP.Keys.PGP_FINGERPRINT;
				public final static String TRUSTED_DESTINATION_ID = BaseColumns._ID;
			}
		}
		
		public final static class Keystore {
			public final static class Keys {
				public final static String CERTS = "certs";
				public final static String TIME_MODIFIED = "timeModified";
			}
		}
		
		public final static class PGP {
			public static final class Keys {
				public final static String PGP_KEY_ID = "pgpKeyId";
				public final static String PGP_FINGERPRINT = "pgpKeyFingerprint";
				public final static String PGP_CREATION_DATE = "pgpCreationDate";
				public final static String PGP_EXPIRY_DATE = "pgpExpiryDate";
				public final static String PGP_EMAIL_ADDRESS = "pgpEmailAddress";
				public final static String PGP_DISPLAY_NAME = "pgpDisplayName";
				public final static String PGP_KEY_IS_TRUSTED = "pgpKeyIsTrusted";
			}
		}
	}
	
	public final static class J3M {
		public static final String LOG = "**************** InformaCam:J3M ****************";
		public static final String ACTION = "j3m_action";
		
		public final static class Chunks {
			public final static int EXTRA_SMALL = 64;
			public final static int SMALL = 1024;
			public final static int MEDIUM = 4096;
			public final static int LARGE = 12288;
			public final static int EXTRA_LARGE = 36864;
			public final static int ALL(int length) {
				return length;
			}
		}
		
		public final static class State {
			public static final int IS_IDLE = 0;
	    	public static final int IS_JEMIFYING = 1;
	    	public static final int IS_JEMIFIED = 2;
		}
		
		public final static class Metadata {
			public static final String SOURCE = "chunk_source";
	    	public static final String INDEX = "chunk_index";
	    	public static final String LENGTH = "chunk_length";
	    	public static final String ENCRYPTION = "encryption";
	    	public static final String BLOB = "blob";
		}
	}
}
