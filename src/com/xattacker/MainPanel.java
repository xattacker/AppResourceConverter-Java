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

import com.xattacker.convert.ResourceConverter;
import com.xattacker.convert.android.Android2iOSResourceConverter;
import com.xattacker.convert.i18n.I18nResourceConverter;
import com.xattacker.convert.ios.IOS2AndroidResourceConverter;
import com.xattacker.convert.ios.IOSResourceFormatter;


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
								showDialog(" SUCCEED ", "format succeed", DialogType.INFORMATION);
							}
							else
							{
								showDialog(" ERROR ", "format failed", DialogType.ERROR);
							}
						}
					},
					"select file (*.strings)|*.strings",
					"strings"
				);
			}
		});
		
		button = new Button("Convert i18n localization Resource File");
		add(button);
		button.setBounds(30, 180, 300, 25);
		button.setEnabled(true);
		button.addActionListener(
		new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				chooseFolder(
					new FileSelectedListener()
					{
						@Override
						public void onFileSelected(File aFile)
						{
							I18nResourceConverter converter = new I18nResourceConverter();
							convertResource(aFile, converter);
						}
					}
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
	
	private void chooseFolder(FileSelectedListener aListener)
	{
		try
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		   chooser.setAcceptAllFileFilterUsed(false); // disable the "All files" option.
		    
			int n = chooser.showOpenDialog(this);
			if (n == JFileChooser.APPROVE_OPTION)
			{
				File selected = chooser.getSelectedFile();
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
			showDialog(" SUCCEED ", "convert succeed", DialogType.INFORMATION);
		}
		else
		{
			showDialog(" ERROR ", "convert failed", DialogType.ERROR);
		}
	}
	
	public enum DialogType
	{
		INFORMATION(JOptionPane.INFORMATION_MESSAGE),
		WARNING(JOptionPane.WARNING_MESSAGE),
		ERROR(JOptionPane.ERROR_MESSAGE);
		
		int _type;
		
		private DialogType(int aType)
		{
			_type = aType;
		}
	};
	
	private void showDialog(String title, String message, DialogType type)
	{
		JOptionPane.showMessageDialog(new JFrame(), message, title, type._type);
	}
}
