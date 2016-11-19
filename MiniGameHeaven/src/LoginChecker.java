import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;

public class LoginChecker{

	static Connection con;
	static Statement st;
	static ResultSet rs;
	
	
	public static void sqlConnect() throws SQLException
	{
		con = DriverManager.getConnection("jdbc:mysql://localhost",	"root", "12345");
		
		st = con.createStatement();
		rs = null;
		st.executeQuery("use minigameheaven");
	}
	
	public static boolean loginCheck(String ID, String password)
	{
		System.out.println("Login Checking...");
		try {
		
			//If ID correct
			if(IDCheck(ID))
			{
				rs = st.executeQuery("select count(password_id) from user where password_id = \"" + password + "\"");
				rs.next();
				//If also password correct
				if(rs.getString(1).equals("1"))
				{
					return true;
					//login success
				}
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return false;
	}
	
	//Check If ID is already exists, if so return true
	public static boolean IDCheck(String ID) throws SQLException
	{
		//connect to mysql		
		sqlConnect();
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost",	"root", "12345");
			st = con.createStatement();			
			rs = null;
			
			st.executeQuery("use minigameheaven");
			rs = st.executeQuery("select count(user_id) from user where user_id = \"" + ID + "\"");
			rs.next();
			
			if(rs.getString(1).equals("1"))
				return true; //ID is already exists
			else 
				return false; //ID doesn't exists
			
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException {
		//Server Connect
		ServerSocket server = new ServerSocket();
		InetSocketAddress ipep = new InetSocketAddress(9999);
		server.bind(ipep);

		Socket client = server.accept();
		System.out.println("Connected");
		
		OutputStream sender = client.getOutputStream();
		InputStream receiver = client.getInputStream();
		String message;
		
		byte[] data = new byte[15];
		receiver.read(data,0,data.length);
		message = new String(data);
		
		//To login
		if(message.startsWith("LoginCheck"))
		{
			System.out.println("!!!!!!");
			//클라이언트에게 ID를 보내라고 요청
			message = "Give me ID";
			data = message.getBytes();
			sender.write(data, 0, data.length);
			
			//ID 받기
			receiver.read(data,0,data.length);
			message = new String(data);
			System.out.println("ID received" + message);
		}
		else if(message.startsWith("IDCheck")) //To redundancy check
		{
			//클라이언트에게 ID를 보내라고 요청
		}
		
	}
	
}
