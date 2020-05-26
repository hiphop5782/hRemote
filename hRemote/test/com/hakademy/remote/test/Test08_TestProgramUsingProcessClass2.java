package com.hakademy.remote.test;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;

import com.hakademy.remote.client.HelperPanel;
import com.hakademy.utility.object.InMemoryObjectLoader;

public class Test08_TestProgramUsingProcessClass2 {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, IOException {
		InMemoryObjectLoader loader = new InMemoryObjectLoader("com.hakademy.remote");
		HelperPanel p = loader.getBean(HelperPanel.class);
		JFrame fr = new JFrame();
		fr.setAlwaysOnTop(true);
		fr.setSize(500, 500);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setLayout(new BorderLayout());
		fr.add(p);
		fr.setVisible(true);
		p.connect();
	}
}
