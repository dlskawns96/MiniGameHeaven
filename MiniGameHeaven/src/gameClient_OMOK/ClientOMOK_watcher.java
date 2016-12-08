package gameClient_OMOK;

<<<<<<< HEAD
public class ClientOMOK_watcher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

=======
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

import DataBase.MySQL;
import DataBase.tempDBinformation;
import gameClient_OMOK.loginOMOK.MyPanel;

import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.image.ImageProducer;


//���� Ŭ���̾�Ʈ
@SuppressWarnings("serial")
public class ClientOMOK_watcher extends ClientOMOK_player implements Runnable, ActionListener {
	
//	����������������������������Ŭ���̾�Ʈ GUI S����������������������������
	 BufferedImage img,pimg = null;
	private TextArea msgView = new TextArea("", 1, 1, 1); // �޽����� �����ִ� ����
	private TextField sendBox = new TextField(""); // ���� �޽����� ���� ����:id
	private TextField nameBox = new TextField(); // ����� �̸� ����:�α����� �� id input
	private int roomBox_player = 1; // �� ��ȣ ����

	// �濡 ������ �ο��� ���� �����ִ� ���̺�
	private Label pInfo = new Label("�����ڸ���Ʈ");

	private java.awt.List pList = new java.awt.List(); // ����� ����� �����ִ� ����Ʈ
	//private Button startButton = new Button("�뱹 ����"); // �뱹 ���� ��ư
	//private Button stopButton = new Button("���"); // ��� ��ư:������ �ٵϾ�Ŭ����ǰ� �������ӵ�
	private Button exitButton = new Button("�α��ι�ư(3��~9��)"); // ���Ƿ� ��ư

	// ���� ������ �����ִ� ���̺�: ������ ������ ������-1���� 2�� �̷� ������
	static private Label infoView = new Label("����ȭ��", 1);
	
	private OMOKPan board = new OMOKPan(); // ������ ��ü
	private BufferedReader reader; // �Է� ��Ʈ��
	public PrintWriter writer; // ��� ��Ʈ��
	private Socket CS; // ����
	private int roomNumber = -1; // �� ��ȣ: �� -1����?
	private String user_ID = null; // ����� �̸�
	private Dialog di;
	private TextArea gameResult;
	private Button closeDi;
	

 ClientOMOK_watcher() { // ������
		//super();
		setLayout(null); // ���̾ƿ��� ������� �ʴ´�. ����� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        
		// ���� ������Ʈ�� �����ϰ� ��ġ�Ѵ�.
		msgView.setEditable(false);//ä��â ��ġ��
		
		add(infoView);
		add(board);//OMOKPan ������Ʈ �߰�
		//		�α���
		Panel p = new Panel();
		

		p.setBackground(Color.lightGray);
		p.setLayout(new GridLayout(3, 3));
		Label label = new Label("�� �� ��", Label.CENTER);
		label.setBounds(38, 29, 250, 23);
		p.add(label);
		p.add(nameBox);
		board.setLocation(10,70);
		p.add(exitButton);//
		p.setBounds(500,60,250,70);

		
	
	
//		������
		Panel p2 = new Panel();
		p2.setBackground(Color.lightGray);
		p2.setLayout(new BorderLayout());
		Panel p2_1 = new Panel();
		//p2_1.add(startButton);
		//p2_1.add(stopButton);
		p2.add(pInfo, "North");
		p2.add(pList, "Center");
		p2.add(p2_1, "South");
		//startButton.setEnabled(false);
		//stopButton.setEnabled(false);//p2_1�� �߰��� ��ư 2���� ���Ұ��ϰ�
		p2.setBounds(500, 140, 250, 90);
//		ä��
		Panel p3 = new Panel();
		p3.setBackground(new Color(200, 255, 255));
		p3.setLayout(new BorderLayout());
		p3.add(msgView, "Center");
		p3.add(sendBox, "South");
		p3.setBounds(500, 240, 250, 280);

		add(p);
		add(p2);
		add(p3);

		// �̺�Ʈ �����ʸ� ����Ѵ�.
		sendBox.addActionListener(this);
		exitButton.addActionListener(this);
		//startButton.addActionListener(this);
		//stopButton.addActionListener(this);

		
		setSize(770, 550);//Client ����â ũ�� ����
		setVisible(true);
		connect();//server�� ���� ��û
		
	}//������ ��

 
public int getRoom_player()
{
	return roomBox_player;
}
 public void setFrameVisible(boolean k)
 {
	 this.setVisible(k);
 }
 public void login(int roomNum)
 {
	 try {
			goToWaitRoom();
			//startButton.setEnabled(false);
			//stopButton.setEnabled(false);
			writer.println("room_msg_fromWatcher" + roomNum);//���ȣ���� ����
			msgView.setText("");

		} catch (Exception e) {
			infoView.setText("������ �ֽ��ϴ�.");

		}
 }
 
