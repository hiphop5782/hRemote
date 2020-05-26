package com.hakademy.remote.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.imageio.ImageIO;

//TCP Client(for helper)
public class Test04_ClientForReceive {
	public static void main(String[] args) throws IOException {
		//IPv4 channel open
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(true);
		
		//connect
		channel.connect(new InetSocketAddress("localhost", 36500));
		
		//generate direct buffer(use os memory)
		ByteBuffer buffer = ByteBuffer.allocate(1 * 1024 * 1024);
		buffer.clear();
		
		//receive data
		int size = channel.read(buffer);
		buffer.flip();//buffer를 읽기 모드로 전환
		System.out.println("size = " + size);
		System.out.println(buffer.remaining());

		//converting
		byte[] by = new byte[size];
		buffer.get(by);
		ByteArrayInputStream bais = new ByteArrayInputStream(by);
		BufferedImage image = ImageIO.read(bais);
		
		//save image
		ImageIO.write(image, "jpg", new File("tmp-receive.jpg"));
		
		//channel close
		channel.close();
	}
}
