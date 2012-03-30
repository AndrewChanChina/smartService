package com.smarthome.installoruninstall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class InstallAndUninstallListener extends BroadcastReceiver {

	private static final String tag = InstallAndUninstallListener.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		PackageManager manager = context.getPackageManager();

		if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			Log.i(tag, "---收到安装新apk完成的广播");
			String appName = "";
			String packageName = intent.getData().getSchemeSpecificPart();

			try {
				ApplicationInfo appInfo = manager.getApplicationInfo(
						packageName, 0);
				appName = appInfo.loadLabel(manager).toString();
				Log.i("InstallAndUninstallListener", "pack_add_pkgname="
						+ packageName);
				Log.i("InstallAndUninstallListener", "pack_add_appname="
						+ appName);
				Intent intent1 = new Intent(context, PostApkInfoService.class);
				intent1.putExtra(PushApkServiceUtil.OPERATION, "install");
				intent1.putExtra(AppInfo.APP_NAME, appName);
				intent1.putExtra(AppInfo.APP_PACKAGE_NAME, packageName);
				context.startService(intent1);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
			Log.i(tag, "---收到卸载apk完成的广播");
			String appName = "";
			final String packageName = intent.getData().getSchemeSpecificPart();
			try {
				ApplicationInfo appInfo = manager.getApplicationInfo(packageName, 0);
				appName = appInfo.loadLabel(manager).toString();
				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("InstallAndUninstallListener", "pack_remove_pkgname="
					+ packageName);
			Log.i("InstallAndUninstallListener", "pack_remove_appname="
					+ appName);
			Intent intent1 = new Intent(context, PostApkInfoService.class);
			intent1.putExtra(PushApkServiceUtil.OPERATION, "uninstall");
			intent1.putExtra(AppInfo.APP_NAME, appName);
			intent1.putExtra(AppInfo.APP_PACKAGE_NAME, packageName);
			context.startService(intent1);
		}
	}
};