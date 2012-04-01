package com.smarthome.installoruninstall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class DownCompleteReceiver extends BroadcastReceiver {

	private static final String tag = DownCompleteReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i(tag, "---" + intent.getAction());
		DownloadManager dm = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			long downId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			Log.i("DownCompleteReceiver", "---download complete! id : "
					+ downId);
			if (downId != -1) {
				Cursor c = dm.query(new DownloadManager.Query()
						.setFilterById(downId));
				if (c.moveToFirst()) {
					int index = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
					String title = c.getString(index);
					Log.i("DownCompleteReceiver", "---下载的标题=" + title);
					int stateIndex = c
							.getColumnIndex(DownloadManager.COLUMN_STATUS);
					int state = Integer.valueOf(c.getString(stateIndex));
					if (state == DownloadManager.STATUS_SUCCESSFUL
							&& title.equals(ParseReceivedMessageService.TITLE)) {
						int indexLocal = c
								.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
						String localUri = c.getString(indexLocal);
						String filePath = localUri
								.substring("file://".length());
						Log.i("DownCompleteReceiver", "---apk 文件路径 : "
								+ filePath);
						Intent intentInstall = new Intent(
								PushApkServiceUtil.ACTION_PACKAGEINSTALLER_INSTALL);
						intentInstall.putExtra("apkPath", filePath);
						context.sendBroadcast(intentInstall);
					}
				}
				c.close();

			}
		}
	}

}
