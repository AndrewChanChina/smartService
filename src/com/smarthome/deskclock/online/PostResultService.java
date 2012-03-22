package com.smarthome.deskclock.online;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PostResultService extends IntentService {

	public final static String LOG_TAG = "chenyz";
	public final static String URL = "url";
	public final static String DATA = "data";

	public PostResultService() {
		super("PostResultService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String url = intent.getStringExtra(URL);
		String data = intent.getStringExtra(DATA);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("opt","hotelinfo"));
		nvps.add(new BasicNameValuePair("roomNum",DeviceFun.getRoomNum(getContentResolver())));
		nvps.add(new BasicNameValuePair(DATA,data));
		
		try {
			httpPostData(url,nvps);
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void httpPostData(String url, List<NameValuePair> params)
			throws MyException {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);

		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost post = new HttpPost(url);

		UrlEncodedFormEntity p_entity = null;
		try {
			p_entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		post.setEntity(p_entity);

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new MyException(MyException.TYPE_BAD_NETWORK,
					"ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			throw new MyException(MyException.TYPE_BAD_NETWORK, "IOException");
		}
		try {
			String status = convertStreamToString(response.getEntity()
					.getContent());
			Log.i(LOG_TAG, status);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
}
