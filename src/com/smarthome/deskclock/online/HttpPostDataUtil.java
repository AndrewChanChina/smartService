package com.smarthome.deskclock.online;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.smarthome.deskclock.Alarm;
import com.smarthome.deskclock.Alarms;
import com.smarthome.installoruninstall.PushApkServiceUtil;
import com.smarthome.until.GobalFinalData;
import com.smarthome.until.XmlPreference;

public class HttpPostDataUtil {

	private static final String SAVE_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Android/data/com.smarthome.alarmclock/files/music";
	public static final String MUSIC_TITLE = "alarm_music";

	public static void postOperationAlarm(Context c, Alarm a) {
		Intent intent = new Intent(c, PostResultService.class);
		String alarmXml = AlarmXmlParse.alarmToXmlItem(a);
		Log.i(PostResultService.LOG_TAG, "send alarm data to server:"
				+ alarmXml);
		intent.putExtra(PostResultService.DATA, alarmXml);
		// intent.putExtra(PostResultService.URL,
		// "http://192.168.0.195:8080/pushcmsserver/hotel.do");
		c.startService(intent);
	}

	public static String getListAll(Context context) {
		ContentResolver cr = context.getContentResolver();
		StringBuilder sb = new StringBuilder();
		Cursor cursor = Alarms.getAlarmsCursor(cr);
		if (null == cursor) {
			return sb.toString();
		}

		cursor.moveToFirst();
		List<Alarm> la = new ArrayList<Alarm>(cursor.getCount());
		while (cursor.isAfterLast() == false) {
			Alarm a = new Alarm(cursor);
			la.add(a);
			cursor.moveToNext();
		}
		return AlarmXmlParse.alarmsToXmlItems(la);

	}

	public static boolean addAlarm(Context context, String data) {

		AlarmXmlParse ap = new AlarmXmlParse(data);
		List<Alarm> la = ap.parse();
		if (null == la) {
			return false;
		}
		for (Alarm a : la) {
			switch (AlarmXmlParse.Operation.mapString(a.operation)) {
			case AlarmXmlParse.Operation.ADD:
				Alarms.addAlarm(context, a);
				// 请求服务器端“添加”操作的地址
				postAlarmId(context, a);
				if (a.musicPath != null
						&& !a.musicPath.isEmpty()
						&& Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
					downloadMusic(context, a.musicPath, a);
				}
				break;
			case AlarmXmlParse.Operation.DELETE:
				Alarms.deleteAlarm(context, a.id);
				// 请求服务器端“删除”操作的地址
				postAlarmId(context, a);
				break;
			case AlarmXmlParse.Operation.UPDATE:
				if (a.musicPath != null
						&& !a.musicPath.isEmpty()
						&& Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
					downloadMusic(context, a.musicPath, a);
				}
				Alarms.setAlarm(context, a);
				// 请求服务器端“修改”操作的地址
				postAlarmId(context, a);
				break;
			}

		}
		return true;
	}

	// 下载操作
	private static void downloadMusic(Context context, String url, Alarm a) {
		DownloadManager dm = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		int index = url.lastIndexOf("/");
		String sub = url.substring(0, index);
		int lastIndex = sub.lastIndexOf("/");
		String musicName = url.substring(lastIndex + 1).replace("/", "_");
		File file = new File(SAVE_DIRECTORY, musicName);
		if (!file.exists()) {
			DownloadManager.Request request = new DownloadManager.Request(Uri
					.parse(url));
			request
					.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
			request.setAllowedOverRoaming(false);
			request.setTitle(MUSIC_TITLE);
			request.setDescription(AlarmXmlParse.alarmToXmlItem(a));
			request.setDestinationInExternalFilesDir(context, "/music",
					musicName);
			request.setVisibleInDownloadsUi(false);
			request.setShowRunningNotification(false);
			dm.enqueue(request);
		} else {
			a.alert = Uri.parse(file.getAbsolutePath());
			Alarms.setAlarm(context, a);
		}

	}

	public static void httpPostData(String url, List<NameValuePair> params)
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
			Log.i(PostResultService.LOG_TAG, status);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

	static void postAlarmId(Context context,Alarm a){
    	String url = a.commit_url.replace('*','&');
    	String desUrl = url + "true&id=" + a.id;
    	Log.i("HttpPostDataUtil","---提交alarm_id的地址---" + desUrl);
    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(PushApkServiceUtil.ROOMNUM,DeviceFun.getRoomNum(context.getContentResolver())));
		try {
			HttpPostDataUtil.httpPostData(desUrl, nvps);
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
