package gameClient_OMOK;

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
class OMOKPan extends Canvas {
	// ����� ���� ���
	public static final int BLACK = 1, WHITE = -1;

	private int[][] omok_pan; // ������ 2���� �迭
	private int omok_pan_size; // omok_pan_size�� ������ ���� �Ǵ� ���� ����, 15�� ���Ѵ�.
	private int cell; // ������ ũ��(pixel)
	private String info = "���� �α��� �����..."; // ������ ���� ��Ȳ�� ��Ÿ���� ���ڿ�:ó���� �����(������ ���� ����)
	private int color = BLACK; // ������� �� ����

	// true�̸� ����ڰ� ���� ���� �� �ִ� ���¸� �ǹ��ϰ�,
	// false�̸� ����ڰ� ���� ���� �� ���� ���¸� �ǹ��Ѵ�.
	private boolean enable = false;
	private boolean running = false; // ������ ���� ���ΰ��� ��Ÿ���� ����
	private PrintWriter writer; // ������� �޽����� �����ϱ� ���� ��Ʈ��
	private Graphics gbuff; // ĵ������ ���۸� ���� �׷��Ƚ� ��ü
	private Image buff; // ���� ���۸��� ���� ����
	
	private Toolkit myToolkit = Toolkit.getDefaultToolkit();
	private Image panImage = myToolkit.getImage("OMOK_BGboard.jpg");

	OMOKPan() {
		this.omok_pan_size = 15;
		this.cell = 30;

		omok_pan = new int[omok_pan_size + 2][]; // ���� ũ�⸦ ���Ѵ�.
		for (int i = 0; i < omok_pan.length; i++)
			omok_pan[i] = new int[omok_pan_size + 2];
		//������ ĵ������ ũ�� ����
		setSize(470,470);																								
		// �������� ���콺 �̺�Ʈ ó��
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) { // ���콺�� ������
				if (!enable)
					return; // ����ڰ� ���� �� ���� �����̸� ���� ���´�.

				// ���콺�� ��ǥ�� omok_pan ��ǥ�� ����Ѵ�.
				int x = (int) Math.round(me.getX() / (double) cell);
				int y = (int) Math.round(me.getY() / (double) cell);

				// �������� ���ڸ��� �ƴϸ� ����
				if (x == 0 || y == 0 || x == omok_pan_size + 1 || y == omok_pan_size + 1)
					return;

				// �ش� �� �ڸ��� ���� �ִٸ� ����
				if (omok_pan[x][y] == BLACK || omok_pan[x][y] == WHITE)
					return;

				// ������� ���� ���� ��ǥ�� �����Ѵ�.
				writer.println("[STONE]" + x + " " + y);

				omok_pan[x][y] = color;

				// �̰���� �˻��Ѵ�.
				if (check(new Point(x, y), color)) {
					info = "�̰���ϴ�.";
					writer.println("[WIN]");
				}

				else
					info = "��밡 �α⸦ ��ٸ��ϴ�.";
				repaint(); // �������� �׸���.

				// ����ڰ� �� �� ���� ���·� �����.
				// ������� �θ� enable�� true�� �Ǿ� ����ڰ� �� �� �ְ� �ȴ�.
				enable = false;
			}
		});
	}

	public boolean isRunning() { // ������ ���� ���¸� ��ȯ�Ѵ�.
		return running;
	}

	public void startGame(String col) { // ������ �����Ѵ�.

		running = true;
		if (col.equals("BLACK")) { // ���� ���õǾ��� ��
			enable = true;
			color = BLACK;
			info = "���� ����! ����� �����Դϴ�.";
		} else { // ���� ���õǾ��� ��
			enable = false;
			color = WHITE;
			info = "���� ����! ������ �����Դϴ�.";
		}
	}

	public void stopGame() { // ������ �����.
		reset(); // �������� �ʱ�ȭ�Ѵ�.
		writer.println("[STOPGAME]"); // ������� �޽����� ������.
		enable = false;
		running = false;
	}

	public void putOpponent(int x, int y) { // ������� ���� ���´�.
		omok_pan[x][y] = -color;
		info = "��밡 �ξ����ϴ�. �μ���.";
		repaint();
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
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

	private void drawWhite(int x, int y) { // �� ���� (x, y)�� �׸���.
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

	private boolean check(Point p, int col) {
		if (count(p, 1, 0, col) + count(p, -1, 0, col) == 4)
			return true;
		if (count(p, 0, 1, col) + count(p, 0, -1, col) == 4)
			return true;
		if (count(p, -1, -1, col) + count(p, 1, 1, col) == 4)
			return true;
		if (count(p, 1, -1, col) + count(p, -1, 1, col) == 4)
			return true;
		return false;
	}

	private int count(Point p, int dx, int dy, int col) {
		int i = 0;
		for (; omok_pan[p.x + (i + 1) * dx][p.y + (i + 1) * dy] == col; i++)
			;
		return i;
	}
} // OmokPan ���� ��