package com.hakademy.remote.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

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
	}
	
}
