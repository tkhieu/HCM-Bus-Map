/***
  Copyright (c) 2008-2011 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
 */

package hieu.trankim.android.busmap.map;

import android.R.bool;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.greengar.hackathon.app.R;

import hieu.trankim.android.busmap.entity.LatLng;
import hieu.trankim.android.busmap.entity.Route;
import hieu.trankim.android.busmap.entity.Step;

import java.util.ArrayList;
import java.util.List;

public class ActivityMap extends MapActivity implements LocationListener {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in
																		// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
																	// Milliseconds
	boolean check;

	public ActivityMap() {
		super();
		listGeoPoint = new ArrayList<GeoPoint>();

		

		defaultPoint = new GeoPoint(0, 0);
		beginPoint = new GeoPoint(0, 0);
		endPoint = new GeoPoint(0, 0);
	}

	private GeoPoint beginPoint;
	private GeoPoint endPoint;

	private GeoPoint defaultPoint;
	private MapView map = null;
	private MapController mapController;
	private MyLocationOverlay me = null;
	private ArrayList<GeoPoint> listGeoPoint = null;
	private LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		Route route = (Route) getIntent().getSerializableExtra("data");

		check = (boolean) getIntent().getBooleanExtra("check", false);

		if (check) {
			listGeoPoint.clear();
			for (int i = 0; i < route.getSteps().size(); i++) {
				Step step = route.getSteps().get(i);
				ArrayList<ArrayList<LatLng>> k = step.getLocations();
				for (ArrayList<LatLng> arrayList : k) {
					for (LatLng latLng : arrayList) {
						GeoPoint gp = new GeoPoint((int) (latLng.lat * 1E6),
								(int) (latLng.lng * 1E6));
						listGeoPoint.add(gp);
					}
				}
			}
		}

		map = (MapView) findViewById(R.id.map);
		mapController = map.getController();

		defaultPoint = getCurrentPoint();
		
		if(!check){
			
			beginPoint = getCurrentPoint();
			endPoint = getCurrentPoint();
			listGeoPoint.remove(0);
			listGeoPoint.add(0, beginPoint);
			listGeoPoint.remove(listGeoPoint.size() - 1);
			listGeoPoint.add(listGeoPoint.size() - 1, endPoint);
		} else {
			beginPoint = listGeoPoint.get(0);
			beginPoint = listGeoPoint.get(listGeoPoint.size()-1);
		}
		

		mapController.setZoom(15);
		mapController.setCenter(defaultPoint);
		map.setBuiltInZoomControls(true);

