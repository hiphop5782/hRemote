package com.hakademy.remote.test;

import java.io.IOException;

import com.hakademy.remote.client.ClientProcess;
import com.hakademy.utility.object.InMemoryObjectLoader;

public class Test08_TestProgramUsingProcessClass {
	public static void main(String[] args) throws InterruptedException, IllegalArgumentException, IllegalAccessException, IOException {
		InMemoryObjectLoader loader = new InMemoryObjectLoader("com.hakademy.remote");
		ClientProcess cp = loader.getBean(ClientProcess.class);
		cp.setPort(36500);
		cp.setFrame(30);
		cp.connect();
	}
}
