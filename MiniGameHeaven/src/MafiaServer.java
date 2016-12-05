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

	private static final int PORT = 9997;

	private static HashSet<String> names = new HashSet<String>();

	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

	private static List<MafiaPlayer> players = new ArrayList<MafiaPlayer>();

	private static List<MafiaRoom> rooms = new ArrayList<MafiaRoom>();
	
	
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
		private int people;
		private String input;
		MafiaPlayer tmp = new MafiaPlayer();
		MafiaRoom tmpRoom = new MafiaRoom();

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {

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

				// Accept messages from this client and broadcast them.
				// Ignore other clients that cannot be broadcasted to.
				while (true) {
					input = in.readLine();
					System.out.println(input);
					if (input == null) {
						return;
					}
					
					// 일반 메시지 처리
					for (int i = 0; i < players.size(); i++) {
						tmp = players.get(i);
						if (input.split(" ")[0].equals(Integer.toString(tmp.roomNumber)))
							tmp.writer.println("MESSAGE " + name + ": " + input.substring(2));
					}
					// 게임 스타트 처리
					if (input.endsWith("GameStart")) {
						int k;
						for (k = 0; k < rooms.size(); k++) {
							if (rooms.get(k).roomNumber == Integer.parseInt(input.split(" ")[0])) {
								if (rooms.get(k).peopleNum != 7)
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
					}
					
					//역할 분배
					if(input.startsWith("ROLE"))
					{
						disRole(Integer.parseInt(input.split(" ")[0]));
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
		int mafia = 2, civil = 3;
		
		for (int i = 0; i < players.size(); i++) {
			tmp = players.get(i);
			if (rn == tmp.roomNumber)
				playersInRoom.add(tmp);
		}
		
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
		
	}
}