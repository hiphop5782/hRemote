package com.hakademy.remote.command;

public enum CommandHeader {
	CHANGE_SCREEN('s'),
	KEYBOARD_TYPE_CONTROL('k'),
	MOUSE_MOVE_CONTROL('m'),
	MOUSE_CLICK_CONTROL('c'),
	MOUSE_PRESS_CONTROL('p'),
	MOUSE_RELEASE_CONTROL('r'),
	KEYBOARD_PRESS_CONTROL('P'),
	KEYBOARD_RELEASE_CONTROL('R');
	
	private char value;
	CommandHeader(char value){
		this.value = value;
	}
	public char getValue() {return value;}
}
