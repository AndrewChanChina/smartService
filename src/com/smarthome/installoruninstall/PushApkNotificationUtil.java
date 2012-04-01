package com.smarthome.installoruninstall;

import com.smarthome.deskclock.online.PostResultService;
import com.smarthome.deskclock.online.PushServiceUtil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushApkNotificationUtil {
	
	public static String PREFE_NAME = "apk_record";
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
				"tmticb0yfyRl4O71gXTxpbiTC92DvWFf");
		regIntent.putExtra(PushServiceUtil.PUSH_CATEGORY, "com.smarthome.installoruninstall");

		// TODO should I stop service
		context.startService(regIntent);
		Log.e(PostResultService.LOG_TAG, "start register apk service"); 
	}

}
