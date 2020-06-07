package com.hakademy.remote;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.hakademy.remote.client.ui.HelperFrame;

public class HRemoteHelperApplication {
	public static void main(String[] args) {
		HelperFrame frame = null;
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			frame = HRemoteApplication.getBean(HelperFrame.class);
			frame.display();
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "오류 발생 : "+e.getMessage(), "연결 오류", JOptionPane.PLAIN_MESSAGE);
			System.exit(-1);
		}
	}
}
