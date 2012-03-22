package com.smarthome.deskclock.online;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushNotificationUtil {
	
	public static String PREFE_NAME = "record";
	public static String ID = "id";
	
	public static void regPushService(Context context,boolean bReg) {
		Intent regIntent = new Intent(PushServiceUtil.ACTION_SERVICE_REGISTER);

		if (bReg) {
			regIntent.putExtra(PushServiceUtil.PUSH_TYPE,
					PushServiceUtil.PUSH_TYPE_REG);
		} else {
			regIntent.putExtra(PushServiceUtil.PUSH_TYPE,
					PushServiceUtil.PUSH_TYPE_UNREG);
		}

		regIntent.putExtra(PushServiceUtil.PUSH_DEVELOPER, "admin");
		regIntent.putExtra(PushServiceUtil.PUSH_NAME_KEY,
				"T3aXoTF0oz8nIbqCBdEq34a00O67raaa");
		regIntent.putExtra(PushServiceUtil.PUSH_CATEGORY, "com.smarthome.deskclock");

		// TODO should I stop service
		context.startService(regIntent);
		Log.e(PostResultService.LOG_TAG, "start register alarm clock service"); 
	}

}
