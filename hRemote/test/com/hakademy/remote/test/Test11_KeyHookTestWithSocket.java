package com.hakademy.remote.test;

import java.io.IOException;
import java.net.UnknownHostException;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.HelperProcess;

public class Test11_KeyHookTestWithSocket {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		HelperProcess p = HRemoteApplication.getBean(HelperProcess.class);
		p.connect("192.168.0.8", 36500);
		Thread.currentThread().join();
	}
}
