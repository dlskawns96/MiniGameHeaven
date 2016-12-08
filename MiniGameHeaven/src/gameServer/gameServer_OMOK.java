package gameServer;

import java.net.*;
import java.text.SimpleDateFormat;
import java.awt.TextArea;
import java.io.*;
import java.util.*;

import DataBase.tempDBinformation;
import DataBase.MySQL;

//import Clinet.OmokClient;

public class gameServer_OMOK {
	// 생성한 소켓을 담기 위한 SS변수
	private ServerSocket SS;
	// 클라이언트와 연결된 스레드를 담기위한 ThreadSoket 변수
	private Socket TS;
	private TempMessage OM = new TempMessage(); // 메시지 메세지 객체
	private Random rnd = new Random(); // 흑과 백을 임의로 정하기 위한 변수
	private Omok_Thread OT;
	
	/*날짜계산 포멧*/
	private SimpleDateFormat fm = new SimpleDateFormat("yyyy년 MM월 dd일 HH시MM분 ss초");//날짜형식 포맷 객체 생성
	private tempDBinformation TDI = new tempDBinformation();//게임 결과 데이터를 저장하기 위한 객체
	private long startTime,endTime;//시작시간, 끝시간
	
	/*JDBC저장을 위한 포멧*/
	private MySQL ms = new MySQL();//JDBC connect 및 query전송을 위한 객체
	private Hashtable insertDate = new Hashtable(); //게임 결과값을 저장하기 위한 collection
	private Vector userList=new Vector();//user 수를 측정하기 위한 벡터
	private Vector playRoom=new Vector();
	//public static int roomNum=1;

	
	private OmokPan OP = new OmokPan();
	private int doll=0;
	private TextArea ID_set;
	private TextArea XY_set;
	private Setmessage handler;
	
