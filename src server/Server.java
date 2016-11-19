import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	
	// Socket is the connection between computers -> server and client
	private Socket connection;
	
	// constructor
	public Server() {
		// Title
		super("Instant Messenger/Server example");
		
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				// Send message once enter key is pressed
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		
		// Add to screen
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	// Set up and run the server
	public void startRunning() {
		try {
			// Port: 6789, Backlog(queue length): how many people can wait to access the server
			server = new ServerSocket(6789, 100);
			while(true) {
				try {
					// First, wait for someone else to connect
					waitForConnection();
					// Set up output and input streams -> connection
					setUpStreams();
					// Allow sending messages
					whileChatting();
				}
				catch(EOFException eofException) {
					// End of connection
					showMessage("\n Server ended the connection!");
				}
				finally {
					closeEverything();
				}
			}
		}
		catch(IOException ioexception) {
			// If error, print where the error is
			ioexception.printStackTrace();
		}
	}
	
	// This method will wait for a connection, then dieplay connection information
	private void waitForConnection() throws IOException{
		showMessage("Waiting for someone to connect...\n");
		
		// Make a new socket once a connection is made
		connection = server.accept();
		// Return internet ip address
		showMessage("Now connect to " + connection.getInetAddress().getHostName());
	}
	
	// Get stream to send and receive data
	// Stream is a way to communicate with another computer
	private void setUpStreams() throws IOException{
		
		// Send
		// Creating a pathway to connect to the computer the socket created
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		// Receive
		// The socket where data is coming in
		input = new ObjectInputStream(connection.getInputStream());
		
		showMessage("\n Streams are now set up! \n");
	}
	
	// This will be run during the chat conversation
	private void whileChatting() throws IOException {
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);
		
		do {
			// Have a conversation
			try {
				message = (String)input.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n No idea what the user sent.");
			}
		}
		// Ends the conversation when the client types END
		while(!message.equals("ClIENT - END"));
	}
	
	// Shuts down all streams and sockets once the conversation is over
	private void closeEverything() {
		showMessage("\nClosing connections...\n");
		ableToType(false);
		
		// Shut down streams and sockets
		try {
			output.close();
			input.close();
			connection.close();
		}
		catch (IOException ioexception){
			ioexception.printStackTrace();
		}
	}
	
	// Send a message to client
	private void sendMessage(String message) {
		try {
			
			// Create an object and send it to the output stream
			output.writeObject("SERVER - " + message);
			output.flush();
			
			// Now that the message is sent, you want it to display on the screen
			showMessage("\nSERVER - " + message);
		}
		catch(IOException ioexception) {
			chatWindow.append("\n ERROR: Message cannot be sent.");
		}
	}
	
	// Updates chat window
	private void showMessage(final String text) {
		// Only update a part of the GUI -> a thread
		SwingUtilities.invokeLater(
			// Create a new thread
			new Runnable() {
				public void run() {
					chatWindow.append(text);
				}
			}
		);
	}
	
	// Lets the user type in the box
	private void ableToType(final boolean input) {
		SwingUtilities.invokeLater(
			// Create a new thread
			new Runnable() {
				public void run() {
					userText.setEditable(input);
				}
			}
		);
	}
}
