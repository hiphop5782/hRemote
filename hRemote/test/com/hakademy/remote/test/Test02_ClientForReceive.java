package com.hakademy.remote.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

//UDP는 64KB로 용량제한이 있어서 불가
public class Test02_ClientForReceive {
	public static void main(String[] args) throws IOException {
		//IPv4 channel open
		DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
		
		//channel bind
		channel.bind(new InetSocketAddress(36500));
		
		//generate direct buffer(use os memory)
		ByteBuffer buffer = ByteBuffer.allocateDirect(1 * 1024 * 1024);
		buffer.clear();
		
		//receive data
		SocketAddress address = channel.receive(buffer);
		
		//print result
		System.out.println("address");
		System.out.println(address);
		System.out.println("buffer");
		System.out.println(buffer.array().length);
		
		//channel close
		channel.close();
	}
}
