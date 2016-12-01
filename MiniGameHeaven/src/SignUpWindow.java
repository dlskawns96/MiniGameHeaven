import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class SignUpWindow extends JFrame {

	private JPanel contentPane;
	private JTextField IDField;
	private JTextField PWField;
	private JTextField PWAgainField;

	private static Socket client;
	private static boolean IDChecked = false;
	private JTextField nameField;

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
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 403);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setIconImage(new ImageIcon("titleIcon.png").getImage());
		JLabel lblId = new JLabel("ID (Will be used as your nickname)");
		lblId.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblId.setBounds(72, 119, 310, 18);
		contentPane.add(lblId);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblPassword.setBounds(72, 176, 310, 18);
		contentPane.add(lblPassword);

		JLabel lblRep = new JLabel("Password Again");
		lblRep.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblRep.setBounds(72, 232, 310, 18);
		contentPane.add(lblRep);

		IDField = new JTextField();
		IDField.setBounds(72, 140, 209, 24);
		contentPane.add(IDField);
		IDField.setColumns(10);

		PWField = new JPasswordField();
		PWField.setBounds(72, 196, 209, 24);
		contentPane.add(PWField);
		PWField.setColumns(10);

		PWAgainField = new JPasswordField();
		PWAgainField.setBounds(72, 262, 209, 24);
		contentPane.add(PWAgainField);
		PWAgainField.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblName.setBounds(72, 59, 62, 18);
		contentPane.add(lblName);

		nameField = new JTextField();
		nameField.setBounds(72, 83, 116, 24);
		contentPane.add(nameField);
		nameField.setColumns(10);

		JButton IDCheckBtn = new JButton("Check");
		IDCheckBtn.setForeground(Color.WHITE);
		IDCheckBtn.setBounds(293, 139, 73, 27);
		IDCheckBtn.setBackground(new Color(37,183,211));
		contentPane.add(IDCheckBtn);
		IDCheckBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("ID check button clicked");

				try {
					client = new Socket("127.0.0.1", 9996);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try (DataOutputStream sender = new DataOutputStream(client.getOutputStream());
						BufferedReader receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
					sender.writeBytes("IDCheck\n");
					if (receiver.readLine().endsWith("ID")) {
						sender.writeBytes(IDField.getText() + "\n");
					} else {
						client.close();
						return;
					}
					if (receiver.readLine().startsWith("success")) {
						IDField.setEditable(false);
						IDChecked = true;
					} else {
						client.close();
						return;
					}
					client.close();
					sender.close();
					receiver.close();
				} catch (Throwable e) {

				}
			}
		});

		JButton signUpBtn = new JButton("Sign Up");
		signUpBtn.setForeground(Color.WHITE);
		signUpBtn.setBackground(new Color(37,183,211));
		signUpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Sign up Button Clicked");
				String ID = IDField.getText();
				String PW = PWField.getText();
				String PWre = PWAgainField.getText();

				if (!IDChecked) {
					JOptionPane.showMessageDialog(null, "Check Your ID First!!");
					return;
				}

				if (!(PW.equals(PWre))) {
					JOptionPane.showMessageDialog(null, "Your Password is different");
					PWField.setText("");
					PWAgainField.setText("");
					return;
				}

				String name = nameField.getText();
				try {
					client = new Socket("127.0.0.1", 9996);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try (DataOutputStream sender = new DataOutputStream(client.getOutputStream());
						BufferedReader receiver = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

					sender.writeBytes("Sign Up\n");

					if (receiver.readLine().equals("OK")) {
						sender.writeBytes(ID + "\n");
						sender.writeBytes(PW + "\n");
						sender.writeBytes(name + "\n");
						if (receiver.readLine().equals("OK")) {
							JOptionPane.showMessageDialog(null, "Success to sign up!!");
							setVisible(false);
							dispose();
						}
					} else
						return;

				} catch (Throwable e) {

				}

			}
		});

		signUpBtn.setBounds(165, 317, 105, 27);
		contentPane.add(signUpBtn);

	}
}
