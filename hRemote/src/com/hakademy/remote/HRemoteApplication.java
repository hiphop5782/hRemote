package com.hakademy.remote;

import com.hakademy.utility.object.InMemoryObjectLoader;

public class HRemoteApplication {
	public static void main(String[] args) {
		try {
			InMemoryObjectLoader loader = new InMemoryObjectLoader("com.hakademy.remote");
			
			System.out.println("Application loading complete");
		}
		catch(Exception e) {
			System.err.println("Application Running Error");
			e.printStackTrace();
		}
	}
}
