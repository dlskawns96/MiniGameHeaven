import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
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
	private static String ID;
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
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setEnabled(false);
		getContentPane().setForeground(Color.WHITE);
		setBounds(100, 100, 450, 590);
		setResizable(false);
		setIconImage(new ImageIcon("titleIcon.png").getImage());
		setTitle("Mini Game Heaven");
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 213, 226));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		IDField = new JTextField();
		IDField.setBounds(138, 402, 129, 24);
		contentPane.add(IDField);
		IDField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(138, 438, 129, 24);
		contentPane.add(passwordField);

		JLabel lblId = new JLabel("ID");
		lblId.setFont(new Font("돋움", Font.BOLD, 14));
		lblId.setBounds(60, 405, 62, 18);
		lblId.setForeground(Color.white);
		contentPane.add(lblId);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("돋움", Font.BOLD, 12));
		lblPassword.setForeground(Color.white);
		lblPassword.setBounds(60, 441, 77, 18);
		contentPane.add(lblPassword);

		// Sign in Button
		JButton signInBtn = new JButton("Sign in");
		signInBtn.setBackground(new Color(37, 183, 211));
		signInBtn.setFont(new Font("Gulim", Font.BOLD, 15));
		signInBtn.setForeground(Color.white);
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

				try (DataOutputStream sender = new DataOutputStream(client.getOutputStream());
						BufferedReader receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

					String message;

					// send to server
					message = "LoginCheck";
					sender.writeBytes(message + '\n');

					// receive from server
					message = receiver.readLine();
					System.out.println(message);

					if (message.endsWith("ID")) // Send ID to server
					{
						message = ID;
						sender.writeBytes(message + '\n');
					}

					// 서버의 비번 요청
					message = receiver.readLine();
					if (message.endsWith("password")) {
						message = password;
						sender.writeBytes(message + "\n"); // 서버에게 비번 전송
						System.out.println("Sent PW");
					} else if (message.endsWith("wrong")) {
						JOptionPane.showMessageDialog(null, "패스워드가 틀렸습니다.");
						System.out.println("Login Failed1");
						client.close();
						return;
					} else if (message.contains("already")) {
						JOptionPane.showMessageDialog(null, "이미 접속중인 사용자 입니다.");
						System.out.println("Login Failed2");
						client.close();
						return;
					} else {
						JOptionPane.showMessageDialog(null, "존재하지 않는 아이디 입니다.");
						System.out.println("Login Failed3");
						client.close();
						return;
					}

					// 로그인 성공/실패 메시지 받기
					message = receiver.readLine();
					System.out.println(message + "$$$$$$$$$$$$$$$$$");
					// 로그인 성공
					if (message.contains("Success")) {
						String[] temp = message.split("#");
						new WaitMain(ID, WaitMain.IP,temp[1].toString());
						JOptionPane.showMessageDialog(null, ID + " 님 환영합니다!");
						System.out.println("Login Success");
						setVisible(false);
						dispose();
					} // 로그인 실패

					else {
						IDField.setText("");
						passwordField.setText("");
						JOptionPane.showMessageDialog(null, "Failed to Login!!");
						System.out.println("Login Failed");
					}
					client.close();
					sender.close();
					receiver.close();
				} catch (Throwable e) {

				}
			}
		});

		signInBtn.setBounds(281, 401, 105, 61);
		contentPane.add(signInBtn);

		// Find Button
		JButton FindBtn = new JButton("Forgot ID/PW");
		FindBtn.setBackground(new Color(37, 183, 211));
		FindBtn.setForeground(Color.white);
		FindBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Find Button Clicked");
				FindWindow.run();

			}
		});

		FindBtn.setFont(new Font("굴림", Font.BOLD, 12));
		FindBtn.setBounds(138, 474, 129, 27);
		contentPane.add(FindBtn);

		// Sign Up Button
		JButton signUpBtn = new JButton("Sign Up");
		signUpBtn.setBackground(new Color(37, 183, 211));
		signUpBtn.setFont(new Font("Gulim", Font.BOLD, 12));
		signUpBtn.setBounds(281, 474, 105, 27);
		signUpBtn.setForeground(Color.white);
		contentPane.add(signUpBtn);

		JLabel LoginWindowIcon = new JLabel("");
		ImageIcon icon = new ImageIcon("titleIcon.png");
		LoginWindowIcon.setIcon(icon);
		LoginWindowIcon.setBounds(157, 161, 175, 129);
		contentPane.add(LoginWindowIcon);
		signUpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Sign Up Button Clicked");
				SignUpWindow suw = new SignUpWindow();
				suw.run();
			}
		});
	}
}
