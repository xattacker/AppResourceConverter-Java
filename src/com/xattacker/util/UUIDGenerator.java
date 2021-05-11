package com.xattacker.util;

import com.xattacker.convert.SystemParams;

public final class UUIDGenerator
{
	private UUIDGenerator()
	{
	}
	
   public static String generateUUID()
   {
  	 	return SystemParams.generateUUID();
   }
}
