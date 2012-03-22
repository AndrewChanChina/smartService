package com.smarthome.deskclock.online;

import java.io.File;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemProperties;
import android.util.Log;

public class DeviceFun{
	public static final Uri CONTENT_PASSWORD_URI = Uri.parse("content://" + 
			"cn.com.ehome.database.EHotelProvider" + "/"+"passinfo");
	
	public static String getDeviceID(){
		String CPUID =  SystemProperties.get(
				"ro.hardware.cpuid", "0");		
		return CPUID;
	}
	
	//取当前是不是管理员
	public static boolean IsAdmin(ContentResolver mContentResolver){
		Cursor mCursor;
		int admin;
		
		mCursor=mContentResolver.query(CONTENT_PASSWORD_URI, null, null, null, null);
		if(mCursor == null)
			return false;
		mCursor.moveToFirst();
		admin=mCursor.getInt(1);
		mCursor.close();
		mCursor.close();
		if (admin<1) {
			return false;
		}else {
			return true;
		}
	}
	public static String getRoomNum(ContentResolver mContentResolver){
		Cursor mCursor;
		String roomNum;
		
		mCursor=mContentResolver.query(CONTENT_PASSWORD_URI, null, null, null, null);
		if(mCursor == null)
			return null;
		mCursor.moveToFirst();
		roomNum=mCursor.getString(4);
		mCursor.close();
		mCursor.close();
		
		if(roomNum==null || roomNum.isEmpty()){
			roomNum = android.os.Build.PRODUCT;
    	}else if(roomNum.isEmpty()){
    		roomNum = android.os.Build.DEVICE;
    	}
		return roomNum;
	}
	
	public static void printDeviceInf(String tag){
		StringBuilder sb = new StringBuilder();
		sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
		sb.append("BOARD ").append(android.os.Build.BOARD).append("\n");
		sb.append("BOOTLOADER ").append(android.os.Build.BOOTLOADER).append("\n");
		sb.append("BRAND ").append(android.os.Build.BRAND).append("\n");
		sb.append("CPU_ABI ").append(android.os.Build.CPU_ABI).append("\n");
		sb.append("CPU_ABI2 ").append(android.os.Build.CPU_ABI2).append("\n");
		sb.append("DEVICE ").append(android.os.Build.DEVICE).append("\n");
		sb.append("DISPLAY ").append(android.os.Build.DISPLAY).append("\n");
		sb.append("FINGERPRINT ").append(android.os.Build.FINGERPRINT).append("\n");
		sb.append("HARDWARE ").append(android.os.Build.HARDWARE).append("\n");
		sb.append("HOST ").append(android.os.Build.HOST).append("\n");
		sb.append("ID ").append(android.os.Build.ID).append("\n");
		sb.append("MANUFACTURER ").append(android.os.Build.MANUFACTURER).append("\n");
		sb.append("MODEL ").append(android.os.Build.MODEL).append("\n");
		sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
		sb.append("RADIO ").append(android.os.Build.RADIO).append("\n");
		sb.append("SERIAL ").append(android.os.Build.SERIAL).append("\n");
		sb.append("TAGS ").append(android.os.Build.TAGS).append("\n");
		sb.append("TIME ").append(android.os.Build.TIME).append("\n");
		sb.append("TYPE ").append(android.os.Build.TYPE).append("\n");
		sb.append("USER ").append(android.os.Build.USER).append("\n");
		Log.i(tag,sb.toString());
	}
}