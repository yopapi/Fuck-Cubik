package tk.wurst_client.fuck_cubik.preview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import tk.wurst_client.fuck_cubik.Main;

public class PreviewToolBar extends JToolBar
{
	public JButton resetButton;
	public JButton refreshButton;
	public JCheckBox gridBox;
	public JCheckBox focusBox;
	
	public PreviewToolBar()
	{
		super();
		resetButton = new JButton("Reset camera");
		resetButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.renderer.reset();
			}
		});
		this.add(resetButton);
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new SwingWorker<Object, Object>()
				{
					
					@Override
					protected Object doInBackground() throws Exception
					{
						try
						{
							Main.renderer.refresh();
						}catch(Exception e)
						{
							System.out.println("--- REFRESH FAILED ---");
							e.printStackTrace();
						}
						return null;
					}
				}.execute();
			}
		});
		this.add(refreshButton);
		gridBox = new JCheckBox("Show grid", true);
		gridBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.frame.desktop.preview.showGrid = gridBox.isSelected();
			}
		});
		this.add(gridBox);
		focusBox = new JCheckBox("Show focus", true);
		focusBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Main.frame.desktop.preview.showFocus = focusBox.isSelected();
			}
		});
		this.add(focusBox);
	}
}
