import java.net.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class LoginChecker {

	static Connection con;
	static Statement st;
	static ResultSet rs;

	public static void sqlConnect() throws SQLException, MalformedURLException
	{
		URL url = new URL("http://mnhServer.mooo.com");
		con = DriverManager.getConnection("jdbc:mysql://" + url.getHost() + ":3306/minigameheaven", "root", "12345");

		st = con.createStatement();
		rs = null;
	}

	public static boolean passwordCheck(String password) {
		System.out.println("Login Checking...");
		try {
			rs = st.executeQuery("select count(user_pw) from user where user_pw = \"" + password + "\"");
			rs.next();
			// If also password correct
			if (rs.getString(1).equals("1")) {
				return true;
				// login success
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return false;
	}

	// Check If ID is already exists, if so return true
	public static boolean IDCheck(String ID) throws SQLException, MalformedURLException {
		// connect to MySql
		sqlConnect();

		try {
			rs = st.executeQuery("select count(user_id) from user where user_id = \"" + ID + "\"");
			rs.next();

			if (rs.getString(1).equals("1"))
				return true; // ID is already exists
			else
				return false; // ID doesn't exists

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
		}
		return false;
	}

	private static void loginCheck(DataOutputStream sender, BufferedReader receiver) throws IOException, SQLException {
		String message, receivedID, receivedPW;
		System.out.println("!!!!!!");
		// 클라이언트에게 ID를 보내라고 요청
		message = "Give me ID";
		sender.writeBytes(message + '\n');

		// ID 받기
		receivedID = receiver.readLine();
		System.out.println("ID received");

		if (IDCheck(receivedID)) // ID가 맞으면
		{
			// password 요청
			System.out.println("PW Checking...");
			sender.writeBytes("Give me password\n");
			receivedPW = receiver.readLine();
			System.out.println("PW received");
			if (passwordCheck(receivedPW))// passwordCheck 실행
			{
				sender.writeBytes("Login Success\n"); // Login 성공

			} else {
				sender.writeBytes("Login Failed\n"); // Login 실패
			}
		} else
			sender.writeBytes("Login Failed\n"); // Login 실패
	}

	public static void main(String[] args) throws IOException, SQLException, ParseException {
		// Server Connect
		ServerSocket server = new ServerSocket(8888);
		String message;
		System.out.println("로그인 서버 실행중...");
		// ChatServer chatServer = new ChatServer();
		while (true) {
			Socket client = server.accept();
			System.out.println("Connected");

			DataOutputStream sender = new DataOutputStream(client.getOutputStream());
			BufferedReader receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));

			message = receiver.readLine();

			// To login
			if (message.startsWith("LoginCheck")) {
				loginCheck(sender, receiver);
			} else if (message.startsWith("IDCheck")) {
				System.out.println("ID Checking...");
				sender.writeBytes("Give Me ID\n");
				message = receiver.readLine();

				if (!IDCheck(message)) {
					System.out.println("Success");
					sender.writeBytes("success\n");
				} else
					sender.writeBytes("failed\n");
			} else if (message.startsWith("Sign"))// To Sign Up
				{
				sender.writeBytes("OK\n");
				String ID, PW, name;
				ID = receiver.readLine();
				PW = receiver.readLine();
				name = receiver.readLine();
				DateFormat time = new SimpleDateFormat("dd/MM/yyyy");
				String stime = time.format(new Date());
				Date date = time.parse(stime);
				java.sql.Date cal = new java.sql.Date(date.getTime());

				String insert = "insert into user(user_id,user_pw,name,time_signup) values(?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(insert);
				ps.setString(1, ID);
				ps.setString(2, PW);
				ps.setString(3, name);
				ps.setDate(4, cal);

				ps.executeUpdate();
				sender.writeBytes("OK\n");
			}
		}
	}

}
