import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Heart {

	public JLabel[] heart = new JLabel[5];
	public int numOfHeart;
	ImageIcon heartOk = new ImageIcon("heartOk.png");
	ImageIcon heartNo = new ImageIcon("heartNo.png");

	Heart(int nh) {
		Image hO = heartOk.getImage();
		hO = hO.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		heartOk = new ImageIcon(hO);
		Image hX = heartNo.getImage();
		hX = hX.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		heartNo = new ImageIcon(hX);

		int i = 0;
		for (JLabel j : heart) {
			heart[i] = new JLabel();
			heart[i].setIcon(heartOk);
			i++;
		}

		i = 0;
		for (JLabel j : heart) {
			heart[i].setBounds(500 + 50 * i, 20, 35, 35);
			i++;
		}
	}
}
