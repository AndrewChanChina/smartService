package com.smarthome.deskclock.online;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.smarthome.deskclock.Alarm;
import com.smarthome.deskclock.Alarms;

public class HttpPostDataUtil {

	
	private Context context;

	public HttpPostDataUtil(Context c) {
		context = c;
	}

	public static void postOperationAlarm(Context c, Alarm a) {
		Intent intent = new Intent(c, PostResultService.class);
		String alarmXml = AlarmXmlParse.alarmToXmlItem(a);
		Log.i(PostResultService.LOG_TAG, "send alarm data to server:" + alarmXml);
		intent.putExtra(PostResultService.DATA, alarmXml);
		intent.putExtra(PostResultService.URL,
				"http://192.168.0.195:8080/pushcmsserver/hotel.do");
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
				break;
			case AlarmXmlParse.Operation.DELETE:
				Alarms.deleteAlarm(context, a.id);
				break;
			case AlarmXmlParse.Operation.UPDATE:
				Alarms.setAlarm(context, a);
				break;
			}

		}
		return true;
	}

}
