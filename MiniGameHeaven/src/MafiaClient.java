import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.Color;

public class MafiaClient extends JFrame implements ActionListener {

	private JPanel contentPane;
	BufferedReader in;
	PrintWriter out;
	private JTextField textField;
	private JTextArea messageArea;
	private String ID;
	private String IP = "";
	private String role;
	private int roomNum;
	private static ArrayList<MafiaPlayer> players = new ArrayList<MafiaPlayer>();
	private static int r, g, b;
	private Timer timer = new Timer(10, this);
	private JLabel dayOrnight;
	private boolean isMaster = false;
	private int people = 0;
	private JLabel peopleInRoom;
	private JButton gameStartBtn;
	private JLabel roleLabel;
	private JButton voteBtn;
	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// for test
		MafiaClient a = new MafiaClient("17", "127.0.0.1", 3, false);
		a.run();
	}

	/**
	 * Create the frame.
	 */
	public MafiaClient(String id, String ip, int room, boolean isMaster) {

		this.isMaster = isMaster;
		this.ID = id;
		this.IP = ip;
		this.roomNum = room;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 528);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(14, 435, 504, 24);
		contentPane.add(textField);
		textField.setColumns(10);

		messageArea = new JTextArea();
		messageArea.setEditable(false);

		JScrollPane scrollBar = new JScrollPane(messageArea);
		scrollBar.setBackground(Color.WHITE);
		scrollBar.setBounds(14, 50, 504, 373);
		scrollBar.setOpaque(false);
		scrollBar.setViewportView(messageArea);
		contentPane.add(scrollBar);

		JButton chatBtn = new JButton("Enter");
		chatBtn.setBounds(525, 433, 105, 27);
		contentPane.add(chatBtn);

		JScrollPane userScroll = new JScrollPane();
		userScroll.setBounds(532, 50, 196, 136);
		contentPane.add(userScroll);

		JTextArea playerList = new JTextArea();
		playerList.setEditable(false);
		userScroll.setViewportView(playerList);

		gameStartBtn = new JButton("Game Start");
		gameStartBtn.setBounds(525, 372, 105, 50);
		gameStartBtn.setEnabled(isMaster);
		contentPane.add(gameStartBtn);

		dayOrnight = new JLabel();
		dayOrnight.setBounds(14, 10, 105, 30);
		dayOrnight.setBackground(Color.white);
		contentPane.add(dayOrnight);

		peopleInRoom = new JLabel("");
		peopleInRoom.setBounds(532, 55, 196, 15);
		contentPane.add(peopleInRoom);
		
		roleLabel = new JLabel();
		roleLabel.setBounds(530, 349, 100, 15);
		contentPane.add(roleLabel);
		
		voteBtn = new JButton("Vote for Kill");
		voteBtn.setBounds(579, 222, 97, 61);
		

		// ���� �������� �׼�
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(roomNum + " " + textField.getText());
				textField.setText("");
			}
		});
		// ä�� ��ư �������� �׼�
		chatBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(roomNum + " " + textField.getText());
				textField.setText("");
			}
		});
		// ���� ���� ��ư �׼�
		gameStartBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(roomNum + " GameStart");
			}
		});

		setVisible(true);

	}

	private void run() throws IOException {

		r = 255;
		g = 255;
		b = 255;
		// Make connection and initialize streams
		Socket socket = new Socket(IP, 9997);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Process all messages from server, according to the protocol.
		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out.println(ID);
			} else if (line.startsWith("SUBMITROOM")) {
				if (isMaster)
					out.println(roomNum + " MASTER");
				else
					out.println(roomNum + "");
			} else if (line.startsWith("NAMEACCEPTED")) {
				textField.setEditable(true);
			} else if (line.startsWith("MESSAGE")) {
				messageArea.append(line.substring(8) + "\n");
			} else if (line.startsWith("<SYSTEM>")) { 
				peopleInRoom.setText("People In Room : " + line.split(" ")[line.split(" ").length - 1]);
				messageArea.append(line + "\n");
			} else if (line.startsWith("From") || line.startsWith("To")) { 
				messageArea.append(line + "\n");
			} else if (line.startsWith("GAMESTART")) {
				gameStartBtn.setEnabled(false);
				mafiaStart();
			}
			messageArea.setCaretPosition(messageArea.getDocument().getLength());
		} 
	}

	private void mafiaStart() throws IOException {
		out.println("ROLE");
		role = in.readLine();
		roleLabel.setText("You are " + role.toUpperCase());
		timer.setDelay(1000);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(r);
		contentPane.setBackground(new Color(r--, g--, b--));
		dayOrnight.setText("Day left : " + (r - 9));
		dayOrnight.setForeground(new Color(255 - r, 255 - g, 255 - b));
		peopleInRoom.setForeground(new Color(255 - r, 255 - g, 255 - b));
		if (r < 10)
		{				
			timer.stop();
			contentPane.add(voteBtn);
			contentPane.remove(voteBtn);
		}
	}
}