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
    JTextField loginTextField;//text�� password�� LoginFrame���� ��� ������??
    JPasswordField passwordField;
    JButton bt;
	private StartGame main;
 
    // ������
    public LoginFrame(StartGame main) {
    	this.main=main;
        setTitle("start");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// ������Ը����:���� �� �������?
 
        // ���̾ƿ� ����
        setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 400, 400);
        layeredPane.setLayout(null);
 
        // �г�1:���(������ �̹���)
        // �̹��� �޾ƿ���
        try {
            img = ImageIO.read(new File("img/login.png"));
        } catch (IOException e) {
            System.out.println("�̹��� �ҷ����� ����");
            System.exit(0);
        }
         
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 400, 400);        
 
        // �α��ι�ư �߰�
        bt = new JButton(new ImageIcon("img/start_btn.jpg"));
        bt.setBounds(17, 240, 300, 60);
        bt.addActionListener(this);

        layeredPane.add(bt);
 
        
        layeredPane.add(panel);
        add(layeredPane);
        setVisible(true);//��ü â�� visible
    }// LoginFrame() end

 
    class MyPanel extends JPanel {//MyPanel Ŭ������ �α��ι��ȭ�� ������
        public void paint(Graphics g) {
            g.drawImage(img, 17, 5, null);
        }
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.dispose();//action�� ������� �� �� JFrame�� ����
			
		new client();//�ϰ� client ������Ʈ ����
	}

}