package com.xattacker;

import java.io.File;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter
{
	private Hashtable exts;
	private String extension;
	private String description;

	public MyFileFilter(String desc)
	{
		exts = new Hashtable();

		this.description = desc;
	}

	public void addExt(String aExt)
	{
		exts.put(aExt, String.valueOf(exts.size()));
	}

	public static String getExtension(File f)
	{
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		
		if (i > 0 && i < s.length() - 1)
		{
			ext = s.substring(i + 1).toLowerCase();
		}
		
		return ext;
	}

	public boolean accept(File f)
	{
		if (f.isDirectory())
		{
			return true;
		}

		String ext = getExtension(f);
		if (ext != null)
		{
			if (exts.get(ext) != null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		return false;
	}

	public String getDescription()
	{
		return this.description;
	}
}
