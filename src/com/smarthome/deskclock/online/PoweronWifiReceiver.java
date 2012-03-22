package com.smarthome.deskclock.online;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class PoweronWifiReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {	
	
		Log.d(PostResultService.LOG_TAG, "ConnectivityReceiver.onReceive()...");
	    String action = intent.getAction();
	    Log.d(PostResultService.LOG_TAG, "action=" + action);
	
	    ConnectivityManager connectivityManager = (ConnectivityManager) context
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	
	    if (networkInfo != null) {
	        Log.d(PostResultService.LOG_TAG, "Network Type  = " + networkInfo.getTypeName());
	        Log.d(PostResultService.LOG_TAG, "Network State = " + networkInfo.getState());
	        if (networkInfo.isConnected()) {
	            Log.i(PostResultService.LOG_TAG, "Network connected");
	             SharedPreferences sp = context.getSharedPreferences(
				PushNotificationUtil.PREFE_NAME, 0);
	             String id = sp.getString(PushNotificationUtil.ID,"");
	             if(id == null || id.isEmpty()){
	            	 PushNotificationUtil.regPushService(context, true);
	             }        
	        }
	    } else {
	        Log.e(PostResultService.LOG_TAG, "Network unavailable");        
	    }
	}
}
