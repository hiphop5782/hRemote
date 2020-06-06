package com.hakademy.remote.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Client {
	private String no;
	private String name;
	private String ip;
	private int port;
	private Timestamp ctime;
	private Timestamp utime;
	private boolean connectable;
}
