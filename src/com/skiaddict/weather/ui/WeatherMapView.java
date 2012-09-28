package com.skiaddict.weather.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.skiaddict.common.views.CustomMapView;

public class WeatherMapView extends CustomMapView {

	public WeatherMapView(Context context, String apiKey) {
        super(context, apiKey);
    }
 
    public WeatherMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public WeatherMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    @Override
	public void onLongPress(MotionEvent motionEvent) {
		super.onLongPress(motionEvent);
		GeoPoint selectedLocation = getProjection().fromPixels((int)motionEvent.getX(), (int)motionEvent.getY());
		String latitude = String.valueOf(selectedLocation.getLatitudeE6()/1000000.0);
		String longitude = String.valueOf(selectedLocation.getLongitudeE6()/1000000.0);
		Toast.makeText(getContext(), "Lat: " + latitude + " Long: " + longitude, Toast.LENGTH_LONG).show();
	}
}
