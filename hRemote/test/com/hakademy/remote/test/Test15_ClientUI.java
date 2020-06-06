package com.hakademy.remote.test;

import javax.swing.UIManager;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.ui.ClientFrame;

public class Test15_ClientUI {
	public static void main(String[] args) throws Exception{
		UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		ClientFrame frame = HRemoteApplication.getBean(ClientFrame.class);
		frame.start();
	}
}
