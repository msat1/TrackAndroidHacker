/*package com.appzdevelopers.trackandhackerf;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.test.TouchUtils;
import android.widget.Toast;

public class GPSHelper extends MapActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapcontent);
        
        final MapView map=(MapView)this.findViewById(R.id.mapView);

	MapController controller=map.getController();
    }
			public String getCurrentLocation(final Context mContext) {
	
	LocationManager location = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);
	LocationListener listner = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			Toast.makeText(mContext,new GeoPoint((int)location.getLatitude(),(int)location.getLongitude()).toString(), Toast.LENGTH_SHORT).show();
		}
	};
	return null;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
*/