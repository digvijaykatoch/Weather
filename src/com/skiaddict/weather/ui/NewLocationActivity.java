package com.skiaddict.weather.ui;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.skiaddict.weather.R;

public class NewLocationActivity extends MapActivity {

	WeatherMapView mMapView;
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
        setContentView(R.layout.new_location_activity);
        mMapView = (WeatherMapView)findViewById(R.id.mapview);

        mMapView.getController().setCenter(getPoint(47.6097,-122.3331));
        mMapView.getController().setZoom(17);
        mMapView.setBuiltInZoomControls(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}		    
}
