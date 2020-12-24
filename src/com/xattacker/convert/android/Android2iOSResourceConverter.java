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
			LinkedHashMap<String, PropertyValue> properties = loadAndroidResource(aFromPath);
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
				result = exporter.exportToSResourceFile(properties, aToPath.toString());
	
				aDuplicated.addAll(this.duplicateds);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return result;
	}
	
	private LinkedHashMap<String, PropertyValue> loadAndroidResource(String aPath) throws Exception
	{
		LinkedHashMap<String, PropertyValue> properties = new LinkedHashMap<String, PropertyValue>();

		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder builder = factory.newDocumentBuilder();  
		   Document doc = builder.parse(aPath);
		   
		   Element root = (Element)doc.getDocumentElement();
		   NodeList list = root.getChildNodes();

		   for (int i = 0; i < list.getLength(); i++)
		   {
		   	Node node = list.item(i);
		   	
		   	if (node instanceof Element)
		   	{
		   		Element elem = (Element)node;
		   		//System.out.println(elem.getAttribute("name"));
		   		
		   		if (elem.getFirstChild() instanceof Text)
		   		{
		   			Text text = (Text)elem.getFirstChild();
		   			System.out.println(text.getData());
		   			
		   			PropertyValue value = new PropertyValue();
						value.setType(PropertyType.RESOURCE);
						value.setContent(text.getData());
						properties.put(elem.getAttribute("name"), value);
		   		}
		   	}
		   	else if (node instanceof Comment)
		   	{
		   		Comment comment = (Comment)node;
		   		
					PropertyValue value = new PropertyValue();
					value.setType(PropertyType.COMMENTS);
					value.setContent(comment.getData());
					properties.put(generateUUID(), value);
		   	}
		   	else if (node instanceof Text)
		   	{
		   		Text text = (Text)node;
	
		   		String data = text.getData();
		   		//System.out.println("["+data+"]");
		   		
		   		if (data.startsWith("\n\n"))
		   		{	
			   		// next line
						PropertyValue value = new PropertyValue();
						value.setType(PropertyType.EMPTY_LINE);
						value.setContent(data);
						properties.put(generateUUID(), value);
		   		}
		   	}
		   }
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			System.gc();
		}
		
		return properties;
	}
}