	public gameServer_OMOK(Setmessage handler)
	{
		this.handler = handler;
	}

//	private static StopWatch time= new StopWatch();;
	void startServer() { // 서버를 실행
		try {
			// 인자 port로 포트번호를 지정해 ServerSocket 객체를 생성
			SS = new ServerSocket(7777);
			
			
			while (true) {

				// 접속자와 연결된 스레드를 대입
				TS = SS.accept();

				// 스레드 생성
				OT = new Omok_Thread(TS);

				// 스레드 시작
				OT.start();

				// OM에 스레드를 추가한다.
				OM.add(OT);

				System.out.println("접속자 수: " + OM.size());
				
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	// 클라이언트와 통신하는 스레드 inner클래스
	class Omok_Thread extends Thread {
		
		//private int Rnum = -1;
		private int Rnum=-1;
		// 사용자의 이름을 저장하는 변수
		private String ID = null; // 사용자 이름
		// 각 사용자의 소켓을 저장하는 변수
		private Socket socket; // 소켓

		// 사용자의 준비 상태를 체크하는 변수
		// 대국 시작을 눌렸으면 true
		// 대국 시작을 누르지 않았으면 false
		private boolean ready_state = false;

		private BufferedReader reader; // 입력 스트림
		private PrintWriter writer; // 출력 스트림

		Omok_Thread(Socket socket) {
			this.socket = socket;
		}

		// 소켓 getter
	
		Socket getSocket() {
			return socket;
		}

		// 방번호 getter
		int getRnum() {
			return Rnum;
		}

		// ID getter
		String getID() {
			return ID;
		}

		// 사용자의 준비 상태 getter
		boolean isready_state() {
			return ready_state;
		}

		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
				
				String msg; // 클라이언트의 메시지

				while ((msg = reader.readLine()) != null) {
					
					// msg가 "[NAME]"으로 시작되는 메시지이면
					if (msg.startsWith("[NAME]")) {
						ID = msg.substring(6); // ID을 정한다.
					}

					// msg가 "room_msg"으로 시작되면 방 번호를 정한다.
					else if (msg.startsWith("room_msg")) {
						if(msg.substring(8).startsWith("_fromWatcher"))//watcher 측에서 온 입장메
						{
							int roomNum=Integer.parseInt(msg.substring(20));
						}
						int roomNum = Integer.parseInt(msg.substring(8));
						
						if (!OM.isFull(roomNum)) { // 방이 찬 상태가 아니면

							// 현재 방의 다른 사용에게 사용자의 퇴장을 알린다.
							if (Rnum != -1)
								OM.sendToOthers(this, "[EXIT]" + ID);

							// 사용자의 새 방 번호를 지정한다.
							Rnum = roomNum;

							// 사용자에게 메시지를 그대로 전송하여 입장할 수 있음을 알린다.
							writer.println(msg);

							// 사용자에게 새 방에 있는 사용자 이름 리스트를 전송한다.
							writer.println(OM.getNamesInRoom(Rnum));
//★★★★★★★★서버에 접속한 유저아이디
							System.out.println(OM.getNamesInRoom(1));
							handler.CON_Play(OM.getNamesInRoom(1));
							if(ID_set!=null){
								ID_set.append(OM.getNamesInRoom(1));
								ID_set.invalidate();
							}

							// 새 방에 있는 다른 사용자에게 사용자의 입장을 알린다.
							OM.sendToOthers(this, "[ENTER]" + ID);
						} else
							writer.println("[FULL]"); // 사용자에 방이 찼음을 알린다.
					}

					// "[STONE]" 메시지는 상대편에게 전송한다.
					else if (Rnum >= 1 && msg.startsWith("[STONE]")){
						OM.sendToOthers(this, msg);
//★★★★★★★★돌의 위치
						
						if(doll==0){
							handler.Game_XY("흑돌좌표"+msg);doll=1;
							String temp = msg.substring(7);
							
							int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
							int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
							

//							돌을 그려주자. x,y는 각각 좌표
							doll=1;
						}
						else{
							handler.Game_XY("백돌좌표"+msg);
							String temp = msg.substring(7);
							
							int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
							int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
							

//							돌을 그려주자. x,y는 각각 좌표
							
							doll=0;
						}
					}
					// 대화 메시지를 방에 전송한다.
					else if (msg.startsWith("[MSG]")){
						OM.sendToRoom(Rnum, "[" + ID + "]: " + msg.substring(5));
						handler.CHAT("[" + ID + "]: " + msg.substring(5));
					}
					// "[START]" 메시지이면
					else if (msg.startsWith("[START]")) {
						ready_state = true; // 게임을 시작할 준비가 되었다.

						// Rnum방의 다른 사용자도 게임을 시작한 준비가 되었으면 게임시작 설정해줌
						if (OM.isReady(Rnum)) {
							TDI.setStartDate(fm.format(new Date()));//사용자들의 게임 준비가 완료되면 시작 시간을 set
							
							/*시간차를 계산하기 위한 currentTime*/
							try {
								startTime = System.currentTimeMillis();//현재 시간을 변수에 저장.
							} catch (Exception e) {
								e.printStackTrace();
							}
							//****************************************************
							
							// 흑과 백을 정하고 사용자와 상대편에게 전송한다.
							int doll = rnd.nextInt(2);
							if (doll == 0) {
								writer.println("[COLOR]BLACK");
								OM.sendToOthers(this, "[COLOR]WHITE");

							} else {
								writer.println("[COLOR]WHITE");
								OM.sendToOthers(this, "[COLOR]BLACK");
							}			
							
						}
					}

					// 사용자가 게임을 중지하는 메시지를 보내면
					else if (msg.startsWith("[STOPGAME]")){
						ready_state = false;
						handler.CHAT("게임이 중지되었습니다.");
					}
					// 사용자가 게임을 기권하는 메시지를 보내면
					else if (msg.startsWith("[DROPGAME]")) {
						TDI.setEndDate(fm.format(new Date()));//게임이 종료되는 순간
						String gameMsg = ID+"의 기권처리";//기권처리의 경우 기권저리 메시지를 ID에 저장.
						TDI.setWinner(gameMsg);//기권의 경우 기권처리 사용자를 저장.
						
						/*시간차를 계산하기 위한 currentTiem*/
						try {
							endTime = System.currentTimeMillis();//경기가 끝나면 그 시간을 변수에 저장.
						} catch (Exception e) {
							e.printStackTrace();
						}
						/*시간 계산 작업*/
						long sumTime = endTime-startTime;//시작시간과 끝시간의 차이 계산
						long MTime = (sumTime / 1000) / 60;//분 계산
						long STime = (sumTime / 1000) % 60;//초 계산
						String playdate = Integer.toString((int)MTime)+"분"+Integer.toString((int)STime)+"초";
						//계산된 값을 이용해 *분 *초가 경과되었는지 String으로 저장.
						
						insertDate.put("winner",TDI.getWinner());//collection을 이용해 게임 데이터를 저장
						insertDate.put("startdate",TDI.getStartDate());
						insertDate.put("enddate",TDI.getEndDate());
						insertDate.put("palydate",playdate);
						
						ms.insertGameResult(insertDate);//저장된 collection을 데이터 삽입 함수의 인자값으로 준다.
						
						
				
						 
					}
					else if(msg.startsWith("[RESTARTALL]")){
						OM.sendToOthers(this, "[RESTART]");
					}
					
					// 사용자가 이겼다는 메시지를 보내면
					else if (msg.startsWith("[WIN]")) {
						
						TDI.setEndDate(fm.format(new Date()));//게임이 종료되는 순간
						TDI.setWinner(ID);//승자의 아이디를 저장
						
						/*시간차를 계산하기 위한 currentTiem*/
						try {
							endTime = System.currentTimeMillis();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						/*시간 계산 작업*/
						long sumTime = endTime-startTime;
						long MTime = (sumTime/ 1000)/60;
						long STime = (sumTime/1000)%60;
						String playdate = Integer.toString((int)MTime)+"분"+Integer.toString((int)STime)+"초";
						
						insertDate.put("winner",TDI.getWinner());
						insertDate.put("startdate",TDI.getStartDate());
						insertDate.put("enddate",TDI.getEndDate());
						insertDate.put("palydate",playdate);
						
						ms.insertGameResult(insertDate);

						
						
						ready_state = false;
						// 사용자에게 메시지를 보낸다.
						writer.println("[WIN]");

						// 상대편에는 졌음을 알린다.
						OM.sendToOthers(this, "[LOSE]");
					}
				}
			} catch (Exception e) {
			} finally {
				try {
					OM.remove(this);
					reader.close();
					writer.close();
					socket.close();
					
					System.out.println(ID + "님 종료");
					handler.CHAT(ID + "님 종료");
					System.out.println("현재 " + OM.size() + "명 접속 중 입니다.");
					handler.CHAT("현재 " + OM.size() + "명 접속 중 입니다.");
					/*for(int i=0;i<OM.size();i++)
					{
						System.out.print(" [사용자"+i+"]"+OM.elementAt(i));
					}*/
					// 사용자가 접속을 끊었음을 같은 방에 알린다.
					OM.sendToRoom(Rnum, "[DISCONNECT]" + ID);
				} catch (Exception e) {
				}
			}
		}
	}

}
