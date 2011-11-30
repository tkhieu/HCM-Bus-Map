package com.greengar.hackathon.app.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.greengar.hackathon.app.R;
import com.greengar.hackathon.app.entity.LatLng;
import com.greengar.hackathon.app.service.BusStationService;
import com.greengar.hackathon.app.service.Callback;

public class BusStationAutocompleteTextView extends RelativeLayout {

	private ProgressBar pbLoading;
	private AutoCompleteTextView acBusStation;
	private Button btnDelete;
	private List<LatLng> listLatlng;
	private int position = -1;
	private Activity mActivity;

	public LatLngCallBach callback;

	public BusStationAutocompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mActivity = (Activity) context;

		View view = LayoutInflater.from(context).inflate(
				R.layout.bus_station_autocompleteview, this);

		btnDelete = (Button) view.findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				acBusStation.setText("");
				callback.onFinish(null, 0);
			}
		});

		pbLoading = (ProgressBar) view.findViewById(R.id.loading);

		acBusStation = (AutoCompleteTextView) view.findViewById(R.id.acSource);
		acBusStation.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				pbLoading.setVisibility(View.VISIBLE);
				btnDelete.setVisibility(View.GONE);
				searchBusStations();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		acBusStation.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				LatLng location = listLatlng.get(position);
				callback.onFinish(location, position);
			}
		});

	}

	private void searchBusStations() {
		BusStationService.getBusStations(acBusStation.getText().toString(),
				new Callback() {

					public void onSucess(final List<String> busStationNames,
							final List<LatLng> locations) {
						// TODO Auto-generated method stub
						mActivity.runOnUiThread(new Runnable() {

							public void run() {
								// TODO Auto-generated method stub
								listLatlng = locations;
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										getContext().getApplicationContext(),
										android.R.layout.simple_dropdown_item_1line,
										busStationNames);
								System.out.println(busStationNames);
								acBusStation.setAdapter(adapter);
								adapter.notifyDataSetChanged();

								acBusStation.showDropDown();

								pbLoading.setVisibility(View.GONE);
								btnDelete.setVisibility(View.VISIBLE);
							}
						});
					}

					public void onFail() {
						// TODO Auto-generated method stub
						mActivity.runOnUiThread(new Runnable() {

							public void run() {
								// TODO Auto-generated method stub
								pbLoading.setVisibility(View.GONE);
								btnDelete.setVisibility(View.VISIBLE);
							}
						});
					}
				});
	}

	public interface LatLngCallBach {
		void onFinish(LatLng location, int position);
	}

}
