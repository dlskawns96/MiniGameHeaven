
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ChatServer extends JFrame {
	private ArrayList<MultiServerThread> list;
	private Socket socket;
	JTextArea ta;
	JTextField tf;
	int i = 1;
	
	public ChatServer() {
		
		setTitle("ä�� ���� ver 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ta = new JTextArea();
		add(new JScrollPane(ta));
		tf = new JTextField();
		tf.setEditable(false);
		add(tf, BorderLayout.SOUTH);
		setSize(300, 300);
		setVisible(true);
		
		list = new ArrayList<MultiServerThread>();
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			MultiServerThread mst = null;// �� ����� ����� ä�� ��ü
			boolean isStop = false; // ��� ��
			tf.setText("������ ���� �������Դϴ�.\n");
			
			while (!isStop) {
				socket = serverSocket.accept();// Ŭ���̾�Ʈ�� ���� ����
				ta.append(socket.getInetAddress()  + "����ڰ� �����߽��ϴ�" + i + "\n");
				mst = new MultiServerThread();// ä�� ��ü ����
				list.add(mst);// ArrayList�� ä�� ��ü �ϳ� ��´�.
				mst.start();// ������ ����
				i++;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		new ChatServer();
	}

	// ���� Ŭ����
	class MultiServerThread extends Thread {
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		@Override
		public void run() {
			boolean isStop = false;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				String message = null; // ä�� ������ ������ ����
				
				while (!isStop) {
					message = (String) ois.readObject();// Ŭ���̾�Ʈ �Է� �ޱ�
					String[] str = message.split("#");// ȫ�浿#�氡�氡
					
					if (str[1].equals("exit")) { // ȫ�浿#exit, �����ϰڴٴ� ��
						broadCasting(message);// ��� ����ڿ��� ���� ����
						isStop = true; // ����
					} else {
						broadCasting(message);// ��� ����ڿ��� ä�� ���� ����
					}
				} 
				list.remove(this);
				ta.append(socket.getInetAddress() + " IP �ּ��� ����ڲ��� �����ϼ̽��ϴ�.\n");
				tf.setText("���� ����� �� : " + list.size());
			} catch (Exception e) {
				list.remove(this);
				ta.append(socket.getInetAddress() + " IP �ּ��� ����ڲ��� ������ �����ϼ̽��ϴ�.\n");
				tf.setText("���� ����� �� : " + list.size());
			} 
		}

		public void broadCasting(String message) {// ��ο��� ����
			for (MultiServerThread ct : list) {
				ct.send(message);
			} 
		}

		public void send(String message) { // �� ����ڿ��� ����
			try {
				oos.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}