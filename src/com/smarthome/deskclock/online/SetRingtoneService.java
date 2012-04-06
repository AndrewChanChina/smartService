package com.smarthome.deskclock.online;

import java.util.List;

import com.smarthome.deskclock.Alarm;
import com.smarthome.deskclock.Alarms;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SetRingtoneService extends IntentService {

	public SetRingtoneService() {
		super("SetRingtoneService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		long downId = intent.getLongExtra(
				MusicDownCompleteReceiver.DOWNLOAD_ID, -1);
		if (downId != -1) {
			Cursor c = dm.query(new DownloadManager.Query()
					.setFilterById(downId));
			if (c.moveToFirst()) {
				int index = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
				String title = c.getString(index);
				Log.i("SetRingtoneService", "---下载的标题=" + title);
				int stateIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
				int state = Integer.valueOf(c.getString(stateIndex));
				if(state == DownloadManager.STATUS_SUCCESSFUL){
					if (title.equals(HttpPostDataUtil.MUSIC_TITLE)) {
						// 音乐下载完成，设置闹钟的铃声
						int idIndex = c
								.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION);
						String des = c.getString(idIndex);
						int indexLocal = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
						 String localUri = c.getString(indexLocal);
						 String filePath = localUri.substring("file://".length());
						 Log.i("SetRingtoneService","---apk 文件路径 : " + filePath);
						AlarmXmlParse xmlParse = new AlarmXmlParse(des);
						List<Alarm> list = xmlParse.parse();
						if(list!=null && !list.isEmpty()){
							for (Alarm a : list) {
								a.alert = Uri.parse(filePath);
								Log.i("SetRingtoneService","---设置的铃声---" + a.alert.toString());
								Alarms.setAlarm(this, a);
								HttpPostDataUtil.postAlarmId(this, a);
							}
						}
				     }
				
				}
			}
			c.close();
		}
	}

}
