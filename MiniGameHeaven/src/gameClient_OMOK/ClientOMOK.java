package gameClient_OMOK;

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

import java.awt.image.ImageProducer;



//���� Ŭ���̾�Ʈ
@SuppressWarnings("serial")
public class ClientOMOK extends Frame implements Runnable, ActionListener {
	
//	����������������������������Ŭ���̾�Ʈ GUI S����������������������������
	 BufferedImage img,pimg = null;
	private TextArea msgView = new TextArea("", 1, 1, 1); // �޽����� �����ִ� ����
	private TextField sendBox = new TextField(""); // ���� �޽����� ���� ����:id
	private TextField nameBox = new TextField(); // ����� �̸� ����:userlist
	private int roomBox = 1; // �� ��ȣ ����

	// �濡 ������ �ο��� ���� �����ִ� ���̺�
	private Label pInfo = new Label("����� ���");

	private java.awt.List pList = new java.awt.List(); // ����� ����� �����ִ� ����Ʈ
	private Button startButton = new Button("�غ�Ϸ�"); // �뱹 ���� ��ư
	private Button stopButton = new Button("���"); // ��� ��ư:������ �ٵϾ�Ŭ����ǰ� �������ӵ�
	private Button exitButton = new Button("�г��� ���"); // ���Ƿ� ��ư

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
	

 ClientOMOK() { // ������
		super();//?clientOMOK�� super�� ������?JFrame??
		setLayout(null); // ���̾ƿ��� ������� �ʴ´�. ����� ����
		
		add(infoView);
		add(board);
		
		board.setLocation(10,70);
		
		// ������ �ݱ� ó��: �������� ������ ����?
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
	}//������ ��

 
	// ������Ʈ���� �׼� �̺�Ʈ ó��
	/*public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == sendBox) { // �޽��� �Է� �����̸�
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

		else if (ae.getSource() == exitButton) { // �α��η� ��ư�̸�
			try {
				goToWaitRoom();
				startButton.setEnabled(false);
				stopButton.setEnabled(false);
				writer.println("room_msg" + roomBox);
				msgView.setText("");

			} catch (Exception e) {
				infoView.setText("������ �ҽ��ϴ�.");

			}
		}

		else if (ae.getSource() == startButton) { // �뱹 ���� ��ư�̸�
			try {
				writer.println("[START]");
				infoView.setText("����� ������ ��ٸ��ϴ�.");
				startButton.setEnabled(false);
			} catch (Exception e) {
			}
		}

		else if (ae.getSource() == stopButton) { // ��� ��ư�̸�
			try {
				writer.println("[DROPGAME]");
				endGame("����Ͽ����ϴ�.");
			} catch (Exception e) {
			}
		}
	}*/

/* void goToWaitRoom() { // �α��η� ��ư�� ������ ȣ��ȴ�.
		if (user_ID == null) {
			String name = nameBox.getText().trim();
			if (name.length() <= 2 || name.length() > 10) {
				infoView.setText("Name Fault. 3~10");
				nameBox.requestFocus();
				return;
			}
			user_ID = name;
			writer.println("[NAME]" + user_ID);
			nameBox.setText(user_ID);
			nameBox.setEditable(false);
		} else {
			msgView.setText("");
			writer.println("room_msg0");
			infoView.setText("���ӽ����� �����ּ���.");
			exitButton.setEnabled(false);
		}
	}*/
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
					board.startGame(color); // ������ �����Ѵ�.

					if (color.equals("BLACK"))
						infoView.setText("�浹�� ��ҽ��ϴ�.");
					else
						infoView.setText("�鵹�� ��ҽ��ϴ�.");
					stopButton.setEnabled(true); // ��� ��ư Ȱ��ȭ
				}

				else if (server_message.startsWith("[DROPGAME]")) // ��밡 ����ϸ�
					endGame("���� ���!");

				else if (server_message.startsWith("[WIN]")) // �̰�����
					endGame("����� �¸�!");

				else if (server_message.startsWith("[LOSE]")) // ������
					endGame("����� �й�!");

				// ��ӵ� �޽����� �ƴϸ� �޽��� ������ �����ش�.
				else
					msgView.append(server_message + "\n");			}
		} catch (IOException ie) {
			msgView.append(ie + "\n");
		}
		msgView.append("������ü");
	}

	private void endGame(String server_message) { // ������ �����Ű�� �޼ҵ�
		infoView.setText(server_message);
		startButton.setEnabled(false);
		stopButton.setEnabled(false);

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		} // 2�ʰ� ���

		if (board.isRunning())
			board.stopGame();
		if (pList.getItemCount() == 2)
			startButton.setEnabled(true);
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
			startButton.setEnabled(true);
		else
			startButton.setEnabled(false);
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
			//msgView.append("���̵��� �Է��ϼ���.\n");
			reader = new BufferedReader(new InputStreamReader(CS.getInputStream()));
			writer = new PrintWriter(CS.getOutputStream(), true);
			new Thread(this).start();
			board.setWriter(writer);
		} catch (Exception e) {
			msgView.append(e + "\n\n���� ����..\n");
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}