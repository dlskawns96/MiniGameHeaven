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
	String ss = "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;���� ������ ���&nbsp;] <br> </html>";
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
		userList.setFont(new Font("����", Font.BOLD, 15));
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
				jlb.setText("���� ��� : " + jfc.getSelectedFile().toString());
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		try {
			MultiClient frame1 = new MultiClient();
			frame.setVisible(true);

			String ServerIP = "localhost";
			Socket socket = new Socket(ServerIP, 9995); // ���� ��ü ����
			System.out.println("������ ������ �Ǿ����ϴ�......");
			// ����ڷκ��� ���� ���ڿ��� ������ �������ִ� ������ �ϴ� ������.

			Thread sender = new Sender(socket);
			Thread receiver = new Receiver(socket);

			sender.start(); // ������ �õ�
			receiver.start(); // ������ �õ�

		} catch (Exception e) {
			System.out.println("����[MultiClient class]:" + e);
		}
	}

	/////////////////////////////////////////////////////////////////////

	// �����κ��� �޽����� �д� Ŭ����
	public static class Receiver extends Thread {

		Socket socket;
		DataInputStream in;

		// Socket�� �Ű������� �޴� ������.
		public Receiver(Socket socket) {
			this.socket = socket;

			try {
				in = new DataInputStream(this.socket.getInputStream());
			} catch (Exception e) {
				System.out.println("����:" + e);
			}
		}

		/** �޽��� �ļ� */
		public String[] getMsgParse(String msg) {
			String[] tmpArr = msg.split("[|]");
			return tmpArr;
		}

		@Override
		public void run() {

			while (in != null) {
				try {

					String msg = in.readUTF(); // �Է½�Ʈ���� ���� �о�� ���ڿ��� msg�� �Ҵ�.
					String[] msgArr = getMsgParse(msg.substring(msg.indexOf("|") + 1));

					// �޼��� ó�� ----------------------------------------------

					MultiClient.chatState = 1; // ä�� ���¸� ä�ù����� �Ϸ�� ��ȭ���ɻ���
					if (msg.startsWith("enterRoom#yes")) { // �׷�����
						// enterRoom#yes|���ӹ�
						System.out.println("[##] ä�ù� (" + msgArr[0] + ") �� �����Ͽ����ϴ�.");
						MultiClient.chatState = 2; // ê ���� ���� ( ä�ù����� �Ϸ��
													// ��ȭ���ɻ���)

					} else if (msg.startsWith("show")) { // �������������ϰ����ϴ� �޽���

						// show|�޽�������
						System.out.println(msgArr[0]);

					} else if (msg.startsWith("say")) { // ��ȭ����
						// say|���̵�|��ȭ����
						System.out.println("[" + msgArr[0] + "] " + msgArr[1]);

					} else if (msg.startsWith("whisper")) { // �ӼӸ�
						// whisper|���̵�|��ȭ����
						System.out.println("[��][" + msgArr[0] + "] " + msgArr[1]);

					} else if (msg.startsWith("req_PvPchat")) { // �ش� ����ڿ���
																// 1:1��ȭ ��û
						// req_PvPchat|��³���
						MultiClient.chatState = 3; // ê ���� ���� (������ 1:1��ȭ��û��
													// �������
						System.out.println(msgArr[0]); // �޼����� ����
						System.out.print("������:");

					} else if (msg.startsWith("req_fileSend")) { // ������ ����
																	// ����ڿ���
																	// �������� ����
																	// ��û
						// req_fileSend|��³���
						// req_fileSend|[##] name �Բ��� ���� ������ �õ��մϴ�.
						// �����Ͻðڽ��ϱ�?(Y/N)
						MultiClient.chatState = 5; // ���� ���� (������ �������ڿ��� ����������
													// ������û�� ����)
						System.out.println(msgArr[0]); // �޼����� ����
						System.out.print("������:");
						sleep(100);

					} else if (msg.startsWith("fileSender")) { // ������ ���������� ���ϼ���
																// �غ�
						// fileSender|filepath;
						System.out.println("fileSender:" + InetAddress.getLocalHost().getHostAddress());
						System.out.println("fileSender:" + msgArr[0]);
						// String
						// ip=InetAddress.getLocalHost().getHostAddress();

						try {
							new FileSender(msgArr[0]).start(); // ������ ����.
						} catch (Exception e) {
							System.out.println("FileSender ������ ����:");
							e.printStackTrace();
						}

					} else if (msg.startsWith("fileReceiver")) { // ���Ϲޱ�
						// fileReceiver|ip|fileName;

						System.out.println("fileReceiver:" + InetAddress.getLocalHost().getHostAddress());
						System.out.println("fileReceiver:" + msgArr[0] + "/" + msgArr[1]);

						String ip = msgArr[0]; // ������ �����Ǹ� ���� ����
						String fileName = msgArr[1]; // �������� ������ �����̸�.

						try {
							new FileReceiver(ip, fileName).start(); // ������ ����.
						} catch (Exception e) {
							System.out.println("FileSender ������ ����:");
							e.printStackTrace();
						}

					} else if (msg.startsWith("req_exit")) { // ����

					}

				} catch (SocketException e) {
					System.out.println("����:" + e);
					System.out.println("##�������� ������ ������ ���������ϴ�.");
					return;

				} catch (Exception e) {
					System.out.println("Receiver:run() ����:" + e);

				}
			} // while----
		}// run()------
	}// class Receiver -------

	/////////////////////////////////////////////////////////////////////

	// ������ �޽����� �����ϴ� Ŭ����
	public static class Sender extends Thread {
		Socket socket;
		DataOutputStream out;
		String name = "zz";
		String msg;

		// ������ ( �Ű������� ���ϰ� ����� �̸� �޽��ϴ�. )
		public Sender(Socket socket) { // ���ϰ� ����� �̸��� �޴´�.
			this.socket = socket;
			try {
				out = new DataOutputStream(this.socket.getOutputStream());
			} catch (Exception e) {
				System.out.println("����:" + e);
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
						// continue; //�ֿܼ��� �������� �ѱ�°��� ���� ȿ������.
					} else if (MultiClient.chatState == 2) {
						// req_say|���̵�|��ȭ����
						out.writeUTF("req_say|" + name + "|" + msg);
					} else if (msg.trim().startsWith("/")) {
						if (msg.equalsIgnoreCase("/exit")) {
							System.out.println("[##] Ŭ���̾�Ʈ�� �����մϴ�.");
							System.exit(0);
							break;
						} else {
							out.writeUTF("req_cmdMsg|" + name + "|" + msg);
							// req_cmdMsg|��ȭ��|/������
						}
					} else if (MultiClient.chatState == 3) { // 3 : ������
																// 1:1��ȭ��û��
																// ���� ,
						// PvPchat|result)
						msg = msg.trim(); // �޽��� ��������
						if (msg.equalsIgnoreCase("y")) {
							out.writeUTF("PvPchat|yes");
						} else if (msg.equalsIgnoreCase("n")) {
							out.writeUTF("PvPchat|no");
						} else {
							System.out.println("�Է��� ���� �ùٸ��� �ʽ��ϴ�.");
							out.writeUTF("PvPchat|no");
						}
						MultiClient.chatState = 2; // 1:1��ȭ ��û�� ����Ϸ� ����

					} else if (MultiClient.chatState == 5) { // 5 : ������ ����������
																// �õ��Ͽ�
																// ������� ������û��
																// ��ٸ�.
						// fileSend|result)
						if (msg.trim().equalsIgnoreCase("y")) {
							out.writeUTF("fileSend|yes");
						} else if (msg.trim().equalsIgnoreCase("n")) {
							out.writeUTF("fileSend|no");
						} else {
							System.out.println("�Է��� ���� �ùٸ��� �ʽ��ϴ�.");
							out.writeUTF("fileSend|no");
						}

						MultiClient.chatState = 2; // �������ۼ�����û������ ����Ϸ� ����

					}

				} catch (SocketException e) {
					System.out.println("Sender:run()����:" + e);
					System.out.println("##�������� ������ ������ ���������ϴ�.");
					return;
				} catch (IOException e) {
					System.out.println("����:" + e);
				}
			} // while------

		}// run()------
	}// class Sender-------

}
