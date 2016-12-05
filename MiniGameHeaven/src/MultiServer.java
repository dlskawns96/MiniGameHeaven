import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/*�ܼ� ��Ƽä�� ���� ���α׷�*/
public class MultiServer {
	HashMap<String, HashMap<String, ServerRecThread>> globalMap; // ���Ӻ� �ؽ�����
																	// �����ϴ� �ؽø�
	ServerSocket serverSocket = null;
	Socket socket = null;
	static int connUserCount = 0; // ������ ���ӵ� ���� ī��Ʈ

	// ������
	public MultiServer() {
		globalMap = new HashMap<String, HashMap<String, ServerRecThread>>();

		// clientMap = new HashMap<String,DataOutputStream>(); //Ŭ���̾�Ʈ�� ��½�Ʈ����
		// ������ �ؽ��� ����.
		Collections.synchronizedMap(globalMap); // �ؽ��� ����ȭ ����.

		HashMap<String, ServerRecThread> group01 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group01); // �ؽ��� ����ȭ ����.

		HashMap<String, ServerRecThread> group02 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group02); // �ؽ��� ����ȭ ����.

		HashMap<String, ServerRecThread> group03 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group03); // �ؽ��� ����ȭ ����.

		HashMap<String, ServerRecThread> group04 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group04); // �ؽ��� ����ȭ ����.

		HashMap<String, ServerRecThread> group05 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group05); // �ؽ��� ����ȭ ����.

		HashMap<String, ServerRecThread> group06 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group06); // �ؽ��� ����ȭ ����.

		HashMap<String, ServerRecThread> group07 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group07); // �ؽ��� ����ȭ ����.

		globalMap.put("����", group01);
		globalMap.put("����1", group02);
		globalMap.put("����2", group03);
		globalMap.put("����3", group04);
		globalMap.put("���Ǿ�1", group05);
		globalMap.put("���Ǿ�2", group06);
		globalMap.put("���Ǿ�3", group07);

	}// ������----

	public void init() {
		try {
			serverSocket = new ServerSocket(9995);
			System.out.println("##������ ���۵Ǿ����ϴ�.");

			while (true) {
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + ":" + socket.getPort());
				Thread msr = new ServerRecThread(socket); // ä�������� ���� ������ ����.
				msr.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ���� ���� ä�÷� **/
	public void sendAllMsg(String msg) {

		Iterator global_it = globalMap.keySet().iterator();

		while (global_it.hasNext()) {
			try {
				HashMap<String, ServerRecThread> it_hash = globalMap.get(global_it.next());
				Iterator it = it_hash.keySet().iterator();
				while (it.hasNext()) {
					ServerRecThread st = it_hash.get(it.next());
					st.out.writeUTF(msg);
				}
			} catch (Exception e) {
				System.out.println("����:" + e);
			}
		}
	}// sendAllMsg()-----------

	/** �ش� Ŭ���̾�Ʈ�� �����ִ� ���ӹ濡 ���ؼ��� �޽��� ����. **/
	public void sendGroupMsg(String loc, String msg) {

		HashMap<String, ServerRecThread> gMap = globalMap.get(loc);
		Iterator<String> group_it = globalMap.get(loc).keySet().iterator();
		while (group_it.hasNext()) {
			try {
				ServerRecThread st = gMap.get(group_it.next());
				if (!st.chatMode) { // 1:1��ȭ��尡 �ƴ� ������Ը�.
					st.out.writeUTF(msg);
				}
			} catch (Exception e) {
				System.out.println("����:" + e);
			}
		}
	}// sendGroupMsg()-----------

	/** 1:1 ��ȭ */
	public void sendPvPMsg(String loc, String fromName, String toName, String msg) {

		try {
			globalMap.get(loc).get(toName).out.writeUTF(msg);
			globalMap.get(loc).get(fromName).out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}// sendPvPMsg()-----------

	/** �ӼӸ� */
	public void sendToMsg(String loc, String fromName, String toName, String msg) {

		try {
			globalMap.get(loc).get(toName).out.writeUTF("whisper|" + fromName + "|" + msg);
			globalMap.get(loc).get(fromName).out.writeUTF("whisper|" + fromName + "|" + msg);
		} catch (Exception e) {
			System.out.println("����:" + e);
		}

	}// sendAllMsg()-----------

	/**
	 * �� ���ӹ��� �����ڼ��� ������ ���ӵ� ������ ��ȯ �ϴ� �޼ҵ�
	 **/
	public String getEachMapSize() {
		return getEachMapSize(null);
	}// getEachMapSize()-----------

	/**
	 * �� ���ӹ��� �����ڼ��� ������ ���ӵ� ������ ��ȯ �ϴ� �޼ҵ� �߰� ������ ���޹����� �ش� ������ üũ
	 */
	public String getEachMapSize(String loc) {

		Iterator global_it = globalMap.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		int sum = 0;
		sb.append("=== �׷� ��� ===" + System.getProperty("line.separator"));
		while (global_it.hasNext()) {
			try {
				String key = (String) global_it.next();

				HashMap<String, ServerRecThread> it_hash = globalMap.get(key);
				// if(key.equals(loc)) key+="(*)"; //���� ������ ���ӵ� �� ǥ��
				int size = it_hash.size();
				sum += size;
				sb.append(key + ": (" + size + "��)" + (key.equals(loc) ? "(*)" : "") + "\r\n");

			} catch (Exception e) {
				System.out.println("����:" + e);
			}
		}
		// sb.append("������ ��ȭ�� �����ϰ��ִ� ������ :"+ MultiServer.connUserCount);
		sb.append("������ ��ȭ�� �����ϰ��ִ� ������ :" + sum + "�� \r\n");
		return sb.toString();
	}// getEachMapSize()-----------

	/** ���ӵ� ���� �ߺ�üũ */
	public boolean isNameGlobla(String name) {
		boolean result = false;
		Iterator<String> global_it = globalMap.keySet().iterator();
		while (global_it.hasNext()) {
			try {
				String key = global_it.next();
				HashMap<String, ServerRecThread> it_hash = globalMap.get(key);
				if (it_hash.containsKey(name)) {
					result = true; // �ߺ��� ���̵� ����.
					break;
				}

			} catch (Exception e) {
				System.out.println("isNameGlobla()����:" + e);
			}
		}

		return result;
	}// isNameGlobla()-----------

	/** ���ڿ� null �� �� "" �� ��ü ���ڿ��� ���԰���. */
	public String nVL(String str, String replace) {
		String output = "";
		if (str == null || str.trim().equals("")) {
			output = replace;
		} else {
			output = str;
		}
		return output;
	}

	// main�޼���
	public static void main(String[] args) {
		MultiServer ms = new MultiServer(); // ������ü ����.
		ms.init();// ����.
	}// main()------

	class ServerRecThread extends Thread {

		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		String id = ""; // ���̵� ����
		String loc = ""; // ���ӹ� ����
		String toNameTmp = null;// 1:1��ȭ ���
		String fileServerIP; // ���ϼ��� ������ ����
		String filePath; // ���� �������� ������ ���� �н� ����.
		boolean chatMode; // 1:1��ȭ��� ����

		// ������.
		public ServerRecThread(Socket socket) {
			this.socket = socket;
			try {
				// Socket���κ��� �Է½�Ʈ���� ��´�.
				in = new DataInputStream(socket.getInputStream());
				// Socket���κ��� ��½�Ʈ���� ��´�.
				out = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				System.out.println("ServerRecThread ������ ����:" + e);
			}
		}// ������ ------------

		/** ���ӵ� ��������Ʈ ���ڿ��� ��ȯ */
		public String showUserList() {

			StringBuilder output = new StringBuilder("==�����ڸ��==\r\n");
			Iterator it = globalMap.get(loc).keySet().iterator(); // �ؽ��ʿ� ��ϵ�
																	// ������̸���
																	// ������.
			while (it.hasNext()) { // �ݺ��ϸ鼭 ������̸��� StringBuilder�� �߰�
				try {
					String key = (String) it.next();
					// out.writeUTF(output);
					if (key.equals(id)) { // �������� üũ
						key += " (*) ";
					}

					output.append(key + "\r\n");
				} catch (Exception e) {
					System.out.println("����:" + e);
				}
			} // while---------
			output.append("==" + globalMap.get(loc).size() + "�� ������==\r\n");
			System.out.println(output.toString());
			return output.toString();
		}// showUserList()-----------

		/** �޽��� �ļ� */
		public String[] getMsgParse(String msg) {
			System.out.println("msgParse():msg?   " + msg);
			String[] tmpArr = msg.split("[|]");
			return tmpArr;
		}

		@Override
		public void run() { 
			HashMap<String, ServerRecThread> clientMap = null; 

			try {
				while (in != null) { 
					String msg = in.readUTF(); 
					String[] msgArr = getMsgParse(msg.substring(msg.indexOf("|") + 1));

					// �޼��� ó�� ----------------------------------------------
					if (msg.startsWith("req_logon")) { 
						if (!(msgArr[0].trim().equals("")) && !isNameGlobla(msgArr[0])) {
							id = msgArr[0]; 
							MultiServer.connUserCount++; // �����ڼ� ����. 
							out.writeUTF("logon#yes|" + getEachMapSize()); 
							
						} else {
							out.writeUTF("logon#no|err01");
						}

					} else if (msg.startsWith("req_enterRoom")) { 
						loc = msgArr[1]; 

						if (isNameGlobla(msgArr[0])) {
							out.writeUTF("logon#no|" + id);

						} else if (globalMap.containsKey(loc)) {
							sendGroupMsg(loc, "show|[##] " + id + "���� �����ϼ̽��ϴ�.");
							clientMap = globalMap.get(loc);
							clientMap.put(id, this); 
							System.out.println(getEachMapSize()); 
							out.writeUTF("enterRoom#yes|" + loc); 

						} else {
							out.writeUTF("enterRoom#no|" + loc);
						}

					} else if (msg.startsWith("req_cmdMsg")) { 
						if (msgArr[1].trim().equals("/������")) {
							out.writeUTF("show|" + showUserList()); 
						} else if (msgArr[1].trim().startsWith("/�ӼӸ�")) {
						
							String[] msgSubArr = msgArr[1].split(" ", 3);

							if (msgSubArr == null || msgSubArr.length < 3) {
								out.writeUTF("show|[##] �ӼӸ� ������ �߸��Ǿ����ϴ�.\r\n usage : /�ӼӸ� [�����̸�] [�����޽���].");
							} else if (id.equals(msgSubArr[1])) {
								out.writeUTF("show|[##] �ڽſ��� �ӼӸ��� �Ҽ������ϴ�.\r\n usage : /�ӼӸ� [�����̸�] [�����޽���].");
							} else {
								String toName = msgSubArr[1];
								String toMsg = msgSubArr[2];
								if (clientMap.containsKey(toName)) { // ����üũ
									System.out.println("�ӼӸ�!");
									sendToMsg(loc, id, toName, toMsg);

								} else {
									out.writeUTF("show|[##] �ش� ������ �������� �ʽ��ϴ�.");
								}

							} // if

						} else if (msgArr[1].trim().startsWith("/����")) {

							String[] msgSubArr = msg.split(" ");
							if (msgSubArr.length == 1) { 
								out.writeUTF("show|" + getEachMapSize(loc));
							} else if (msgSubArr.length == 2) {
								String tmpLoc = msgSubArr[1]; 

								if (loc.equals(tmpLoc)) {
									out.writeUTF("show|[##] ��ɾ� ������ �߸��Ǿ����ϴ�.\r\n ������ �����ϰ� �ִ� ���ӹ��� �����ϽǼ������ϴ�.\r\n "
											+ "usage : ������� ���� : /����" + "\r\n usage : ���ӹ� ���� �ϱ� : /���� [�����������̸�].");
									continue;
								}

								if (globalMap.containsKey(tmpLoc) && !this.chatMode) { // ����üũ
									out.writeUTF("show|[##] ������ " + loc + "���� " + tmpLoc + "�� �����մϴ�. ");

									clientMap.remove(id); // ���� ���� �ؽ��ʿ��� �ش�
															// �����带 ����.
									sendGroupMsg(loc, "show|[##] " + id + "���� �����ϼ̽��ϴ�.");

									System.out.println("��������(" + loc + ")���� ���� " + id + "����");
									loc = tmpLoc;
									clientMap = globalMap.get(loc);
									sendGroupMsg(loc, "show|[##] " + id + "���� �����ϼ̽��ϴ�.");
									clientMap.put(id, this); // ���κ���� ������
																// ���������� ����.

								} else {
									out.writeUTF("##�Է��� ������ �������� �ʰų� ���� �̵��Ҽ����� �����Դϴ�.");
								} // if-----

							} else {
								out.writeUTF("show|[##] ��ɾ� ������ �߸��Ǿ����ϴ�.\r\n " + "usage : ������� ���� : /����"
										+ "\r\n usage : �������� �ϱ� : /���� [�����������̸�].");

							} // if---------

						} else if (msgArr[1].trim().startsWith("/��ȭ��û")) {
							String[] msgSubArr = msgArr[1].split(" ", 2);

							if (msgSubArr.length != 2) {
								out.writeUTF("show|[##] ��ɾ� ������ �߸��Ǿ����ϴ�.\r\n " + "usage : 1:1��ȭ��û�ϱ� : /��ȭ��û [�����ȭ��]");
								continue;
							} else if (id.equals(msgSubArr[1])) {
								out.writeUTF(
										"show|[##] ��ɾ� ������ �߸��Ǿ����ϴ�.\r\n ������ ��ȭ���� �����ϽǼ������ϴ�.1:1��ȭ�� �� ������ ��ȭ���� �������ּ���.\r\n "
												+ "usage : 1:1��ȭ��û�ϱ� : /��ȭ��û [�����ȭ��]");
								continue;
							}

							if (!chatMode) {

								String toName = msgSubArr[1].trim();
								out.writeUTF("show|[##] " + toName + "�Բ� ��ȭ��û�� �մϴ�. ");
								if (clientMap.containsKey(toName) && !clientMap.get(toName).chatMode) { // ����üũ
									// req_PvPchat|��û��|������|�޽��� .... ���
									// req_PvPchat|�޽��� .... �� ����

									clientMap.get(toName).out.writeUTF(
											"req_PvPchat|[##] " + id + "�Բ��� 1:1��ȭ��û�� ��û�Ͽ����ϴ�\r\n �����Ͻðڽ��ϱ�?(y,n)");
									toNameTmp = toName;
									clientMap.get(toNameTmp).toNameTmp = id;
								} else {
									out.writeUTF("show|[##] �ش� ������ ���������ʰų� ������ 1:1��ȭ�� �Ҽ����� �����Դϴ�.");
								}

							} else {
								out.writeUTF("show|[##] 1:1��ȭ ����̹Ƿ� ��ȭ��û�� �ϽǼ������ϴ�.");
							}

						} else if (msgArr[1].startsWith("/��ȭ����")) {

							if (chatMode) {
								chatMode = false; // 1:1��ȭ��� ����
								out.writeUTF("show|[##] " + toNameTmp + "�԰� 1:1��ȭ�� �����մϴ�.");
								clientMap.get(toNameTmp).chatMode = false; // ���浵
																			// 1:1��ȭ���
																			// ����
								clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + "�Բ��� 1:1��ȭ�� �����Ͽ����ϴ�");
								toNameTmp = "";
								clientMap.get(toNameTmp).toNameTmp = "";

							} else {
								out.writeUTF("show|[##] 1:1��ȭ���϶��� ����Ҽ��ִ� ��ɾ��Դϴ�. ");
							}

						} else if (msgArr[1].trim().startsWith("/��������")) {

							if (!chatMode) {
								out.writeUTF("show|[##] 1:1��ȭ���϶��� ����Ҽ��ִ� ��ɾ��Դϴ�. ");
								continue;
							}

							String[] msgSubArr = msgArr[1].split(" ", 2);
							if (msgSubArr.length != 2) {
								out.writeUTF("show|[##] �������� ��ɾ� ������ �߸��Ǿ����ϴ�.\r\n usage : /�������� [���������ϰ��]");
								continue;
							}
							filePath = msgSubArr[1];
							File sendFile = new File(filePath);
							String availExtList = "txt,java,jpeg,jpg,png,gif,bmp";

							if (sendFile.isFile()) {
								String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
								if (availExtList.contains(fileExt)) {
									Socket s = globalMap.get(loc).get(toNameTmp).socket;
									// ���ϼ��������� �ϴ� Ŭ���̾�Ʈ ������ �ּ� �˱����� ���� ��ü ����.

									// System.out.println("s.getLocalSocketAddress()=>"+s.getLocalSocketAddress());
									// System.out.println("s.getLocalAddress()=>"+s.getLocalAddress());
									System.out.println("s.getInetAddress():���ϼ���������=>" + s.getInetAddress());
									// ���ϼ��������� �ϴ� Ŭ���̾�Ʈ ������ ���

									fileServerIP = s.getInetAddress().getHostAddress();
									clientMap.get(toNameTmp).out.writeUTF("req_fileSend|[##] " + id + "�Բ��� ����["
											+ sendFile.getName() + "] ������ �õ��մϴ�. \r\n�����Ͻðڽ��ϱ�?(Y/N)");
									out.writeUTF("show|[##] " + toNameTmp + "�Բ� ����[" + sendFile.getAbsolutePath()
											+ "] ������ �õ��մϴ�.");

								} else {

									out.writeUTF("show|[##] ���۰����� ������ �ƴմϴ�. \r\n[" + availExtList
											+ "] Ȯ���ڸ� ���� ���ϸ� ���۰����մϴ�.");
								} // if

							} else {
								out.writeUTF("show|[##] �������� �ʴ� �����Դϴ�.");
							} // if
						} else {
							out.writeUTF("show|[##] �߸��� ��ɾ��Դϴ�.");
						} // if

					} else if (msg.startsWith("req_say")) { // ��ȭ���� ����
						if (!chatMode) {
							// req_say|���̵�|��ȭ����
							sendGroupMsg(loc, "say|" + id + "|" + msgArr[1]);
							// ��½�Ʈ������ ������.
						} else {
							sendPvPMsg(loc, id, toNameTmp, "say|" + id + "|" + msgArr[1]);
						}
					} else if (msg.startsWith("req_whisper")) { // �ӼӸ� ����
						if (msgArr[1].trim().startsWith("/�ӼӸ�")) {
							// req_cmdMsg|��ȭ��|/�ӼӸ� �����ȭ�� ��ȭ����
							String[] msgSubArr = msgArr[1].split(" ", 3); // �޾ƿ�
																			// msg��
																			// "
																			// "(����)��
																			// ��������
																			// 3����
																			// �и�

							if (msgSubArr == null || msgSubArr.length < 3) {
								out.writeUTF("show|[##] �ӼӸ� ������ �߸��Ǿ����ϴ�.\r\n usage : /�ӼӸ� [�����̸�] [�����޽���].");
							} else {
								String toName = msgSubArr[1];
								// String toMsg =
								// "��:from("+name+")=>"+((msgArr[2]!=null)?msgArr[2]:"");
								String toMsg = msgSubArr[2];
								if (clientMap.containsKey(toName)) { // ����üũ
									sendToMsg(loc, id, toName, toMsg);

								} else {
									out.writeUTF("show|[##] �ش� ������ �������� �ʽ��ϴ�.");
								}

							} // if
						} // if

					} else if (msg.startsWith("PvPchat")) { // 1:1��ȭ��û ��������� ����
															// ó��
						// PvPchat|result
						String result = msgArr[0];
						if (result.equals("yes")) {
							chatMode = true;
							clientMap.get(toNameTmp).chatMode = true;
							System.out.println("##1:1��ȭ ��� ����");
							try {
								out.writeUTF("show|[##] " + toNameTmp + "�԰� 1:1 ��ȭ�� �����մϴ�.");
								clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + "�԰� 1:1 ��ȭ�� �����մϴ�.");
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else /* (r.equals("no")) */ {
							clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + " �Բ��� ��ȭ��û�� �����ϼ̽��ϴ�.");
						}

					} else if (msg.startsWith("fileSend")) { // ��������
						// fileSend|result
						String result = msgArr[0];
						if (result.equals("yes")) {
							System.out.println("##��������##YES");
							try {
								String tmpfileServerIP = clientMap.get(toNameTmp).fileServerIP;
								String tmpfilePath = clientMap.get(toNameTmp).filePath;

								// fileSender|filepath;
								clientMap.get(toNameTmp).out.writeUTF("fileSender|" + tmpfilePath);
								// ������ ������ Ŭ���̾�Ʈ���� ���������� ���� filePath�� ����� ������
								// �о�ͼ� OutputStream���� ���

								// fileReceiver|ip|fileName;
								// String fileName =
								// tmpfilePath.substring(tmpfilePath.lastIndexOf("\\")+1);
								// //���� �� ����
								String fileName = new File(tmpfilePath).getName();
								out.writeUTF("fileReceiver|" + tmpfileServerIP + "|" + fileName);

								/* ���� */
								clientMap.get(toNameTmp).filePath = "";
								clientMap.get(toNameTmp).fileServerIP = "";

							} catch (IOException e) {
								e.printStackTrace();
							}
						} else /* (result.equals("no")) */ {
							clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + " �Բ��� ���������� �����Ͽ����ϴ�.");
						} // if

					} else if (msg.startsWith("req_exit")) { // ����

					}
					// ------------------------------------------------- �޼��� ó��

				} // while()---------
			} catch (Exception e) {
				System.out.println("MultiServerRec:run():" + e.getMessage() + "----> ");
				// e.printStackTrace();
			} finally {
				// ���ܰ� �߻��Ҷ� ����. �ؽ��ʿ��� �ش� ������ ����.
				// ���� �����ϰų� ������ java.net.SocketException: ���ܹ߻�
				if (clientMap != null) {
					clientMap.remove(id);
					sendGroupMsg(loc, "## " + id + "���� �����ϼ̽��ϴ�.");
					System.out.println("##���� ������ ���ӵ� ������ " + (--MultiServer.connUserCount) + "�� �Դϴ�.");
				}
			}
		}// run()------------
	}// class MultiServerRec-------------
}