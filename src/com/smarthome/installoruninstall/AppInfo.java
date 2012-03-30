package com.smarthome.installoruninstall;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AppInfo implements Parcelable{
	public static final String APP_NAME = "appname";
	public static final String APP_PACKAGE_NAME = "packagename";
	public static final String APP_OPERATION = "operation";
	public static final String APP_APK_URL = "apkurl";
	
	private String appName; 
	private String packageName;  
	private String operation;
	private String apkUrl;
    
	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}



	public void appInfoPrint() { 
    	
    	 Log.i("AppInformation:","appname="  + appName);
    	 Log.i("AppInformation:","pname="  + packageName);
    }

	public static final Parcelable.Creator<AppInfo> CREATOR = new Creator<AppInfo>() {

		@Override
		public AppInfo createFromParcel(Parcel arg0) {
			// TODO Auto-generated method stub
			AppInfo appInfo = new AppInfo();  
			appInfo.appName = arg0.readString();  
			appInfo.packageName = arg0.readString();  
			appInfo.operation = arg0.readString(); 
			appInfo.apkUrl = arg0.readString(); 
	        return appInfo;  
		}

		@Override
		public AppInfo[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new AppInfo[arg0];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(appName);  
        arg0.writeString(packageName);
        arg0.writeString(operation);
        arg0.writeString(apkUrl);
	}     
}
