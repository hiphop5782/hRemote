package com.hakademy.remote.test;

import com.hakademy.remote.execute.Elevator;

public class Test13_ElevatorTest {
	public static void main(String[] args) throws InterruptedException {
		System.out.println(System.getProperty("java.home"));
		String path = "\""+String.join("\\", System.getProperty("java.home"), "bin", "java.exe")+"\"";
		String command = String.join(" ", "-jar", "\""+System.getProperty("user.dir")+"\\h-remote.jar"+"\"");
		System.out.println(path + " " + command);
		Elevator.executeAsAdmin(path, command);
		Thread.currentThread().join();
	}
}
