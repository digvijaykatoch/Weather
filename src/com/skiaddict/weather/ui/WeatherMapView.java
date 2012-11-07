package com.skiaddict.weather.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class WeatherMapView extends MapView {

	public WeatherMapView(Context context, String apiKey) {
        super(context, apiKey);
    }
 
    public WeatherMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public WeatherMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
	public void onLongPress(MotionEvent motionEvent) {
		GeoPoint selectedLocation = getProjection().fromPixels((int)motionEvent.getX(), (int)motionEvent.getY());
		String latitude = String.valueOf(selectedLocation.getLatitudeE6()/1000000.0);
		String longitude = String.valueOf(selectedLocation.getLongitudeE6()/1000000.0);
		Toast.makeText(getContext(), "Lat: " + latitude + " Long: " + longitude, Toast.LENGTH_LONG).show();
	}

    GestureDetector mGestureDetector = new GestureDetector(new CustomGestureListener());
	
	@Override
	public boolean onTouchEvent(final MotionEvent motionEvent) {
		mGestureDetector.onTouchEvent(motionEvent);
		return super.onTouchEvent(motionEvent);
	}


	private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public void onLongPress(MotionEvent motionEvent) {
			super.onLongPress(motionEvent);
			WeatherMapView.this.onLongPress(motionEvent);
		}
		
	}
}
