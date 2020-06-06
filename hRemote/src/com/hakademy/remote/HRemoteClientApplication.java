package com.hakademy.remote;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.hakademy.remote.client.ClientProcess;
import com.hakademy.remote.client.ui.ClientFrame;

public class HRemoteClientApplication {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			ClientFrame frame = HRemoteApplication.getBean(ClientFrame.class);
			frame.start();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "오류가 발생했습니다 : "+e.getMessage(), "연결 오류", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
