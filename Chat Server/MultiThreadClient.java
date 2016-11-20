import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MultiThreadClient extends JFrame implements Runnable {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private String message = "";
	private String serverIP;
	private static int port = 6789;
	
	// Client Socket
	private Socket clientSocket;
	// Output Stream
	private ObjectOutputStream os = null;
	// Input Stream
	private ObjectInputStream is = null;
	
	//private static boolean closed = false;
	
	public static void main(String args[]) {
		MultiThreadClient c = new MultiThreadClient();
	}
	
	// Constructor
	public MultiThreadClient() {
		super("Client Instant Messaging");
		
		// Input host
		String host = JOptionPane.showInputDialog("Enter in the IPv4 address: ");
		
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
		setSize(500,500);
		setVisible(true);
		
		/*
		 * Open a socket on a given host and port. Open input and output streams.
		 */
		try {
			// Create a new socket
			connectToServer();
			// Create input and output streams
			setUpStreams();
			whileChatting();
		}
		catch(UnknownHostException e) {
			showMessage("\nDon't know about host " + host);
		}
		catch(EOFException e) {
			showMessage("\nYou left the room.");
		}
		catch(IOException e) {
			e.getStackTrace();
		}
		
	    /*
	     * If everything has been initialized then we want to write some data to the
	     * socket we have opened a connection to on the port.
	     */
		if(!clientSocket.isClosed() && os != null && is != null) {
			try {
				/* Create a thread to read from the server. */
		        new Thread(new MultiThreadClient()).start();
		        
		        /*
		         * Close the output stream, input stream, and socket.
		         */
		        closeEverything();
			}
			catch (Exception e){
				e.getStackTrace();
			}
		}
	}
	
	// Send a message to server
	private void sendMessage(String message) {
		try {
			
			// Create an object and send it to the output stream
			os.writeObject(message);
			os.flush();
			
			// Now that the message is sent, you want it to display on the screen
			//showMessage("\n" + message);
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
	
	
	
/*	public void startRunning() {
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
	}*/
	
	// Connect to server
	private void connectToServer() throws IOException {
		showMessage("Attempting connection...\n");
		
		// Make a new socket
		// Passes in IP address and Port on the server
		clientSocket = new Socket(InetAddress.getByName(serverIP), port);
		showMessage("Connected to " + clientSocket.getInetAddress().getHostName());
	}
	
	// Set up streams
	private void setUpStreams() throws IOException{
		
		os = new ObjectOutputStream(clientSocket.getOutputStream());
		os.flush();
		
		is = new ObjectInputStream(clientSocket.getInputStream());
		showMessage("\nStreams are now set up! \n");
	}
	
	// While chatting with server
	private void whileChatting() throws IOException {
		//String message = "You are now connected!";
		//sendMessage(message);
		ableToType(true);
		
		do {
			// Have a conversation
			try {
				message = (String)is.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n Cannot compute.");
			}
		}
		// Ends the conversation when the client types END
		while(!message.equals("Server - END"));
	}
	
	// Closing streams and sockets
	private void closeEverything() {
		showMessage("\nClosing connections...\n");
		ableToType(false);
		
		// Shut down streams and sockets
		try {
			os.close();
			is.close();
			clientSocket.close();
		}
		
		catch (IOException ioexception){
			ioexception.printStackTrace();
		}
	}
	
	/*
	 * Create a thread to read from the server. (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
	}
	
}
