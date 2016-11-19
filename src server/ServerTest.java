

public class ServerTest{
    
    
    
    
    public ServerTest()
    {
        // Title
        
        
        
		
		
		
    }
    
    public static void main(String args[]) {
        
        ServerFrame chatbox = new ServerFrame();
        ServerClient initialServerClient = new ServerClient(chatbox);
        
        
        initialServerClient.run();
    }
   
}
