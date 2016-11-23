import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Button;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class SelectGame extends WaitMain{

	JFrame frameSelectGame;
	BufferedReader in;
	PrintWriter out;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SelectGame window = new SelectGame();
					window.frameSelectGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SelectGame() {
		initialize();
		this.frameSelectGame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frameSelectGame = new JFrame();
		frameSelectGame.getContentPane().setBackground(new Color(25, 25, 112));
		frameSelectGame.getContentPane().setLayout(null);
		
		JButton leftSide = new JButton("<");
		leftSide.setBounds(12, 249, 70, 55);
		frameSelectGame.getContentPane().add(leftSide);
		
		JButton rightSide = new JButton(">");
		rightSide.setBounds(712, 249, 70, 55);
		frameSelectGame.getContentPane().add(rightSide);
		
		JPanel gameTitle = new JPanel();
		gameTitle.setBounds(122, 55, 232, 43);
		frameSelectGame.getContentPane().add(gameTitle);
		
		JPanel rankList = new JPanel();
		rankList.setBounds(122, 146, 547, 296);
		frameSelectGame.getContentPane().add(rankList);
		
		JButton createGame = new JButton("\uAC8C\uC784 \uB9CC\uB4E4\uAE30");
		createGame.setBounds(486, 465, 183, 63);
		frameSelectGame.getContentPane().add(createGame);
		frameSelectGame.setResizable(false);
		frameSelectGame.setIconImage(new ImageIcon("titleIcon.png").getImage());
		createGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				InGameOMOK ig = new InGameOMOK();
				waitMain.frame.setVisible(false);
				frameSelectGame.setVisible(false);
			}
		});


		frameSelectGame.setBounds(400, 200, 800, 600);
		frameSelectGame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
