import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.Environment;

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
	private int people = 5;
	private int mafiaNum = 2;
	private JLabel peopleInRoom;
	private JButton gameStartBtn;
	private JLabel roleLabel;
	private JButton[] vote = new JButton[7];
	private int p;
	private int k = 0;
	private String tmp;
	private String playerL = "";
	public int time = 0;
	JTextArea playerList;
	
	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		// for test
		MafiaClient b = new MafiaClient("C456", "127.0.0.1", 5, false);
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

		gameStartBtn = new JButton("Game Start");
		gameStartBtn.setBounds(525, 372, 105, 50);
		gameStartBtn.setEnabled(isMaster);
		contentPane.add(gameStartBtn);

		
		
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
		
		playerList = new JTextArea();
		playerList.setBounds(532, 51, 196, 113);
		contentPane.add(playerList);
		
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
				if(r <= 75)
					out.println("VOTEFOR " + roomNum + " " + vote[0].getText() + " " + people);
				else
					out.println("VOTEFOR " + roomNum + " " + vote[0].getText() + " " + mafiaNum);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[1].getText());
				if(r <= 75)
					out.println("VOTEFOR " + roomNum + " " + vote[1].getText() + " " + people);
				else
					out.println("VOTEFOR " + roomNum + " " + vote[1].getText() + " " + mafiaNum);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[2].getText());if(r == 0)
					out.println("VOTEFOR " + roomNum + " " + vote[2].getText() + " " + people);
				else
					out.println("VOTEFOR " + roomNum + " " + vote[2].getText() + " " + mafiaNum);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[3].getText());
				if(r <= 75)
					out.println("VOTEFOR " + roomNum + " " + vote[3].getText() + " " + people);
				else
					out.println("VOTEFOR " + roomNum + " " + vote[3].getText() + " " + mafiaNum);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[4].getText());
				if(r <= 75)
					out.println("VOTEFOR " + roomNum + " " + vote[4].getText() + " " + people);
				else
					out.println("VOTEFOR " + roomNum + " " + vote[4].getText() + " " + mafiaNum);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[5].getText());
				if(r <= 75)
					out.println("VOTEFOR " + roomNum + " " + vote[5].getText() + " " + people);
				else
					out.println("VOTEFOR " + roomNum + " " + vote[5].getText() + " " + mafiaNum);
				for(int k = 0; k < 7; k++)
					vote[k].setEnabled(false);
			}
		});
		
		vote[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(vote[6].getText());
				if(r <= 75)
					out.println("VOTEFOR " + roomNum + " " + vote[6].getText() + " " + people);
				else
					out.println("VOTEFOR " + roomNum + " " + vote[6].getText() + " " + mafiaNum);
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

		Socket socket = new Socket(IP, 9995);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out.println(ID);
			} else if(line.startsWith("<NEW>")) {
				playerL = playerL +line.split(" ")[1] +"\n";

				playerList.setText(playerL);
				messageArea.append("<SYSTEM> " + line.split(" ")[1]+" is entered.\n");
			} else if(line.startsWith("<EXIT>")) {
				String tp = line.split(" ")[1];
				playerL.replace(tp, "");
				playerList.setText(playerL);
				messageArea.append("<SYSTEM> " + line.split(" ")[1]+" is exit.\n");
			}
			else if (line.startsWith("SUBMITROOM")) {
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
			} else if (line.startsWith("From") || line.startsWith("To")) { 
				messageArea.append(line + "\n");
			} else if (line.startsWith("GAMESTART")) {
				gameStartBtn.setEnabled(false);
				mafiaStart();
			} else if(line.startsWith("MAKEBTN")) {
				System.out.println("RRRRRRRR = " + r);
				if(r > 75)
				{
					if(!role.equals("mafia"))
						continue; 
				}
				tmp = line;
				try {						
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
				k = 0;
				
				System.out.println(line.split(" ")[1] + " is Died");
				messageArea.append("Result : " + line.split(" ")[1] + " is Died\n");
				if(r <= 75)
					messageArea.append("Now it's night, only mafias can talk...\n");
				else
					messageArea.append("It's Day time.\n");
				if(line.split(" ")[1].equals(ID))
				{
					if(role.equals("mafia"))
					{
						out.println(roomNum + " <MINUSMAFIA>");
						
					}
					else
					{	
						out.println(roomNum + " <MINUSPEOPLE>");											
					}
					if(r <= 75)
						out.println(roomNum + " <NIGHT>");
					else
						out.println(roomNum + " <DAY>");
					System.out.println("Im died");
					JOptionPane.showMessageDialog(null,"You are died!!");
					System.exit(1);
					break;
				}
				messageArea.append(line.split(" ")[1] + " died!!\n");
			} else if(line.startsWith("<NIGHT>")) {

				new Col2().start();
				if(!(role.equals("mafia")))
				{
					textField.setEnabled(false);
					messageArea.setVisible(false);
				}
				continue;
			} else if(line.startsWith("<DAY>")) {
				textField.setEnabled(true);
				messageArea.setVisible(true);
				new Col().start();
			} else if(line.startsWith("<MINUSPEOPLE>")) {
				people--;
				if(people == 2)
				{
					if(mafiaNum != 0)
					{
						JOptionPane.showMessageDialog(null,"GAME OVER\n MAFIAS WIN!!");
					}
					else
					{
						JOptionPane.showMessageDialog(null,"GAME OVER\n CIVILS WIN!!");
					}
				}
			} else if(line.startsWith("<MINUSMAFIA>")) {
				people--;
				mafiaNum--;
				if(people == 2)
				{
					if(mafiaNum != 0)
					{
						JOptionPane.showMessageDialog(null,"GAME OVER\n MAFIAS WIN!!");
					}
					else
					{
						JOptionPane.showMessageDialog(null,"GAME OVER\n CIVILS WIN!!");
					}
				}
			}
			messageArea.setCaretPosition(messageArea.getDocument().getLength());
		} 
	}

	private void mafiaStart() throws IOException {
		new Col().start();	
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
			roleLabel.setForeground(new Color(255 - r, 255 - g, 255 - b));
			if(time >= 10)
			{
				r = 0;
				g = 0;
				b = 0;
				dayOrnight.setText("It's Night");
				contentPane.setBackground(new Color(0, 0, 0));
				dayOrnight.setForeground(new Color(255, 255, 255));
				peopleInRoom.setForeground(new Color(255, 255, 255));
				roleLabel.setForeground(new Color(255, 255, 255));
				time = 0;
				t.stop();
				new Make().start();		
			}
		}
	}
	
	class Col2 extends Thread implements ActionListener
	{
		Timer t = new Timer(1000,this);
		public void run()
		{	
			for(int i = 0; i < 7; i++)
			{
				vote[i].setEnabled(false);
				vote[i].setVisible(false);
			}
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
			roleLabel.setForeground(new Color(255 - r, 255 - g, 255 - b));
			if(time >= 10)
			{
				r = 255;
				g = 255;
				b = 255;
				contentPane.setBackground(new Color(r, g, b));
				dayOrnight.setText("It's Day");
				dayOrnight.setForeground(new Color(255 - r, 255 - g, 255 - b));
				peopleInRoom.setForeground(new Color(255 - r, 255 - g, 255 - b));
				roleLabel.setForeground(new Color(255 - r, 255 - g, 255 - b));
				time = 0;
				t.stop();
				new Make().start();
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