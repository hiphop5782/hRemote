package com.hakademy.remote.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.hakademy.utility.screen.ScreenManager;

public class Test01_Capture {
	public static void main(String[] args) throws IOException {
		ScreenManager manager = ScreenManager.getManager();
		
		System.out.println(manager.getMonitorCount());
		BufferedImage image = manager.getCurrentMonitorImage();
		ImageIO.write(image, "png", new File("tmp.png"));
	}
}
