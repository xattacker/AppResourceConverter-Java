package com.xattacker.convert;

import java.util.UUID;

public class SystemParams
{
	 public final static String IOS_SEPARATOR = "=";
	 
	 public static String generateUUID()
    {
   	 return UUID.randomUUID().toString();
    }
}
