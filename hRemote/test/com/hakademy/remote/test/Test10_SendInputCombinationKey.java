package com.hakademy.remote.test;

import com.hakademy.utility.hook.KeyboardHook;
import com.sun.jna.platform.win32.User32;

public class Test10_SendInputCombinationKey {
	public static void main(String[] args) throws InterruptedException {
		KeyboardHook hook = KeyboardHook.getInstance();
		hook.keyPress(KeyboardHook.VK_CONTROL_LEFT);
		hook.keyPress(KeyboardHook.VK_ALT_LEFT);
		Thread.sleep(3L);
		hook.keyRelease(KeyboardHook.VK_ALT_LEFT);
		hook.keyRelease(KeyboardHook.VK_CONTROL_LEFT);
	}
}
