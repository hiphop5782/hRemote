package com.hakademy.remote.client.ui;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.HelperProcess;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

@Component
public class HelperMenu extends JMenuBar{

	private JMenu mainMenu = new JMenu("설정");
	private JMenuItem exitMenu = new JMenuItem("종료");
	private JMenu screenMenu = new JMenu("화면");
	private List<JMenuItem> screenMenuItems = new ArrayList<>();
	
	@Inject
	private HelperProcess process;
	
	@Inject
	private HelperPanel panel;
	
	private ActionListener sendAction = e->{
		int index = screenMenuItems.indexOf(e.getSource());
		try {
			process.sendChangeScreenCommand(index);
		} catch (IOException err) {
			err.printStackTrace();
		}
	};
	
	public HelperMenu() {
		add(mainMenu);
		mainMenu.add(exitMenu);
		exitMenu.addActionListener(e->{
			if(JOptionPane.showConfirmDialog(panel, "정말 종료하시겠습니까?", "프로그램 종료", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
				process.kill();
				System.exit(0);
			}
		});
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




