package tk.wurst_client.fuck_cubik.dialogs;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import tk.wurst_client.fuck_cubik.Main;
import tk.wurst_client.fuck_cubik.update.VersionManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ErrorReport extends JDialog
{
	private JTextArea commentField;
	private JScrollPane commentPane;
	private JButton sendButton;
	
	private String action;
	private String stacktrace;
	private String version;
	private String os;
	private String java;
	private String screen;
	private String comment;
	
	public ErrorReport(String action, String stacktrace)
	{
		super(Main.frame, "Error report");
		this.action = action;
		this.stacktrace = stacktrace;
		version = VersionManager.FORMATTED_VERSION;
		os = System.getProperty("os.name");
		java = System.getProperty("java.version");
		screen = Toolkit.getDefaultToolkit().getScreenSize().width + "x" + Toolkit.getDefaultToolkit().getScreenSize().height;
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JLabel label = new JLabel("<html>"
			+ "<center>"
			+ "<h1>Error report</h1>", JLabel.CENTER);
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		label = new JLabel("<html>"
			+ "<p>The following information will be sent:</p>"
			+ "<ul>"
			+ "<li>Last action before the error occured: " + this.action
			+ "<li>Java stacktrace"
			+ "<li>Fuck Cubik version: " + version
			+ "<li>Operating system: " + os
			+ "<li>Java version: " + java
			+ "<li>Screen resolution: " + screen
			+ "</ul>"
			+ "<p>Add a comment (optional):</p>");
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
		commentField = new JTextArea();
		commentField.setAlignmentX(CENTER_ALIGNMENT);
		commentField.setFont(new Font("Arial", Font.PLAIN, 12));
		commentPane = new JScrollPane(commentField);
		commentPane.setAlignmentX(CENTER_ALIGNMENT);
		commentPane.setPreferredSize(new Dimension(commentPane.getPreferredSize().width, 75));
		commentPane.setMaximumSize(new Dimension(commentPane.getMaximumSize().width, 150));
		add(commentPane);
		sendButton = new JButton("Send");
		sendButton.setAlignmentX(CENTER_ALIGNMENT);
		sendButton.setFont(new Font(sendButton.getFont().getFamily(), Font.BOLD, 18));
		sendButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					ErrorReport.this.comment = ErrorReport.this.commentField.getDocument().getText(0, ErrorReport.this.commentField.getDocument().getLength());
				}catch(BadLocationException e2)
				{
					ErrorReport.this.comment = "";
				}
				ErrorReport.this.dispose();
				try
				{
					HttpURLConnection get = (HttpURLConnection)new URL("http://fuck-cubik.wurst-client.tk/api/v1/error-report/").openConnection();
					get.setRequestMethod("GET");
					get.connect();
					JsonObject json = new JsonParser().parse(new InputStreamReader(get.getInputStream())).getAsJsonObject();
					String formURL = "https://docs.google.com/forms/d/"
						+ json.get("form").getAsString()
						+ "/formResponse"
						+ "?entry." + json.get("params").getAsJsonObject().get("action").getAsString()
						+ "=" + URLEncoder.encode(ErrorReport.this.action, "UTF-8")
						+ "&entry." + json.get("params").getAsJsonObject().get("stacktrace").getAsString()
						+ "=" + URLEncoder.encode(ErrorReport.this.stacktrace, "UTF-8")
						+ "&entry." + json.get("params").getAsJsonObject().get("version").getAsString()
						+ "=" + URLEncoder.encode(ErrorReport.this.version, "UTF-8")
						+ "&entry." + json.get("params").getAsJsonObject().get("os").getAsString()
						+ "=" + URLEncoder.encode(ErrorReport.this.os, "UTF-8")
						+ "&entry." + json.get("params").getAsJsonObject().get("java").getAsString()
						+ "=" + URLEncoder.encode(ErrorReport.this.java, "UTF-8")
						+ "&entry." + json.get("params").getAsJsonObject().get("screen").getAsString()
						+ "=" + URLEncoder.encode(ErrorReport.this.screen, "UTF-8")
						+ "&entry." + json.get("params").getAsJsonObject().get("comment").getAsString()
						+ "=" + URLEncoder.encode(ErrorReport.this.comment, "UTF-8");
					HttpsURLConnection post = (HttpsURLConnection)new URL(formURL).openConnection();
					post.setInstanceFollowRedirects(true);
					post.connect();
					BufferedReader input = new BufferedReader(new InputStreamReader(post.getInputStream()));
					String response = input.readLine();
					for(String line; (line = input.readLine()) != null;)
						response += "\n" + line;
					boolean success = response.contains(json.get("success_message").getAsString());
					int action = JOptionPane.showOptionDialog(
						Main.frame,
						"<html>"
						+ "<body width=\"256px\">"
						+ "<p>Your error report "
						+ (success ? "has been sent" : "was rejected")
						+ ".</p>"
						+ "<p>"
						+ (success ? "Thank you!" : "Maybe the server doesn't accept error reports at the moment.")
						+ "</p>",
						success ? "Success" : "Failure",
						JOptionPane.DEFAULT_OPTION,
						success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE,
						null,
						new String[]{"OK", "View error reports"},
						0);
					if(action == 1)
						Desktop.getDesktop().browse(new URI("http://fuck-cubik.wurst-client.tk/error-reports/"));
				}catch(Exception e1)
				{
					e1.printStackTrace();
					JOptionPane.showMessageDialog(
						Main.frame,
						"<html>"
							+ "<body width=\"256px\">"
							+ "<p>Error report could not be sent.</p>"
							+ "<p>Please check your internet connection.</p>",
						"Failure",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		add(sendButton);
		pack();
		setLocationRelativeTo(Main.frame);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setVisible(true);
	}
}