import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class MainServer {
public static void main(String[] args) throws IOException, SQLException, ParseException{
	
	
	
	Runnable lc = new LoginChecker();
	Thread lcThread = new Thread(lc);
	
	System.out.println("WTF");
	ChatServer cs = new ChatServer(lcThread);
	
	
	
	
}
	MainServer(){
		
		//LoginChecker lc = new LoginChecker();
		//ChatServer cs = new ChatServer();
	}
}
