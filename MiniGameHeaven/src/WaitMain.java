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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class WaitMain implements ActionListener, Runnable {

	public JFrame frame;
	private JTextField chatInput;
	private JTextArea chatRoom;
	public static WaitMain waitMain;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JButton send;
	Heart dispHeart;
	JTextField textField = new JTextField(40);
	int numOfHeart = 5;
	BufferedReader in;
	PrintWriter out;
	private static String ID = "임의의 사용자";
	private static String IP = "127.0.0.1";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					waitMain = new WaitMain(IP, ID);
					// waitMain.chatRun();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	WaitMain() {

	}

	WaitMain(String Ip, String Id) {
		super();
		initialize();
		frame.setVisible(true);
		IP = Ip;
		ID = Id;
		try {
			socket = new Socket(IP, 5000);
			System.out.println("서버에 접속되었습니다.");
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			Thread t = new Thread(this);
			t.start(); // 쓰레드 시작
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		ImageIcon bg = new ImageIcon("BG.jpg");
		JPanel jp = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(bg.getImage(), 0, 0, null);
				Dimension d = getSize();
				g.drawImage(bg.getImage(), 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		TitledBorder tb = new TitledBorder(new LineBorder(Color.PINK, 2));
		frame.setContentPane(jp);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon("titleIcon.png").getImage());

		chatRoom = new JTextArea();
		// chatRoom.setLineWrap(true);
		chatRoom.setEditable(false);
		// chatRoom.setOpaque(true);
		chatRoom.setBackground(new Color(255, 255, 255));

		chatInput = new JTextField();
		chatInput.setBounds(30, 510, 318, 35);
		chatInput.setBorder(new EmptyBorder(10, 10, 10, 10));
		chatInput.setBorder(tb);
		chatInput.addActionListener(this);
		frame.getContentPane().add(chatInput);
		chatInput.setColumns(10);
		// frame.setOpacity(0.5f);
		/*
		 * chatInput.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { //
		 * out.println(chatInput.getText()); chatRoom.append(chatInput.getText()
		 * + '\n'); chatInput.setText(""); } });
		 */

		JPanel word = new JPanel();
		word.setBounds(240, 20, 190, 35);
		frame.getContentPane().add(word);

		JPanel userList = new JPanel();
		JScrollPane userScroll = new JScrollPane();
		userScroll.setOpaque(true);
		userScroll.setBackground(new Color(0, 0, 0, 0));
		userScroll.setBounds(442, 74, 340, 200);
		userScroll.setBorder(tb);

		JLabel userL = new JLabel("현재 접속자 : " + ID);
		userList.setOpaque(true);
		userList.add(userL);
		userList.setBackground(new Color(0, 0, 0, 90));
		userList.setBounds(442, 74, 340, 200);
		userScroll.setViewportView(userList);
		frame.getContentPane().add(userScroll);

		JPanel gameRoomList = new JPanel();
		gameRoomList.setOpaque(true);
		gameRoomList.setBorder(tb);
		gameRoomList.setBackground(new Color(0, 0, 0, 90));
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

		JScrollPane scrollPane = new JScrollPane(chatRoom);
		scrollPane.setBounds(30, 74, 400, 415);
		scrollPane.setOpaque(true);
		scrollPane.setBackground(new Color(0, 0, 0, 0));
		scrollPane.setBorder(tb);
		frame.getContentPane().add(scrollPane);
		dispHeart = new Heart(this.numOfHeart);

		int i = 0;
		for (JLabel j : dispHeart.heart) {
			this.dispHeart.heart[i].setBounds(500 + 50 * i, 20, 35, 35);
			frame.getContentPane().add(this.dispHeart.heart[i]);
			i++;
		}

		send = new JButton("\uC804 \uC1A1");
		send.setBounds(354, 510, 76, 35);
		send.addActionListener(this);
		frame.getContentPane().add(send);
		/*
		 * send.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) {
		 * chatRoom.append(chatInput.getText() + '\n'); chatInput.setText(""); }
		 * });
		 */

		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void run() {
		String message = null;
		String[] receiveMsg = null;
		boolean isStop = false;
		while (!isStop) {
			try {
				message = (String) ois.readObject();// 채팅내용
				receiveMsg = message.split("#");// 홍길동#안녕
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true; // 반복문 종료로 설정
			} // catch
			System.out.println(receiveMsg[0] + ":" + receiveMsg[1]);
			if (receiveMsg[1].equals("exit")) { // 채팅 종료
				if (receiveMsg[0].equals(ID)) { // 해당 사용자
					System.exit(0);
				} else { // 그 외의 사용자
					chatRoom.append(receiveMsg[0] + " 님이 종료했습니다\n");
					// 커서를 현재 채팅 내용의 자리에 보여준다.
					chatRoom.setCaretPosition(chatRoom.getDocument().getLength());
				} // else : 그 외 사용자
			} else { // exit 가 아닐 경우
				// 채팅 내용 보여주기
				chatRoom.append(receiveMsg[0] + " : " + receiveMsg[1] + "\n");
				// 커서를 현재 채팅 내용의 자리에 보여준다.
				chatRoom.setCaretPosition(chatRoom.getDocument().getLength());
			} // else
		} // while

	}

	@Override
	public void actionPerformed(ActionEvent e) { // 이벤트 처리
		Object obj = e.getSource(); // 에벤트 발생 위치 얻기
		String msg = chatInput.getText(); // 채팅 내용 입력 받기
		if (obj == chatInput || obj == send) { // 입력란에서 엔터를 친 경우
			try {
				oos.writeObject(ID + "#" + msg);
			} catch (Exception ee) {
				ee.printStackTrace();
			} // catch
			chatInput.setText(""); // jtf 를 지운다.
		} // else : 내용 O

		/*
		 * else if (obj == jbtn) { // 종료 버튼을 클릭한 경우 try { oos.writeObject(ID +
		 * "#exit"); } catch (Exception ee) { ee.printStackTrace(); } // catch
		 * System.exit(0); }
		 */ // else if : 종료 버튼
	}// actionPerformed
}