		Drawable marker = getResources().getDrawable(R.drawable.marker);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.PASSIVE_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener(
						getApplicationContext()));

		map.getOverlays().add(new SitesOverlay(marker));

		DirectionOverlay directionOverlay = new DirectionOverlay(listGeoPoint);

		me = new MyLocationOverlay(this, map);

		map.getOverlays().add(me);
		map.getOverlays().add(directionOverlay);

	}

	protected GeoPoint getCurrentPoint() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.PASSIVE_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener(
						getApplicationContext()));

		double lat = locationManager.getLastKnownLocation(
				LocationManager.PASSIVE_PROVIDER).getLatitude();
		double log = locationManager.getLastKnownLocation(
				LocationManager.PASSIVE_PROVIDER).getLongitude();
		return new GeoPoint((int) (lat * 1E6), (int) (log * 1E6));
	}

	protected void showCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Toast.makeText(ActivityMap.this, message, Toast.LENGTH_LONG).show();
			defaultPoint = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));

			mapController.animateTo(defaultPoint);
			mapController.setZoom(17);

			DirectionOverlay directionOverlay = new DirectionOverlay(
					listGeoPoint);

			me = new MyLocationOverlay(this, map);

			map.getOverlays().add(me);
			map.getOverlays().add(directionOverlay);
			map.invalidate();
		}

	}

	public void onDrawerOpened() {
		map.setVisibility(ListView.GONE);
	}

	public void onDrawerClosed() {
		map.setVisibility(ListView.VISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();

		me.enableCompass();
	}

	@Override
	public void onPause() {
		super.onPause();

		me.disableCompass();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return (false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			map.setSatellite(!map.isSatellite());
			return (true);
		} else if (keyCode == KeyEvent.KEYCODE_Z) {
			map.displayZoomControls(true);
			return (true);
		}

		return (super.onKeyDown(keyCode, event));
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker = null;
		private OverlayItem inDrag = null;
		private ImageView dragImage = null;
		private int xDragImageOffset = 0;
		private int yDragImageOffset = 0;
		private int xDragTouchOffset = 0;
		private int yDragTouchOffset = 0;

		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker = marker;

			dragImage = (ImageView) findViewById(R.id.drag);
			xDragImageOffset = dragImage.getDrawable().getIntrinsicWidth() / 2;
			yDragImageOffset = dragImage.getDrawable().getIntrinsicHeight();

			
			items.add(new OverlayItem(listGeoPoint.get(0), "", ""));
			items.add(new OverlayItem(listGeoPoint.get(listGeoPoint.size()-1), "", ""));

			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return (items.get(i));
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}

		@Override
		public int size() {
			return (items.size());
		}

		
		 public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		 final int action = event.getAction();
		 final int x = (int) event.getX();
		 final int y = (int) event.getY();
		 boolean result = false;
		
		 if (action == MotionEvent.ACTION_DOWN) {
		
		 for (int i = 0;i< items.size();i++) {
		
		 OverlayItem item = items.get(i);
		 Point p = new Point(0, 0);
		
		 map.getProjection().toPixels(item.getPoint(), p);
		
		 if (hitTest(item, marker, x - p.x, y - p.y)) {
		 result = true;
		 inDrag = item;
		 items.remove(inDrag);
		 populate();
		
		 xDragTouchOffset = 0;
		 yDragTouchOffset = 0;
		
		 setDragImagePosition(p.x, p.y);
		 dragImage.setVisibility(View.VISIBLE);
		
		 xDragTouchOffset = x - p.x;
		 yDragTouchOffset = y - p.y;
		
		 GeoPoint p1 = item.getPoint();
		
		 if(i == 0)
		 beginPoint = p1;
		 if(i==items.size()-1)
		 endPoint = p1;
		
		 StringBuilder stringBuilder = new StringBuilder();
		 stringBuilder.append(p1.getLatitudeE6() / 1E6)
		 .append(" - ")
		 .append(p1.getLongitudeE6() / 1E6);
		
		 Toast.makeText(getApplicationContext(), stringBuilder,
		 Toast.LENGTH_SHORT).show();
		
		 break;
		 }
		 }
		 } else if (action == MotionEvent.ACTION_MOVE && inDrag != null) {
		 setDragImagePosition(x, y);
		 result = true;
		 } else if (action == MotionEvent.ACTION_UP && inDrag != null) {
		 dragImage.setVisibility(View.GONE);
		
		 GeoPoint pt = map.getProjection().fromPixels(
		 x - xDragTouchOffset, y - yDragTouchOffset);
		 OverlayItem toDrop = new OverlayItem(pt, inDrag.getTitle(),
		 inDrag.getSnippet());
		
		 items.add(toDrop);
		 populate();
		
		 inDrag = null;
		 result = true;
		 }
		
		 return (result || super.onTouchEvent(event, mapView));
		 }
		private void setDragImagePosition(int x, int y) {
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragImage
					.getLayoutParams();

			lp.setMargins(x - xDragImageOffset - xDragTouchOffset, y
					- yDragImageOffset - yDragTouchOffset, 0, 0);
			dragImage.setLayoutParams(lp);
		}
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			defaultPoint = new GeoPoint((int) lat * 1000000,
					(int) lng * 1000000);
			mapController.animateTo(defaultPoint);
			showCurrentLocation();
		}

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public class MyLocationListener implements LocationListener {

		Context context;

		public MyLocationListener(Context context) {
			super();
			this.context = context;
		}

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			showCurrentLocation();
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(context, "Provider status changed",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(context,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(context,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}

}
