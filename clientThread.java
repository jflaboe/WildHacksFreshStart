import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. The thread broadcast the incoming messages to all clients and
 * routes the private message to the particular client. When a client leaves the
 * chat room this thread informs also all the clients about that and terminates.
 */
public class clientThread extends Thread {
	
	private String clientName = null;
	
	// Input and output streams
	private ObjectInputStream is = null;
	private ObjectOutputStream os = null;
	
	private Socket clientSocket = null;
	private final clientThread[] threads;
	private int maxClientsCount;
	
	String line;
	private String username;
	
	public clientThread(Socket clientSocket, clientThread[] threads) {
	    this.clientSocket = clientSocket;
	    this.threads = threads;
	    maxClientsCount = threads.length;
	}
	
	public void run() {
		int maxClientsCount = this.maxClientsCount;
		clientThread[] threads = this.threads;
		
		try {
			/*
			 * Create input and output streams for this client
			 */
			is = new ObjectInputStream(clientSocket.getInputStream());
			os = new ObjectOutputStream(clientSocket.getOutputStream());
			
			//username = JOptionPane.showInputDialog("Enter your username: ");
			while(true)	{
				os.writeObject("\nEnter your name.");
				
				try {
					username = ((String) is.readObject()).trim();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
		        if (username.indexOf('@') == -1) {
		            break;
		        } else {
		            os.writeObject("\nThe name should not contain the '@' character.");
		        }
			}
			
			// Welcome the new client
			os.writeObject("\nWelcome " + username + " to the chat!\nTo leave, enter /quit in a new"
					+ "line.\nTo send private messages, enter @<name> <message>.");
		    synchronized (this) {
		    	for (int i = 0; i < maxClientsCount; i++) {
		            if (threads[i] != null && threads[i] == this) {
		              clientName = "@" + username;
		              break;
		            }
		        }
		        for (int i = 0; i < maxClientsCount; i++) {
		            if (threads[i] != null && threads[i] != this) {
		            	threads[i].os.writeObject("\n" + username + " has entered the chat room!");
		            }
		        }
		    }
		    
		    // Start the conversation
		    while(true) {
				try {
					line = (String) is.readObject();
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if(line.startsWith("/quit"))
		    		break;
				
		    	// If the message is private, send to given client
				if(line.startsWith("@")) {
			         String[] words = line.split("\\s", 2);
			         if (words.length > 1 && words[1] != null) {
			        	 words[1] = words[1].trim();
			        	 if (!words[1].isEmpty()) {
			        		 synchronized (this) {
			        			 for (int i = 0; i < maxClientsCount; i++) {
			        				 if (threads[i] != null && threads[i] != this
			        						 && threads[i].clientName != null
			        						 && threads[i].clientName.equals(words[0])) {
			        					 threads[i].os.writeObject("\n<" + username + "> " + words[1]);//println("<" + username + "> " + words[1]);
			        					 /*
			        					  * Echo this message to let the client know the private
			        					  * message was sent.
			        					  */
			        					 this.os.writeObject("\n>" + username + "> " + words[1]);//println(">" + username + "> " + words[1]);
			        					 break;
			        				 }
			        			 }
			        		 }
			        	 }
			         }
				}
				else {
					// The message is public, broadcast to all other clients
					synchronized (this) {
						for (int i = 0; i < maxClientsCount; i++) {
							if (threads[i] != null && threads[i].clientName != null) {
								threads[i].os.writeObject("<" + username + "> " + line);
							}
						}
					}
				}
		    }
		    
		    // When a client leaves the room
		    synchronized (this) {
		    	for (int i = 0; i < maxClientsCount; i++) {
		            if (threads[i] != null && threads[i] != this && threads[i].clientName != null) {
		            	threads[i].os.writeObject("\n" + username
		            			+ " is leaving the chat room.");
		            }
		        }
		    }
		    
		    /*
		     * Clean up -> set the current thread variable to null so that a new
		     * client could be accepted by the server.
		     */
		    synchronized (this) {
		        for (int i = 0; i < maxClientsCount; i++) {
		            if (threads[i] == this)
		            	threads[i] = null;
		        }
		    }
		    
		    /*
		     * Close the output stream, input stream, and socket
		     */
		    is.close();
		    os.close();
		    clientSocket.close();
		}
		catch (IOException e) {
			e.getStackTrace();
		}
	}
}
