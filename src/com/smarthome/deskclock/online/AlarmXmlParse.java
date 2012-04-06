package com.smarthome.deskclock.online;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.net.Uri;

import com.smarthome.deskclock.Alarm;

public class AlarmXmlParse {
	
	public static final String ITEM = "item";
	
	Document mDoc = null;
	Context context;
	String data;
	String mXmlPath = null;
	
	public AlarmXmlParse(Context context, String xmlPath){
		mXmlPath = xmlPath;
		this.context = context;
	}
	
	public AlarmXmlParse(String d){
		data = d;
	}

	public static String alarmToXmlItem(Alarm a){
		
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(ITEM).append(">");
		appendItem(sb,Alarm.Columns._ID,a.id);
		appendItem(sb,Alarm.Columns.HOUR,a.hour);
		appendItem(sb,Alarm.Columns.MINUTES,a.minutes);
		appendItem(sb,Alarm.Columns.DAYS_OF_WEEK,a.daysOfWeek.getCoded());
		appendItem(sb,Alarm.Columns.ALARM_TIME,a.time);
		appendItem(sb,Alarm.Columns.ENABLED,a.enabled?1:0);
		appendItem(sb,Alarm.Columns.VIBRATE,a.vibrate?1:0);
		//appendItem(sb,Alarm.Columns.MESSAGE,a.);
		appendItem(sb,Alarm.Columns.ALERT,a.alert.toString());
		appendItem(sb,Alarm.Columns.OPERATION,a.operation);
		appendItem(sb,Alarm.Columns.MUSIC,a.musicPath);
		sb.append("</").append(ITEM).append(">");
		return sb.toString();
		
	}

	public static String alarmsToXmlItems(List<Alarm> la){
		StringBuilder sb = new StringBuilder();
		sb.append("<").append("root").append(">");
		for(Alarm a : la){
			sb.append(alarmToXmlItem(a));
		}
		sb.append("</").append("root").append(">");
		return sb.toString();
	}
	
	private static StringBuilder appendItem(StringBuilder sb, String nodeName,
			String value) {
		sb.append("<").append(nodeName).append(">").append(value).append("</")
				.append(nodeName).append(">");
		return sb;
	}
	private static StringBuilder appendItem(StringBuilder sb, String nodeName,
			int value) {
		sb.append("<").append(nodeName).append(">").append(value).append("</")
				.append(nodeName).append(">");
		return sb;
	}
	private static StringBuilder appendItem(StringBuilder sb, String nodeName,
			long value) {
		sb.append("<").append(nodeName).append(">").append(value).append("</")
				.append(nodeName).append(">");
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
	
	public List<Alarm> parse(){
		if(init()==false){
			return null;
		}
		NodeList nodeList = mDoc.getElementsByTagName(ITEM);
		if (nodeList != null) {
			List<Alarm> alarms = new ArrayList<Alarm>(nodeList.getLength());
			for(int i = 0; i < nodeList.getLength(); i++){
				Alarm a = new Alarm();
				Node node = nodeList.item(i);
				NamedNodeMap nodeMap = node.getAttributes();
				NodeList nodeList2 = node.getChildNodes();
				for(int j = 0; j<nodeList2.getLength(); j++){
					Node node2 = nodeList2.item(j);
					if(Alarm.Columns._ID.equals(node2.getNodeName()) ){
						a.id = Integer.valueOf(node2.getTextContent());
					}else if(Alarm.Columns.HOUR.equals(node2.getNodeName())){
						a.hour = Integer.valueOf(node2.getTextContent());
					}else if(Alarm.Columns.MINUTES.equals(node2.getNodeName())){
						a.minutes = Integer.valueOf(node2.getTextContent());
					}else if(Alarm.Columns.DAYS_OF_WEEK.equals(node2.getNodeName())){
						a.daysOfWeek = new Alarm.DaysOfWeek(Integer.valueOf(node2.getTextContent()));
					}else if(Alarm.Columns.ALARM_TIME.equals(node2.getNodeName())){
						a.time = Integer.valueOf(node2.getTextContent());
					}else if(Alarm.Columns.ENABLED.equals(node2.getNodeName())){
						a.enabled = Boolean.valueOf(node2.getTextContent());
					}else if(Alarm.Columns.VIBRATE.equals(node2.getNodeName())){
						a.vibrate = Boolean.valueOf(node2.getTextContent());
					}else if(Alarm.Columns.ALERT.equals(node2.getNodeName())){
						// TODO chen
						//a.alert = Uri.fromParts(node2.getTextContent(),0,0);
					}else if(Alarm.Columns.OPERATION.equals(node2.getNodeName())){
						a.operation = node2.getTextContent();
					}else if(Alarm.Columns.MUSIC.equals(node2.getNodeName())){
						a.musicPath = node2.getTextContent();
					}else if(Alarm.Columns.COMMITURL.equals(node2.getNodeName())){
						a.commit_url = node2.getTextContent();
					}					
				}
				alarms.add(a);
			}
			return alarms;
		}
		
		return null;
	}
	
	public static class Operation{
		public final static int NONE = -1;
		public final static int DELETE = 0;
		public final static int ADD = 1;
		public final static int UPDATE = 2;
		public final static int LIST = 3;
		public final static int RINGING = 4;
		public final static int DELALL = 5;
		
		public final static String s_add = "add";
		public final static String s_del = "delete";
		public final static String s_update = "update";
		public final static String s_list = "list";
		public final static String s_ring = "ring";
		public final static String s_delall = "del";
		
		public static int mapString(String op){
			int data = NONE;
			if(s_add.equals(op)){
				data = ADD;
			}else if(s_del.equals(op)){
				data = DELETE;
			}else if(s_update.equals(op)){
				data = UPDATE;
			}else if(s_list.equals(op)){
				data = LIST;
			}
			return data;
		}
	}
}
