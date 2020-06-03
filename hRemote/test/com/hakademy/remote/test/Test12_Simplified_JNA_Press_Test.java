package com.hakademy.remote.test;

import me.coley.simplejna.Keyboard;
import me.coley.simplejna.hook.key.KeyEventReceiver;
import me.coley.simplejna.hook.key.KeyHookManager;

public class Test12_Simplified_JNA_Press_Test {
	public static void main(String[] args) throws InterruptedException {
//		windows + d
//		Keyboard.sendKeyDown(91);
//		Keyboard.sendKeyDown(68);
//		Keyboard.sendKeyUp(68);
//		Keyboard.sendKeyUp(91);
		
//		ctrl + esc
//		Keyboard.sendKeyDown(162);
//		Keyboard.sendKeyDown(27);
//		Keyboard.sendKeyUp(27);
//		Keyboard.sendKeyUp(162);
		
//		alt
//		Keyboard.sendKeyDown(164);
//		Keyboard.sendKeyUp(164);
		
//		clear
		for(int i=0; i < 256; i++) {
			Keyboard.sendKeyUp(i);
		}
	}
}
