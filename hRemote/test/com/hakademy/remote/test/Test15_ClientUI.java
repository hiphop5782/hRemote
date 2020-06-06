package com.hakademy.remote.test;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.ui.ClientFrame;

public class Test15_ClientUI {
	public static void main(String[] args) {
		ClientFrame frame = HRemoteApplication.getBean(ClientFrame.class);
		frame.start();
	}
}
