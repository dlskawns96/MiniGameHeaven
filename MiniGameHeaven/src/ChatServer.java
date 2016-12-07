
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
	private User user;
	public UserList us;
	ChatServer ch = this;
	String id;

	public ChatServer(Thread lcThread) {

		us = new UserList();
		lcThread.start();

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
			ServerSocket serverSocket = new ServerSocket(9003);
			MultiServerThread mst = null;// �� ����� ����� ä�� ��ü
			boolean isStop = false;
			tf.setText("������ ���� �������Դϴ�.\n");

			while (!isStop) {
				socket = serverSocket.accept();// Ŭ���̾�Ʈ�� ���� ����
				user = new User();
				ta.append(socket.getInetAddress() + " / " + "����ڰ� �����߽��ϴ�\n");

				// user.setID();
				mst = new MultiServerThread();// ä�� ��ü ����
				list.add(mst);// ArrayList�� ä�� ��ü �ϳ� ��´�.
				mst.start();// ������ ����
				// reList();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// new ChatServer();
	}

	// ���� Ŭ����
	class MultiServerThread extends Thread {
		private DataInputStream in;
		private DataOutputStream out;
		private String filePath;
		private String fileServerIP;

		@Override
		public void run() {
			boolean isStop = false;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				String message = null; // ä�� ������ ������ ����
				String userList = null;
				while (!isStop) {
					message = in.readUTF();// Ŭ���̾�Ʈ �Է� �ޱ�
					String[] str = message.split("#");

					if (str[1].equals("exit")) {
						broadCasting(message);// ��� ����ڿ��� ���� ����
						isStop = true; // ����
					} else if (str[0].equals(("FILE"))) {
						fileTrans(str[1], socket.getInetAddress().toString());
						filePath = str[1];
						id = str[2];
					} else if (str[0].startsWith("fileSend")) { // ��������
						// fileSend|result
						{
							System.out.println("##��������##YES");
							try {
								String tmpfileServerIP = "127.0.0.1";
								String tmpfilePath = filePath;
								System.out.println(filePath);
								broadCasting("fileSender#" + tmpfilePath);
								// ������ ������ Ŭ���̾�Ʈ���� ���������� ���� filePath�� ����� ������
								// �о�ͼ� OutputStream���� ���

								// fileReceiver|ip|fileName;
								// String fileName =
								// tmpfilePath.substring(tmpfilePath.lastIndexOf("\\")+1);
								// //���� �� ����

								String fileName = new File(tmpfilePath).getName();
								out.writeUTF("fileReceiver#" + tmpfileServerIP + "#" + fileName);

								/* ���� */
								filePath = "";
								fileServerIP = "";

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else if (str[0].startsWith("req_exit")) {
						// ����
					} else if (str[0].equals("LIST")) {
						broadCasting("RELIST#" + str[1].toString());
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

		public void fileTrans(String str, String ip) throws IOException {
			System.out.println(str);
			filePath = str;
			File sendFile = new File(filePath);
			String availExtList = "txt,java,jpeg,jpg,png,gif,bmp";

			if (sendFile.isFile()) {
				String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
				if (availExtList.contains(fileExt)) {
					fileServerIP = ip;
					fileBroad("req_fileSend#" + "�Բ��� ����[" + sendFile.getName() + "] ������ �õ��մϴ�.\n");

				} else {
					out.writeUTF("show# ���۰����� ������ �ƴմϴ�. \r\n[" + availExtList + "] Ȯ���ڸ� ���� ���ϸ� ���۰����մϴ�.");
				} // if
			} else {
				out.writeUTF("show# �������� �ʴ� �����Դϴ�.");
			} // if
		}

		public void broadCasting(String message) {// ��ο��� ����
			for (MultiServerThread ct : list) {
				ct.send(message);
			}
		}

		public void fileBroad(String msg) {
			for (MultiServerThread ct : list) {
				if (id.equals(ct.getId()) == false)
					ct.send(msg);
			}
		}

		public void send(String message) { // �� ����ڿ��� ����
			try {
				out.writeUTF(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}