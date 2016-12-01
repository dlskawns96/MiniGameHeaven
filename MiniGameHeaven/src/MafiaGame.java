import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class MafiaGame extends JFrame{

	
	private static JPanel contentPane;
	Timer timer;
	private static int r,g,b;
	private JLabel timeLabel;
	
	public static void main(String[] args) {

		try {
			MafiaGame frame = new MafiaGame();
			frame.setVisible(true);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MafiaGame() { 
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
