package com.greengar.hackathon.app.service;

import java.util.List;

import com.greengar.hackathon.app.entity.LatLng;

public interface Callback {
	public void onSucess(List<String> busStationNames, List<LatLng> locations);
	public void onFail();
}
