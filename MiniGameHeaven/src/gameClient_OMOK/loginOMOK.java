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
    JTextField loginTextField;//text�� password�� LoginFrame���� ��� ������??
    JPasswordField passwordField;
    JButton btP;
    JButton btW;
	private StartGame main;
	
	//private Panel p = new Panel();
	//private TextField nameBox = new TextField(); // ����� �̸� ����:�α����� �� id input
	//private Button exitButton = new Button("�α��ι�ư(3��~9��)"); // ���Ƿ� ��ư
    // ������
    public loginOMOK(StartGame main) {
    	this.main=main;
        setTitle("start");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// ������Ը����:���� �� �������?
 
        // ���̾ƿ� ����
        getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 400, 400);
        layeredPane.setLayout(null);
 
        // �г�1:���(������ �̹���)
        // �̹��� �޾ƿ���
        try {
            img = ImageIO.read(new File("startIMG_OMOK.jpg"));//start������ ���ȭ��:
        } catch (IOException e) {
            System.out.println("�̹��� �ҷ����� ����");
            System.exit(0);
        }
         
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 400, 400);        
        

		//p.setBackground(Color.lightGray);
		//p.setLayout(new GridLayout(3, 3));
		//p.add(new Label("�� �� ��", Label.CENTER));	
		//nameBox.setEditable(true);
		//p.add(nameBox);
		//p.add(exitButton);//
		//p.setBounds(500,60,250,70);

        
        // �α��ι�ư �߰�
        btP = new JButton(new ImageIcon("startButton.png"));//���۹�ư �̹���:
        btP.setBounds(17, 176, 300, 60);
        btP.addActionListener(this);
        btW = new JButton(new ImageIcon("startButton.png"));//���۹�ư �̹���:
        btW.setBounds(17, 240, 300, 60);
        btW.addActionListener(this);

        layeredPane.add(btP);//ClientOMOK_player ��ư �߰�
        layeredPane.add(btW);//ClientOMOK_watcher ��ư �߰�
        
        
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        //add(p);
        setVisible(true);//��ü â�� visible
    }// loginOMOK() end

 
    class MyPanel extends JPanel {//MyPanel Ŭ������ �α��ι��ȭ�� ������
        public void paint(Graphics g) {
            g.drawImage(img, 17, 5, null);
            //add(p);
        }
    }

    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		/*ClientOMOK_player newPlayer=new ClientOMOK_player();//�ϰ� client ������Ʈ ����
		
		int roomBox_player=newPlayer.getPlayer();//loginFrame���� �α��� �� �� �ֵ���
		if (arg0.getSource() == exitButton) { // case2:�α��η� ��ư�̸�
			newPlayer.login(roomBox_player);
		}
		newPlayer.setFrameVisible(true);*/
		this.dispose();//action�� ������� �� �� JFrame�� ����
		
		if(arg0.getSource()==btP)
			new ClientOMOK_player();//�ϰ� client ������Ʈ ����
		else if(arg0.getSource()==btW)
			new ClientOMOK_watcher();//watcher ������Ʈ ����
	}

}