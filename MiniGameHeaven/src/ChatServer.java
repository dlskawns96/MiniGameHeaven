
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

		setTitle("채팅 서버 ver 1.0");
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
			MultiServerThread mst = null;// 한 사용자 담당할 채팅 객체
			boolean isStop = false;
			tf.setText("서버가 정상 실행중입니다.\n");

			while (!isStop) {
				socket = serverSocket.accept();// 클라이언트별 소켓 생성
				user = new User();
				ta.append(socket.getInetAddress() + " / " + "사용자가 접속했습니다\n");

				// user.setID();
				mst = new MultiServerThread();// 채팅 객체 생성
				list.add(mst);// ArrayList에 채팅 객체 하나 담는다.
				mst.start();// 쓰레드 시작
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

	// 내부 클래스
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
				String message = null; // 채팅 내용을 저장할 변수
				String userList = null;
				while (!isStop) {
					message = in.readUTF();// 클라이언트 입력 받기
					String[] str = message.split("#");

					if (str[1].equals("exit")) {
						broadCasting(message);// 모든 사용자에게 내용 전달
						isStop = true; // 종료
					} else if (str[0].equals(("FILE"))) {
						fileTrans(str[1], socket.getInetAddress().toString());
						filePath = str[1];
						id = str[2];
					} else if (str[0].startsWith("fileSend")) { // 파일전송
						// fileSend|result
						{
							System.out.println("##파일전송##YES");
							try {
								String tmpfileServerIP = "127.0.0.1";
								String tmpfilePath = filePath;
								System.out.println(filePath);
								broadCasting("fileSender#" + tmpfilePath);
								// 파일을 전송할 클라이언트에서 서버소켓을 열고 filePath로 저장된 파일을
								// 읽어와서 OutputStream으로 출력

								// fileReceiver|ip|fileName;
								// String fileName =
								// tmpfilePath.substring(tmpfilePath.lastIndexOf("\\")+1);
								// //파일 명만 추출

								String fileName = new File(tmpfilePath).getName();
								out.writeUTF("fileReceiver#" + tmpfileServerIP + "#" + fileName);

								/* 리셋 */
								filePath = "";
								fileServerIP = "";

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else if (str[0].startsWith("req_exit")) {
						// 종료
					} else if (str[0].equals("LIST")) {
						broadCasting("RELIST#" + str[1].toString());
					} else {
						broadCasting(message);// 모든 사용자에게 채팅 내용 전달
					}
				}
				list.remove(this);
				ta.append(socket.getInetAddress() + " IP 주소의 사용자께서 종료하셨습니다.\n");
				tf.setText("남은 사용자 수 : " + list.size());
			} catch (Exception e) {
				list.remove(this);
				ta.append(socket.getInetAddress() + " IP 주소의 사용자께서 비정상 종료하셨습니다.\n");
				tf.setText("남은 사용자 수 : " + list.size());
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
					fileBroad("req_fileSend#" + "님께서 파일[" + sendFile.getName() + "] 전송을 시도합니다.\n");

				} else {
					out.writeUTF("show# 전송가능한 파일이 아닙니다. \r\n[" + availExtList + "] 확장자를 가진 파일만 전송가능합니다.");
				} // if
			} else {
				out.writeUTF("show# 존재하지 않는 파일입니다.");
			} // if
		}

		public void broadCasting(String message) {// 모두에게 전송
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

		public void send(String message) { // 한 사용자에게 전송
			try {
				out.writeUTF(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}