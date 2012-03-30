package com.smarthome.installoruninstall;

import java.util.ArrayList;
import java.util.List;

import com.smarthome.deskclock.Alarm;
import com.smarthome.deskclock.Alarms;
import com.smarthome.deskclock.online.AlarmXmlParse;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class HttpPostApkInfo {
	

	public static String getAllApkInfo(List<AppInfo> list) {
	
		return ApkInfoXmlParse.allApkInfoToXmlItems(list);
	}
	
	// 取得用户安装的所有应用程序
	public static List<AppInfo> getInstalledApps(Context context) {
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		PackageManager packManager = context.getPackageManager();
		final List<PackageInfo> packs = packManager.getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo pkgInfo = packs.get(i);
			//判断是否为非系统预装的应用程序  
			if((pkgInfo.applicationInfo.flags & pkgInfo.applicationInfo.FLAG_SYSTEM)<=0){
				AppInfo appInfo = new AppInfo();
				appInfo.setAppName(pkgInfo.applicationInfo.loadLabel(packManager).toString().trim());
				appInfo.setPackageName(pkgInfo.packageName.trim());
				appInfo.setOperation("install");
				appInfoList.add(appInfo);
			}
		}
		return appInfoList;
	}
	
	public static boolean operateClient(Context context, String data) {
		ApkInfoXmlParse ap = new ApkInfoXmlParse(data);
		List<AppInfo> list = ap.parse();
		if (null == list) {
			return false;
		}
		for (AppInfo a : list) {
			if(a.getOperation().equals(PushApkServiceUtil.INSTALL_PACKAGE)){
				
			}else if(a.getOperation().equals(PushApkServiceUtil.UNINSTALL_PACKAGE)){
				
			}
		}
		return true;
	}
	
}
