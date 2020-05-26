package com.hakademy.remote.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import javax.imageio.ImageIO;

import com.hakademy.utility.screen.ScreenManager;

//UDP는 64KB로 용량제한이 있어서 불가
public class Test03_ClientForSend {
	public static void main(String[] args) throws IOException {
		//IPv4 channel open
		DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
		
		//capture screen
		ScreenManager manager = ScreenManager.getManager();
		BufferedImage image = manager.getCurrentMonitorImage();
		
		//convert to bytebuffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
		SocketAddress address = new InetSocketAddress("localhost", 36500);
		channel.send(buffer, address);
		
		//channel close
		channel.close();
	}
}
