package com.skiaddict.weather.provider;

import com.skiaddict.weather.provider.WeatherContract.LocationTagsColumns;
import com.skiaddict.weather.provider.WeatherContract.LocationsColumns;
import com.skiaddict.weather.provider.WeatherContract.TagsColumns;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class WeatherDb extends SQLiteOpenHelper {
		
    private static final String TAG = "WeatherDatabase";
    public static final String DATABASE_NAME = "WeatherDatabase";
    public static final int DBVER_INITIAL = 1;
    public static final int DATABASE_VERSION = DBVER_INITIAL;
	
    public interface Tables {
        String LOCATIONS = "Locations";
        String TAGS = "Tags";
        String LOCATIONTAGS = "LocationsTags";
        String LOCATIONS_JOIN_LOCATIONTAGS = "Locations JOIN LocationsTags ON Locations._id=LocationsTags.locationId";
    }

    private static final String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + Tables.LOCATIONS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
    		+ LocationsColumns.LOCATION_NAME + " TEXT DEFAULT '',"
    		+ LocationsColumns.LATITUDE + " TEXT DEFAULT '',"
    		+ LocationsColumns.LONGITUDE + " TEXT DEFAULT '');";
    
    private static final String CREATE_TAGS_TABLE = "CREATE TABLE " + Tables.TAGS + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
    		+ TagsColumns.TAG_NAME + " TEXT DEFAULT '');";
    
    private static final String CREATE_LOCATION_TAGS_TABLE = "CREATE TABLE " + Tables.LOCATIONTAGS + " ("
            + LocationTagsColumns.LOCATION_ID + " INTEGER," 
    		+ LocationTagsColumns.TAG_ID + " INTEGER);";
    
	public WeatherDb(Context context) {
        // Use default cursor factory.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");

        db.execSQL(CREATE_LOCATIONS_TABLE);
        db.execSQL(CREATE_TAGS_TABLE);
        db.execSQL(CREATE_LOCATION_TAGS_TABLE);
        
        // Add default tags.
        long tagSkiing = insertTag(db, "Skiing");
        long tagRoadBiking = insertTag(db, "Road Biking");
        long tagMountainBiking = insertTag(db, "Mountain Biking");
        long tagClimbing = insertTag(db, "Climbing");
        
        // Add default locations.
        long locationSeattle = insertLocation(db, "Seattle", "47.54583", "-122.31361");
        long locationLeavenworth = insertLocation(db, "Leavenworth", "47.39889", "-120.20694");
        long locationSanJuanIsland = insertLocation(db, "San Juan Island", "48.52028", "-123.02528");
        long locationCrystalMountain = insertLocation(db, "Crystal Mountain", "46.92", "121.49");
        long locationMountBaker = insertLocation(db, "Mount Baker", "48.865", "-121.678");
        long locationStevensPass = insertLocation(db, "Stevens Pass", "48.3469", "-120.7203");
        long locationSnoqualmiePass = insertLocation(db, "Snoqualmie Pass", "47.427", "-121.418");

        // Climbing Tags.
        tagLocation(db, locationLeavenworth, tagClimbing);

        // Road Biking Tags
        tagLocation(db, locationSeattle, tagRoadBiking);
        tagLocation(db, locationSanJuanIsland, tagRoadBiking);

        // Mountain Biking Tags
        tagLocation(db, locationCrystalMountain, tagMountainBiking);

        // Ski Tags
        tagLocation(db, locationStevensPass, tagSkiing);
        tagLocation(db, locationSnoqualmiePass, tagSkiing);
        tagLocation(db, locationCrystalMountain, tagSkiing);
        tagLocation(db, locationMountBaker, tagSkiing);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + Tables.LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.LOCATIONTAGS);
 
        onCreate(db);
    }
    
    private long insertLocation(SQLiteDatabase db, String locationName, String latitude, String longitude) {
    	ContentValues values = new ContentValues();
    	values.put(WeatherContract.LocationsColumns.LOCATION_NAME, locationName);
    	values.put(WeatherContract.LocationsColumns.LATITUDE, latitude);
    	values.put(WeatherContract.LocationsColumns.LONGITUDE, longitude);
    	return db.insert(Tables.LOCATIONS, null, values);
    }
    
    private long insertTag(SQLiteDatabase db, String tagName) {
    	ContentValues values = new ContentValues();
    	values.put(WeatherContract.TagsColumns.TAG_NAME, tagName);
    	return db.insert(Tables.TAGS, null, values);
    }
    
    private void tagLocation(SQLiteDatabase db, long locationId, long tagId) {
    	ContentValues values = new ContentValues();
       	values.put(WeatherContract.LocationTagsColumns.LOCATION_ID, locationId);
       	values.put(WeatherContract.LocationTagsColumns.TAG_ID, tagId);
       	db.insert(Tables.LOCATIONTAGS, null, values);
    }
}
