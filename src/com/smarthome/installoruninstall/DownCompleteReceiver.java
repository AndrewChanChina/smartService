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
	
	private static final String tag = DownCompleteReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i(tag,"---" + intent.getAction());
		DownloadManager dm = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
		if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
			long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);  
			Log.i("DownCompleteReceiver","---download complete! id : "+downId); 
			if(downId!=-1){
				 Cursor c = dm.query(new DownloadManager.Query().setFilterById(downId));
				 if(c.moveToFirst()){
					 int index = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
					 String title = c.getString(index);
					 Log.i("DownCompleteReceiver","---���صı���=" + title);
					 if(title.equals(ParseReceivedMessageService.TITLE)){
						 int indexLocal = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
						 String localUri = c.getString(indexLocal);
						 String filePath = localUri.substring("file://".length());
						 Log.i("DownCompleteReceiver","---apk �ļ�·�� : " + filePath);
						 Intent intentInstall = new Intent(PushApkServiceUtil.ACTION_PACKAGEINSTALLER_INSTALL);
						 intentInstall.putExtra("apkPath", filePath);
				         context.sendBroadcast(intentInstall);
					 }
			}
				 
				 
		}
			
			//����apk�ɹ�,��ʼ������װ
//            File file = (File)msg.obj;
//            Intent intent = new Intent(Constant.ACTION_PACKAGEINSTALLER_INSTALL);
//            intent.putExtra("apkPath", file.getAbsolutePath());
//            sendBroadcast(intent);
//            Log.i("ApkDownloadService---" ,"���سɹ�" + file.getAbsolutePath());
		}
		/*if (intent.getAction().equals(Constant.ACTION_POST_APK_INFO)) {
			Log.i(tag,"---�յ��ϴ�apk��Ϣ�Ĺ㲥");
			Intent intentPost = new Intent(context,PostApkInfoService.class);
			intentPost.setAction(Constant.ACTION_POST_APK_INFO);
			context.startService(intentPost);

		} else if (intent.getAction().equals(Constant.ACTION_INSTALL_APK)) {
    
			//��������apk����
			Log.i(tag,"---�յ�������װapk�Ĺ㲥");
			String data = intent.getStringExtra("DATA");
			Intent intent1 = new Intent(context,ApkDownloadService.class);
			intent1.putExtra("DATA", data);
			context.startService(intent1);
		
		} else if (intent.getAction().equals(Constant.ACTION_UNINSTALL_APK)) {

			//����ж��apk
			Log.i(tag,"---�յ�����ж��apk�Ĺ㲥");
//			String data = intent.getStringExtra("DATA");
//			ApkInfoXmlParse xmlParse = new ApkInfoXmlParse(data);
//			List<AppInfo> list = xmlParse.parse();
//			ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
//			if(list!=null && list.isEmpty()==false){
//				for(int i=0;i<list.size();i++){
//					appList.add(list.get(i));
//				}
//			}
			Intent intent1 = new Intent(Constant.ACTION_PACKAGEINSTALLER_UNINSTALL);
//			intent1.putParcelableArrayListExtra("appList",appList);
			context.sendBroadcast(intent1);
		} else {
			Log.e(tag, "receiver error type");
		}*/
	}

}
