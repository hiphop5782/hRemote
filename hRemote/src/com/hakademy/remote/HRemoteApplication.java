package com.hakademy.remote;

import com.hakademy.utility.object.InMemoryObjectLoader;

public class HRemoteApplication {
	
	private static InMemoryObjectLoader loader;
	public static InMemoryObjectLoader getLoader() {
		return loader;
	}
	public static <T>T getBean(Class<T> type){
		return loader.getBean(type);
	}
	static {
		try {
			loader = new InMemoryObjectLoader("com.hakademy.remote");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
