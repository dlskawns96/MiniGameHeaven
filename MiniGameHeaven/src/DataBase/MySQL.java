package DataBase;

import java.io.*;
import DataBase.tempDBinformation;
import java.sql.*;
import java.util.*;

public class MySQL {
	
	Statement stm;//DB�� SQL�� �����ϱ� ���� statement��ü 
	ResultSet rs;
	
	Connection conn = null;	
	
	Vector GameResultVC = new Vector();
	
	static String num;
	static String winner;
	static String startDate;
	static String endDate;
	
	private tempDBinformation TDI = new tempDBinformation();
	
	
	
	public void insertGameResult(Hashtable insertDate){ //���� ����� ����, ����, �ð� ���� DB�� ����
		try{
			
		/*=================JDBC Driver ����==================*/
		Class.forName("com.mysql.jdbc.Driver");//jdbcŬ������ ã�´�.
		String url = "jdbc:mysql://localhost:9999/test";//��Ű�� �̸� 
		String id="root";//�����̸�
		String pw="admin";//���� ��й�ȣ
		conn = DriverManager.getConnection(url,id,pw);
		System.out.println("insert connected");
		/*==================================================*/
			
		
		
		/*=================���� �ۼ� �� ����==================*/
		String Qurey = "INSERT INTO gameresult "
				+"(winner, startdate, enddate, playdate) VALUES ("
				+"'"+insertDate.get("winner")+"'"+", "
				+"'"+insertDate.get("startdate")+"'"+", "
				+"'"+insertDate.get("enddate")+"'"+", "
				+"'"+insertDate.get("palydate")+"'"+");";
		
		stm = conn.createStatement();
		int row = stm.executeUpdate(Qurey);
		/*==================================================*/
		
		
		System.out.println("������ row�� �� : " + row);
		
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
	
	
	public Vector printResult(){ //������� Ŭ���� ��������� ��������� DB���� �ҷ��� ���
		try{
			
			/*=================JDBC Driver ����==================*/
			Class.forName("com.mysql.jdbc.Driver");//jdbcŬ������ ã�´�.
			String url = "jdbc:mysql://localhost:9999/test";
			String id="root";
			String pw="admin";
			conn = DriverManager.getConnection(url,id,pw);
			/*==================================================*/
				
			/*=================���� �ۼ� �� ����==================*/
			String Qurey = "select * from gameresult";
			stm = conn.createStatement();
			rs = stm.executeQuery(Qurey);
			/*=================================================*/
			
			while(rs.next()){//������ ������� �ݺ����� ���� �ϳ��� ���.
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
		return GameResultVC;//�� ���� ����
		
	}
}
