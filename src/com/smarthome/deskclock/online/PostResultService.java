package com.smarthome.deskclock.online;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.smarthome.installoruninstall.PushApkServiceUtil;
import com.smarthome.until.GobalFinalData;
import com.smarthome.until.XmlPreference;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PostResultService extends IntentService {

	public final static String LOG_TAG = "chenyz";
	public final static String URL = "url";
	public final static String DATA = "data";
	private String ALARM_POST_URL = "http://192.168.0.195:8080/pushcmsserver/hotel.do";
	private String AlARM_PUSHID_URL = "http://localhost:8080/pushcmsserver/hotel.do?opt=pushServiceInfo";
	
	public PostResultService() {
		super("PostResultService");
	}
	
	private void init(){
		XmlPreference xmlPreference;
		if(GobalFinalData.ISDEBUG){
			xmlPreference = new XmlPreference(true,this,GobalFinalData.INIT_FILE);
		}else{
			xmlPreference = new XmlPreference(GobalFinalData.CONFIG_FILE);
		}
		String value = xmlPreference.getKeyValue("alarm_post_url");
		if (value != null && value.isEmpty() == false) {
			ALARM_POST_URL = value;
		}
		value = xmlPreference.getKeyValue("id_post_url");
		if (value != null && value.isEmpty() == false) {
			AlARM_PUSHID_URL = value;
		}
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		init();
		String operation = intent.getStringExtra(PushServiceUtil.OPERATION);
		if(operation!=null){
			if(operation.equals("postId")){//上传pushId
				String id = intent.getStringExtra(PushServiceUtil.PUSH_ID);
				Log.i("PostResultService","---开始上传闹钟的pushID=" + id);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair(PushApkServiceUtil.ROOMNUM,DeviceFun.getRoomNum(getContentResolver())));
				nvps.add(new BasicNameValuePair(PushApkServiceUtil.PUSHID,id));
				nvps.add(new BasicNameValuePair(PushApkServiceUtil.PUSH_SERVICE_ID,"GVTO6mcPcNGm3556786E8KL48M9L87rr"));
				try {
					HttpPostDataUtil.httpPostData(AlARM_PUSHID_URL, nvps);
				} catch (MyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}else{
			String data = intent.getStringExtra(DATA);
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("opt","hotelinfo"));
			nvps.add(new BasicNameValuePair("roomNum",DeviceFun.getRoomNum(getContentResolver())));
			nvps.add(new BasicNameValuePair(DATA,data));
			
			try {
				HttpPostDataUtil.httpPostData(ALARM_POST_URL,nvps);
			} catch (MyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	

	
}
