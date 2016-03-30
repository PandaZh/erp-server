/*
 * Copyright (C) 2011  SZ7thRoad, Inc.
 * -----------------------------------
 * Development-base framework.
 */
package com.panda.zh.erp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 encrypt
 * 
 * @author jeremy
 */
public final class MD5 {

	private static MessageDigest md5;

	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String digestCrypt(final String value)
	{
		return digestCrypt(value.getBytes());
	}
	
	public static String digestCrypt(final byte[] value){
		
		final StringBuilder hexValue = new StringBuilder();
		
		final byte[] md5Bytes = md5.digest(value);

		for (byte b : md5Bytes)
		{
			final int val = b & 0xFF;
			if (val < 16)
			{
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static String JM(final String value) {
		return KL(value);
	}

	public static String KL(final String value) {
		final char[] a = value.toCharArray();
		final int length = a.length;
		for (int i = 0; i < length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		return new String(a);
	}

	// instanced was not allowed.
	private MD5() {}
}
