import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.Color;

public class MafiaClient extends JFrame {

	
	
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
	private  int r, g, b;
	private JLabel dayOrnight;
	private boolean isMaster = false;
	private int people = 4;
	private JLabel peopleInRoom;
	private JButton gameStartBtn;
	private JLabel roleLabel;
	private JButton[] vote = new JButton[7];
	private int p;
	private int k = 0;
	private String tmp;
	private Col col = new Col();
	private Col2 col2 = new Col2();
	private Make mak = new Make();
	public int time = 0;
	
	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		// for test
		MafiaClient b = new MafiaClient("C4", "127.0.0.1", 4, false);
		b.run();		
		
	}

	/**
	 * Create the frame.
	 * @throws InterruptedException 
	 */
	public MafiaClient(String id, String ip, int room, boolean isMaster) throws InterruptedException {
		this.isMaster = isMaster;
		this.ID = id;
		this.IP = ip;
		this.roomNum = room;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 528);
		setTitle("Mafia Game");
		setIconImage(new ImageIcon("hat.png").getImage());
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
		System.out.println("GAMESTARTBTN @@@@ : " + gameStartBtn.getText());
		
		
		dayOrnight = new JLabel();
		dayOrnight.setBounds(14, 10, 105, 30);
		dayOrnight.setBackground(Color.white);
		contentPane.add(dayOrnight);

		peopleInRoom = new JLabel("");
		peopleInRoom.setBounds(532, 23, 196, 15);
		contentPane.add(peopleInRoom);
		
		roleLabel = new JLabel();
		roleLabel.setBounds(530, 349, 100, 15);
		contentPane.add(roleLabel);
		
		for(p = 0; p < 7; p++)
		{
			vote[p] = new JButton("");	
			contentPane.add(vote[p]);
			vote[p].setVisible(false);
			vote[p].setEnabled(false);
		
		}
		
		vote[0].setBounds(530, 304, 97, 23);
		vote[1].setBounds(631, 304, 97, 23);
		vote[2].setBounds(530, 271, 97, 23);
		vote[3].setBounds(631, 271, 97, 23);
		vote[4].setBounds(530, 238, 97, 23);
		vote[5].setBounds(631, 238, 97, 23);
		
		vote[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[0].getText());
				out.println("VOTEFOR " + roomNum + " " + vote[0].getText() + " " + people);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[1].getText());
				out.println("VOTEFOR " + roomNum + " " + vote[1].getText() + " " + people);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[2].getText());
				out.println("VOTEFOR " + roomNum + " " + vote[2].getText() + " " + people);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[3].getText());
				out.println("VOTEFOR " + roomNum + " " + vote[3].getText() + " " + people);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[4].getText());
				out.println("VOTEFOR " + roomNum + " " + vote[4].getText() + " " + people);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[5].getText());
				out.println("VOTEFOR " + roomNum + " " + vote[5].getText() + " " + people);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[6].getText());
				out.println("VOTEFOR " + roomNum + " " + vote[6].getText() + " " + people);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		

		// 엔터 눌렀을때 액션
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(roomNum + " " + textField.getText());
				textField.setText("");
			}
		});
		// 채팅 버튼 눌렀을때 액션
		chatBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(roomNum + " " + textField.getText());
				textField.setText("");
			}
		});
		// 게임 시작 버튼 액션
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
		Socket socket = new Socket(IP, 9995);
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
			} else if(line.startsWith("MAKEBTN")) {
				tmp = line;
				try {						
					System.out.println("#################################");
					System.out.println("TMP!!!!!!!!!!!  : " + tmp);
					while(!tmp.split(" ")[0].equals("END"))
					{
						System.out.println(tmp);
						if(tmp.split(" ")[1].equals(ID))
						{
							tmp = in.readLine();
							continue;
						}
						else
						{
							vote[k].setText(tmp.split(" ")[1]);
							vote[k].setVisible(true);
							vote[k].setEnabled(true);
							k++;
						}
						tmp = in.readLine();
					}
					System.out.println("This is the end");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if(line.startsWith("RESULT")) {
				System.out.println(line.split(" ")[1] + " is Died");
				messageArea.append("Result : " + line.split(" ")[1] + " is Died\n");
				messageArea.append("Now it's night, only mafias can talk...\n");
				if(line.split(" ")[1].equals(ID))
				{
					out.println(roomNum + " <NIGHT>");
					System.out.println("Im died");
					JOptionPane.showMessageDialog(null,"You are died!!");
					socket.close();
					this.dispose();
					break;
				}
			} else if(line.startsWith("<NIGHT>")) {
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				col2.start();
				if(!(role.equals("mafia")))
				{
					textField.setEnabled(false);
					messageArea.setVisible(false);
				}
			}
			messageArea.setCaretPosition(messageArea.getDocument().getLength());
		} 
	}

	private void mafiaStart() throws IOException {
		col.start();	
		out.println("ROLE " + roomNum);
		role = in.readLine();
		roleLabel.setText("You are " + role.toUpperCase());
		
	}

	class Col extends Thread implements ActionListener
	{
		Timer t = new Timer(1000,this);
		public void run()
		{
			t = new Timer(1000,this);
			t.start();				
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			time++;
			System.out.println(r);
			r -= 20;
			g -= 20;
			b -= 20;
			contentPane.setBackground(new Color(r, g, b));
			dayOrnight.setText("Day left : " + (10-time));
			dayOrnight.setForeground(new Color(255 - r, 255 - g, 255 - b));
			peopleInRoom.setForeground(new Color(255 - r, 255 - g, 255 - b));
			if(time >= 10)
			{
				r = 0;
				g = 0;
				b = 0;
				dayOrnight.setText("It's Night");
				contentPane.setBackground(new Color(0, 0, 0));
				dayOrnight.setForeground(new Color(255, 255, 255));
				peopleInRoom.setForeground(new Color(255, 255, 255));
				time = 0;
				t.stop();
				mak.start();			
			}
		}
	}
	
	class Col2 extends Thread implements ActionListener
	{
		Timer t = new Timer(1000,this);
		public void run()
		{	
			t = new Timer(1000,this);		
			t.start();				
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			time++;
			System.out.println(r);
			r += 20;
			g += 20;
			b += 20;
			contentPane.setBackground(new Color(r, g, b));
			dayOrnight.setText("Day left : " + (10 - time + 1));
			dayOrnight.setForeground(new Color(255 - r, 255 - g, 255 - b));
			peopleInRoom.setForeground(new Color(255 - r, 255 - g, 255 - b));
			if(time >= 10)
			{
				r = 255;
				g = 255;
				b = 255;
				contentPane.setBackground(new Color(r, g, b));
				dayOrnight.setText("It's Day");
				dayOrnight.setForeground(new Color(255 - r, 255 - g, 255 - b));
				peopleInRoom.setForeground(new Color(255 - r, 255 - g, 255 - b));
				time = 0;
				t.stop();
				mak.start();
			}
		}
	}
	
	class Make extends Thread
	{
		public void run()
		{			
			if(isMaster) {
				out.println("<VOTE>"   + " " + roomNum);	
			}
			System.out.println("OK!!");
		}
	}
	
	
	

	
	
}