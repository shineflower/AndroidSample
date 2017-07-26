package com.jackie.sample.utils;

import java.security.MessageDigest;

public class MD5Utils {
	public static String getMd5(String content) {
		StringBuffer buf = null;
		try { 
			MessageDigest messageDigest = MessageDigest.getInstance("MD5Utils");
			messageDigest.update(content.getBytes());
			byte b[] = messageDigest.digest();
	
			int i; 
			buf = new StringBuffer("");

			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if (i < 0) i += 256;
				if(i < 16) 
				buf.append("0");
				buf.append(Integer.toHexString(i));
			} 
			
			System.out.println("result: " + buf.toString());
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		if (buf == null) {
			return null;
		}
		
		return buf.toString();
	} 
}
