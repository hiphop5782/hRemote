package com.hakademy.remote.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.hakademy.remote.client.ClientProcess;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

@Component
public class ClientFrame extends JFrame{
	
	@Inject
	private ClientProcess process;
	
	private JToggleButton button = new JToggleButton("공유");
	private JLabel label = new JLabel("", JLabel.CENTER);
	
	public ClientFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setResizable(false);
		prepare();
	}
	
	public void prepare() {
		JPanel panel = new JPanel(new BorderLayout());
		setContentPane(panel);
		button.setBackground(Color.green);
		button.setForeground(Color.white);
		button.setPreferredSize(new Dimension(80, 80));
		panel.add(button, BorderLayout.EAST);
		panel.add(label);
		label.setFont(new Font("", Font.BOLD, 25));
		label.addMouseMotionListener(new MouseMotionAdapter() {
			int x = 0 , y = 0;
			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.print(e.getX());
				System.out.print(",");
				System.out.println(e.getY());
				int xLoc = getLocationOnScreen().x + e.getXOnScreen() - x;
				int yLoc = getLocationOnScreen().y + e.getYOnScreen() - y;
				setLocation(xLoc, yLoc);
				x = e.getXOnScreen();
				y = e.getYOnScreen();
			}
		});
	}
	
	public void start() {
		label.setText("공유코드 : 101010");
		pack();
		setVisible(true);
	}
	
}
