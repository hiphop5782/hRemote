package com.hakademy.remote.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import lombok.Data;
import me.coley.simplejna.Keyboard;

@Data
public class RemoteProcess extends Thread{
	protected Socket socket;
	protected DataInputStream in;
	protected DataOutputStream out;
	
	protected boolean liveFlag;
	
	protected RemoteProcess() {
		this.liveFlag = true;
	}
	
	public void kill() {
		this.liveFlag = false;
		
		//keyboard clear
		for(int i=0; i < 256; i++) {
			Keyboard.sendKeyUp(i);
		}
	}
	
}
