package com.hakademy.remote.log;

public class LogManager {
	public static void info(String m) {
		System.out.println("[INFO] "+m);
	}
	
	public static void error(String m) {
		error(m, null);
	}
	public static void error(String m, Exception e) {
		System.err.println("[ERROR] " + m);
		
		if(e != null) {
			e.printStackTrace();
		}
	}
}
