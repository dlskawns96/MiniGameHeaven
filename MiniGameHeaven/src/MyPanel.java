import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	Image image;
	float per;

	MyPanel(String img, float p) {
		image = Toolkit.getDefaultToolkit().createImage(img);
		this.per = p;
		setOpaque(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
		}

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, per));
		g2d.setColor(getBackground());
		g2d.fill(getBounds());
		g2d.dispose();
	}
}