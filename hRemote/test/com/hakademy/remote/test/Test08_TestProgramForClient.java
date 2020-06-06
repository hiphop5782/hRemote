package com.hakademy.remote.test;

import java.io.IOException;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.ClientProcess;
import com.hakademy.utility.object.InMemoryObjectLoader;

public class Test08_TestProgramForClient {
	public static void main(String[] args) throws InterruptedException, IllegalArgumentException, IllegalAccessException, IOException {
		ClientProcess cp = HRemoteApplication.getBean(ClientProcess.class);
		cp.setFrame(30);
		if(cp.regist()) {
			cp.connect();
			Thread.currentThread().join();
		}
		else {
			System.out.println("regist failed");
		}
		
	}
}
