package com.smarthome.installoruninstall;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;

public class ApkInfoXmlParse {
	public static final String ITEM = "item";
	Document mDoc = null;
	Context context;
	String data;
	String mXmlPath = null;

	public ApkInfoXmlParse(Context context, String xmlPath) {
		mXmlPath = xmlPath;
		this.context = context;
	}

	public ApkInfoXmlParse(String d) {
		data = d;
	}

	public static String apkInfoToXmlItem(AppInfo a) {

		StringBuilder sb = new StringBuilder();
		sb.append("<").append(ITEM).append(">");
		appendItem(sb, AppInfo.APP_NAME, a.getAppName());
		appendItem(sb, AppInfo.APP_PACKAGE_NAME, a.getPackageName());
	    appendItem(sb, AppInfo.APP_OPERATION, a.getOperation());
		appendItem(sb, AppInfo.APP_APK_URL, a.getApkUrl());
		sb.append("</").append(ITEM).append(">");
		return sb.toString();

	}

	public static String allApkInfoToXmlItems(List<AppInfo> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append("root").append(">");
		for (AppInfo a : list) {
			sb.append(apkInfoToXmlItem(a));
		}
		sb.append("</").append("root").append(">");
		return sb.toString();
	}

	private static StringBuilder appendItem(StringBuilder sb, String name,
			String value) {
		sb.append("<").append(name).append(">").append(value).append("</")
				.append(name).append(">");
		return sb;
	}
	
	private Boolean init(){
		if(mDoc != null){
			return true;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		}
		InputStream inputStream = null;
		try {
			//inputStream = new FileInputStream(new File(mXmlPath));
			if(context != null){
				inputStream = context.getResources().getAssets().open(mXmlPath);
			}else{
				inputStream = new ByteArrayInputStream(data.getBytes("UTF-8")); 
			}
		} catch (IllegalStateException e) {
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		} catch (NullPointerException e) {
			//e.printStackTrace();
			return false;
		}

		
		try {
			mDoc = db.parse(inputStream);
		} catch (SAXException e) {
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
		mDoc.getDocumentElement().normalize();
		return true;		
	}
	
	public List<AppInfo> parse(){
		if(init()==false){
			return null;
		}
		NodeList nodeList = mDoc.getElementsByTagName(ITEM);
		if (nodeList != null) {
			List<AppInfo> appInfoList = new ArrayList<AppInfo>(nodeList.getLength());
			for(int i = 0; i < nodeList.getLength(); i++){
				AppInfo a = new AppInfo();
				Node node = nodeList.item(i);
				NodeList nodeList2 = node.getChildNodes();
				for(int j = 0; j<nodeList2.getLength(); j++){
					Node node2 = nodeList2.item(j);
					if(AppInfo.APP_NAME.equals(node2.getNodeName()) ){
						a.setAppName(node2.getTextContent());
					}else if(AppInfo.APP_PACKAGE_NAME.equals(node2.getNodeName())){
						a.setPackageName(node2.getTextContent());
					}else if(AppInfo.APP_OPERATION.equals(node2.getNodeName())){
						a.setOperation(node2.getTextContent());
					}else if(AppInfo.APP_APK_URL.equals(node2.getNodeName())){
						a.setApkUrl(node2.getTextContent());
					}				
				}
				appInfoList.add(a);
			}
			return appInfoList;
		}
		
		return null;
	}
}
