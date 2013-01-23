package hieu.trankim.android.busmap.entity;

import java.io.Serializable;

public class LatLng implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8984152947730029071L;
	public double lat;
	public double lng;
	
	public LatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
}
