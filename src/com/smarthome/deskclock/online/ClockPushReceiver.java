package com.smarthome.deskclock.online;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ClockPushReceiver extends BroadcastReceiver {

	private static final String LOGTAG = "chenyz";
	private static final String tag = ClockPushReceiver.class.getSimpleName()
			+ "--";

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d(LOGTAG, tag + "onReceiver");

		if (intent.getAction().equals(PushServiceUtil.ACTION_REGISTRATION)) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals(PushServiceUtil.ACTION_RECEIVE)) {
			handleMessage(context, intent);
		} else if (intent.getAction().equals(PushServiceUtil.ACTION_STATUS)) {
			handleStatus(context, intent);
		} else {
			Log.e(LOGTAG, tag + "receiver error type");
		}
	}

	private void handleRegistration(Context context, Intent intent) {

		Log.d(LOGTAG, tag + "handleRegistration");

		String pushId = intent.getStringExtra(PushServiceUtil.PUSH_ID);
		String pustStatus = intent.getStringExtra(PushServiceUtil.PUSH_STATUS);

		Log.d(LOGTAG, tag + "Registration succuss and Id = " + pushId);
		Log.d(LOGTAG, tag + "Registration status = " + pustStatus);

		SharedPreferences sp = context.getSharedPreferences(
				PushNotificationUtil.PREFE_NAME, 0);
		sp.edit().putString(PushNotificationUtil.ID, pushId).commit();

	}

	/**
	 * 处理push过来的消息
	 * 
	 * @param context
	 * @param intent
	 */
	private void handleMessage(Context context, Intent intent) {

		Log.d(LOGTAG, tag + "handleMessage");
		String tille = intent.getStringExtra(PushServiceUtil.NTFY_TITLE);
		String ticker = intent.getStringExtra(PushServiceUtil.NTFY_TICKER);
		String message = intent.getStringExtra(PushServiceUtil.NTFY_MESSAGE);
		String uriString = intent.getStringExtra(PushServiceUtil.NTFY_URI);
		
		// 操作数据库
		
		// 请求网络
		
		
	}

	/**
	 * 接收连接状态的反馈信息
	 * 
	 * @param context
	 * @param intent
	 */
	private void handleStatus(Context context, Intent intent) {
		String status = intent.getStringExtra(PushServiceUtil.PUSH_STATUS);

	}

}
