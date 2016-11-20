import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
/**
 * Write a description of class ServerFrame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ServerFrame extends JFrame
{
    // instance variables - replace the example below with your own
    public JTextField userText;
    public JTextArea chatWindow;
    public ArrayList<ServerClient> connections;

    /**
     * Constructor for objects of class ServerFrame
     */
    public ServerFrame()
    {
        // initialise instance variables
        super("Instant Messenger/Server example");
        userText = new JTextField();
        chatWindow = new JTextArea();
        userText.setEditable(false);
		userText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				// Send message once enter key is pressed
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.NORTH);
		connections = new ArrayList<ServerClient>();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
     public void sendMessage(String st){
        for (ServerClient s: connections)
            s.sendMessage(st);
        
    }
    public void addConnection(ServerClient client){
        connections.add(client);
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
   
}
