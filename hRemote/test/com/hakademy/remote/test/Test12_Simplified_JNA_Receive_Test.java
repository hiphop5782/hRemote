package com.hakademy.remote.test;

import me.coley.simplejna.Keyboard;
import me.coley.simplejna.hook.key.KeyEventReceiver;
import me.coley.simplejna.hook.key.KeyHookManager;

public class Test12_Simplified_JNA_Receive_Test {
	public static void main(String[] args) throws InterruptedException {
		KeyHookManager manager = new KeyHookManager();
		KeyEventReceiver receiver = new KeyEventReceiver(manager) {
			@Override
			public boolean onKeyUpdate(SystemState sysState, PressState pressState, int time, int vkCode) {
				System.out.println("Is pressed:" + (pressState == PressState.DOWN));
		        System.out.println("Alt down:" + (sysState == SystemState.SYSTEM));
//		        System.out.println("Timestamp:" + time);
		        System.out.println("KeyCode:" + vkCode);
				return true;
			}
		};
		manager.hook(receiver);
	}
}
