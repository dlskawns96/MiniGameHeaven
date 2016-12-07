import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFileChooser;
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class WaitMain implements ActionListener, Runnable {

	public static JFrame frame;
	private JTextField chatInput;
	private JTextArea chatRoom;
	public static WaitMain waitMain;
	private Socket socket;
	private DataInputStream ois;
	private DataOutputStream oos;
	private JButton send;
	JLabel userList;
	JTextArea userL;
	String filePath;
	Heart dispHeart;
	JTextField textField = new JTextField(40);
	int numOfHeart = 5;
	DataInputStream in;
	DataOutputStream out;
	protected static String ID = null;
	public static String IP = null;
	WaitMain wm = this;
	StringBuffer sf = new StringBuffer();
	String ss = "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;현재 접속자 목록&nbsp;] <br> </html>";
	public static String plusUser = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Thread w = new Thread();
					// w.start();
					WaitMain wm = new WaitMain("dd", "127.0.0.1", "dd");

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

	WaitMain(String id, String ip, String list) {
		sf.append(ss);
		this.ID = id;
		this.IP = ip;
		this.plusUser = list;
		initialize();
		frame.setVisible(true);
		try {
			socket = new Socket(IP, 9003);
			System.out.println("서버에 접속되었습니다.");
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			Thread t = new Thread(this);
			t.start(); // 쓰레드 시작
			out.writeUTF("LIST#" + plusUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setIDIP(String id, String ip, String list) {
		this.ID = id;
		this.IP = ip;
		// this.plusUser = list;
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
		chatInput.setBounds(30, 510, 318, 35);
		chatInput.setBorder(new EmptyBorder(10, 10, 10, 10));
		chatInput.setBorder(tb);
		chatInput.addActionListener(this);
		frame.getContentPane().add(chatInput);
		chatInput.setColumns(10);

		JLabel word = new JLabel(ID);
		word.setFont(new Font("굴림", Font.BOLD, 15));
		word.setForeground(Color.WHITE);
		word.setBounds(240, 20, 190, 35);
		frame.getContentPane().add(word);

		userList = new JLabel(sf.toString());
		userList.setForeground(Color.white);
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
				new SelectGame(wm);
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

		send = new JButton("파일");
		send.setBounds(354, 510, 76, 35);
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
		String msg = chatInput.getText();
		JFileChooser jfc = new JFileChooser();

		if (obj == chatInput) {
			try {
				out.writeUTF(ID + "#" + msg);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			chatInput.setText("");
		}
		if (obj == send) {
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				filePath = "FILE#" + jfc.getSelectedFile().toString();
			
			try {
				if(filePath!="")
				out.writeUTF(filePath+"#"+this.ID);
				filePath = "";
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		frame.setVisible(true);
		String message = null;
		String[] receiveMsg = null;
		boolean isStop = false;
		while (!isStop) {
			try {
				message = in.readUTF();// 채팅내용
				receiveMsg = message.split("#");
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true; // 반복문 종료로 설정
			}
			// System.out.println(receiveMsg[0] + ":" + receiveMsg[1]);
			if (receiveMsg[0].equals("req_fileSend")) {
				chatRoom.append(this.ID+receiveMsg[1]);
				chatRoom.setCaretPosition(chatRoom.getDocument().getLength());
				try {
					out.writeUTF("fileSend# ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (receiveMsg[0].startsWith("fileSender")) { // 파일을 보내기위해
				try {
					//this.chatRoom.append("파일 전송이 완료되었습니다!\n");
					new FileSender(this.filePath).start(); // 쓰레드 실행.
				} catch (Exception e) {
					System.out.println("FileSender 쓰레드 오류:");
					e.printStackTrace();
				}
			}else if (receiveMsg[0].startsWith("fileReceiver")) { // 파일받기
				// fileReceiver|ip|fileName;

				String ip = receiveMsg[1]; // 서버의 아이피를 전달 받음
				String fileName = receiveMsg[2]; // 서버에서 전송할 파일이름.

				try {
					new FileReceiver(ip, fileName).start(); // 쓰레드 실행.
				} catch (Exception e) {
					System.out.println("FileSender 쓰레드 오류:");
					e.printStackTrace();
				}

			} else if (receiveMsg[0].startsWith("req_exit")) { // 종료

			}

			else if (receiveMsg[0].equals("RELIST")) {
				System.out.println("여기서 리리스트 해줌" + receiveMsg[1]);
				plusUser = "";
				receiveMsg[1] = receiveMsg[1].substring(1, receiveMsg[1].length() - 1);
				System.out.println(receiveMsg[1].toString());
				String[] temp = receiveMsg[1].toString().split(", ");
				int i = 0;
				for (String s : temp) {
					System.out.println(temp[i]);
					plusUser += " ＊   " + temp[i] + "<br>";
					i++;
				}
				// plusUser = receiveMsg[1];
				sf.replace(Integer.parseInt("94"), sf.length() - 7, plusUser + "<br>");
				userList.setText(sf.toString());
			} else {
				// 채팅 내용 보여주기
				chatRoom.append(receiveMsg[0] + " : " + receiveMsg[1] + "\n");
				chatRoom.setCaretPosition(chatRoom.getDocument().getLength());
			}
		}
	}
}
