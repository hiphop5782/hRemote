package com.hakademy.remote.test;

import com.hakademy.utility.hook.KeyboardHook;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;

public class Test11_KeyHookTest {
	public static void main(String[] args) {
		KeyboardHook hook = KeyboardHook.getInstance();
		hook.setGlobalProcess(new LowLevelKeyboardProc() {
			@Override
			public LRESULT callback(int n, WPARAM w, KBDLLHOOKSTRUCT i) {
				System.out.println("global callback = " + i.vkCode);
//				if(n >= 0) {
//					switch(w.intValue()) {
//					case KeyboardHook.KEY_PRESS:
//					case KeyboardHook.SYSKEY_PRESS:
//						System.out.println("press event");
//						hook.keyPress(i.vkCode); 
//						break;
//					case KeyboardHook.KEY_RELEASE:
//					case KeyboardHook.SYSKEY_RELEASE:
//						System.out.println("release event");
//						hook.keyRelease(i.vkCode); 
//						break;
//					}
//				}
				return null;
			}
		});
		hook.startHookAsEnablePrevent();
	}
}
