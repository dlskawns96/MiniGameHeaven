import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

public class SignUpWindow extends JFrame {

	private JPanel contentPane;
	private JTextField IDField;
	private JTextField PWField;
	private JTextField PWAgainField;
	private JTextField emailField;

	/**
	 * Launch the application.
	 */
	
	public void run() {
		try {
			SignUpWindow frame = new SignUpWindow();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public SignUpWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 403);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblId = new JLabel("ID (Will be used as your nickname)");
		lblId.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblId.setBounds(72, 46, 310, 18);
		contentPane.add(lblId);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblPassword.setBounds(72, 103, 310, 18);
		contentPane.add(lblPassword);
		
		JLabel lblRep = new JLabel("Password Again");
		lblRep.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblRep.setBounds(72, 159, 310, 18);
		contentPane.add(lblRep);
		
		JLabel lblNewLabel = new JLabel("Email Adress");
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblNewLabel.setBounds(72, 219, 310, 18);
		contentPane.add(lblNewLabel);
		
		IDField = new JTextField();
		IDField.setBounds(72, 67, 209, 24);
		contentPane.add(IDField);
		IDField.setColumns(10);
		
		PWField = new JTextField();
		PWField.setBounds(72, 123, 209, 24);
		contentPane.add(PWField);
		PWField.setColumns(10);
		
		PWAgainField = new JTextField();
		PWAgainField.setBounds(72, 189, 209, 24);
		contentPane.add(PWAgainField);
		PWAgainField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(72, 249, 209, 24);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		JButton IDCheckBtn = new JButton("Check");
		IDCheckBtn.setBounds(293, 66, 73, 27);
		contentPane.add(IDCheckBtn);
		
		JButton signUpBtn = new JButton("Sign Up");
		signUpBtn.setBounds(164, 304, 105, 27);
		contentPane.add(signUpBtn);
	}

}
