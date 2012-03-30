package com.smarthome.installoruninstall;

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
import com.smarthome.deskclock.online.DeviceFun;
import com.smarthome.deskclock.online.MyException;
import com.smarthome.deskclock.online.PushServiceUtil;
import com.smarthome.until.GobalFinalData;
import com.smarthome.until.XmlPreference;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class PostApkInfoService extends IntentService{
	private static final String tag = PostApkInfoService.class.getSimpleName();
	private static String SERVER_APK_URL = "http://192.168.0.195:8080/pushcmsserver/apk_webservice.do?";
	private static String SERVER_PUSHID_URL = "http://192.168.0.195:8080/pushcmsserver/hotel.do?opt=pushServiceInfo";
    
	public PostApkInfoService() {
		super("PostApkInfoService");
		init();
	}

	private void init(){
		XmlPreference xmlPreference;
		if(GobalFinalData.ISDEBUG){
			xmlPreference = new XmlPreference(true,this,GobalFinalData.INIT_FILE);
		}else{
			xmlPreference = new XmlPreference(GobalFinalData.CONFIG_FILE);
		}
		String value = xmlPreference.getKeyValue("apk_post_url");
		if (value != null && value.isEmpty() == false) {
			SERVER_APK_URL = value;
		}
		value = xmlPreference.getKeyValue("id_post_url");
		if (value != null && value.isEmpty() == false) {
			SERVER_PUSHID_URL = value;
		}
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String operation = intent.getStringExtra(PushApkServiceUtil.OPERATION);
		if(operation.equals("post")){
			List<AppInfo> list = HttpPostApkInfo.getInstalledApps(this);
			startPost(list);
		}else if(operation.equals("install")){
			List<AppInfo> list = new ArrayList<AppInfo>();
			String appName = intent.getStringExtra(AppInfo.APP_NAME);
			String pkgName = intent.getStringExtra(AppInfo.APP_PACKAGE_NAME);
			AppInfo appInfo = new AppInfo();
			appInfo.setAppName(appName);
			appInfo.setPackageName(pkgName);
			appInfo.setOperation(PushApkServiceUtil.INSTALL_PACKAGE);
			list.add(appInfo);
			startPost(list);
			
		}else if(operation.equals("uninstall")){
			List<AppInfo> list = new ArrayList<AppInfo>();
			String appName = intent.getStringExtra(AppInfo.APP_NAME);
			String pkgName = intent.getStringExtra(AppInfo.APP_PACKAGE_NAME);
			AppInfo appInfo = new AppInfo();
			appInfo.setAppName(appName);
			appInfo.setPackageName(pkgName);
			appInfo.setOperation(PushApkServiceUtil.UNINSTALL_PACKAGE);
			list.add(appInfo);
			startPost(list);
		}else if(operation.equals("postId")){
			String id = intent.getStringExtra(PushServiceUtil.PUSH_ID);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair(PushApkServiceUtil.ROOMNUM,DeviceFun.getRoomNum(getContentResolver())));
			nvps.add(new BasicNameValuePair(PushApkServiceUtil.PUSHID,id));
			nvps.add(new BasicNameValuePair(PushApkServiceUtil.PUSH_SERVICE_ID,"GVTO6mcPcNGm3556786E8KL48M9L87rr"));
			try {
				//httpPostApknfo(SERVER_APK_URL, nvps);
				httpPostApknfo(SERVER_PUSHID_URL, nvps);
			} catch (MyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void httpPostApknfo(String url, List<NameValuePair> params) throws MyException{
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
			Log.i("PostApkInfoService---", status);
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
	
	private void startPost(List<AppInfo> list){
		String appInfoXml = HttpPostApkInfo.getAllApkInfo(list);
		Log.i(tag,"---上传apk信息为：" + appInfoXml);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(PushApkServiceUtil.ROOMNUM,DeviceFun.getRoomNum(getContentResolver())));
		nvps.add(new BasicNameValuePair(PushApkServiceUtil.APPINFO,appInfoXml));
		nvps.add(new BasicNameValuePair(PushApkServiceUtil.TYPE,"list"));
		try {
			httpPostApknfo(SERVER_APK_URL, nvps);
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
