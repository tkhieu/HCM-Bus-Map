package hieu.trankim.android.busmap.util;

public class Config {
	
	public static final boolean LOG_REQUEST = false;

	/**
	 * donxebuyt.com API
	 */
	public static final String SERVER = "http://hcm1.appspot.com";	
	public static final String URL_BUS_STATIONS = SERVER + "/location/acc/id_locB/?q=%s";
	public static final String URL_BUS_ROUTES =  SERVER + "/route/?to=%f,%f&from=%f,%f&jsonp=view_direction&by=bus&opt=lazy";
}
