package com.xattacker.convert.android;

import java.io.File;
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

public final class AndroidStringResourceExporter
{
	public boolean exportToResourceFile(LinkedHashMap<String, PropertyValue> properties, String exportedPath) throws Exception
	{
		boolean succeed = false;

		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder builder = factory.newDocumentBuilder();  
		   Document doc = builder.newDocument();

		   Element root = doc.createElement("resources");
         doc.appendChild(root);

			for (Entry<String, PropertyValue> pair : properties.entrySet()) 
			{
				switch (pair.getValue().getType())
				{
					case RESOURCE:
					{
						Element elem = doc.createElement("string");
						elem.setAttribute("name", pair.getKey());
						
		             // convert format args
                  String new_content = pair.getValue().getContent().replace("%@", "%s");
                  for (int i = 1; i < 9; i++)
                  {
                      new_content = new_content.replace("%" + i + "@", "%" + i + "s");
                  }
						
						Text text = doc.createTextNode(new_content);
						elem.appendChild(text);
						
						root.appendChild(elem);
					}
						break;
						
					case EMPTY_LINE:
						root.appendChild(doc.createTextNode("\n\n  "));
						break;
						
					case COMMENTS:
					{
						Comment comment = doc.createComment(pair.getValue().getContent());
						root.appendChild(comment);
					}
						break;
				}
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
		   Transformer transformer = transformerFactory.newTransformer();
		   transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		   transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		   
		   DOMSource source = new DOMSource(doc);
		   StreamResult result = new StreamResult(new File(exportedPath));
		   transformer.transform(source, result);
		    
			succeed = true;
		}
		catch (Exception ex)
		{
			throw ex;
		}
		
		return succeed;
	}
}
