package com.hakademy.remote.client.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import com.hakademy.utility.object.annotation.Component;

@Component
public class HelperMenu extends JMenuBar{

	private JMenu screenMenu = new JMenu("화면");
	private List<JMenuItem> screenMenuItems = new ArrayList<>();
	
	public HelperMenu() {
		add(new JMenu("설정"));
		add(screenMenu);
	}
	
	public void setScreen(int size) {
		if(screenMenuItems.size() == size) {
			return;
		}
		
		screenMenuItems.clear();
		for(int i=1; i <= size; i++) {
			JMenuItem item = new JRadioButtonMenuItem("화면"+i);
			screenMenuItems.add(item);
			screenMenu.removeAll();
			screenMenu.add(item);
		}
		screenMenu.revalidate();
	}
	
	public void setCurrentScreen(int screen) {
		screenMenuItems.get(screen).doClick();
	}
}




