package com.xattacker.convert.android;

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
import com.xattacker.util.UUIDGenerator;

public final class AndroidStringResourceImporter
{
	public LinkedHashMap<String, PropertyValue> loadResource(String aPath) throws Exception
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
					properties.put(UUIDGenerator.generateUUID(), value);
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
						properties.put(UUIDGenerator.generateUUID(), value);
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
