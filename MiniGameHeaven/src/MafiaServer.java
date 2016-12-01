import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class MafiaServer {

	public static void main(String[] args) throws IOException {
		String message;
		ServerSocket server = new ServerSocket(9999);
		Socket mainClient = server.accept();
		
		DataOutputStream sender = new DataOutputStream(mainClient.getOutputStream());
		BufferedReader receiver = new BufferedReader(new InputStreamReader(mainClient.getInputStream()));

		message = receiver.readLine();
		
		while(true)
		{
			if(message.equals("room created"))
			{
				MafiaServerThread newThread = new MafiaServerThread();
				newThread.start();
				message = receiver.readLine();				
				//방 없애기
				if(message.equals("end"))
				{
					
				}
			}
		}

	}

}
