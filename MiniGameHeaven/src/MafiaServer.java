import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.sound.midi.Synthesizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MafiaServer extends JFrame {

	private static final int PORT = 9995;

	private static HashSet<String> names = new HashSet<String>();

	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

	private static List<MafiaPlayer> players = new ArrayList<MafiaPlayer>();

	private static List<MafiaRoom> rooms = new ArrayList<MafiaRoom>();
	private static int k = 0;
	private static int maxIndex = 0;
	
	public static void main(String[] args) throws Exception {
		System.out.println("The Mafia server is running.");

		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private int roomNum;
		private int people, voted = 0;
		private String input;
		MafiaPlayer tmp = new MafiaPlayer();
		MafiaPlayer tmp2 = new MafiaPlayer();
		MafiaRoom tmpRoom = new MafiaRoom();
		
		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				tmpRoom.peopleNum = 0;
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					if (name == null) {
						return;
					}
					tmp.name = name;
					synchronized (names) {
						if (!names.contains(name)) {
							names.add(name);
							break;
						}
					}
				}

				out.println("SUBMITROOM");
				input = in.readLine();
				if (input.endsWith("MASTER")) // 방장 등장
				{
					roomNum = Integer.parseInt(input.split(" ")[0]);
					tmpRoom.roomNumber = Integer.parseInt(input.split(" ")[0]);
					tmpRoom.peopleNum++;
					people = 1;
					rooms.add(tmpRoom);
				} else {
					roomNum = Integer.parseInt(input);
					for (int i = 0; i < rooms.size(); i++) {
						if (rooms.get(i).roomNumber == Integer.parseInt(input)) {
							tmpRoom.roomNumber = rooms.get(i).roomNumber;
							tmpRoom.peopleNum = rooms.get(i).peopleNum + 1;
							rooms.set(i, tmpRoom);
							people = rooms.get(i).peopleNum;
							break;
						}
					}
				}
				System.out.println(people);
				tmp.roomNumber = roomNum;

				out.println("NAMEACCEPTED");
				writers.add(out);

				tmp.writer = out;
				tmp.role = "null";
				players.add(tmp);

				for (int i = 0; i < players.size(); i++) {
					tmp = players.get(i);
					if (roomNum == tmp.roomNumber)
						tmp.writer.println("<SYSTEM> " + name + " Is entered. " + Integer.toString(people));
				}

				while (true) {
					input = in.readLine();
					System.out.println(input);
					if (input == null) {
						return;
					}

					// 게임 스타트 처리
					if (input.endsWith("GameStart")) {
						int k;
						for (k = 0; k < rooms.size(); k++) {
							if (rooms.get(k).roomNumber == Integer.parseInt(input.split(" ")[0])) {
								if (rooms.get(k).peopleNum != 4)
									break;
							}
						}
						if(k != rooms.size())
						{	
							for (int i = 0; i < players.size(); i++) {
								tmp = players.get(i);
								if (input.split(" ")[0].equals(Integer.toString(tmp.roomNumber)))
									tmp.writer.println("MESSAGE " + "You Need 7 people");
							}
						}
						else
						{
							for (int i = 0; i < players.size(); i++) {
								tmp = players.get(i);
								if (input.split(" ")[0].equals(Integer.toString(tmp.roomNumber)))
									tmp.writer.println("GAMESTART");
							}
						}
						continue;
					}
					
					//역할 분배
					if(input.startsWith("ROLE"))
					{
						disRole(Integer.parseInt(input.split(" ")[1]));
						continue;
					}
					
					//투표 시작
					if(input.startsWith("<VOTE>"))
					{
						System.out.println("Lets Vote!!!");
						for (int i = 0; i < players.size(); i++) {
							tmp = players.get(i);
							if (Integer.parseInt(input.split(" ")[1]) == tmp.roomNumber)
							{
								System.out.println("MAKEBTN " + tmp.name);
								for (int j = 0; j < players.size(); j++) {
									tmp2 = players.get(j);
									if (Integer.parseInt(input.split(" ")[1]) == tmp2.roomNumber)
										tmp2.writer.println("MAKEBTN " + tmp.name);
								}
							}
							System.out.println("@@@@@@@@@@@@@@ i = " + i);
							
						}
						for (int i = 0; i < players.size(); i++) {
							tmp = players.get(i);
							if (Integer.parseInt(input.split(" ")[1]) == tmp.roomNumber)
								tmp.writer.println("END"+ " .");
						}
						continue;
					}
					//표결
					if(input.startsWith("VOTEFOR"))
					{
						for (int i = 0; i < players.size(); i++) {
							tmp = players.get(i);
							if (input.split(" ")[1].equals(Integer.toString(tmp.roomNumber)))
							{
								if(tmp.name.equals(input.split(" ")[2]))
								{
									tmp.voted++;
									players.set(i, tmp);
									System.out.println("%%%%%% : " + players.get(i).voted);
								}
							}
							
						}
						k++;
						// 개표 끝	
						if(Integer.parseInt(input.split(" ")[3]) == k )
						{
							System.out.println("The Reult is ...");
						
							int max = -1;
							for (int i = 0; i < players.size(); i++) {
								tmp = players.get(i);
								if (input.split(" ")[1].equals(Integer.toString(tmp.roomNumber)))
								{
									if(players.get(i).voted > max)
									{
										maxIndex = i;
										max = players.get(i).voted;
									}
								}
							}
							for (int i = 0; i < players.size(); i++) {
								tmp = players.get(i);
								if (input.split(" ")[1].equals(Integer.toString(tmp.roomNumber)))
								{
									tmp.writer.println("RESULT " + players.get(maxIndex).name);
								}
							}
						}
						continue;
					}
					
					if(input.endsWith("<NIGHT>"))
					{
						System.out.println("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
						for (int i = 0; i < players.size(); i++) {
							tmp = players.get(i);
							if (input.split(" ")[0].equals(Integer.toString(tmp.roomNumber)))
							{	
								if(!tmp.name.equals(players.get(maxIndex).name))
									tmp.writer.println("<NIGHT>");						
							}
						}
						maxIndex = 0;
						k = 0;
						continue;
					}
					
					// 일반 메시지 처리
					for (int i = 0; i < players.size(); i++) {
						tmp = players.get(i);
						if (input.split(" ")[0].equals(Integer.toString(tmp.roomNumber)))
							tmp.writer.println("MESSAGE " + name + ": " + input.substring(2));
					}
					

				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				System.out.println("EXIT SOMEONE");

				for (int i = 0; i < rooms.size(); i++) {
					if (rooms.get(i).roomNumber == roomNum) {
						tmpRoom.roomNumber = rooms.get(i).roomNumber;
						tmpRoom.peopleNum = rooms.get(i).peopleNum - 1;
						rooms.set(i, tmpRoom);
						people = rooms.get(i).peopleNum;
						break;
					}
				}

				for (PrintWriter writer : writers) {
					writer.println("<SYSTEM> " + name + " Is left. " + Integer.toString(people));
				}
				if (name != null) {
					names.remove(name);
				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	private static void disRole(int rn)
	{
		List<MafiaPlayer> playersInRoom = new ArrayList<MafiaPlayer>();
		MafiaPlayer tmp = new MafiaPlayer();
		
		Random rand = new Random();
		int r;
		int mafia = 1, civil = 1;
		
		
		for (int i = 0; i < players.size(); i++) {
			tmp = players.get(i);
			if (rn == tmp.roomNumber)
				playersInRoom.add(tmp);
		}
		
		tmp = playersInRoom.get(0);
		tmp.role = "mafia";
		tmp.writer.println("mafia");
		playersInRoom.set(0, tmp);
		
		tmp = playersInRoom.get(1);
		tmp.role = "mafia";
		tmp.writer.println("mafia");
		playersInRoom.set(1, tmp);
		
		tmp = playersInRoom.get(2);
		tmp.role = "mafia";
		tmp.writer.println("mafia");
		playersInRoom.set(2, tmp);
		
		tmp = playersInRoom.get(3);
		tmp.role = "mafia";
		tmp.writer.println("mafia");
		playersInRoom.set(3, tmp);
		
		
		/*
		for(;mafia > 0; mafia--)
		{
			r = rand.nextInt(playersInRoom.size());
			while(playersInRoom.get(r).role != "null")
				r = rand.nextInt(playersInRoom.size());
			tmp = playersInRoom.get(r);
			tmp.role = "mafia";
			tmp.writer.println("mafia");
			playersInRoom.set(r, tmp);
		}		
		
		r = rand.nextInt(playersInRoom.size());
		while(playersInRoom.get(r).role != "null")
			r = rand.nextInt(playersInRoom.size());
		tmp = playersInRoom.get(r);
		tmp.role = "police";
		tmp.writer.println("police");
		playersInRoom.set(r, tmp);
		
		r = rand.nextInt(playersInRoom.size());
		while(playersInRoom.get(r).role != "null")
			r = rand.nextInt(playersInRoom.size());
		tmp = playersInRoom.get(r);
		tmp.role = "doctor";
		tmp.writer.println("doctor");
		playersInRoom.set(r, tmp);
		
		for(;civil > 0; civil--)
		{
			r = rand.nextInt(playersInRoom.size());
			while(playersInRoom.get(r).role != "null")
				r = rand.nextInt(playersInRoom.size());
			tmp = playersInRoom.get(r);
			tmp.role = "civil";
			tmp.writer.println("civil");
			playersInRoom.set(r, tmp);
		}
		*/
	}
}