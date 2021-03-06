package hieu.trankim.android.busmap.service;

import hieu.trankim.android.busmap.entity.LatLng;
import hieu.trankim.android.busmap.network.ErrorData;
import hieu.trankim.android.busmap.network.GetRequest;
import hieu.trankim.android.busmap.network.RequestBackgroundWorker;
import hieu.trankim.android.busmap.util.Config;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class BusStationService {
	
	public static void getBusStations(String keyword, final Callback callback) {
		GetRequest request = new GetRequest() {
			
			@Override
			public void onRequestFailed(ErrorData errorData) {
				callback.onFail();
			}
			
			@Override
			public void onRequestComplete(String responseData) {
				responseData = responseData.replace("id_locB(", "").replace(");", "");
				
				try {
					JSONArray array = new JSONArray(responseData);
					int len = array.length();
					JSONObject jsonObject;
					List<String> busStationNames = new LinkedList<String>();
					List<LatLng> locations = new LinkedList<LatLng>();
					for (int i = 0; i < len; ++i) {
						jsonObject = array.getJSONObject(i);
						if (jsonObject.has("latlng")) {
							busStationNames.add(jsonObject.getString("title"));
							jsonObject = jsonObject.getJSONObject("latlng");
							locations.add(new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lon")));
						}
					}
					
					callback.onSucess(busStationNames, locations);
				} catch (Exception e) {
					e.printStackTrace();
					callback.onFail();
				}
			}
		};
		request.url = String.format(Config.URL_BUS_STATIONS, URLEncoder.encode(keyword));
		RequestBackgroundWorker.queueRequest(request);
	}
}
