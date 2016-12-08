package gameClient_OMOK;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.TextField;
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
    JButton btP;
    JButton btW;
	private StartGame main;
	
	//private Panel p = new Panel();
	//private TextField nameBox = new TextField(); // 사용자 이름 상자:로그인할 때 id input
	//private Button exitButton = new Button("로그인버튼(3자~9자)"); // 대기실로 버튼
    // 생성자
    public loginOMOK(StartGame main) {
    	this.main=main;
        setTitle("start");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 사라지게만들기:뭐가 왜 사라지지?
 
        // 레이아웃 설정
        getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 400, 400);
        layeredPane.setLayout(null);
 
        // 패널1:배경(오목판 이미지)
        // 이미지 받아오기
        try {
            img = ImageIO.read(new File("startIMG_OMOK.jpg"));//start프레임 배경화면:
        } catch (IOException e) {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
         
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 400, 400);        
        

		//p.setBackground(Color.lightGray);
		//p.setLayout(new GridLayout(3, 3));
		//p.add(new Label("아 이 디", Label.CENTER));	
		//nameBox.setEditable(true);
		//p.add(nameBox);
		//p.add(exitButton);//
		//p.setBounds(500,60,250,70);

        
        // 로그인버튼 추가
        btP = new JButton(new ImageIcon("startButton.png"));//시작버튼 이미지:
        btP.setBounds(17, 176, 300, 60);
        btP.addActionListener(this);
        btW = new JButton(new ImageIcon("startButton.png"));//시작버튼 이미지:
        btW.setBounds(17, 240, 300, 60);
        btW.addActionListener(this);

        layeredPane.add(btP);//ClientOMOK_player 버튼 추가
        layeredPane.add(btW);//ClientOMOK_watcher 버튼 추가
        
        
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        //add(p);
        setVisible(true);//전체 창을 visible
    }// loginOMOK() end

 
    class MyPanel extends JPanel {//MyPanel 클래스에 로그인배경화면 입히기
        public void paint(Graphics g) {
            g.drawImage(img, 17, 5, null);
            //add(p);
        }
    }

    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		/*ClientOMOK_player newPlayer=new ClientOMOK_player();//하고 client 오브젝트 선언
		
		int roomBox_player=newPlayer.getPlayer();//loginFrame에서 로그인 할 수 있도록
		if (arg0.getSource() == exitButton) { // case2:로그인로 버튼이면
			newPlayer.login(roomBox_player);
		}
		newPlayer.setFrameVisible(true);*/
		this.dispose();//action이 수행됐을 때 이 JFrame만 종료
		
		if(arg0.getSource()==btP)
			new ClientOMOK_player();//하고 client 오브젝트 선언
		else if(arg0.getSource()==btW)
			new ClientOMOK_watcher();//watcher 오브젝트 선언
	}

}