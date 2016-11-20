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
	
	public static boolean passwordCheck(String password)
	{
		System.out.println("Login Checking...");
		try {		
			rs = st.executeQuery("select count(password_id) from user where password_id = \"" + password + "\"");
			rs.next();
			//If also password correct
			if(rs.getString(1).equals("1"))
			{
				return true;
				//login success
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
	
	public static void main(String[] args) throws IOException, SQLException {
		//Server Connect
		ServerSocket server = new ServerSocket(1112);
		String message;
		String receivedID;
		String receivedPW;
		
		Socket client = server.accept();
		System.out.println("Connected");
		
		DataOutputStream sender = new DataOutputStream(client.getOutputStream());
		BufferedReader receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		
		
		message = receiver.readLine();
		
		//To login
		if(message.startsWith("LoginCheck"))
		{
			System.out.println("!!!!!!");
			//클라이언트에게 ID를 보내라고 요청
			message = "Give me ID";
			sender.writeBytes(message + '\n');
			
			//ID 받기
			receivedID = receiver.readLine();
			System.out.println("ID received" + receivedID);
			
			if(IDCheck(receivedID)) //ID가 맞으면
			{
				//password 요청
				sender.writeBytes("Give me password\n");
				receivedPW = receiver.readLine();
				if(passwordCheck(receivedPW))//passwordCheck 실행
					sender.writeBytes("Login Success\n"); //Login 성공				
				else
					sender.writeBytes("Login Failed"); //Login 실패
			}
			else
				sender.writeBytes("Login Failed"); //Login 실패
		}
		else if(message.startsWith("IDCheck")) //To redundancy check
		{
			//클라이언트에게 ID를 보내라고 요청
		}
	
		
	}
	
}
