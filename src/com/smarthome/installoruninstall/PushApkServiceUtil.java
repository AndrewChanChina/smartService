package com.smarthome.installoruninstall;

public class PushApkServiceUtil {

	public static final String OPERATION = "operation";
	
	/*
	 * �㲥��PackageInstaller
	 */
	public static final String ACTION_PACKAGEINSTALLER_INSTALL = "com.smit.smartService.BATCH_INSTALL_APK";
	
	public static final String ACTION_PACKAGEINSTALLER_UNINSTALL = "com.smit.smartService.BATCH_UNINSTALL_APK";
	
	
	/*
	 * ����AppInfo���е�operation����ȡֵ
	 */
	public static final String INSTALL_PACKAGE = "install";
	public static final String UNINSTALL_PACKAGE = "uninstall";
	
	
	/*
	 * ApkPushReceiver���յĹ㲥
	 */
	public static final String ACTION_REGISTRATION = "com.openims.pushApkService.REGISTRATION";
    public static final String ACTION_RECEIVE = "com.openims.pushApkService.RECEIVE"; 
    public static final String ACTION_STATUS = "com.openims.pushApkService.CONNECT_STATUS";
    
    /*
     * post����ʱ����Ҫ�Ĳ���
     */
    public static final String APPINFO = "data";
    public static final String ROOMNUM = "roomNum";
    public static final String TYPE = "type";
    public static final String PUSHID = "pushid";
    public static final String PUSH_SERVICE_ID = "pushserviceid";
    
}
