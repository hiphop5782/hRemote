package com.hakademy.remote.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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
					try {
						changeState(false);
					}
					catch(Exception e) {}
					process.kill();
					System.exit(0);
				}
			}
		});
		try {
			List<Image> icons = new ArrayList<>();
			icons.add(ImageIO.read(new File("image", "icon16x16.ico")));
			icons.add(ImageIO.read(new File("image", "icon32x32.ico")));
			setIconImages(icons);
		}
		catch(Exception e) {/* pass */}
		setAlwaysOnTop(true);
		setResizable(false);
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
			try {
				changeState(button.isSelected());
			}
			catch(Exception err) {}
		});
		changeButtonState(false);
		panel.add(button, BorderLayout.EAST);
		panel.add(label);
		label.setBorder(null);
		label.setFont(new Font("", Font.BOLD, 25));
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
//		라벨 클릭 시 복사
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				label.setText("클릭하여 복사");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				label.setText("<html><body><center>공유코드<br>"+process.getClient().getNo()+"</center></body></html>");
			}
			@Override
			public void mousePressed(MouseEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String copyString = process.getClient().getNo();
				if(copyString != null){
				     StringSelection contents = new StringSelection(copyString);
				     clipboard.setContents(contents, null);
				     label.setText("복사 완료!");
				}
			}
		});
		
//		label 클릭으로 이동이 가능하도록 하는 설정
//		label.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				x = e.getX();
//				y = e.getY();
//			}
//		});
//		label.addMouseMotionListener(new MouseMotionAdapter() {
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				int xgap = e.getX() - x;
//				int ygap = e.getY() - y;
//				int xLoc = getLocationOnScreen().x + xgap;
//				int yLoc = getLocationOnScreen().y + ygap;
//				setLocation(xLoc, yLoc);
//			}
//		});
	}
	
	public void start() throws IOException {
		process.regist();
		label.setText("<html><body><center>공유코드<br>"+process.getClient().getNo()+"</center></body></html>");
		pack();
		setVisible(true);
	}
	private void changeState(boolean connectable) throws IOException {
		process.changeState(connectable);
		changeButtonState(connectable);
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
