import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class MainServer {

	public static ChatServer cs;

	public static void main(String[] args) throws IOException, SQLException, ParseException {

		Runnable lc = new LoginChecker();
		Thread lcThread = new Thread(lc);
		cs = new ChatServer(lcThread);
		
	}

	MainServer() {
		
	}
}
