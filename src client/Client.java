import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	// Constructor
	public Client(String host) {
		super("Client Instant Messager");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				// Send message once enter key is pressed
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	public void startRunning() {
		try {
			connectToServer();
			setUpStreams();
			whileChatting();
		}
		catch (EOFException eofException) {
			showMessage("\n Client terminated connection.");
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		finally {
			closeEverything();
		}
	}
	
	// Connect to server
	private void connectToServer() throws IOException {
		showMessage("Attempting connection...\n");
		
		// Make a new socket
		// Passes in IP address and Port on the server
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connected to " + connection.getInetAddress().getHostName());
	}
	
	// Set up streams
	private void setUpStreams() throws IOException{
		
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now set up! \n");
	}
	
	// While chatting with server
	private void whileChatting() throws IOException {
		//String message = "You are now connected!";
		//sendMessage(message);
		ableToType(true);
		
		do {
			// Have a conversation
			try {
				message = (String)input.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n Cannot compute.");
			}
		}
		// Ends the conversation when the client types END
		while(!message.equals("SERVER - END"));
	}
	
	// Closing streams and sockets
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
	
	// Send a message to server
	private void sendMessage(String message) {
		try {
			
			// Create an object and send it to the output stream
			output.writeObject("CLIENT - " + message);
			output.flush();
			
			// Now that the message is sent, you want it to display on the screen
			showMessage("\nCLIENT - " + message);
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
