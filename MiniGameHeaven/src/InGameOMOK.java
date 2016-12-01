
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

public class InGameOMOK extends WaitMain {

	private JFrame frame;
	private JTextField chatInput;
	private JTextArea chatRoom;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JButton send;
	BufferedReader in;
	public Heart dispHeart;
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

		ImageIcon bg = new ImageIcon("BG2.png");
		JPanel jp = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(bg.getImage(), 0, 0, null);
				Dimension d = getSize();
				g.drawImage(bg.getImage(), 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		frame.setContentPane(jp);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon("titleIcon.png").getImage());
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

		frame.getContentPane().setBackground(Color.WHITE);

		JPanel cloverNum = new JPanel();

		cloverNum.setBounds(592, 5, 190, 35);
		// frame.getContentPane().add(waitMain.dispHeart);
		this.numOfHeart = waitMain.numOfHeart;
		dispHeart = new Heart(this.numOfHeart);
		int i = 0;
		for (JLabel j : dispHeart.heart) {
			dispHeart.heart[i].setBounds(510 + 50 * i, 12, 35, 35);
			frame.getContentPane().add(dispHeart.heart[i]);
			i++;
		}
		JPanel userList = new JPanel();
		userList.setOpaque(true);
		userList.setBackground(new Color(0, 0, 0, 90));
		userList.setBounds(478, 55, 304, 156);
		frame.getContentPane().add(userList);

		ImageIcon btnImg_1 = new ImageIcon();
		chatRoom = new JTextArea();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(478, 221, 304, 227);
		frame.getContentPane().add(scrollPane);
		chatRoom = new JTextArea();
		scrollPane.setViewportView(chatRoom);
		chatRoom.setEditable(false);
		send = new JButton("\uC804 \uC1A1");
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

		JPanel gameDisplay = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(ic.getImage(), 0, 0, d.width, d.height, null);

				setOpaque(false);
				super.paintComponent(g);
			}
		};

		gameDisplay.setBounds(26, 55, 440, 442);

		frame.getContentPane().add(gameDisplay);

		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void run() {
		String message = null;
		String[] receiveMsg = null;
		boolean isStop = false;
		while (!isStop) {
			try {
				message = (String) ois.readObject();// 채팅내용
				receiveMsg = message.split("$");
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true; // 반복문 종료로 설정
			}
			System.out.println(receiveMsg[0] + ":" + receiveMsg[1]);
			if (receiveMsg[1].equals("exit")) {
				if (receiveMsg[0].equals(ID)) {
					System.exit(0);
				} else {
					chatRoom.append(receiveMsg[0] + " 님이 종료했습니다\n");
					chatRoom.setCaretPosition(chatRoom.getDocument().getLength());
				}
			} else {
				// 채팅 내용 보여주기
				chatRoom.append(receiveMsg[0] + " : " + receiveMsg[1] + "\n");
				chatRoom.setCaretPosition(chatRoom.getDocument().getLength());
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String msg = chatInput.getText();
		if (obj == chatInput || obj == send) {
			try {
				oos.writeObject(waitMain.ID + "$" + msg);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			chatInput.setText("");
		}

		/*
		 * else if (obj == jbtn) { // 종료 버튼을 클릭한 경우 try { oos.writeObject(ID +
		 * "#exit"); } catch (Exception ee) { ee.printStackTrace(); } // catch
		 * System.exit(0); }
		 */ // else if : 종료 버튼
	}// actionPerformed
}
