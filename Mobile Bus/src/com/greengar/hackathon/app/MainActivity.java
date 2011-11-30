package com.greengar.hackathon.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.greengar.hackathon.app.entity.LatLng;
import com.greengar.hackathon.app.entity.Route;
import com.greengar.hackathon.app.entity.RoutesAdapter;
import com.greengar.hackathon.app.network.RequestBackgroundWorker;
import com.greengar.hackathon.app.service.BusRoutesService;
import com.greengar.hackathon.app.service.BusRoutesService.CallbackRoutes;
import com.greengar.hackathon.app.view.BusStationAutocompleteTextView;
import com.greengar.hackathon.app.view.BusStationAutocompleteTextView.LatLngCallBach;
import com.greengar.hackathon.app.view.InfoBusRoute;
import com.greengar.hackathon.map.ActivityMap;

public class MainActivity extends Activity {
	BusStationAutocompleteTextView busSrc, busDes;
	Button btnSearch;
	LatLng locationSrc = null;
	LatLng locationDes = null;
	ListView listView;
	ProgressDialog process;
	InfoBusRoute info;
	Button btnMapSearch;
	Route routes;
	LinearLayout panel;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        busSrc = (BusStationAutocompleteTextView) findViewById(R.id.busSrc);
        busDes = (BusStationAutocompleteTextView) findViewById(R.id.busDes);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        listView = (ListView) findViewById(R.id.listStepRoutes);
        busSrc.callback = callbackSrc;
        busDes.callback = callbackDes;
        btnMapSearch = (Button) findViewById(R.id.btnMapSearch);
        info = (InfoBusRoute) findViewById(R.id.infoBusRoute);
        btnSearch.setOnClickListener(btnSearchOnClick);
        btnMapSearch.setOnClickListener(btnMapSeachOnClick);
        setEnableButtonSearch();
        panel = (LinearLayout) findViewById(R.id.panel);
         
        RequestBackgroundWorker.startWaitingForRequest();
         
    }
    
    android.view.View.OnClickListener btnMapSeachOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this, ActivityMap.class);
			intent.putExtra("data", routes);
			intent.putExtra("check", true);
			startActivityForResult(intent, 1);
		}
	};
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		
	}
	
    OnClickListener btnSearchOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			process = ProgressDialog.show(MainActivity.this, "", "Processing...");
			 //InputMethodManager imm = (InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
			 //imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), locationSrc + ", " + locationDes, Toast.LENGTH_LONG).show();
			BusRoutesService.getBusRoutes(locationSrc, locationDes, new CallbackRoutes() {
				
				public void onFinish(final Route route) {
					// TODO Auto-generated method stub
					MainActivity.this.runOnUiThread(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							process.dismiss();
							if(route.isNull != 1){
								panel.setVisibility(View.VISIBLE);
								info.setVisibility(View.VISIBLE);
								routes = route;
								btnMapSearch.setVisibility(View.VISIBLE);
								info.setValue(String.valueOf(route.getDuration()).split(" ")[0] + " min", String.valueOf(route.getPrice()), String.format("%.2f km", (route.getLen() / 1000)));
								RoutesAdapter adapter = new RoutesAdapter(MainActivity.this, route.getSteps());
								listView.setAdapter(adapter);
							}else{
								Toast.makeText(MainActivity.this, "Can not find bus routes.", Toast.LENGTH_LONG).show();
							}
							
						}
					});
					
				}
				
				public void onFail() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	};
    
	private void setEnableButtonSearch(){
		if(locationDes == null || locationSrc == null)
			btnSearch.setEnabled(false);
		else
			btnSearch.setEnabled(true);
	}
	
    LatLngCallBach callbackDes = new LatLngCallBach() {
		
		public void onFinish(LatLng location, int position) {
			// TODO Auto-generated method stub
			locationDes = location;
			setEnableButtonSearch();
		
		}
	};
    
    LatLngCallBach callbackSrc = new LatLngCallBach() {
		
		public void onFinish(LatLng location, int position) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), location.lat + ", " + location.lng, Toast.LENGTH_LONG).show();
			locationSrc = location;
			setEnableButtonSearch();
		}
	};
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	RequestBackgroundWorker.stopThread();
    }
}