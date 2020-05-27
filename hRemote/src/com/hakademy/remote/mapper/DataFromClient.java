package com.hakademy.remote.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *	JSON Mapper for client's data 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataFromClient {
	private byte[] screenData;
	private int screenNumber;
	private int monitorCount;
}
