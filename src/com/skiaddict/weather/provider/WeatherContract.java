
package com.skiaddict.weather.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class WeatherContract {
	
    interface LocationsColumns {
        String LOCATION_NAME = "locationName";        
        String LONGITUDE = "longitude";
        String LATITUDE = "latitude";
    }

    interface TagsColumns {
        String TAG_NAME = "tagName";        
        String TAG_IS_ALL = "tagIsAll";        
    }

    interface LocationTagsColumns {
        String LOCATION_ID = "locationId";        
        String TAG_ID = "tagId";        
    }

    public static final String CONTENT_AUTHORITY = "com.skiaddict.weather.provider";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_LOCATIONS = "locations";
    
    public static final String PATH_TAGS = "tags";
    
    public static final String WITH_TAG = "withTag";
    
    public static class Locations implements LocationsColumns, BaseColumns {
 
    	// Content Uri for Tracks.
    	public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATIONS).build();
        
        // Mimetype for collection of locations.
        public static final String CONTENT_TYPE = "vnd.skiaddict.cursor.dir/vnd.weather.location";

        // Mimetype for single location
        public static final String CONTENT_ITEM_TYPE = "vnd.skiaddict.cursor.item/vnd.weather.location";

        public static Uri buildLocationsWithTagUri(int tagId) {
            return CONTENT_URI.buildUpon().appendPath(WITH_TAG).appendPath(String.valueOf(tagId)).build();
        }    
    }
	
    public static class Tags implements TagsColumns, BaseColumns {
 
    	// Content Uri for Tracks.
    	public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAGS).build();
        
        // Mimetype for collection of tags.
        public static final String CONTENT_TYPE = "vnd.skiaddict.cursor.dir/vnd.weather.tag";

        // Mimetype for single tag
        public static final String CONTENT_ITEM_TYPE = "vnd.skiaddict.cursor.item/vnd.weather.tag";

        // Default sort order.
        public static final String DEFAULT_SORT_ORDER = BaseColumns._ID + " ASC";
    }
}
