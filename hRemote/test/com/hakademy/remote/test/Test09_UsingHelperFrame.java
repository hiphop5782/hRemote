package com.hakademy.remote.test;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.ui.HelperFrame;

public class Test09_UsingHelperFrame {
	public static void main(String[] args) throws Exception {
		HelperFrame fr = HRemoteApplication.getBean(HelperFrame.class);
		fr.start("192.168.0.8", 2201);
//		fr.start("localhost");
	}
}
