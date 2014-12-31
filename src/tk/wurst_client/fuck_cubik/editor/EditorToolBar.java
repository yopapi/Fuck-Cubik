package tk.wurst_client.fuck_cubik.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import tk.wurst_client.fuck_cubik.Main;
import tk.wurst_client.fuck_cubik.dialogs.ErrorMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class EditorToolBar extends JToolBar
{
	public JButton formatButton;
	public JButton newElementButton;
	
	public EditorToolBar()
	{
		formatButton = new JButton("Format code");
		formatButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					String oldCode = Main.frame.desktop.editor.textarea.getDocument().getText(0, Main.frame.desktop.editor.textarea.getDocument().getLength());
					String newCode = gson.toJson(new JsonParser().parse(oldCode));
					if(newCode.equals("null"))
						throw new JsonSyntaxException("No code found.");
					Main.frame.desktop.editor.textarea.setText(newCode);
				}catch(Exception e1)
				{
					new ErrorMessage("formatting code", e1);
				}
			}
		});
		this.add(formatButton);
		newElementButton = new JButton("New element");
		newElementButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		this.add(newElementButton);
	}
}
