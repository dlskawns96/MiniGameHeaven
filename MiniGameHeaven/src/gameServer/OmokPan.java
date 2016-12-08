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
// 오목판클래스
class OmokPan extends Canvas {
	// 사용자 돌의 흑백
	public static final int BLACK = 1, WHITE = -1;

	private int[][] omok_pan; // 오목판 2차원 배열
	private int omok_pan_size; // omok_pan_size는 격자의 가로 또는 세로 개수, 15로 정한다.
	private int cell; // 격자의 크기(pixel)
	private String info = "게임 중지"; // 게임의 진행 상황을 나타내는 문자열:좌측 상단에 빨간글씨
	private int color = BLACK; // 사용자의 돌 색깔

	// true이면 사용자가 돌을 놓을 수 있는 상태를 의미하고,
	// false이면 사용자가 돌을 놓을 수 없는 상태를 의미한다.
	private boolean running = false; // 게임이 진행 중인가를 나타내는 변수

	private Graphics gbuff; // 캔버스와 버퍼를 위한 그래픽스 객체:보류 나중에 이해하자
	private Image buff; // 더블 버퍼링을 위한 버퍼
	
	private Toolkit myToolkit = Toolkit.getDefaultToolkit();
<<<<<<< HEAD
	private Image panImage = myToolkit.getImage("omokpan.png");
=======
	private Image panImage = myToolkit.getImage("OMOK_BGboard.jpg");
>>>>>>> origin/master

	OmokPan() {
		this.omok_pan_size = 15;
		this.cell = 30;

		omok_pan = new int[omok_pan_size + 2][]; // 맵의 크기를 정한다.
		for (int i = 0; i < omok_pan.length; i++)
			omok_pan[i] = new int[omok_pan_size + 2];
		//오목판 캔버스의 크기 지정
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
	
	public boolean isRunning() { // 게임의 진행 상태를 반환한다.
		return running;
	}

	public void update(Graphics g) { // repaint를 호출하면 자동으로 호출된다.
		paint(g); // paint를 호출한다.
	}

	public void paint(Graphics g) { // 화면을 그린다.
		if (gbuff == null) { // 버퍼가 없으면 버퍼를 만든다.
			buff = createImage(getWidth(), getHeight());
			gbuff = buff.getGraphics();
		}
		drawBoard(g); // 오목판을 그린다.
	}

	public void reset() { // 오목판을 초기화시킨다.
		for (int i = 0; i < omok_pan.length; i++)
			for (int j = 0; j < omok_pan[i].length; j++)
				omok_pan[i][j] = 0;
		info = "게임 중지";
		repaint();
	}

	private void drawLine() { // 오목판에 선을 긋는다.
		gbuff.setColor(Color.black);
		for (int i = 1; i <= omok_pan_size; i++) {
			gbuff.drawLine(cell, i * cell, cell * omok_pan_size, i * cell);
			gbuff.drawLine(i * cell, cell, i * cell, cell * omok_pan_size);
		}
	}

	private void drawBlack(int x, int y) { // 흑 돌을 (x, y)에 그린다.
		Graphics2D gbuff = (Graphics2D) this.gbuff;
		gbuff.setColor(Color.black);
		gbuff.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
		gbuff.setColor(Color.white);
		gbuff.drawOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
	}

	public void drawWhite(int x, int y) { // 백 돌을 (x, y)에 그린다.
		gbuff.setColor(Color.white);
		gbuff.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
		gbuff.setColor(Color.black);
		gbuff.drawOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
	}

	private void drawStones() { // omok_pan 놓여진 돌들을 모두 그린다.
		for (int x = 1; x <= omok_pan_size; x++)
			for (int y = 1; y <= omok_pan_size; y++) {
				if (omok_pan[x][y] == BLACK)
					drawBlack(x, y);
				else if (omok_pan[x][y] == WHITE)
					drawWhite(x, y);
			}
	}
	synchronized private void drawBoard(Graphics g) { // 오목판을 그린다.
		// 버퍼에 먼저 그리고 버퍼의 이미지를 오목판에 그린다.
		gbuff.clearRect(0, 0, getWidth(), getHeight());
		gbuff.drawImage(panImage, 0, 0, this);//이미지 추가
		drawLine();
		drawStones();
		gbuff.setColor(Color.red);
		gbuff.drawString(info, 20, 15);
		g.drawImage(buff, 0, 0, this);
	}

} // OmokPan 정의 끝