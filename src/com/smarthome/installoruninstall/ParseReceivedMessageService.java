package com.smarthome.installoruninstall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.smarthome.deskclock.online.PushServiceUtil;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ParseReceivedMessageService extends IntentService {

	public static final String TITLE = "batch_install";
	public static final String SAVE_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Android/data/com.smarthome.alarmclock/files";
	private DownloadManager dm;

	public ParseReceivedMessageService() {
		super("ParseReceivedMessageService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		String msg = intent.getStringExtra(PushServiceUtil.NTFY_MESSAGE);
		String cmd = intent.getStringExtra(PushServiceUtil.NTFY_URI);
		Log.i("ParseReceivedMessageService", "收到服务器端发过来的消息---" + msg);
		ArrayList<AppInfo> appUninstallList = new ArrayList<AppInfo>();
		ArrayList<AppInfo> appInstallList = new ArrayList<AppInfo>();
		if (cmd.equals("set")) {
			// 解析msg
			if (!msg.isEmpty()) {
				ApkInfoXmlParse xmlParse = new ApkInfoXmlParse(msg);
				List<AppInfo> list = xmlParse.parse();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						AppInfo app = list.get(i);
						Log.i("ParseReceivedMessageService", "---apk应用程序名"
								+ app.getAppName());
						if (app.getOperation().equals(PushApkServiceUtil.INSTALL_PACKAGE)) {
							appInstallList.add(app);
						} else if (app.getOperation().equals(
								PushApkServiceUtil.UNINSTALL_PACKAGE)) {
							appUninstallList.add(app);
						}
					}
				}
			}
			if (appUninstallList.size() > 0) { // 开始批量卸载
				String[] uninstallArray = new String[appUninstallList.size()];
				for(int i = 0; i<appUninstallList.size(); i++){
					uninstallArray[i] = appUninstallList.get(i).getPackageName();
				}
				Intent uninstallIntent = new Intent(
						PushApkServiceUtil.ACTION_PACKAGEINSTALLER_UNINSTALL);
				uninstallIntent.putExtra("appList",
						uninstallArray);
				this.sendBroadcast(uninstallIntent);
			}
			if (appInstallList.size() > 0
					&& Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {// 开始批量下载并安装
				// 启动下载apk服务
				File dir = new File(SAVE_DIRECTORY);
				if (dir.exists()) {
					File[] files = dir.listFiles();
					for (File f : files) {
						f.delete();
					}
				} 
				for (int i = 0; i < appInstallList.size(); i++) {
					download(appInstallList.get(i).getApkUrl());
				}
			}
		} else if (cmd.equals("list")) {
			// 上传本地apk信息
			Intent intent1 = new Intent(this, PostApkInfoService.class);
			intent1.putExtra(PushApkServiceUtil.OPERATION, "post");
			startService(intent1);
		}

	}

	private void download(String url) {
		int index = url.lastIndexOf("/");
	    String apkName = url.substring(index+1);
	    File file = new File(SAVE_DIRECTORY, apkName);
	    if(file.exists()){
	        file.delete();
	    }
		DownloadManager.Request request = new DownloadManager.Request(Uri
				.parse(url));
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		request.setTitle(TITLE);
		request.setDestinationInExternalFilesDir(this, null,
				apkName);
		request.setVisibleInDownloadsUi(false);
		request.setShowRunningNotification(false);
		dm.enqueue(request);
	}

}
