package com.hakademy.remote.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.hakademy.remote.client.ClientProcess;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

@Component
public class ClientFrame extends JFrame{
	
	@Inject
	private ClientProcess process;
	
	private JToggleButton button = new JToggleButton();
	private JLabel label = new JLabel("", JLabel.CENTER);
	
	public ClientFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int choice = JOptionPane.showConfirmDialog(ClientFrame.this, "종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(choice == JOptionPane.YES_OPTION) {
					changeState(false);
					process.kill();
					System.exit(0);
				}
			}
		});
		setAlwaysOnTop(true);
		setResizable(false);
		setUndecorated(true);
		prepare();
	}
	
	int x = 0 , y = 0;
	public void prepare() {
		JPanel panel = new JPanel(new BorderLayout());
		setContentPane(panel);
		button.setBackground(Color.green);
		button.setForeground(Color.white);
		button.setPreferredSize(new Dimension(80, 80));
		button.setFont(new Font("", Font.PLAIN, 25));
		button.addActionListener(e->{
			changeState(button.isSelected());
		});
		changeButtonState(false);
		panel.add(button, BorderLayout.EAST);
		panel.add(label);
		label.setBorder(null);
		label.setFont(new Font("", Font.BOLD, 25));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
				y = e.getY();
			}
		});
		label.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int xgap = e.getX() - x;
				int ygap = e.getY() - y;
				int xLoc = getLocationOnScreen().x + xgap;
				int yLoc = getLocationOnScreen().y + ygap;
				setLocation(xLoc, yLoc);
			}
		});
	}
	
	public void start() throws IOException {
		process.regist();
		label.setText("<html><body><center>공유코드<br>"+process.getClient().getNo()+"</center></body></html>");
		pack();
		setVisible(true);
	}
	private void changeState(boolean connectable) {
		try {
			process.changeState(connectable);
			changeButtonState(connectable);
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ClientFrame.this, "통신 오류", "오류", JOptionPane.PLAIN_MESSAGE);
		}
	}
	private void changeButtonState(boolean selected) {
		if(selected) {
			button.setBackground(Color.green);
			button.setText("공유");
		}
		else {
			button.setBackground(Color.red);
			button.setText("차단");
		}
	}
	
}
