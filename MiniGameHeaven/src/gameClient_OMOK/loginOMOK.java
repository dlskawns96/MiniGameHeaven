package gameClient_OMOK;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
 
public class loginOMOK extends JFrame implements ActionListener {
    BufferedImage img = null;
    JTextField loginTextField;//text랑 password는 LoginFrame에서 어디서 쓰이지??
    JPasswordField passwordField;
    JButton bt;
	private StartGame main;
 
    // 생성자
    public LoginFrame(StartGame main) {
    	this.main=main;
        setTitle("start");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 사라지게만들기:뭐가 왜 사라지지?
 
        // 레이아웃 설정
        setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 400, 400);
        layeredPane.setLayout(null);
 
        // 패널1:배경(오목판 이미지)
        // 이미지 받아오기
        try {
            img = ImageIO.read(new File("img/login.png"));
        } catch (IOException e) {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
         
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 400, 400);        
 
        // 로그인버튼 추가
        bt = new JButton(new ImageIcon("img/start_btn.jpg"));
        bt.setBounds(17, 240, 300, 60);
        bt.addActionListener(this);

        layeredPane.add(bt);
 
        
        layeredPane.add(panel);
        add(layeredPane);
        setVisible(true);//전체 창을 visible
    }// LoginFrame() end

 
    class MyPanel extends JPanel {//MyPanel 클래스에 로그인배경화면 입히기
        public void paint(Graphics g) {
            g.drawImage(img, 17, 5, null);
        }
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.dispose();//action이 수행됐을 때 이 JFrame만 종료
			
		new client();//하고 client 오브젝트 선언
	}

}