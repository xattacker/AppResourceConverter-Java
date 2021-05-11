package com.xattacker.convert.ios;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.xattacker.convert.PropertyValue;
import com.xattacker.convert.ResourceConverter;
import com.xattacker.convert.android.AndroidStringResourceExporter;

//將 iOS project 中的 Localizable.strings 檔 轉成 Android project 的 string.xml 檔
public final class IOS2AndroidResourceConverter extends ResourceConverter
{
	@Override
	public boolean convert(String aFromPath, StringBuilder aToPath, ArrayList<String> aDuplicated)
	{
		boolean result = false;
		
		try
		{
			IOSStringResourceImporter loader = new IOSStringResourceImporter();
			LinkedHashMap<String, PropertyValue> properties = loader.loadStringResource(aFromPath, this.duplicateds);
			if (!properties.isEmpty())
			{
				File file = new File(aFromPath);
				File out_file = new File(file.getParent() + File.separator + "strings.xml");
				if (out_file.exists())
				{
					out_file.delete();
				}
				
				aToPath.append(out_file.getAbsolutePath());
				
				AndroidStringResourceExporter exporter = new AndroidStringResourceExporter();
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
