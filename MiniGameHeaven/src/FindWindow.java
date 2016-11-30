import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class FindWindow extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField IDField;
	private Icon icon = new ImageIcon("titleIcon.png");
	
	
	public static void run() {
		try {
			FindWindow frame = new FindWindow();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public FindWindow() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 590);
		setResizable(false);
		setIconImage(new ImageIcon("titleIcon.png").getImage());
		setTitle("Mini Game Heaven");
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel IDLabel = new JLabel("ID");
		IDLabel.setBounds(125, 383, 62, 18);
		IDLabel.setForeground(Color.white);
		contentPane.add(IDLabel);
		
		JLabel NameLabel = new JLabel("Name");
		NameLabel.setBounds(125, 353, 62, 18);
		NameLabel.setForeground(Color.white);
		contentPane.add(NameLabel);
		
		nameField = new JTextField();
		nameField.setBounds(208, 350, 116, 24);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		IDField = new JTextField();
		IDField.setBounds(208, 380, 116, 24);
		contentPane.add(IDField);
		IDField.setColumns(10);
				
		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setBounds(151, 100, 152, 151);
		backgroundLabel.setIcon(icon);
		contentPane.add(backgroundLabel);
	

		JButton findButton = new JButton("Find Password");
		findButton.setForeground(Color.white);
		findButton.setBounds(125, 428, 199, 55);
		findButton.setBackground(new Color(37,183,211));
		contentPane.add(findButton);
		
		findButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
				String ID,name;
				name = nameField.getText();
				ID = IDField.getText();
				//서버에게 아이디, 이름 전송
				//서버에서 패스워드를 받아서 뛰어준다
				JOptionPane.showMessageDialog(null, "이거시 너의 비밀 번호다");
			}

		});
	}	
}
