import javax.swing.*;

public class ClientTest {
	public static void main(String args[]) {
		Client c;
		String ip = JOptionPane.showInputDialog("Enter the IP Address: ");
		
		// View your own computer as a server
		c = new Client(ip);//"127.0.0.1");
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.startRunning();
		c.setVisible(true);
	}
}
