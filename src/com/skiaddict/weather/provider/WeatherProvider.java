package com.skiaddict.weather.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class WeatherProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int TAGS = 1;
    
    private static final int LOCATIONS_FILTERED = 2;
    
    private WeatherDb mWeatherDb;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, WeatherContract.PATH_TAGS, TAGS);
        matcher.addURI(authority, WeatherContract.PATH_LOCATIONS + "/" + WeatherContract.WITH_TAG + "/*", LOCATIONS_FILTERED);
        return matcher;
    }
    
	@Override
	public boolean onCreate() {
		mWeatherDb = new WeatherDb(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case TAGS:
            return WeatherContract.Tags.CONTENT_TYPE;
        case LOCATIONS_FILTERED:
        	return WeatherContract.Locations.CONTENT_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        
        switch(sUriMatcher.match(uri)) {
        case TAGS:
            qb.setTables(WeatherDb.Tables.TAGS);
        	break;
        case LOCATIONS_FILTERED:
        	String tagId = uri.getLastPathSegment();
        	qb.setTables(WeatherDb.Tables.LOCATIONS_JOIN_LOCATIONTAGS);
            qb.appendWhere(WeatherContract.LocationTagsColumns.TAG_ID + "=" + tagId);
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

         // Get the database and run the query
        SQLiteDatabase db = mWeatherDb.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
    @Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		return 0;
	}


}
