package com.xattacker.convert;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class IOSStringResourceExporter
{
	public boolean exportToSResourceFile(LinkedHashMap<String, PropertyValue> properties, String exportedPath) throws Exception
	{
		boolean succeed = false;
		BufferedWriter writer = null;
		
		try
		{
		   writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportedPath)));
		   
			for (Entry<String, PropertyValue> pair : properties.entrySet()) 
			{
				switch (pair.getValue().getType())
				{
					case RESOURCE:
					{
                  // convert format args
                  String new_content = replaceArg(pair.getValue().getContent(), "s");
                  new_content = replaceArg(new_content, "S");
                  new_content = new_content.replaceAll("\\\"", "\\\\\"");
                  
						writer.write( "\"" + pair.getKey() +  "\"" + SystemParams.IOS_SEPARATOR + "\"" + new_content +  "\";");
					}
						break;
						
					case EMPTY_LINE:
						//writer.newLine();
						break;
						
					case COMMENTS:
					{
						String comments = pair.getValue().getContent();
						
						String[] array = comments.split("\n");
						
						for (String comment : array)
						{
							writer.write("//" + comment + "\n");
						}
					}
						break;
				}
				
				writer.newLine();
			}
			
		   writer.flush();
			succeed = true;
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (Exception ex)
				{
				}

				writer = null;
			}

			System.gc();
		}
		
		return succeed;
	}
	
	private String replaceArg(String aContent, String aReplaced)
	{
		String new_content = aContent;
      new_content = new_content.replace("%" + aReplaced, "%@");
      
      for (int i = 1; i < 9; i++)
      {
          new_content = new_content.replace("%" + i + aReplaced, "%" + i + "@");
      }
      
      for (int i = 1; i < 9; i++)
      {
          new_content = new_content.replace("%" + i + "$" + aReplaced, "%" + i + "$@");
      }
		
		return new_content;
	}
}
