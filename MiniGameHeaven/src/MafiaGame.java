import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class MafiaGame extends JFrame{

	
	private static JPanel contentPane;
	Timer timer;
	private static int r,g,b;
	
	public static void main(String[] args) {

		try {
			MafiaGame frame = new MafiaGame();
			frame.setVisible(true);
			while(true)
			{
				System.out.println("hi");
			}

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
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
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
					if (r < 10)
						timer.stop();
				}
			};
			timer = new Timer(10, counter);
			timer.start();
		}		
	}
	
	
	
	
}
