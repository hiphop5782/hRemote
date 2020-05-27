package com.hakademy.remote.command;

public enum CommandHeader {
	CHANGE_SCREEN('s'),
	KEYBOARD_CONTROL('k'),
	MOUSE_MOVE_CONTROL('m'),
	MOUSE_CLICK_CONTROL('c');
	
	private char value;
	CommandHeader(char value){
		this.value = value;
	}
	public char getValue() {return value;}
}
