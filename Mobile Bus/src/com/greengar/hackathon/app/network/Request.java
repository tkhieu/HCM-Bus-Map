package com.greengar.hackathon.app.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.greengar.hackathon.app.util.Config;


public abstract class Request {
	public static final String TAG = "Request";
	
	public static final int PRIORITY_HIGHEST = 0;
	public static final int PRIORITY_NORMAL = 1;
	public static final int PRIORITY_CAN_DISCARD_SIMILIAR_REQUESTS = 2;
	public static final int PRIORITY_CAN_DISCARD = 3;
	
	public static int CONNECTION_TIMEOUT = 30000;
	public static int SOCKET_TIMEOUT = 30000;
	protected static int numOfRequests = 0;
	
	public int id;
	public String url;
	public int priority = PRIORITY_NORMAL;
	
	// in order to check duplicated request
	@Override
	public boolean equals(Object o) {
		Request other = (Request) o;
		return url.equals(other.url);
	}
	
	protected static HttpClient httpClient;
	
	static {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
		
		httpClient = new DefaultHttpClient(httpParameters);
		
		if (Config.LOG_REQUEST) Log.w(TAG, "Init HttpClient");
	}
	
	public abstract void onRequestComplete(String responseData);
	public abstract void onRequestFailed(ErrorData errorData);
	public abstract void send();
	
	protected void handleResponse(String response) {
		ErrorData errorData = hasError(response);
		if (errorData != null) {
			onRequestFailed(errorData);
		} else {
			if (response != null) {
				onRequestComplete(response);
			} else {
				onRequestFailed(new ErrorData());
			}
		}
	}
	
	private ErrorData hasError(String response) {
		// TODO check error in your response
		// null means no error found
		return null;
	}

	protected static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				reader = null;
				
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
