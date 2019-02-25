package com.xattacker.convert;

public final class PropertyValue
{
	private PropertyType _type;
	private String _content;
	  
	public PropertyType getType()
	{
		return _type;
	}
	
	public void setType(PropertyType aType)
	{
		_type = aType;
	}
	
	public String getContent()
	{
		return _content;
	}
	
	public void setContent(String aContent)
	{
		_content = aContent;
	}
}