	// ������Ʈ���� �׼� �̺�Ʈ ó��
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == sendBox) { // case1:�޽��� �Է� �����̸�(ä��)
			String msg = sendBox.getText();
			if (msg.length() == 0)
				return;
			if (msg.length() >= 30)
				msg = msg.substring(0, 30);//msg�� ���̴� 30������ ����.

			try {
				writer.println("[MSG]" + msg);
				sendBox.setText("");//sendBox �ʱ�ȭ
			} catch (Exception ie) {
			}
		}

		else if (ae.getSource() == exitButton) { // case2:�α��η� ��ư�̸�
			login(roomBox_player);
		}

		/*else if (ae.getSource() == startButton) { // case3:�غ�Ϸ� ��ư�̸�
			try {
				writer.println("[START]");
				infoView.setText("����� ������ ��ٸ��ϴ�.");
				startButton.setEnabled(false);
			} catch (Exception e) {
			}
		}

		else if (ae.getSource() == stopButton) { // ��� ��ư�̸�??????????????????????popupEnd �˾�â���
			try {
				int endResult;
				endResult=JOptionPane.showConfirmDialog(null,"retire game?");
				//msgView.append("endresult: "+endResult);
				if(endResult==0)//���
				{
					writer.println("[DROPGAME]");
					endGame("����Ͽ����ϴ�.");
				}else if(endResult==1){//�����
					writer.println("[RESTARTALL]");
					writer.println("[STOPGAME]");
					writer.println("[START]");
				}else{//endResult==2:���
					;
				}*/
				/*JFrame popupEnd = new JFrame("end game");
				popupEnd.setSize(100,200);
				JLabel endMSG=new JLabel("quit game?or retry game?");
				popupEnd.getContentPane().add(endMSG);
				JButton endGame=new JButton("quit");
				JButton restartGame=new JButton("restart");
				popupEnd.getContentPane().add(endGame);
				popupEnd.getContentPane().add(restartGame);*/
				
			/*} catch (Exception e) {
			}
		}*/
		
		
	}

 void goToWaitRoom() { // �α��� ��ư�� ������ ȣ��ȴ�: 
		if (user_ID == null) {
			String name = nameBox.getText().trim();
			if (name.length() <= 2 || name.length() > 10) {
				infoView.setText("Name Fault. 3~10");
				nameBox.requestFocus();
				return;
			}
			user_ID = name;
			writer.println("[NAME]_fromWatcher" + user_ID);
			nameBox.setText(user_ID);
			nameBox.setEditable(false);
		} else {
			msgView.setText("");
			writer.println("room_msg0");
			infoView.setText("���ӽ����� �����ּ���.");
			exitButton.setEnabled(false);
		}
	}
//	����������������������������Ŭ���̾�Ʈ GUI E����������������������������
	
