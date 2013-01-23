package hieu.trankim.android.busmap.network;

import hieu.trankim.android.busmap.util.Config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;


public abstract class GetRequest extends Request {

	public abstract void onRequestComplete(String responseData);

	public abstract void onRequestFailed(ErrorData errorData);

	@Override
	public void send() {
		long now = System.currentTimeMillis();

		id = numOfRequests++;
		if (Config.LOG_REQUEST)
			Log.d(TAG, "Start request " + id);
		if (Config.LOG_REQUEST)
			Log.d(TAG, "URL = " + url);

		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		HttpEntity entity = null;
		String response = null;
		InputStream instream = null;

		try {
			httpResponse = httpClient.execute(httpGet);
			entity = httpResponse.getEntity();

			if (entity != null) {
				instream = entity.getContent();
				response = convertStreamToString(instream);
				if (Config.LOG_REQUEST) {
					Log.i(TAG,
							"Get response from network after "
									+ (System.currentTimeMillis() - now)
									+ " miliseconds");
				}
				instream.close();
			}

			if (Config.LOG_REQUEST)
				Log.d(TAG, "Response of request " + id + " = " + response);

			handleResponse(response);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			onRequestFailed(new ErrorData(e.getMessage()));
		} catch (IOException e) {
			e.printStackTrace();
			onRequestFailed(new ErrorData(e.getMessage()));
		}
	}

}
