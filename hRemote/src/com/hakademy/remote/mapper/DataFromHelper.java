package com.hakademy.remote.mapper;

import com.hakademy.remote.command.CommandHeader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *	JSON Mapper for helper's command 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataFromHelper {
	private CommandHeader header;
	private int keyCode;
	private int[] keyMask;
	private int mouseButton;
	private int xpos, ypos;
	private int width, height;
	private int screenNumber;
}
