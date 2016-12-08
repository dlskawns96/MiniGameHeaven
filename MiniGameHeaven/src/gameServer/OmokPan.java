package gameServer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;

@SuppressWarnings("serial")
// ������Ŭ����
class OmokPan extends Canvas {
	// ����� ���� ���
	public static final int BLACK = 1, WHITE = -1;

	private int[][] omok_pan; // ������ 2���� �迭
	private int omok_pan_size; // omok_pan_size�� ������ ���� �Ǵ� ���� ����, 15�� ���Ѵ�.
	private int cell; // ������ ũ��(pixel)
	private String info = "���� ����"; // ������ ���� ��Ȳ�� ��Ÿ���� ���ڿ�:���� ��ܿ� �����۾�
	private int color = BLACK; // ������� �� ����

	// true�̸� ����ڰ� ���� ���� �� �ִ� ���¸� �ǹ��ϰ�,
	// false�̸� ����ڰ� ���� ���� �� ���� ���¸� �ǹ��Ѵ�.
	private boolean running = false; // ������ ���� ���ΰ��� ��Ÿ���� ����

	private Graphics gbuff; // ĵ������ ���۸� ���� �׷��Ƚ� ��ü:���� ���߿� ��������
	private Image buff; // ���� ���۸��� ���� ����
	
	private Toolkit myToolkit = Toolkit.getDefaultToolkit();
<<<<<<< HEAD
	private Image panImage = myToolkit.getImage("omokpan.png");
=======
	private Image panImage = myToolkit.getImage("OMOK_BGboard.jpg");
>>>>>>> origin/master

	OmokPan() {
		this.omok_pan_size = 15;
		this.cell = 30;

		omok_pan = new int[omok_pan_size + 2][]; // ���� ũ�⸦ ���Ѵ�.
		for (int i = 0; i < omok_pan.length; i++)
			omok_pan[i] = new int[omok_pan_size + 2];
		//������ ĵ������ ũ�� ����
		setSize(470,470);																								
		repaint();
	}
	
	public void putWhiteOpponent(int x, int y) {
		omok_pan[x][y] = WHITE;
		repaint();
	}
	public void putBlackOpponent(int x, int y) {
		omok_pan[x][y] = BLACK;
		repaint();
	}
	
	public boolean isRunning() { // ������ ���� ���¸� ��ȯ�Ѵ�.
		return running;
	}

	public void update(Graphics g) { // repaint�� ȣ���ϸ� �ڵ����� ȣ��ȴ�.
		paint(g); // paint�� ȣ���Ѵ�.
	}

	public void paint(Graphics g) { // ȭ���� �׸���.
		if (gbuff == null) { // ���۰� ������ ���۸� �����.
			buff = createImage(getWidth(), getHeight());
			gbuff = buff.getGraphics();
		}
		drawBoard(g); // �������� �׸���.
	}

	public void reset() { // �������� �ʱ�ȭ��Ų��.
		for (int i = 0; i < omok_pan.length; i++)
			for (int j = 0; j < omok_pan[i].length; j++)
				omok_pan[i][j] = 0;
		info = "���� ����";
		repaint();
	}

	private void drawLine() { // �����ǿ� ���� �ߴ´�.
		gbuff.setColor(Color.black);
		for (int i = 1; i <= omok_pan_size; i++) {
			gbuff.drawLine(cell, i * cell, cell * omok_pan_size, i * cell);
			gbuff.drawLine(i * cell, cell, i * cell, cell * omok_pan_size);
		}
	}

	private void drawBlack(int x, int y) { // �� ���� (x, y)�� �׸���.
		Graphics2D gbuff = (Graphics2D) this.gbuff;
		gbuff.setColor(Color.black);
		gbuff.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
		gbuff.setColor(Color.white);
		gbuff.drawOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
	}

	public void drawWhite(int x, int y) { // �� ���� (x, y)�� �׸���.
		gbuff.setColor(Color.white);
		gbuff.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
		gbuff.setColor(Color.black);
		gbuff.drawOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
	}

	private void drawStones() { // omok_pan ������ ������ ��� �׸���.
		for (int x = 1; x <= omok_pan_size; x++)
			for (int y = 1; y <= omok_pan_size; y++) {
				if (omok_pan[x][y] == BLACK)
					drawBlack(x, y);
				else if (omok_pan[x][y] == WHITE)
					drawWhite(x, y);
			}
	}
	synchronized private void drawBoard(Graphics g) { // �������� �׸���.
		// ���ۿ� ���� �׸��� ������ �̹����� �����ǿ� �׸���.
		gbuff.clearRect(0, 0, getWidth(), getHeight());
		gbuff.drawImage(panImage, 0, 0, this);//�̹��� �߰�
		drawLine();
		drawStones();
		gbuff.setColor(Color.red);
		gbuff.drawString(info, 20, 15);
		g.drawImage(buff, 0, 0, this);
	}

} // OmokPan ���� ��