package com.xattacker.convert;

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
				
				result = exportToAndroidResourceFile(properties, aToPath.toString());
	
				aDuplicated.addAll(this.duplicateds);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return result;
	}

	private boolean exportToAndroidResourceFile(LinkedHashMap<String, PropertyValue> properties, String exportedPath) throws Exception
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
