package com.hakademy.remote.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.imageio.ImageIO;

import com.hakademy.utility.screen.ScreenManager;

//TCP Server(for user)
public class Test05_ClientForSend {
	public static void main(String[] args) throws IOException {
		//IPv4 channel open
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(true);
		
		//channel bind
		channel.bind(new InetSocketAddress(36500));
		
		SocketChannel socket = channel.accept();
		InetSocketAddress addr = (InetSocketAddress)socket.getRemoteAddress();
		
		//capture screen
		ScreenManager manager = ScreenManager.getManager();
		BufferedImage image = manager.getCurrentMonitorImage();
		
		//convert to bytebuffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", new File("tmp-send.jpg"));//test
		ImageIO.write(image, "jpg", baos);
		baos.flush();
		byte[] by = baos.toByteArray();
		baos.close();
		System.out.println("compress image size");
		System.out.println(by.length);
		
		ByteBuffer buffer = ByteBuffer.wrap(by);
		
		System.out.println("send size");
		System.out.println(buffer.array().length);
		
		//send
		socket.write(buffer);
		
		//channel close
		channel.close();
	}
}
