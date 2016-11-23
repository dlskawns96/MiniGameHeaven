import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

public class InGameOMOK extends WaitMain{

	private JFrame frame;
	private JTextField chatInput;
	private JTextArea chatRoom;
	BufferedReader in;
	PrintWriter out;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InGameOMOK window = new InGameOMOK();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InGameOMOK() {
		initialize();
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(25, 25, 112));
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon("rsrc/titleIcon.png").getImage());
		chatInput = new JTextField();
		chatInput.setBounds(478, 458, 216, 39);
		frame.getContentPane().add(chatInput);
		chatInput.setColumns(10);
		chatInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// out.println(chatInput.getText());

				chatRoom.append(chatInput.getText() + '\n');
				chatInput.setText("");
			}
		});
		JPanel cloverNum = new JPanel();
		cloverNum.setBounds(592, 10, 190, 35);
		frame.getContentPane().add(cloverNum);

		JPanel userList = new JPanel();
		userList.setBackground(Color.LIGHT_GRAY);
		userList.setBounds(478, 55, 304, 156);
		frame.getContentPane().add(userList);

		ImageIcon btnImg_1 = new ImageIcon("rsrc/button_1.png");
		chatRoom = new JTextArea();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(478, 221, 304, 227);
		frame.getContentPane().add(scrollPane);
		chatRoom = new JTextArea();
		scrollPane.setViewportView(chatRoom);
		chatRoom.setEditable(false);
		JButton send = new JButton("\uC804 \uC1A1");
		send.setBounds(706, 458, 76, 39);
		frame.getContentPane().add(send);
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// out.println(chatInput.getText());

				chatRoom.append(chatInput.getText() + '\n');
				chatInput.setText("");
			}
		});
		
		JButton back = new JButton("\uB300\uAE30\uC2E4 \uAC00\uAE30");
		back.setBounds(26, 507, 114, 39);
		frame.getContentPane().add(back);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				waitMain.frame.setVisible(true);
			}
		});
		
		ImageIcon ic = new ImageIcon("badook_board.jpg");
		
		JPanel gameDisplay = new JPanel(){
			public void paintComponent(Graphics g){
				Dimension d = getSize();
				g.drawImage(ic.getImage(), 0, 0,d.width,d.height, null);
				
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		gameDisplay.setBounds(26, 55, 440, 442);
		
		frame.getContentPane().add(gameDisplay);

		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
