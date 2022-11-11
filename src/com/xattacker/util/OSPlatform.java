package com.xattacker.util;

public enum OSPlatform
{
	Unknown,
	
	Windows,
	Linux,
	Unix,
	Solaris,
	Mac;
	
	public static OSPlatform getOS()
	{
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.contains("win"))
		{
			return OSPlatform.Windows;
		}
		else if (os.contains("mac"))
		{
			return OSPlatform.Mac;
		}
		else if (os.contains("linux"))
		{
			return OSPlatform.Linux;
		}
		else if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
		{
			return OSPlatform.Unix;
		}
		else if (os.contains("sunos"))
		{
			return OSPlatform.Solaris;
		}
		
		return OSPlatform.Unknown;
	}
}
