package hieu.trankim.android.busmap.service;

import hieu.trankim.android.busmap.entity.LatLng;
import hieu.trankim.android.busmap.entity.Route;
import hieu.trankim.android.busmap.entity.Step;
import hieu.trankim.android.busmap.network.ErrorData;
import hieu.trankim.android.busmap.network.GetRequest;
import hieu.trankim.android.busmap.network.RequestBackgroundWorker;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusRoutesService {
	public interface CallbackRoutes{
		void onFinish(Route route);
		void onFail();
	}
	
	public static void getBusRoutes(LatLng busSrc, LatLng busDes, final CallbackRoutes callback){
		GetRequest request = new GetRequest() {
			
			@Override
			public void onRequestFailed(ErrorData errorData) {
				// TODO Auto-generated method stub
				callback.onFail();
			}
			
			@Override
			public void onRequestComplete(String responseData) {
				// TODO Auto-generated method stub
				Route route = new Route();
				
				System.out.println("??????????? "+ responseData);
				responseData = responseData.replace("view_direction(", "").replace(",\"\");", "");
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(responseData);
					jsonObject = jsonObject.getJSONArray("routes").getJSONObject(0);
					route.setPrice(jsonObject.getString("price"));
					route.setLen(jsonObject.getDouble("len"));
					route.setTo(jsonObject.getString("to"));
					route.setTrans(jsonObject.getString("trans"));
					route.setDuration(jsonObject.getString("duration"));
					JSONArray jsonArray = jsonObject.getJSONArray("steps");
					for(int i = 0; i < jsonArray.length(); ++i){
						JSONObject jsonObject1 = jsonArray.getJSONObject(i);
						Step step = new Step();
						step.setDirection(jsonObject1.getString("direction"));
						step.setPrice(jsonObject1.getDouble("price"));
						step.setLen(jsonObject1.getDouble("len"));
						step.setNote(jsonObject1.getString("note"));
						step.setTo(jsonObject1.getString("to"));
						step.setWaiting(jsonObject1.getString("waiting"));
						step.setTrans(jsonObject1.getString("trans"));
						JSONArray jsonArray1 = jsonObject1.getJSONArray("points");
						
						for(int j = 0; j < jsonArray1.length(); ++j){
							JSONArray  jsonArrayLocation = jsonArray1.getJSONArray(j);
							ArrayList<LatLng> loca = new ArrayList<LatLng>();
							for(int k = 0; k < jsonArrayLocation.length(); ++k){
								JSONArray json = jsonArrayLocation.getJSONArray(k);
								LatLng location = new LatLng(json.getDouble(0), json.getDouble(1));
								loca.add(location);
							}
							step.getLocations().add(loca);
						}
						route.getSteps().add(step);
					}
					callback.onFinish(route);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					route = new Route();
					route.isNull = 1;
					e.printStackTrace();
					callback.onFinish(route);
					//callback.onFail();
				}
				
				System.out.println("@@@@@ " + route);
			}
		};
		
		request.url = String.format(hieu.trankim.android.busmap.util.Config.URL_BUS_ROUTES, busDes.lat, busDes.lng, busSrc.lat, busSrc.lng);
		System.out.println("???? " + request.url);
		RequestBackgroundWorker.queueRequest(request);
	}
}
