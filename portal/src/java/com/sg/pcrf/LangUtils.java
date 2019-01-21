package com.sg.pcrf;

import java.util.List;
import java.util.Map;

public class LangUtils {
	
	public static boolean isEmpty(String a){
		if(a == null || a.length() == 0)return true;
		return false;
	}
	
	public static boolean isNotEmpty(String a){
		if(a == null || a.length() == 0)return false;
		return true;
	}
	
	public static <T> void clearList(List<T> list){
		if(list != null){
			list.clear();
			list = null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void clearMap(Map map){
		if(map != null){
			map.clear();
			map = null;
		}
	}

	public static boolean isListEmpty(List<?> subIDList) {
		if(subIDList == null || subIDList.size() == 0)return true;
		return false;
	}
	
	public static boolean isMapEmpty(Map<?,?> map) {
		if(map == null || map.size() == 0)return true;
		return false;
	}
}
