package DataBase;

import java.io.*;
import DataBase.tempDBinformation;
import java.sql.*;
import java.util.*;

public class MySQL {
	
	Statement stm;//DB에 SQL을 전달하기 위한 statement객체 
	ResultSet rs;
	
	Connection conn = null;	
	
	Vector GameResultVC = new Vector();
	
	static String num;
	static String winner;
	static String startDate;
	static String endDate;
	
	private tempDBinformation TDI = new tempDBinformation();
	
	
	
	public void insertGameResult(Hashtable insertDate){ //게임 종료시 승자, 패자, 시간 등을 DB에 저장
		try{
			
		/*=================JDBC Driver 연결==================*/
		Class.forName("com.mysql.jdbc.Driver");//jdbc클래스를 찾는다.
		String url = "jdbc:mysql://localhost:9999/test";//스키마 이름 
		String id="root";//유저이름
		String pw="admin";//유저 비밀번호
		conn = DriverManager.getConnection(url,id,pw);
		System.out.println("insert connected");
		/*==================================================*/
			
		
		
		/*=================쿼리 작성 및 전송==================*/
		String Qurey = "INSERT INTO gameresult "
				+"(winner, startdate, enddate, playdate) VALUES ("
				+"'"+insertDate.get("winner")+"'"+", "
				+"'"+insertDate.get("startdate")+"'"+", "
				+"'"+insertDate.get("enddate")+"'"+", "
				+"'"+insertDate.get("palydate")+"'"+");";
		
		stm = conn.createStatement();
		int row = stm.executeUpdate(Qurey);
		/*==================================================*/
		
		
		System.out.println("생성된 row의 수 : " + row);
		
		}catch(SQLException e1){
			e1.printStackTrace();
		}catch(Exception e2){
			e2.printStackTrace();
		}finally{
			try{
				if(stm != null){stm.close();}
			}catch(Exception ex){}
		}
	}
	
	
	public Vector printResult(){ //대전기록 클릭시 현재까지의 대전기록을 DB에서 불러와 출력
		try{
			
			/*=================JDBC Driver 연결==================*/
			Class.forName("com.mysql.jdbc.Driver");//jdbc클래스를 찾는다.
			String url = "jdbc:mysql://localhost:9999/test";
			String id="root";
			String pw="admin";
			conn = DriverManager.getConnection(url,id,pw);
			/*==================================================*/
				
			/*=================쿼리 작성 및 전송==================*/
			String Qurey = "select * from gameresult";
			stm = conn.createStatement();
			rs = stm.executeQuery(Qurey);
			/*=================================================*/
			
			while(rs.next()){//쿼리의 결과값을 반복문을 통해 하나씩 출력.
				GameResultVC.addElement(rs.getString("Num"));
				GameResultVC.addElement(rs.getString("winner"));
				GameResultVC.addElement(rs.getString("startdate"));
				GameResultVC.addElement(rs.getString("enddate"));
				GameResultVC.addElement(rs.getString("playdate"));
			}
			
			}catch(SQLException e1){
				e1.printStackTrace();
			}catch(Exception e2){
				e2.printStackTrace();
			}finally{
				try{
					if(stm != null){stm.close();}
			    }catch(Exception ex){}
		}
		return GameResultVC;//그 값을 리턴
		
	}
}
