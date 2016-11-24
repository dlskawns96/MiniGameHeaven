import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.AlphaComposite;
import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

public class WaitMain {

	public JFrame frame;
	private JTextField chatInput;
	private JTextArea chatRoom;
	public static WaitMain waitMain;
	Heart dispHeart;
	JTextField textField = new JTextField(40);
	int numOfHeart = 5;
	BufferedReader in;
	PrintWriter out;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					waitMain = new WaitMain();
					waitMain.frame.setVisible(true);
					// waitMain.chatRun();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * private void chatRun() throws IOException {
	 * 
	 * // Make connection and initialize streams String serverAddress =
	 * getServerAddress(); Socket socket = new Socket(serverAddress, 8888); in =
	 * new BufferedReader(new InputStreamReader(socket.getInputStream())); out =
	 * new PrintWriter(socket.getOutputStream(), true);
	 * 
	 * // Process all messages from server, according to the protocol. { String
	 * line = in.readLine(); if (line.startsWith("SUBMITNAME")) {
	 * out.println(getName()); } else if (line.startsWith("NAMEACCEPTED")) {
	 * chatInput.setEditable(true); } else if (line.startsWith("MESSAGE")) {
	 * chatRoom.append(line.substring(8) + "\n"); } } }
	 * 
	 * private String getServerAddress() { return
	 * JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:",
	 * "Welcome to the Chatter", JOptionPane.QUESTION_MESSAGE); }
	 * 
	 * private String getName() { return JOptionPane.showInputDialog(frame,
	 * "Choose a screen name:", "Screen name selection",
	 * JOptionPane.PLAIN_MESSAGE); }
	 */
	/**
	 * Create the application.
	 */
	public WaitMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		ImageIcon bg = new ImageIcon("BG.jpg");
		JPanel jp = new JPanel(){
			public void paintComponent(Graphics g){
				g.drawImage(bg.getImage(),0,0,null);
				Dimension d = getSize();
				g.drawImage(bg.getImage(), 0, 0, d.width,d.height,null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		frame.setContentPane(jp);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon("titleIcon.png").getImage());

		chatInput = new JTextField();
		chatInput.setBounds(30, 510, 318, 35);
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

		JPanel word = new JPanel();
		word.setBounds(240, 20, 190, 35);
		frame.getContentPane().add(word);

		JPanel userList = new JPanel();
		userList.setBackground(Color.LIGHT_GRAY);
		userList.setBounds(442, 74, 340, 200);
		frame.getContentPane().add(userList);

		JPanel gameRoomList = new JPanel();
		gameRoomList.setBackground(Color.LIGHT_GRAY);
		gameRoomList.setBounds(442, 284, 340, 205);
		frame.getContentPane().add(gameRoomList);

		JButton joinGame = new JButton();
		joinGame.setText("\uAC8C\uC784 \uCC38\uC5EC\uD558\uAE30");
		joinGame.setFocusable(false);
		joinGame.setBounds(617, 510, 165, 35);
		frame.getContentPane().add(joinGame);

		JButton makeGame = new JButton();
		makeGame.setText("\uAC8C\uC784 \uB9CC\uB4E4\uAE30");
		makeGame.setBounds(445, 510, 165, 35);
		makeGame.setFocusable(false);
		frame.getContentPane().add(makeGame);

		makeGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SelectGame sg = new SelectGame();
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 74, 400, 415);
		frame.getContentPane().add(scrollPane);
		chatRoom = new JTextArea();
		scrollPane.setViewportView(chatRoom);
		chatRoom.setEditable(false);

		dispHeart = new Heart(this.numOfHeart);
		int i = 0;
		for (JLabel j : dispHeart.heart) {
			this.dispHeart.heart[i].setBounds(500 + 50*i, 20, 35, 35);
			frame.getContentPane().add(this.dispHeart.heart[i]);
			i++;
		}

		JButton send = new JButton("\uC804 \uC1A1");
		send.setBounds(354, 510, 76, 35);
		frame.getContentPane().add(send);

		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chatRoom.append(chatInput.getText() + '\n');
				chatInput.setText("");
			}
		});

		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
