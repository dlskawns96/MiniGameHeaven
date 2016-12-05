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

/*콘솔 멀티채팅 서버 프로그램*/
public class MultiServer {
	HashMap<String, HashMap<String, ServerRecThread>> globalMap; // 게임별 해쉬맵을
																	// 관리하는 해시맵
	ServerSocket serverSocket = null;
	Socket socket = null;
	static int connUserCount = 0; // 서버에 접속된 유저 카운트

	// 생성자
	public MultiServer() {
		globalMap = new HashMap<String, HashMap<String, ServerRecThread>>();

		// clientMap = new HashMap<String,DataOutputStream>(); //클라이언트의 출력스트림을
		// 저장할 해쉬맵 생성.
		Collections.synchronizedMap(globalMap); // 해쉬맵 동기화 설정.

		HashMap<String, ServerRecThread> group01 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group01); // 해쉬맵 동기화 설정.

		HashMap<String, ServerRecThread> group02 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group02); // 해쉬맵 동기화 설정.

		HashMap<String, ServerRecThread> group03 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group03); // 해쉬맵 동기화 설정.

		HashMap<String, ServerRecThread> group04 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group04); // 해쉬맵 동기화 설정.

		HashMap<String, ServerRecThread> group05 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group05); // 해쉬맵 동기화 설정.

		HashMap<String, ServerRecThread> group06 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group06); // 해쉬맵 동기화 설정.

		HashMap<String, ServerRecThread> group07 = new HashMap<String, ServerRecThread>();
		Collections.synchronizedMap(group07); // 해쉬맵 동기화 설정.

		globalMap.put("메인", group01);
		globalMap.put("오목1", group02);
		globalMap.put("오목2", group03);
		globalMap.put("오목3", group04);
		globalMap.put("마피아1", group05);
		globalMap.put("마피아2", group06);
		globalMap.put("마피아3", group07);

	}// 생성자----

	public void init() {
		try {
			serverSocket = new ServerSocket(9995);
			System.out.println("##서버가 시작되었습니다.");

			while (true) {
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + ":" + socket.getPort());
				Thread msr = new ServerRecThread(socket); // 채팅유제에 대한 쓰레드 생성.
				msr.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 대기방 메인 채팅룸 **/
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
				System.out.println("예외:" + e);
			}
		}
	}// sendAllMsg()-----------

	/** 해당 클라이언트가 속해있는 게임방에 대해서만 메시지 전달. **/
	public void sendGroupMsg(String loc, String msg) {

		HashMap<String, ServerRecThread> gMap = globalMap.get(loc);
		Iterator<String> group_it = globalMap.get(loc).keySet().iterator();
		while (group_it.hasNext()) {
			try {
				ServerRecThread st = gMap.get(group_it.next());
				if (!st.chatMode) { // 1:1대화모드가 아닌 사람에게만.
					st.out.writeUTF(msg);
				}
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}
	}// sendGroupMsg()-----------

	/** 1:1 대화 */
	public void sendPvPMsg(String loc, String fromName, String toName, String msg) {

		try {
			globalMap.get(loc).get(toName).out.writeUTF(msg);
			globalMap.get(loc).get(fromName).out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}// sendPvPMsg()-----------

	/** 귓속말 */
	public void sendToMsg(String loc, String fromName, String toName, String msg) {

		try {
			globalMap.get(loc).get(toName).out.writeUTF("whisper|" + fromName + "|" + msg);
			globalMap.get(loc).get(fromName).out.writeUTF("whisper|" + fromName + "|" + msg);
		} catch (Exception e) {
			System.out.println("예외:" + e);
		}

	}// sendAllMsg()-----------

	/**
	 * 각 게임방의 접속자수와 서버에 접속된 유저를 반환 하는 메소드
	 **/
	public String getEachMapSize() {
		return getEachMapSize(null);
	}// getEachMapSize()-----------

	/**
	 * 각 게임방의 접속자수와 서버에 접속된 유저를 반환 하는 메소드 추가 지역을 전달받으면 해당 지역을 체크
	 */
	public String getEachMapSize(String loc) {

		Iterator global_it = globalMap.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		int sum = 0;
		sb.append("=== 그룹 목록 ===" + System.getProperty("line.separator"));
		while (global_it.hasNext()) {
			try {
				String key = (String) global_it.next();

				HashMap<String, ServerRecThread> it_hash = globalMap.get(key);
				// if(key.equals(loc)) key+="(*)"; //현재 유저가 접속된 곳 표시
				int size = it_hash.size();
				sum += size;
				sb.append(key + ": (" + size + "명)" + (key.equals(loc) ? "(*)" : "") + "\r\n");

			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}
		// sb.append("⊙현재 대화에 참여하고있는 유저수 :"+ MultiServer.connUserCount);
		sb.append("⊙현재 대화에 참여하고있는 유저수 :" + sum + "명 \r\n");
		return sb.toString();
	}// getEachMapSize()-----------

	/** 접속된 유저 중복체크 */
	public boolean isNameGlobla(String name) {
		boolean result = false;
		Iterator<String> global_it = globalMap.keySet().iterator();
		while (global_it.hasNext()) {
			try {
				String key = global_it.next();
				HashMap<String, ServerRecThread> it_hash = globalMap.get(key);
				if (it_hash.containsKey(name)) {
					result = true; // 중복된 아이디가 존재.
					break;
				}

			} catch (Exception e) {
				System.out.println("isNameGlobla()예외:" + e);
			}
		}

		return result;
	}// isNameGlobla()-----------

	/** 문자열 null 값 및 "" 은 대체 문자열로 삽입가능. */
	public String nVL(String str, String replace) {
		String output = "";
		if (str == null || str.trim().equals("")) {
			output = replace;
		} else {
			output = str;
		}
		return output;
	}

	// main메서드
	public static void main(String[] args) {
		MultiServer ms = new MultiServer(); // 서버객체 생성.
		ms.init();// 실행.
	}// main()------

	class ServerRecThread extends Thread {

		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		String id = ""; // 아이디 저장
		String loc = ""; // 게임방 저장
		String toNameTmp = null;// 1:1대화 상대
		String fileServerIP; // 파일서버 아이피 저장
		String filePath; // 파일 서버에서 전송할 파일 패스 저장.
		boolean chatMode; // 1:1대화모드 여부

		// 생성자.
		public ServerRecThread(Socket socket) {
			this.socket = socket;
			try {
				// Socket으로부터 입력스트림을 얻는다.
				in = new DataInputStream(socket.getInputStream());
				// Socket으로부터 출력스트림을 얻는다.
				out = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				System.out.println("ServerRecThread 생성자 예외:" + e);
			}
		}// 생성자 ------------

		/** 접속된 유저리스트 문자열로 반환 */
		public String showUserList() {

			StringBuilder output = new StringBuilder("==접속자목록==\r\n");
			Iterator it = globalMap.get(loc).keySet().iterator(); // 해쉬맵에 등록된
																	// 사용자이름을
																	// 가져옴.
			while (it.hasNext()) { // 반복하면서 사용자이름을 StringBuilder에 추가
				try {
					String key = (String) it.next();
					// out.writeUTF(output);
					if (key.equals(id)) { // 현재사용자 체크
						key += " (*) ";
					}

					output.append(key + "\r\n");
				} catch (Exception e) {
					System.out.println("예외:" + e);
				}
			} // while---------
			output.append("==" + globalMap.get(loc).size() + "명 접속중==\r\n");
			System.out.println(output.toString());
			return output.toString();
		}// showUserList()-----------

		/** 메시지 파서 */
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

					// 메세지 처리 ----------------------------------------------
					if (msg.startsWith("req_logon")) { 
						if (!(msgArr[0].trim().equals("")) && !isNameGlobla(msgArr[0])) {
							id = msgArr[0]; 
							MultiServer.connUserCount++; // 접속자수 증가. 
							out.writeUTF("logon#yes|" + getEachMapSize()); 
							
						} else {
							out.writeUTF("logon#no|err01");
						}

					} else if (msg.startsWith("req_enterRoom")) { 
						loc = msgArr[1]; 

						if (isNameGlobla(msgArr[0])) {
							out.writeUTF("logon#no|" + id);

						} else if (globalMap.containsKey(loc)) {
							sendGroupMsg(loc, "show|[##] " + id + "님이 입장하셨습니다.");
							clientMap = globalMap.get(loc);
							clientMap.put(id, this); 
							System.out.println(getEachMapSize()); 
							out.writeUTF("enterRoom#yes|" + loc); 

						} else {
							out.writeUTF("enterRoom#no|" + loc);
						}

					} else if (msg.startsWith("req_cmdMsg")) { 
						if (msgArr[1].trim().equals("/접속자")) {
							out.writeUTF("show|" + showUserList()); 
						} else if (msgArr[1].trim().startsWith("/귓속말")) {
						
							String[] msgSubArr = msgArr[1].split(" ", 3);

							if (msgSubArr == null || msgSubArr.length < 3) {
								out.writeUTF("show|[##] 귓속말 사용법이 잘못되었습니다.\r\n usage : /귓속말 [상대방이름] [보낼메시지].");
							} else if (id.equals(msgSubArr[1])) {
								out.writeUTF("show|[##] 자신에게 귓속말을 할수없습니다.\r\n usage : /귓속말 [상대방이름] [보낼메시지].");
							} else {
								String toName = msgSubArr[1];
								String toMsg = msgSubArr[2];
								if (clientMap.containsKey(toName)) { // 유저체크
									System.out.println("귓속말!");
									sendToMsg(loc, id, toName, toMsg);

								} else {
									out.writeUTF("show|[##] 해당 유저가 존재하지 않습니다.");
								}

							} // if

						} else if (msgArr[1].trim().startsWith("/지역")) {

							String[] msgSubArr = msg.split(" ");
							if (msgSubArr.length == 1) { 
								out.writeUTF("show|" + getEachMapSize(loc));
							} else if (msgSubArr.length == 2) {
								String tmpLoc = msgSubArr[1]; 

								if (loc.equals(tmpLoc)) {
									out.writeUTF("show|[##] 명령어 사용법이 잘못되었습니다.\r\n 본인이 참여하고 있는 게임방을 지정하실수없습니다.\r\n "
											+ "usage : 지역목록 보기 : /지역" + "\r\n usage : 게임방 변경 하기 : /지역 [변경할지역이름].");
									continue;
								}

								if (globalMap.containsKey(tmpLoc) && !this.chatMode) { // 지역체크
									out.writeUTF("show|[##] 지역을 " + loc + "에서 " + tmpLoc + "로 변경합니다. ");

									clientMap.remove(id); // 현재 지역 해쉬맵에서 해당
															// 쓰레드를 제거.
									sendGroupMsg(loc, "show|[##] " + id + "님이 퇴장하셨습니다.");

									System.out.println("이전지역(" + loc + ")에서 에서 " + id + "제거");
									loc = tmpLoc;
									clientMap = globalMap.get(loc);
									sendGroupMsg(loc, "show|[##] " + id + "님이 입장하셨습니다.");
									clientMap.put(id, this); // 새로변경된 지역에
																// 서버쓰레드 저장.

								} else {
									out.writeUTF("##입력한 지역이 존재하지 않거나 현재 이동할수없는 상태입니다.");
								} // if-----

							} else {
								out.writeUTF("show|[##] 명령어 사용법이 잘못되었습니다.\r\n " + "usage : 지역목록 보기 : /지역"
										+ "\r\n usage : 지역변경 하기 : /지역 [변경할지역이름].");

							} // if---------

						} else if (msgArr[1].trim().startsWith("/대화신청")) {
							String[] msgSubArr = msgArr[1].split(" ", 2);

							if (msgSubArr.length != 2) {
								out.writeUTF("show|[##] 명령어 사용법이 잘못되었습니다.\r\n " + "usage : 1:1대화신청하기 : /대화신청 [상대방대화명]");
								continue;
							} else if (id.equals(msgSubArr[1])) {
								out.writeUTF(
										"show|[##] 명령어 사용법이 잘못되었습니다.\r\n 본인의 대화명을 지정하실수없습니다.1:1대화를 할 상대방의 대화명을 지정해주세요.\r\n "
												+ "usage : 1:1대화신청하기 : /대화신청 [상대방대화명]");
								continue;
							}

							if (!chatMode) {

								String toName = msgSubArr[1].trim();
								out.writeUTF("show|[##] " + toName + "님께 대화신청을 합니다. ");
								if (clientMap.containsKey(toName) && !clientMap.get(toName).chatMode) { // 유저체크
									// req_PvPchat|신청자|응답자|메시지 .... 취소
									// req_PvPchat|메시지 .... 로 변경

									clientMap.get(toName).out.writeUTF(
											"req_PvPchat|[##] " + id + "님께서 1:1대화신청을 요청하였습니다\r\n 수락하시겠습니까?(y,n)");
									toNameTmp = toName;
									clientMap.get(toNameTmp).toNameTmp = id;
								} else {
									out.writeUTF("show|[##] 해당 유저가 존재하지않거나 상대방이 1:1대화를 할수없는 상태입니다.");
								}

							} else {
								out.writeUTF("show|[##] 1:1대화 모드이므로 대화신청을 하실수없습니다.");
							}

						} else if (msgArr[1].startsWith("/대화종료")) {

							if (chatMode) {
								chatMode = false; // 1:1대화모드 해제
								out.writeUTF("show|[##] " + toNameTmp + "님과 1:1대화를 종료합니다.");
								clientMap.get(toNameTmp).chatMode = false; // 상대방도
																			// 1:1대화모드
																			// 해제
								clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + "님께서 1:1대화를 종료하였습니다");
								toNameTmp = "";
								clientMap.get(toNameTmp).toNameTmp = "";

							} else {
								out.writeUTF("show|[##] 1:1대화중일때만 사용할수있는 명령어입니다. ");
							}

						} else if (msgArr[1].trim().startsWith("/파일전송")) {

							if (!chatMode) {
								out.writeUTF("show|[##] 1:1대화중일때만 사용할수있는 명령어입니다. ");
								continue;
							}

							String[] msgSubArr = msgArr[1].split(" ", 2);
							if (msgSubArr.length != 2) {
								out.writeUTF("show|[##] 파일전송 명령어 사용법이 잘못되었습니다.\r\n usage : /파일전송 [전송할파일경로]");
								continue;
							}
							filePath = msgSubArr[1];
							File sendFile = new File(filePath);
							String availExtList = "txt,java,jpeg,jpg,png,gif,bmp";

							if (sendFile.isFile()) {
								String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
								if (availExtList.contains(fileExt)) {
									Socket s = globalMap.get(loc).get(toNameTmp).socket;
									// 파일서버역할을 하는 클라이언트 아이피 주소 알기위해 소켓 객체 얻어옴.

									// System.out.println("s.getLocalSocketAddress()=>"+s.getLocalSocketAddress());
									// System.out.println("s.getLocalAddress()=>"+s.getLocalAddress());
									System.out.println("s.getInetAddress():파일서버아이피=>" + s.getInetAddress());
									// 파일서버역할을 하는 클라이언트 아이피 출력

									fileServerIP = s.getInetAddress().getHostAddress();
									clientMap.get(toNameTmp).out.writeUTF("req_fileSend|[##] " + id + "님께서 파일["
											+ sendFile.getName() + "] 전송을 시도합니다. \r\n수락하시겠습니까?(Y/N)");
									out.writeUTF("show|[##] " + toNameTmp + "님께 파일[" + sendFile.getAbsolutePath()
											+ "] 전송을 시도합니다.");

								} else {

									out.writeUTF("show|[##] 전송가능한 파일이 아닙니다. \r\n[" + availExtList
											+ "] 확장자를 가진 파일만 전송가능합니다.");
								} // if

							} else {
								out.writeUTF("show|[##] 존재하지 않는 파일입니다.");
							} // if
						} else {
							out.writeUTF("show|[##] 잘못된 명령어입니다.");
						} // if

					} else if (msg.startsWith("req_say")) { // 대화내용 전송
						if (!chatMode) {
							// req_say|아이디|대화내용
							sendGroupMsg(loc, "say|" + id + "|" + msgArr[1]);
							// 출력스트림으로 보낸다.
						} else {
							sendPvPMsg(loc, id, toNameTmp, "say|" + id + "|" + msgArr[1]);
						}
					} else if (msg.startsWith("req_whisper")) { // 귓속말 전송
						if (msgArr[1].trim().startsWith("/귓속말")) {
							// req_cmdMsg|대화명|/귓속말 상대방대화명 대화내용
							String[] msgSubArr = msgArr[1].split(" ", 3); // 받아온
																			// msg을
																			// "
																			// "(공백)을
																			// 기준으로
																			// 3개를
																			// 분리

							if (msgSubArr == null || msgSubArr.length < 3) {
								out.writeUTF("show|[##] 귓속말 사용법이 잘못되었습니다.\r\n usage : /귓속말 [상대방이름] [보낼메시지].");
							} else {
								String toName = msgSubArr[1];
								// String toMsg =
								// "귓:from("+name+")=>"+((msgArr[2]!=null)?msgArr[2]:"");
								String toMsg = msgSubArr[2];
								if (clientMap.containsKey(toName)) { // 유저체크
									sendToMsg(loc, id, toName, toMsg);

								} else {
									out.writeUTF("show|[##] 해당 유저가 존재하지 않습니다.");
								}

							} // if
						} // if

					} else if (msg.startsWith("PvPchat")) { // 1:1대화신청 수락결과에 대한
															// 처리
						// PvPchat|result
						String result = msgArr[0];
						if (result.equals("yes")) {
							chatMode = true;
							clientMap.get(toNameTmp).chatMode = true;
							System.out.println("##1:1대화 모드 변경");
							try {
								out.writeUTF("show|[##] " + toNameTmp + "님과 1:1 대화를 시작합니다.");
								clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + "님과 1:1 대화를 시작합니다.");
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else /* (r.equals("no")) */ {
							clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + " 님께서 대화신청을 거절하셨습니다.");
						}

					} else if (msg.startsWith("fileSend")) { // 파일전송
						// fileSend|result
						String result = msgArr[0];
						if (result.equals("yes")) {
							System.out.println("##파일전송##YES");
							try {
								String tmpfileServerIP = clientMap.get(toNameTmp).fileServerIP;
								String tmpfilePath = clientMap.get(toNameTmp).filePath;

								// fileSender|filepath;
								clientMap.get(toNameTmp).out.writeUTF("fileSender|" + tmpfilePath);
								// 파일을 전송할 클라이언트에서 서버소켓을 열고 filePath로 저장된 파일을
								// 읽어와서 OutputStream으로 출력

								// fileReceiver|ip|fileName;
								// String fileName =
								// tmpfilePath.substring(tmpfilePath.lastIndexOf("\\")+1);
								// //파일 명만 추출
								String fileName = new File(tmpfilePath).getName();
								out.writeUTF("fileReceiver|" + tmpfileServerIP + "|" + fileName);

								/* 리셋 */
								clientMap.get(toNameTmp).filePath = "";
								clientMap.get(toNameTmp).fileServerIP = "";

							} catch (IOException e) {
								e.printStackTrace();
							}
						} else /* (result.equals("no")) */ {
							clientMap.get(toNameTmp).out.writeUTF("show|[##] " + id + " 님께서 파일전송을 거절하였습니다.");
						} // if

					} else if (msg.startsWith("req_exit")) { // 종료

					}
					// ------------------------------------------------- 메세지 처리

				} // while()---------
			} catch (Exception e) {
				System.out.println("MultiServerRec:run():" + e.getMessage() + "----> ");
				// e.printStackTrace();
			} finally {
				// 예외가 발생할때 퇴장. 해쉬맵에서 해당 데이터 제거.
				// 보통 종료하거나 나가면 java.net.SocketException: 예외발생
				if (clientMap != null) {
					clientMap.remove(id);
					sendGroupMsg(loc, "## " + id + "님이 퇴장하셨습니다.");
					System.out.println("##현재 서버에 접속된 유저는 " + (--MultiServer.connUserCount) + "명 입니다.");
				}
			}
		}// run()------------
	}// class MultiServerRec-------------
}