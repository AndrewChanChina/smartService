package com.smarthome.deskclock.online;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.smarthome.deskclock.Alarm;
import com.smarthome.deskclock.Alarms;
import com.smarthome.deskclock.online.PushServiceUtil;
import com.smarthome.deskclock.online.AlarmXmlParse.Operation;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ParseClockMessageService extends IntentService {

	public static final String TITLE = "batch_install";
	public static final String SAVE_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Android/data/com.smarthome.alarmclock/files";
	private DownloadManager dm;

	public ParseClockMessageService() {
		super("ParseClockMessageService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		String msg = intent.getStringExtra(PushServiceUtil.NTFY_MESSAGE);
		String cmd = intent.getStringExtra(PushServiceUtil.NTFY_URI);
		Log.i("ParseReceivedMessageService", "�յ��������˷���������Ϣ---" + msg);
		if (cmd.equals("set")) {//����ɾ���Ĳ���
			HttpPostDataUtil.addAlarm(this, msg);

		} else if (cmd.equals("list")) {
			// �ϴ��������е�������Ϣ
			Log.i(PostResultService.LOG_TAG, "---��ʼ�ϴ����ص�����������Ϣ:");
			String allAlarmsXml = HttpPostDataUtil.getListAll(this);
			Intent postIntent = new Intent(this, PostResultService.class);
			Log.i(PostResultService.LOG_TAG, "---���ص�����������Ϣ:" + allAlarmsXml);
			postIntent.putExtra(PostResultService.DATA, allAlarmsXml);
			postIntent.putExtra(PostResultService.URL,
					"http://192.168.0.195:8080/pushcmsserver/hotel.do");
			startService(postIntent);
		}

	}


}
