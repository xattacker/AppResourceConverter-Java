package com.xattacker.convert.ios;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.xattacker.convert.PropertyValue;

//重整 iOS project 中的 Localizable.strings 檔中, 移除掉重覆的key值 
public class IOSResourceFormatter
{
	public boolean format(String aFromPath, StringBuilder aOutFilePath)
	{
		boolean result = false;
		
		if (!new File(aFromPath).exists())
		{
			return false;
		}

		
		try
		{
		   ArrayList<String> duplicateds = new ArrayList<String>();
			
			IOSStringResourceImporter loader = new IOSStringResourceImporter();
			LinkedHashMap<String, PropertyValue> properties = loader.loadStringResource(aFromPath, duplicateds);
			
			File file = new File(aFromPath);
			File out_file = new File(file.getParent() + File.separator + "Localizable.strings");
			if (out_file.exists())
			{
				out_file.delete();
			}
			
			aOutFilePath.append(out_file.getAbsolutePath());
			
			IOSStringResourceExporter exporter = new IOSStringResourceExporter();
			result = exporter.exportToResourceFile(properties, aOutFilePath.toString());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return result;
	}
}
