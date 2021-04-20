package com.xattacker.convert.i18n;

import java.io.File;
import java.util.ArrayList;

import com.xattacker.convert.ResourceConverter;

//將 i18n 多國語言 resource 檔 轉成 UniCode properties 檔
public final class I18nResourceConverter extends ResourceConverter
{
	@Override
	public boolean convert(String aFromPath, StringBuilder aToPath, ArrayList<String> aDuplicated)
	{
		boolean result = false;
		
		try
		{
			int converted = 0;
			File[] files = new File(aFromPath).listFiles();
			
			for (File file : files)
			{
				if (file.isFile() && file.getName().endsWith(".resource"))
				{
					String name = file.getName();
					String toPath = file.getParent() + File.separator + name.substring(0, name.indexOf(".")) + ".properties";
					String cmd = String.format("native2ascii -encoding UTF-8 %s %s", file.getAbsolutePath(), toPath);
					Process p = Runtime.getRuntime().exec(cmd);
					p.waitFor();
					p.destroy();
					
					converted++;
				}
			}
			
			result = converted > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
