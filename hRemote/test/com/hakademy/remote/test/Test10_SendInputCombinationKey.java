package com.hakademy.remote.test;

import com.hakademy.utility.hook.KeyboardHook;
import com.sun.jna.platform.win32.User32;

public class Test10_SendInputCombinationKey {
	public static void main(String[] args) {
		KeyboardHook hook = KeyboardHook.getInstance();
		hook.keyPress(User32.VK_LCONTROL);
		hook.keyPress(User32.VK_LSHIFT);
		hook.keyPress(0x1b);
		hook.keyRelease(0x1b);
		hook.keyRelease(User32.VK_LSHIFT);
		hook.keyRelease(User32.VK_LCONTROL);
	}
}
