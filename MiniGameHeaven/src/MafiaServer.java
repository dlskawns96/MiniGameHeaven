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
                if(input.endsWith("MASTER")) //방장 등장
                {
                    roomNum = Integer.parseInt(input.split(" ")[0]);
                	tmpRoom.roomNumber = Integer.parseInt(input.split(" ")[0]);
                	tmpRoom.peopleNum++;
                	people = 1;
                	rooms.add(tmpRoom);
                }
                else
                {
                	roomNum = Integer.parseInt(input);
                	for(int i = 0; i < rooms.size(); i++)
                	{
                		if(rooms.get(i).roomNumber == Integer.parseInt(input))
                		{
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
                players.add(tmp);
            
                for(int i = 0; i < players.size(); i++) {
                    tmp = players.get(i);
                    if(roomNum == tmp.roomNumber) 
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
                    //whispering
                    else if(input.startsWith("/w")) {
                       if(input.split(" ").length < 2)
                          continue;
                       
                       int i;
                       //find index of reciever's name in clients List
                       for(i = 0; i < players.size(); i++) {
                          tmp = players.get(i);
                          if(input.split(" ")[1].equals(tmp.name)) 
                             break;
                       }

                       String whisper = "From <" + name + "> " + input.split(" ", 3)[2];
                       tmp.writer.println(whisper);
                       
                       //find index of sender's name in clients List
                       for(i = 0; i < players.size(); i++) {
                          tmp = players.get(i);
                          if(tmp.name.equals(name)) 
                             break;
                       }

                       whisper = "To <" + input.split(" ")[1] + "> " + input.split(" ", 3)[2];
                       tmp.writer.println(whisper);
                          
                       continue;
                    }
                    
                    //일반 메시지 처리
                    for(int i = 0; i < players.size(); i++) {
                        tmp = players.get(i);
                        if(input.split(" ")[0].equals(Integer.toString(tmp.roomNumber))) 
                           tmp.writer.println("MESSAGE " + name + ": " + input.substring(2));
                     }
                    //게임 스타트 처리
                    if(input.endsWith("GameStart"))
                    {
                    	  for(int i = 0; i < players.size(); i++) {
                              tmp = players.get(i);
                              if(input.split(" ")[0].equals(Integer.toString(tmp.roomNumber))) 
                                 tmp.writer.println("GAMESTART");
                           }
                    }
                    
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                
               for (PrintWriter writer : writers) {
                  writer.println("<SYSTEM> " + name + " Is left. ");
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
}