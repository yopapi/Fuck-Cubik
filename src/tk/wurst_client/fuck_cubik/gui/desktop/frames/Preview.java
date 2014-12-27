package tk.wurst_client.fuck_cubik.gui.desktop.frames;

import java.awt.BorderLayout;
import java.awt.Canvas;

import tk.wurst_client.fuck_cubik.preview.PreviewToolBar;

public class Preview extends AbstractFrame
{
	public Canvas canvas;
	public PreviewToolBar toolbar;
	public boolean showGrid = true;
	public boolean showFocus = true;
	
	public Preview()
	{
		super("Preview", false, false, false);
		this.canvas = new Canvas();
		canvas.setSize(600, 600);
		canvas.setMinimumSize(canvas.getSize());
		canvas.setMaximumSize(canvas.getSize());
		canvas.setPreferredSize(canvas.getSize());
		this.toolbar = new PreviewToolBar();
		this.add(toolbar, BorderLayout.NORTH);
		this.add(canvas, BorderLayout.CENTER);
		this.setSize(600, 600);
		this.setLocation(0, 0);
	}
}