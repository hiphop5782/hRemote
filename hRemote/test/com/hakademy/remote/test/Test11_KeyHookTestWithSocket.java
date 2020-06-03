package com.hakademy.remote.test;

import java.io.IOException;
import java.net.UnknownHostException;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.HelperProcess;
import com.hakademy.utility.hook.KeyboardHook;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;

public class Test11_KeyHookTestWithSocket {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		HelperProcess p = HRemoteApplication.getBean(HelperProcess.class);
		p.setHost("192.168.0.8");
		p.setPort(36500);
		p.connect();
		Thread.currentThread().join();
	}
}
