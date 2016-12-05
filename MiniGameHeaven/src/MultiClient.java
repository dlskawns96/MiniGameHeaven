import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

public class MultiClient extends JFrame implements ActionListener {

	private boolean isTrans = false;
	static boolean chatmode = false;
	static int chatState = 0;
	public static JFrame frame;
	public static JTextField chatInput;
	private JTextArea chatRoom;
	public static WaitMain waitMain;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JButton send;
	JLabel userList;
	JTextArea userL;
	Heart dispHeart;
	JTextField textField = new JTextField(40);
	int numOfHeart = 5;
	BufferedReader in;
	PrintWriter out;
	protected static String ID = null;
	public static String IP = null;
	StringBuffer sf = new StringBuffer();
	String ss = "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;현재 접속자 목록&nbsp;] <br> </html>";
	public static String plusUser = null;
	MultiClient mc = this;
	private JPanel contentPane;

	public MultiClient() {
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

		TitledBorder tb = new TitledBorder(new LineBorder(new Color(37, 183, 211), 3));
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
		chatInput.setBounds(30, 510, 299, 35);
		chatInput.setBorder(new EmptyBorder(10, 10, 10, 10));
		chatInput.setBorder(tb);
		chatInput.addActionListener(this);
		frame.getContentPane().add(chatInput);
		chatInput.setColumns(10);

		JPanel word = new JPanel();
		word.setBounds(240, 20, 190, 35);
		frame.getContentPane().add(word);

		userList = new JLabel(sf.toString());
		userList.setForeground(new Color(25, 25, 112));
		userList.setFont(new Font("굴림", Font.BOLD, 15));
		userList.setPreferredSize(new java.awt.Dimension(190, 35));
		userList.setVerticalAlignment(SwingConstants.TOP);
		JScrollPane userScroll = new JScrollPane();
		userScroll.setOpaque(true);
		userScroll.setBackground(new Color(0, 0, 0, 0));
		userScroll.setBounds(442, 74, 340, 200);
		userScroll.setBorder(tb);

		userList.setOpaque(true);
		// userList.add(userList);
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
		joinGame.setForeground(Color.white);
		joinGame.setBackground(new Color(37, 183, 211));
		frame.getContentPane().add(joinGame);

		JButton makeGame = new JButton();
		makeGame.setText("\uAC8C\uC784 \uB9CC\uB4E4\uAE30");
		makeGame.setBounds(445, 510, 165, 35);
		makeGame.setFocusable(false);
		makeGame.setForeground(Color.white);
		makeGame.setBackground(new Color(37, 183, 211));
		frame.getContentPane().add(makeGame);

		makeGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// new SelectGame(mc);
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

		send = new JButton("\uD30C\uC77C\uC804\uC1A1");
		send.setBounds(341, 510, 89, 35);
		send.addActionListener(this);
		send.setForeground(Color.white);
		send.setBackground(new Color(37, 183, 211));
		frame.getContentPane().add(send);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		JLabel jlb = new JLabel(" ");
		JFileChooser jfc = new JFileChooser();
		if (obj == send) {
			if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
				jlb.setText("열기 경로 : " + jfc.getSelectedFile().toString());
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		try {
			MultiClient frame1 = new MultiClient();
			frame.setVisible(true);

			String ServerIP = "localhost";
			Socket socket = new Socket(ServerIP, 9995); // 소켓 객체 생성
			System.out.println("서버와 연결이 되었습니다......");
			// 사용자로부터 얻은 문자열을 서버로 전송해주는 역할을 하는 쓰레드.

			Thread sender = new Sender(socket);
			Thread receiver = new Receiver(socket);

			sender.start(); // 스레드 시동
			receiver.start(); // 스레드 시동

		} catch (Exception e) {
			System.out.println("예외[MultiClient class]:" + e);
		}
	}

	/////////////////////////////////////////////////////////////////////

	// 서버로부터 메시지를 읽는 클래스
	public static class Receiver extends Thread {

		Socket socket;
		DataInputStream in;

		// Socket을 매개변수로 받는 생성자.
		public Receiver(Socket socket) {
			this.socket = socket;

			try {
				in = new DataInputStream(this.socket.getInputStream());
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}

		/** 메시지 파서 */
		public String[] getMsgParse(String msg) {
			String[] tmpArr = msg.split("[|]");
			return tmpArr;
		}

		@Override
		public void run() {

			while (in != null) {
				try {

					String msg = in.readUTF(); // 입력스트림을 통해 읽어온 문자열을 msg에 할당.
					String[] msgArr = getMsgParse(msg.substring(msg.indexOf("|") + 1));

					// 메세지 처리 ----------------------------------------------

					MultiClient.chatState = 1; // 채팅 상태를 채팅방입장 완료로 대화가능상태
					if (msg.startsWith("enterRoom#yes")) { // 그룹입장
						// enterRoom#yes|게임방
						System.out.println("[##] 채팅방 (" + msgArr[0] + ") 에 입장하였습니다.");
						MultiClient.chatState = 2; // 챗 상태 변경 ( 채팅방입장 완료로
													// 대화가능상태)

					} else if (msg.startsWith("show")) { // 서버에서전달하고자하는 메시지

						// show|메시지내용
						System.out.println(msgArr[0]);

					} else if (msg.startsWith("say")) { // 대화내용
						// say|아이디|대화내용
						System.out.println("[" + msgArr[0] + "] " + msgArr[1]);

					} else if (msg.startsWith("whisper")) { // 귓속말
						// whisper|아이디|대화내용
						System.out.println("[귓][" + msgArr[0] + "] " + msgArr[1]);

					} else if (msg.startsWith("req_PvPchat")) { // 해당 사용자에게
																// 1:1대화 요청
						// req_PvPchat|출력내용
						MultiClient.chatState = 3; // 챗 상태 변경 (상대방이 1:1대화신청을
													// 했을경우
						System.out.println(msgArr[0]); // 메세지만 추출
						System.out.print("▶선택:");

					} else if (msg.startsWith("req_fileSend")) { // 상대방이 현재
																	// 사용자에게
																	// 파일전송 수락
																	// 요청
						// req_fileSend|출력내용
						// req_fileSend|[##] name 님께서 파일 전송을 시도합니다.
						// 수락하시겠습니까?(Y/N)
						MultiClient.chatState = 5; // 상태 변경 (상대방이 현재사용자에게 파일전송을
													// 수락요청한 상태)
						System.out.println(msgArr[0]); // 메세지만 추출
						System.out.print("▶선택:");
						sleep(100);

					} else if (msg.startsWith("fileSender")) { // 파일을 보내기위해 파일서버
																// 준비
						// fileSender|filepath;
						System.out.println("fileSender:" + InetAddress.getLocalHost().getHostAddress());
						System.out.println("fileSender:" + msgArr[0]);
						// String
						// ip=InetAddress.getLocalHost().getHostAddress();

						try {
							new FileSender(msgArr[0]).start(); // 쓰레드 실행.
						} catch (Exception e) {
							System.out.println("FileSender 쓰레드 오류:");
							e.printStackTrace();
						}

					} else if (msg.startsWith("fileReceiver")) { // 파일받기
						// fileReceiver|ip|fileName;

						System.out.println("fileReceiver:" + InetAddress.getLocalHost().getHostAddress());
						System.out.println("fileReceiver:" + msgArr[0] + "/" + msgArr[1]);

						String ip = msgArr[0]; // 서버의 아이피를 전달 받음
						String fileName = msgArr[1]; // 서버에서 전송할 파일이름.

						try {
							new FileReceiver(ip, fileName).start(); // 쓰레드 실행.
						} catch (Exception e) {
							System.out.println("FileSender 쓰레드 오류:");
							e.printStackTrace();
						}

					} else if (msg.startsWith("req_exit")) { // 종료

					}

				} catch (SocketException e) {
					System.out.println("예외:" + e);
					System.out.println("##접속중인 서버와 연결이 끊어졌습니다.");
					return;

				} catch (Exception e) {
					System.out.println("Receiver:run() 예외:" + e);

				}
			} // while----
		}// run()------
	}// class Receiver -------

	/////////////////////////////////////////////////////////////////////

	// 서버로 메시지를 전송하는 클래스
	public static class Sender extends Thread {
		Socket socket;
		DataOutputStream out;
		String name = "zz";
		String msg;

		// 생성자 ( 매개변수로 소켓과 사용자 이름 받습니다. )
		public Sender(Socket socket) { // 소켓과 사용자 이름을 받는다.
			this.socket = socket;
			try {
				out = new DataOutputStream(this.socket.getOutputStream());
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}

		@Override
		public void run() {
			while (out != null) {
				try {
					if (MultiClient.chatState == 1) {
						out.writeUTF("req_logon|" + name);
						MultiClient.chatState = 2;
					}
					msg = chatInput.getText();
					if (msg == null || msg.trim().equals("")) {
						msg = " ";
						// continue; //콘솔에선 공백으로 넘기는것이 좀더 효과적임.
					} else if (MultiClient.chatState == 2) {
						// req_say|아이디|대화내용
						out.writeUTF("req_say|" + name + "|" + msg);
					} else if (msg.trim().startsWith("/")) {
						if (msg.equalsIgnoreCase("/exit")) {
							System.out.println("[##] 클라이언트를 종료합니다.");
							System.exit(0);
							break;
						} else {
							out.writeUTF("req_cmdMsg|" + name + "|" + msg);
							// req_cmdMsg|대화명|/접속자
						}
					} else if (MultiClient.chatState == 3) { // 3 : 상대방이
																// 1:1대화요청한
																// 상태 ,
						// PvPchat|result)
						msg = msg.trim(); // 메시지 공백제거
						if (msg.equalsIgnoreCase("y")) {
							out.writeUTF("PvPchat|yes");
						} else if (msg.equalsIgnoreCase("n")) {
							out.writeUTF("PvPchat|no");
						} else {
							System.out.println("입력한 값이 올바르지 않습니다.");
							out.writeUTF("PvPchat|no");
						}
						MultiClient.chatState = 2; // 1:1대화 요청에 응답완료 상태

					} else if (MultiClient.chatState == 5) { // 5 : 상대방이 파일전송을
																// 시도하여
																// 사용자의 수락요청을
																// 기다림.
						// fileSend|result)
						if (msg.trim().equalsIgnoreCase("y")) {
							out.writeUTF("fileSend|yes");
						} else if (msg.trim().equalsIgnoreCase("n")) {
							out.writeUTF("fileSend|no");
						} else {
							System.out.println("입력한 값이 올바르지 않습니다.");
							out.writeUTF("fileSend|no");
						}

						MultiClient.chatState = 2; // 파일전송수락요청에대한 응답완료 상태

					}

				} catch (SocketException e) {
					System.out.println("Sender:run()예외:" + e);
					System.out.println("##접속중인 서버와 연결이 끊어졌습니다.");
					return;
				} catch (IOException e) {
					System.out.println("예외:" + e);
				}
			} // while------

		}// run()------
	}// class Sender-------

}
