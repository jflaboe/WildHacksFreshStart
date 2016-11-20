
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * A chat server that delivers public and private messages
 */
public class MultiThreadServer extends JFrame {
	
	private JTextField userText;
	public JTextArea chatWindow;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	public String username;
	private boolean connected;
	
	// Socket is the connection between computers -> server and client
	
	// The server socket
	private ServerSocket serverSocket = null;
	// The client socket
	private Socket clientSocket = null;
	
	// This chat server can accept up to maxClientsCount clients' connections.
	private int maxClientsCount = 10;
	private clientThread[] threads = new clientThread[maxClientsCount];
	
	private static int port = 6789;
	
	
	
	
	public static void main(String args[]) {
		MultiThreadServer m = new MultiThreadServer();
	}
	
	// constructor
	public MultiThreadServer() {
		// Title
		super("Server Instant Messaging");
		
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
		setSize(500,500);
		setVisible(true);
		
		
	    /*
	     * Open a server socket on the port number (default 6789)
	     */
		try {
			serverSocket = new ServerSocket(port);
		}
		catch (IOException e){
			e.getStackTrace();
		}
		
		/*
		 * Create a client socket for each connection
		 * and pass it through a new client thread.
		 */
		while(true) {
			try {
				clientSocket = serverSocket.accept();
				int i = 0;
				for(i = 0; i < maxClientsCount; i++) {
					if(threads[i] == null) {
						(threads[i] = new clientThread(clientSocket, threads)).start();
						break;
					}
				}
				if(i == maxClientsCount) {
					ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
					os.writeObject("Server too busy. Try again later.");
					os.close();
					clientSocket.close();
				}
			}
			catch (IOException e) {
				e.getStackTrace();
			}
		}
	}
	
	// Send a message to client
	private void sendMessage(String message) {
		try {
			
			// Create an object and send it to the output stream
			os.writeObject("SERVER - " + message);
			os.flush();
			
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
	/*
	// Set up and run the server
	public void startRunning() {
		try {
			// Port: 6789, Backlog(queue length): how many people can wait to access the server
			serverSocket = new ServerSocket(6789, 100);
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
		socket = serverSocket.accept();
		
		// Return internet ip address
		showMessage("Now connected to " + socket.getInetAddress().getHostName());
	}
	
	// Get stream to send and receive data
	// Stream is a way to communicate with another computer
	private void setUpStreams() throws IOException{
		
		// Send
		// Creating a pathway to connect to the computer the socket created
		output = new ObjectOutputStream(socket.getOutputStream());
		output.flush();
		
		// Receive
		// The socket where data is coming in
		input = new ObjectInputStream(socket.getInputStream());
		
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
			socket.close();
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
	}*/
}