package com.smarthome.deskclock.online;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class MusicDownCompleteReceiver extends BroadcastReceiver {

	private static final String tag = MusicDownCompleteReceiver.class
			.getSimpleName();
	public static final String DOWNLOAD_ID = "download_id";

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i(tag, "---" + intent.getAction());

		if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			long downId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			Intent setRingIntent = new Intent(context, SetRingtoneService.class);
			setRingIntent.putExtra(DOWNLOAD_ID, downId);
			Log.i("DownCompleteReceiver", "---download complete! id : "
					+ downId);
			context.startService(setRingIntent);
		}

	}

}
