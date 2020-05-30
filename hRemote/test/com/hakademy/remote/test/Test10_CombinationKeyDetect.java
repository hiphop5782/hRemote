package com.hakademy.remote.test;

import java.util.HashSet;
import java.util.Set;

import com.hakademy.utility.hook.KeyboardHook;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;

public class Test10_CombinationKeyDetect {
	static HHOOK hhk = null;
	public static void main(String[] args) throws InterruptedException {
		Set<Integer> keyPool = new HashSet<>();
		KeyboardHook hook = KeyboardHook.getInstance();
		LowLevelKeyboardProc proc = new LowLevelKeyboardProc() {
			@Override
			public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
				if(nCode >= 0) {
					switch(wParam.intValue()) {
					case KeyboardHook.KEY_PRESS:
						System.out.println(info.vkCode + "press");
						switch(info.vkCode) {
						case User32.VK_CONTROL:						case User32.VK_LCONTROL:
						case User32.VK_RCONTROL:					case User32.VK_MENU:
						case User32.VK_RMENU:							case User32.VK_LMENU:
						case User32.VK_RSHIFT:							case User32.VK_LSHIFT:
							keyPool.add(info.vkCode);
							break;
						default:
							if(keyPool.isEmpty()) {
								System.out.println("press : "+info.vkCode);
							}
							else {
								System.out.println("press : "+keyPool+" + "+info.vkCode);
							}
						}
						break;
					case KeyboardHook.KEY_RELEASE:
						System.out.println(info.vkCode+" release");
						switch(info.vkCode) {
						case User32.VK_CONTROL:						case User32.VK_LCONTROL:
						case User32.VK_RCONTROL:					case User32.VK_MENU:
						case User32.VK_RMENU:							case User32.VK_LMENU:
						case User32.VK_RSHIFT:							case User32.VK_LSHIFT:
							keyPool.remove(info.vkCode);
							break;
						default:
							if(keyPool.isEmpty()) {
								System.out.println("release : "+info.vkCode);
							}
							else {
								System.out.println("release : "+keyPool+" + "+info.vkCode);
							}
						}
					}
				}
				
				return new LRESULT(0);
			}
		};
		HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, proc, hMod, 0);
		hook.setGlobalProcess(proc);
		hook.startHook();
		
		Thread.currentThread().join();
	}
}
