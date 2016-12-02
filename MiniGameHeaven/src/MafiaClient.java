import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class MafiaClient extends JFrame{

	
	private static JPanel contentPane;
	Timer timer;
	private static int r,g,b;
	private JLabel timeLabel;
	JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
	private  JFrame frame = new JFrame("Mafia Game");
	
	private BufferedReader in;
	private PrintWriter out;
	
	public static void main(String[] args) {
		MafiaClient client = new MafiaClient();
		try {
			client.frame = new MafiaClient();
			client.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MafiaClient() { 
		textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		timeLabel = new JLabel("");
		timeLabel.setFont(new Font("Yu Mincho Demibold", Font.PLAIN, 25));
		timeLabel.setBounds(361, 0, 189, 87);
		contentPane.add(timeLabel);
		r = 255;
		g = 255;
		b = 255;
		ColorChange colorThread = new ColorChange();
		colorThread.start();		
	}
	
	private void run() throws UnknownHostException, IOException
	{
		// Make connection and initialize streams
        String serverAddress = new URL("mnhServer.mooo.com").getHost();
        Socket socket = new Socket(serverAddress, 9999);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            } else if(line.startsWith("<SYSTEM>")) { //handle system messages that notices entering and leaving
            	messageArea.append(line + "\n");
            } else if(line.startsWith("From") || line.startsWith("To")) { //handle whispering messages
            	messageArea.append(line +"\n");
            }
        }
	}
	

	class ColorChange extends Thread
	{
		public void run()
		{
			ActionListener counter = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					r -= 1;
					g -= 1;
					b -= 1;
					contentPane.setBackground(new Color(r,g,b));
					timeLabel.setText(Integer.toString(r));
					timeLabel.setForeground(new Color(255-r,255-g,255-b));
					if (r  == 0)
						timer.stop();
				}
			};
			timer = new Timer(10, counter);
			timer.start();
		}		
	}
}
