package com.skiaddict.weather.ui;

import com.skiaddict.weather.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.skiaddict.weather.WeatherPreferences;
import com.skiaddict.weather.provider.WeatherContract.Locations;
import com.skiaddict.weather.provider.WeatherContract.Tags;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnNavigationListener  {

	private interface TagsQuery {
        String[] PROJECTION = {
        		Tags._ID, Tags.TAG_NAME};
        int _ID = 0;
        int TAG_NAME = 1;
    };
    
    private interface LocationsQuery {
        String[] PROJECTION = {
        		Locations._ID, Locations.LOCATION_NAME};
        int LOCATION_NAME = 1;
    };
    
    static private String TAG_FILTER_ID = "tagFilterId";
    static private int LOCATION_LOADER_ID = 0;
    static private int ID_NEW_LOCATION = 0;
    
    private TagsSpinnerAdapter mTagsSpinnerAdapter;
    private LocationsAdapter mLocationsAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_ForceOverflow);
		setContentView(R.layout.activity_main);

		mLocationsAdapter = new LocationsAdapter(this, null);
		ListView listView = (ListView)findViewById(R.id.locationsListView);
		listView.setAdapter(mLocationsAdapter);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
        // Restore previously set tag filter
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int selectedTagIndex = prefs.getInt(WeatherPreferences.KEY_TAG_FILTER, 0);

		// Set up the tag filters (REVIEW: doing a query on the main thread here, but it should be pretty quick since we'll have a limited number of tags).
		Cursor tagsCursor = getContentResolver().query(Tags.CONTENT_URI, TagsQuery.PROJECTION, null, null, Tags.DEFAULT_SORT_ORDER);
		tagsCursor.moveToPosition(selectedTagIndex);
		int selectedTagId = tagsCursor.getInt(TagsQuery._ID);

		mTagsSpinnerAdapter = new TagsSpinnerAdapter(this, tagsCursor);
        actionBar.setListNavigationCallbacks(mTagsSpinnerAdapter, this);
        actionBar.setSelectedNavigationItem(selectedTagIndex);
        
        Bundle args = new Bundle();
        args.putInt(TAG_FILTER_ID, selectedTagId);
        getSupportLoaderManager().initLoader(LOCATION_LOADER_ID, args, this);
}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, ID_NEW_LOCATION, Menu.NONE, R.string.menu_new)
        .setIcon(R.drawable.new_location)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (ID_NEW_LOCATION == item.getItemId()) {
			startActivity(new Intent(this, NewLocationActivity.class));
			return true;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	// OnNavigationListener Overrides
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // requery with the new filter
        Cursor cursor = mTagsSpinnerAdapter.getCursor();
        cursor.moveToPosition(itemPosition);

        Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        prefsEditor.putInt(WeatherPreferences.KEY_TAG_FILTER, itemPosition);
        prefsEditor.commit();

        Bundle args = new Bundle();
        args.putInt(TAG_FILTER_ID, cursor.getInt(TagsQuery._ID));
        getSupportLoaderManager().restartLoader(LOCATION_LOADER_ID, args, this);

        return true;
	}

	// LoaderManager.LoaderCallbacks<Cursor> Overrides
	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {
		int tagId = loaderArgs.getInt(TAG_FILTER_ID);
        return new CursorLoader(this, Locations.buildLocationsWithTagUri(tagId), LocationsQuery.PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mLocationsAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mLocationsAdapter.swapCursor(null);
	}

	private class TagsSpinnerAdapter extends CursorAdapter {

		public TagsSpinnerAdapter(Context context, Cursor cursor) {
			super(context, cursor, 0);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.sherlock_spinner_dropdown_item, parent, false);
			bindView(view, context, cursor);
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String tagName = cursor.getString(TagsQuery.TAG_NAME);
			TextView tagNameView = (TextView)view.findViewById(android.R.id.text1);
			tagNameView.setText(tagName);
		}
	}

	private class LocationsAdapter extends CursorAdapter {

		public LocationsAdapter(Context context, Cursor cursor) {
			super(context, cursor, 0);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			bindView(view, context, cursor);
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String tagName = cursor.getString(LocationsQuery.LOCATION_NAME);
			TextView tagNameView = (TextView)view.findViewById(android.R.id.text1);
			tagNameView.setText(tagName);
		}
	}
}