//	����������������������������Ŭ���̾�Ʈ Thread S����������������������������
	public void run() {
		// ������ ���� ���� �޼����� �����Ŵ.
		String server_message;
		try {
			while ((server_message = reader.readLine()) != null) {

				if (server_message.startsWith("[STONE]")) { // ������� ���� ���� ��ǥ
					String temp = server_message.substring(7);
					int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
					int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
					board.putOpponent(x, y); // ������� ���� �׸���.
					board.setEnable(true); // ����ڰ� ���� ���� �� �ֵ��� �Ѵ�.
				}

				else if (server_message.startsWith("room_msg")) { // �濡 ����
					if (!server_message.equals("room_msg0")) { // ������ �ƴ� ���̸�
						exitButton.setEnabled(true);
						infoView.setText(server_message.substring(8) + "�� �濡 �����ϼ̽��ϴ�.");
					} else
						infoView.setText("���ǿ� �����ϼ̽��ϴ�.");

					roomNumber = Integer.parseInt(server_message.substring(8)); // �� ��ȣ ����
					
					if (board.isRunning()) { // ������ �������� �����̸�
						board.stopGame(); // ������ ������Ų��.
					}
				}

				else if (server_message.startsWith("[FULL]")) { // ���� �� �����̸�
					infoView.setText("���� ���� ������ �� �����ϴ�.");

				}

				else if (server_message.startsWith("[PLAYERS]")) { // �濡 �ִ� ����� ���
					nameList(server_message.substring(9));
				}

				else if (server_message.startsWith("[ENTER]")) { // �մ� ����

					pList.add(server_message.substring(7));
					playersInfo();
					msgView.append("[" + server_message.substring(7) + "]���� �����Ͽ����ϴ�.\n");

				} else if (server_message.startsWith("[EXIT]")) { // �մ� ����
					pList.remove(server_message.substring(6)); // ����Ʈ���� ����
					playersInfo(); // �ο����� �ٽ� ����Ͽ� �����ش�.
					msgView.append("[" + server_message.substring(6) + "]���� �ٸ� ������ �����Ͽ����ϴ�.\n");
					if (roomNumber != 0)
						endGame("��밡 �������ϴ�.");
				}

				else if (server_message.startsWith("[DISCONNECT]")) { // �մ� ���� ����
					pList.remove(server_message.substring(12));
					playersInfo();
					msgView.append("[" + server_message.substring(12) + "]���� ������ �������ϴ�.\n");
					if (roomNumber != 0)
						endGame("��밡 �������ϴ�.");
				}

				else if (server_message.startsWith("[COLOR]")) { // ���� ���� �ο��޴´�.
					String color = server_message.substring(7);
					board.startGame(color); // ������ �����Ѵ�.(�浶����, ���� ���)
					
					if (color.equals("BLACK"))
						infoView.setText("�浹�� ��ҽ��ϴ�.");
					else
						infoView.setText("�鵹�� ��ҽ��ϴ�.");
					board.setEnable(true);//
					msgView.append("���ӽ���!"+color);//
					//stopButton.setEnabled(true); // ��� ��ư Ȱ��ȭ
				}
				else if(server_message.startsWith("[RESTART]"))//�ٸ� �� ������ restartall�� ������ �� �� ������ restart�ȴ�.
				{
					String color=server_message.substring(9);
					endGame(color+"���� ���!");
					writer.println("[START]");
				}
				else if (server_message.startsWith("[DROPGAME]")) // ��밡 ����ϸ�
					endGame("���� ���!");

				else if (server_message.startsWith("[WIN]")) // �̰�����
					endGame("����� �¸�!");

				else if (server_message.startsWith("[LOSE]")) // ������
					endGame("����� �й�!");

				// ��ӵ� �޽����� �ƴϸ� �޽��� ������ �����ش�.
				else
					msgView.append(server_message + "\n");
			}
		} catch (IOException ie) {
			msgView.append(ie + "\n");
		}
		msgView.append("������ü");
	}

	private void endGame(String server_message) { // ������ �����Ű�� �޼ҵ�
		infoView.setText(server_message);
		//startButton.setEnabled(false);
		//stopButton.setEnabled(false);

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		} // 2�ʰ� ���

		if (board.isRunning())
			board.stopGame();
		if (pList.getItemCount() == 2)
			;//startButton.setEnabled(true);
	}

	private void playersInfo() { // �濡 �ִ� �������� ���� �����ش�.
		int count = pList.getItemCount();
		if (roomNumber == 0)
			pInfo.setText("������: " + count + "��");
		else {
			pInfo.setText(roomNumber + " �� ��: " + count + "��");

		}
		// �뱹 ���� ��ư�� Ȱ��ȭ ���¸� �����Ѵ�.
		if (count == 2 && roomNumber != 0)
			;//startButton.setEnabled(true);
		else
			;//startButton.setEnabled(false);
	}

	// ����� ����Ʈ���� ����ڵ��� �����Ͽ� pList�� �߰��Ѵ�.
	private void nameList(String server_message) {
		pList.removeAll();
		StringTokenizer st = new StringTokenizer(server_message, "\t");
		while (st.hasMoreElements())
			pList.add(st.nextToken());
		playersInfo();
	}

	private void connect() { // ����
		try {
			msgView.append("���� ���� ��~\n");
			CS = new Socket("127.0.0.1", 7777);
			msgView.append("���� ���� ����!\n");
			msgView.append("���̵��� �Է��ϼ���.\n");
			reader = new BufferedReader(new InputStreamReader(CS.getInputStream()));
			writer = new PrintWriter(CS.getOutputStream(), true);
			new Thread(this).start();
			board.setWriter(writer);//board�� ä��â�� outputStream ����
		} catch (Exception e) {
			msgView.append(e + "\n\n���� ����..\n");
		}
	}
>>>>>>> origin/master
}
