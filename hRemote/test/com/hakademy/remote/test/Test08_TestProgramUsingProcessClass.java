package com.hakademy.remote.test;

import java.io.IOException;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.ClientProcess;
import com.hakademy.utility.object.InMemoryObjectLoader;

public class Test08_TestProgramUsingProcessClass {
	public static void main(String[] args) throws InterruptedException, IllegalArgumentException, IllegalAccessException, IOException {
		ClientProcess cp = HRemoteApplication.getBean(ClientProcess.class);
		cp.setPort(36500);
		cp.setFrame(30);
		cp.connect();
		
		Thread.currentThread().join();
	}
}
