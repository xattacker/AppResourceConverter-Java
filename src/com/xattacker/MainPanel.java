package com.xattacker;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.xattacker.convert.Android2iOSResourceConverter;
import com.xattacker.convert.IOS2AndroidResourceConverter;
import com.xattacker.convert.IOSResourceFormatter;
import com.xattacker.convert.ResourceConverter;


public final class MainPanel extends Frame
{
	public MainPanel()
	{
		super("AppResourceConverter");
		
		setBounds(0, 0, 360, 250);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension s = getSize();
		this.setLayout(null);
		setLocation((d.width - s.width) / 2, (d.height - s.height) / 2);
		setResizable(false);
		setVisible(true);
		
		Button button = new Button("Android string resource to iOS");
		add(button);
		button.setBounds(30, 60, 300, 25);
		button.setEnabled(true);
		button.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				chooseFile(
					new FileSelectedListener()
					{
						@Override
						public void onFileSelected(File aFile)
						{
							Android2iOSResourceConverter converter = new Android2iOSResourceConverter();							
							convertResource(aFile, converter);
						}
					},
					"select file (*.xml)|*.xml",
					"xml"
				);
			}
		});
		
		button = new Button("iOS string resource to Android");
		add(button);
		button.setBounds(30, 100, 300, 25);
		button.setEnabled(true);
		button.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				chooseFile(
					new FileSelectedListener()
					{
						@Override
						public void onFileSelected(File aFile)
						{
							IOS2AndroidResourceConverter converter = new IOS2AndroidResourceConverter();
							convertResource(aFile, converter);
						}
					},
					"select file (*.strings)|*.strings",
					"strings"
				);
			}
		});
		
		
		button = new Button("Format iOS String Resource file");
		add(button);
		button.setBounds(30, 140, 300, 25);
		button.setEnabled(true);
		button.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				chooseFile(
					new FileSelectedListener()
					{
						@Override
						public void onFileSelected(File aFile)
						{
							StringBuilder out_path = new StringBuilder();
							IOSResourceFormatter formatter = new IOSResourceFormatter();
							boolean result = formatter.format(aFile.getAbsolutePath(), out_path);
							
							if (result)
							{
								JOptionPane.showMessageDialog(new JFrame(), "format succeed", " SUCCEED ", JOptionPane.INFORMATION_MESSAGE);
							}
							else
							{
								JOptionPane.showMessageDialog(new JFrame(), "format failed", " ERROR ", JOptionPane.ERROR_MESSAGE);
							}
						}
					},
					"select file (*.strings)|*.strings",
					"strings"
				);
			}
		});
		
	
		addWindowListener(
		new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				dispose();
				System.exit(0);
			}
		});
	}
	
	private void chooseFile(FileSelectedListener aListener, String desc, String... exts)
	{
		try
		{
			JFileChooser jfc = new JFileChooser();
			MyFileFilter filter = new MyFileFilter(desc);
			
			for (String ext : exts)
			{
				filter.addExt(ext);
			}

			jfc.setFileFilter(filter);

			int n = jfc.showOpenDialog(this);
			if (n == JFileChooser.APPROVE_OPTION)
			{
				File selected = jfc.getSelectedFile();
				aListener.onFileSelected(selected);
			}
		}
		catch (Exception e)
		{
		}
	}
	
	private void convertResource(File aFile, ResourceConverter aConverter)
	{
		StringBuilder outPath = new StringBuilder();
		ArrayList<String> duplicated = new ArrayList<String>();
		boolean result = aConverter.convert(aFile.getAbsolutePath(), outPath, duplicated);
		
		if (result)
		{
			JOptionPane.showMessageDialog(new JFrame(), "converted succeed", " SUCCEED ", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(new JFrame(), "converted failed", " ERROR ", JOptionPane.ERROR_MESSAGE);
		}
	}
}
