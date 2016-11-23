import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.io.*;
import java.net.*;

public class LoginWindow extends JFrame {

	private JPanel contentPane;
	private JTextField IDField;
	private JPasswordField passwordField;
	
	private static Socket client;
	/*
	 * Launch the application.
	*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginWindow() {
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		IDField = new JTextField();
		IDField.setBounds(131, 76, 129, 24);
		contentPane.add(IDField);
		IDField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(131, 112, 129, 24);
		contentPane.add(passwordField);
		
		
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(53, 79, 62, 18);
		contentPane.add(lblId);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(53, 115, 77, 18);
		contentPane.add(lblPassword);
		
		//Sign in Button
		JButton signInBtn = new JButton("Sign in");
		signInBtn.setFont(new Font("Gulim", Font.PLAIN, 15));
		signInBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client = new Socket("127.0.0.1", 9996);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String ID = IDField.getText();
				String password = passwordField.getText();

				System.out.println("Sign In Button Clicked\nID = " + ID + " PassWord = " + password);
				
				try(DataOutputStream sender = new DataOutputStream(client.getOutputStream());
					BufferedReader receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));){
					
					String message;
					
					//send to server
					message = "LoginCheck";
					sender.writeBytes(message + '\n');
					
					//receive from server
					message = receiver.readLine();
					System.out.println(message);
					
					if(message.endsWith("ID")) //Send ID to server
					{
						message = ID;
						sender.writeBytes(message + '\n');
					}
					
					//서버의 비번 요청
					message = receiver.readLine();
					if(message.endsWith("password"))
					{
						message = password;
						sender.writeBytes(message + "\n"); //서버에게 비번 전송
						System.out.println("Sent PW");
					}
					else
					{
						System.out.println("Login Failed");
						client.close();
						return;
					}
					
					//로그인 성공/실패 메시지 받기
					message = receiver.readLine();
					if(message.endsWith("Success")) //로그인 성공
					{
						System.out.println("Login Success");
					}
					else //로그인 실패
					{
						System.out.println("Login Failed");
					}
					client.close();
					sender.close();
					receiver.close();
				} catch(Throwable e) {
					
				}
			}
		});
		
		signInBtn.setBounds(274, 75, 105, 61);
		contentPane.add(signInBtn);
		
		//Find Button
		JButton FindBtn = new JButton("Forgot ID/PW");
		FindBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Find Button Clicked");
			}
		});

		FindBtn.setFont(new Font("굴림", Font.PLAIN, 15));
		FindBtn.setBounds(131, 148, 129, 27);
		contentPane.add(FindBtn);
		
		//Sign Up Button
		JButton signUpBtn = new JButton("Sign Up");
		signUpBtn.setFont(new Font("Gulim", Font.PLAIN, 15));
		signUpBtn.setBounds(274, 148, 105, 27);
		contentPane.add(signUpBtn);
		signUpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Sign Up Button Clicked");
				SignUpWindow suw = new SignUpWindow();
				suw.run();
			}
		});
	}
}
