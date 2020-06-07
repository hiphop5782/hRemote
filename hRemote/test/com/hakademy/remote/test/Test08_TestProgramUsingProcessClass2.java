package com.hakademy.remote.test;

import java.io.IOException;

import javax.swing.JFrame;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.HelperProcess;
import com.hakademy.remote.client.ui.HelperPanel;

public class Test08_TestProgramUsingProcessClass2 {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, IOException, InterruptedException {
		HelperPanel p = HRemoteApplication.getBean(HelperPanel.class);
		HelperProcess proc = HRemoteApplication.getBean(HelperProcess.class);
		JFrame fr = new JFrame();
		fr.setAlwaysOnTop(true);
		fr.setSize(500, 500);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setContentPane(p);
		fr.setVisible(true);
		p.connect();
//		proc.connect();
//		Thread.currentThread().join();
	}
}
