package com.xattacker.convert;

import java.util.ArrayList;

public abstract class ResourceConverter
{
	 protected ArrayList<String> duplicateds = new ArrayList<String>();
    
    public abstract boolean convert(String fromPath, StringBuilder toPath, ArrayList<String> duplicated);
}
