package com.xattacker.convert.ios;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.xattacker.convert.PropertyType;
import com.xattacker.convert.PropertyValue;
import com.xattacker.convert.SystemParams;

public final class IOSStringResourceImporter
{
	public LinkedHashMap<String, PropertyValue> loadStringResource(String aPath, ArrayList<String> aDuplicateds) throws Exception
	{
		LinkedHashMap<String, PropertyValue> properties = new LinkedHashMap<String, PropertyValue>();
		BufferedReader in = null;

		try
		{
			in = new BufferedReader(new InputStreamReader(new FileInputStream(aPath)));

			String line = null;
			int index = -1;

			do
			{
				line = in.readLine();
				if (line == null)
				{
					break;
				}

				if (line.length() > 0)
				{
					line = line.trim();
					System.out.println("[" + line + "]");
					
					if ((index = line.indexOf(SystemParams.IOS_SEPARATOR)) != -1)
					{
						line = line.replace("\"", "");
						line = line.replace(";", "");
						index = line.indexOf(SystemParams.IOS_SEPARATOR);

						try
						{
							String key = line.substring(0, index).trim();
							if (!properties.containsKey(key))
							{
								PropertyValue value = new PropertyValue();
								value.setType(PropertyType.RESOURCE);
								value.setContent(line.substring(index + 1));
								properties.put(key, value);
							}
							else
							{
								aDuplicateds.add(key);
							}
						}
						catch (Exception ex)
						{
							// ignore one line error
						}
					}
					else if (line.startsWith("//"))
					{
						// append comments
						PropertyValue value = new PropertyValue();
						value.setType(PropertyType.COMMENTS);
						value.setContent(line.substring(2));
						properties.put(SystemParams.generateUUID(), value);
					}
				}
				else
				{
					// append empty line
					PropertyValue value = new PropertyValue();
					value.setType(PropertyType.EMPTY_LINE);
					value.setContent(line);
					properties.put(SystemParams.generateUUID(), value);
				}

			} while (true);
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception ex)
				{
				}

				in = null;
			}

			System.gc();
		}

		return properties;
	}
}
