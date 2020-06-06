package com.hakademy.remote.test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.hakademy.remote.HRemoteApplication;

public class Test16_JarTest {
	public static void main(String[] args) throws IOException {
		String basePackageName = "com.hakademy.remote";
		ClassLoader loader = HRemoteApplication.class.getClassLoader();
		Enumeration<URL> urls = loader.getResources(basePackageName.replace(".", "/"));
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			String urlString = url.toExternalForm();
			load(basePackageName, urlString);
		}
	}
	
	public static void load(String basePackageName, String url) throws IOException {
		if(url.startsWith("jar:")) {
			System.out.println("jar-file");
			int index = url.indexOf("!");
			if(index < 0) {
				url = url.substring("jar:file:/".length());
			}
			else {
				url = url.substring("jar:file:/".length(), index);
			}
			loadJarFile(basePackageName, url);
		}
		else if(url.startsWith("file:")) {
			System.out.println("file");
			int index = url.indexOf("!");
			if(index < 0) {
				url = url.substring("file:/".length());
			}
			else {
				url = url.substring("file:/".length(), index);
			}
			System.out.println(url);
		}
	}
	
	public static void loadJarFile(String basePackageName, String path) throws IOException {
		JarFile jar = new JarFile(path);
		Enumeration<JarEntry> entries = jar.entries();
		String convertBasePackageName = basePackageName.replace(".", "/");
		while(entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			
			if(!entryName.startsWith(convertBasePackageName)) 
				continue;
			if(!entryName.endsWith(".class")) continue;
			if(entryName.contains("$")) continue;
			
			
		}
	}
}
