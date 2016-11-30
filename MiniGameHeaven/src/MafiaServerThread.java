import java.io.IOException;
import java.net.*;

public class MafiaServerThread extends Thread{

	
	public void run()
	{	
		ServerSocket server;
		Socket client;
		try {
			 server = new ServerSocket(9999);
			 client = server.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void deleteRoom()
	{
		
	}
}
