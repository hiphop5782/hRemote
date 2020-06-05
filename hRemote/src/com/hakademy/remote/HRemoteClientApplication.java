package com.hakademy.remote;

import javax.swing.JOptionPane;

import com.hakademy.remote.client.ClientProcess;

public class HRemoteClientApplication {
	public static void main(String[] args) {
		try {
			ClientProcess cp = HRemoteApplication.getBean(ClientProcess.class);
			cp.setFrame(30);
			if(cp.regist("tester")) {
				cp.connect();
				Thread.currentThread().join();
			}
			else {
				JOptionPane.showMessageDialog(null, "등록이 실패했습니다", "연결 오류", JOptionPane.PLAIN_MESSAGE);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "오류가 발생했습니다 : "+e.getMessage(), "연결 오류", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
