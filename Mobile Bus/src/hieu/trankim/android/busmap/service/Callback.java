package hieu.trankim.android.busmap.service;

import hieu.trankim.android.busmap.entity.LatLng;

import java.util.List;


public interface Callback {
	public void onSucess(List<String> busStationNames, List<LatLng> locations);
	public void onFail();
}
