import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class SelectGame extends WaitMain {

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
					//SelectGame window = new SelectGame();
					//window.frameSelectGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	SelectGame() {
		initialize();
		frameSelectGame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frameSelectGame = new JFrame();
		ImageIcon bg = new ImageIcon("BG2.png");
		JPanel jp = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(bg.getImage(), 0, 0, null);
				Dimension d = getSize();
				g.drawImage(bg.getImage(), 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		frameSelectGame.setContentPane(jp);
		frameSelectGame.getContentPane().setLayout(null);

		JButton leftSide = new JButton("<");
		leftSide.setBounds(12, 249, 70, 55);
		leftSide.setForeground(Color.white);
		leftSide.setBackground(new Color(37,183,211));
		frameSelectGame.getContentPane().add(leftSide);

		JButton rightSide = new JButton(">");
		rightSide.setBounds(712, 249, 70, 55);
		rightSide.setForeground(Color.white);
		rightSide.setBackground(new Color(37,183,211));
		frameSelectGame.getContentPane().add(rightSide);

		JPanel gameTitle = new JPanel();
		gameTitle.setBounds(122, 55, 232, 43);
		frameSelectGame.getContentPane().add(gameTitle);

		JPanel rankList = new JPanel();
		rankList.setBounds(122, 115, 547, 330);
		frameSelectGame.getContentPane().add(rankList);

		JButton createGame = new JButton("\uAC8C\uC784 \uB9CC\uB4E4\uAE30");
		createGame.setBounds(486, 465, 183, 63);
		createGame.setForeground(Color.white);
		createGame.setBackground(new Color(37,183,211));
		frameSelectGame.getContentPane().add(createGame);
		frameSelectGame.setResizable(false);
		frameSelectGame.setIconImage(new ImageIcon("titleIcon.png").getImage());
		createGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (waitMain.numOfHeart != 0) {
					waitMain.dispHeart.heart[5 - waitMain.numOfHeart].setIcon(waitMain.dispHeart.heartNo);
					InGameOMOK ig = new InGameOMOK();
					for (int i = 5; i >= waitMain.numOfHeart; --i) {
						ig.dispHeart.heart[5 - i].setIcon(ig.dispHeart.heartNo);
					}
					waitMain.numOfHeart--;
					waitMain.frame.setVisible(false);
					frameSelectGame.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "하트가 부족합니다.");
				}
			}
		});

		frameSelectGame.setBounds(400, 200, 800, 600);
		frameSelectGame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
