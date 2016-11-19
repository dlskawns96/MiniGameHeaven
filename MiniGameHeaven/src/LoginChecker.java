import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public static void main(String[] args) {
		
	}
	
}
