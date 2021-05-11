package com.xattacker.convert.android;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.xattacker.convert.PropertyType;
import com.xattacker.convert.PropertyValue;
import com.xattacker.convert.ResourceConverter;
import com.xattacker.convert.ios.IOSStringResourceExporter;

public final class Android2iOSResourceConverter extends ResourceConverter
{
	@Override
	public boolean convert(String aFromPath, StringBuilder aToPath, ArrayList<String> aDuplicated)
	{
		boolean result = false;
		
		try
		{
			AndroidStringResourceImporter importer = new AndroidStringResourceImporter();
			LinkedHashMap<String, PropertyValue> properties = importer.loadResource(aFromPath);
			if (!properties.isEmpty())
			{
				File file = new File(aFromPath);
				File out_file = new File(file.getParent() + File.separator + "Localizable.strings");
				if (out_file.exists())
				{
					out_file.delete();
				}
				
				aToPath.append(out_file.getAbsolutePath());
				
				IOSStringResourceExporter exporter = new IOSStringResourceExporter();
				result = exporter.exportToResourceFile(properties, aToPath.toString());
	
				aDuplicated.addAll(this.duplicateds);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return result;
	}
}
