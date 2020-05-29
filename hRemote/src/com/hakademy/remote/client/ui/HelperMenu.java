package com.hakademy.remote.client.ui;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.HelperProcess;
import com.hakademy.utility.object.annotation.Component;

@Component
public class HelperMenu extends JMenuBar{

	private JMenu screenMenu = new JMenu("화면");
	private List<JMenuItem> screenMenuItems = new ArrayList<>();
	
	private ActionListener sendAction = e->{
		int index = screenMenuItems.indexOf(e.getSource());
		try {
			HRemoteApplication.getBean(HelperProcess.class).sendChangeScreenCommand(index);
		} catch (IOException err) {
			err.printStackTrace();
		}
	};
	
	public HelperMenu() {
		add(new JMenu("설정"));
		add(screenMenu);
	}
	
	public void setScreenSize(int size) {
		if(screenMenuItems.size() == size) {
			return;
		}
		
		screenMenuItems.clear();
		screenMenu.removeAll();
		
		ButtonGroup group = new ButtonGroup();
		for(int i=1; i <= size; i++) {
			JMenuItem item = new JRadioButtonMenuItem("화면"+i);
			screenMenuItems.add(item);
			screenMenu.add(item);
			group.add(item);
			
			item.addActionListener(sendAction);
		}
		screenMenu.revalidate();
	}
	
	public void setCurrentScreen(int screen) {
		if(screenMenuItems.get(screen).isSelected()) 
			return;
		screenMenuItems.get(screen).setSelected(true);
	}
}




