import javax.swing.*;

public class ClientTest {
	public static void main(String args[]) {
		Client c;
		String ip = JOptionPane.showInputDialog("Enter the IPv4 Address: ");
		
		c = new Client(ip);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.startRunning();
		c.setVisible(true);
	}
}
