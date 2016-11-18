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
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class LoginWindow extends JFrame {

	private JPanel contentPane;
	private JTextField IDField;
	private JPasswordField passwordField;

	/**
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		
		JButton signInBtn = new JButton("Sign in");
		signInBtn.setFont(new Font("Gulim", Font.PLAIN, 15));
		signInBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Sign In Button Clicked");
			}
		});
		signInBtn.setBounds(274, 75, 105, 61);
		contentPane.add(signInBtn);
		
		JButton FindBtn = new JButton("Forgot ID/PW");
		FindBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Find Button Clicked");
			}
		});

		FindBtn.setFont(new Font("±¼¸²", Font.PLAIN, 15));
		FindBtn.setBounds(131, 148, 129, 27);
		contentPane.add(FindBtn);
		
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
