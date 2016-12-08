package gameClient_OMOK;

import java.awt.*;
import java.awt.event.*;
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
import javax.swing.JScrollPane;

import java.awt.image.ImageProducer;
import javax.swing.UIManager;


//메인 클라이언트
@SuppressWarnings("serial")
public class ClientOMOK_player extends JFrame implements Runnable, ActionListener {
	
//	■■■■■■■■■■■■■■■■■■■■■■■■■■■클라이언트 GUI S■■■■■■■■■■■■■■■■■■■■■■■■■■■
	 BufferedImage img,pimg = null;
	private TextArea msgView = new TextArea("", 1, 1, 1); // 메시지를 보여주는 영역
	private TextField sendBox = new TextField(""); // 보낼 메시지를 적는 상자:id
	private TextField nameBox = new TextField(); // 사용자 이름 상자:로그인할 때 id input
	private int roomBox_player = 1; // 방 번호 상자

	// 방에 접속한 인원의 수를 보여주는 레이블
	private Label pInfo = new Label("게임참여자");

	private java.awt.List pList = new java.awt.List(); // 사용자 명단을 보여주는 리스트
	private Button startButton = new Button("게임 시작"); // 대국 시작 버튼
	private Button stopButton = new Button("기권하기"); // 기권 버튼:누르면 바둑알클리어되고 게임지속됨
	private Button exitButton = new Button("로그인하기(3~9글자)"); // 대기실로 버튼

	// 각종 정보를 보여주는 레이블: 접속자 정보를 보여줌-1번방 2명 이런 식으로
	static private Label infoView = new Label("게임화면", 1);
	
	private OMOKPan board = new OMOKPan(); // 오목판 객체
	private BufferedReader reader; // 입력 스트림
	public PrintWriter writer; // 출력 스트림
	private Socket CS; // 소켓
	private int roomNumber = -1; // 방 번호: 왜 -1이지?
	private String user_ID = null; // 사용자 이름
	private Dialog di;
	private TextArea gameResult;
	private Button closeDi;
	
	private JFrame frame;
	ImageIcon BGIcon=new ImageIcon("BG2.png");
	
 ClientOMOK_player() { // 생성자
		super();
		getContentPane().setLayout(null); // 레이아웃을 사용하지 않는다. 사용자 지정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		 //JScrollPane scrollPane;
		
		frame = new JFrame("OMOK_game");
		//frame.setTitle("OMOK game");
		JPanel BGpanel = new JPanel() {
			   public void paintComponent(Graphics g) {
				   g.drawImage(BGIcon.getImage(), 0, 0, null);
					Dimension d = getSize();
					g.drawImage(BGIcon.getImage(), 0, 0, d.width, d.height, null);
				    setOpaque(false);
				    super.paintComponent(g);
				   }
		 };
		BGpanel.setBackground(UIManager.getColor("Panel.background"));
		 BGpanel.setBounds(0,0,750,530);
		 //scrollPane=new JScrollPane(BGpanel);
		 //setContentPane(scrollPane);
		//getContentPane().add(BGpanel);
		 
		 ImageIcon logoIcon=new ImageIcon("titleIcon.png");
		 JPanel Logopanel = new JPanel() {
			   public void paintComponent(Graphics g) {
				   g.drawImage(logoIcon.getImage(), 0, 0, null);
					Dimension d = getSize();
					g.drawImage(logoIcon.getImage(), 0, 0, d.width, d.height, null);
				    setOpaque(false);
				    super.paintComponent(g);
				   }
		 };
		 
		 
		// 각종 컴포넌트를 생성하고 배치한다.
		msgView.setEditable(false);//채팅창 못치게
		
		getContentPane().add(infoView);
		board.setSize(470, 470);
		getContentPane().add(board);//OMOKPan 오브젝트 추가
		//		로그인
		Panel p = new Panel();
		

		p.setBackground(Color.lightGray);
		p.setLayout(new GridLayout(3, 3));
		Label label = new Label("아 이 디", Label.CENTER);
		label.setBounds(38, 29, 250, 23);
		p.add(label);
		p.add(nameBox);
		board.setLocation(10,60);
		p.add(exitButton);//
		p.setBounds(500,60,250,70);

		
	
	
//		접속자
		Panel p2 = new Panel();
		p2.setBackground(Color.lightGray);
		p2.setLayout(new BorderLayout());
		Panel p2_1 = new Panel();
		p2_1.add(startButton);
		p2_1.add(stopButton);
		p2.add(pInfo, "North");
		p2.add(pList, "Center");
		p2.add(p2_1, "South");
		startButton.setEnabled(false);
		stopButton.setEnabled(false);//p2_1에 추가한 버튼 2개를 사용불가하게
		p2.setBounds(500, 140, 250, 90);
//		채팅
		Panel p3 = new Panel();
		p3.setBackground(new Color(200, 255, 255));
		p3.setLayout(new BorderLayout());
		p3.add(msgView, "Center");
		p3.add(sendBox, "South");
		p3.setBounds(500, 240, 250, 280);

		getContentPane().add(p);
		getContentPane().add(p2);
		getContentPane().add(p3);
		//BGpanel.add(p);
		//BGpanel.add(p2);
		//BGpanel.add(p3);
		getContentPane().add(BGpanel);
		// 이벤트 리스너를 등록한다.
		sendBox.addActionListener(this);
		exitButton.addActionListener(this);
		startButton.addActionListener(this);
		stopButton.addActionListener(this);

		
		setSize(773, 580);//Client 윈동창 크기 설정
		setVisible(true);
		connect();//server에 연결 요청
		
	}//생성자 끝

 
 
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
			startButton.setEnabled(false);
			stopButton.setEnabled(false);
			writer.println("room_msg" + roomNum);//방번호상자 전송
			msgView.setText("");

		} catch (Exception e) {
			infoView.setText("오류가 있습니다.");

		}
 }
 
	// 컴포넌트들의 액션 이벤트 처리
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == sendBox) { // case1:메시지 입력 상자이면(채팅)
			String msg = sendBox.getText();
			if (msg.length() == 0)
				return;
			if (msg.length() >= 30)
				msg = msg.substring(0, 30);//msg의 길이는 30까지만 보냄.

			try {
				writer.println("[MSG]" + msg);
				sendBox.setText("");//sendBox 초기화
			} catch (Exception ie) {
			}
		}

		else if (ae.getSource() == exitButton) { // case2:로그인로 버튼이면
			login(roomBox_player);
		}

		else if (ae.getSource() == startButton) { // case3:준비완료 버튼이면
			try {
				writer.println("[START]");
				infoView.setText("상대의 결정을 기다립니다.");
				startButton.setEnabled(false);
			} catch (Exception e) {
			}
		}

		else if (ae.getSource() == stopButton) { // 기권 버튼이면??????????????????????popupEnd 팝업창띄움
			try {
				int endResult;
				endResult=JOptionPane.showConfirmDialog(null,"retire game?");
				//msgView.append("endresult: "+endResult);
				if(endResult==0)//기권
				{
					writer.println("[DROPGAME]");
					endGame("기권하였습니다.");
					System.exit(1);
				}else if(endResult==1){//재시작
					writer.println("[RESTARTALL]");
					writer.println("[STOPGAME]");
					endGame("기권하였습니다.");
					writer.println("[START]");
				}else{//endResult==2:취소
					;
				}
				/*JFrame popupEnd = new JFrame("end game");
				popupEnd.setSize(100,200);
				JLabel endMSG=new JLabel("quit game?or retry game?");
				popupEnd.getContentPane().add(endMSG);
				JButton endGame=new JButton("quit");
				JButton restartGame=new JButton("restart");
				popupEnd.getContentPane().add(endGame);
				popupEnd.getContentPane().add(restartGame);*/
				
			} catch (Exception e) {
			}
		}
		
		
	}

 void goToWaitRoom() { // 로그인 버튼을 누르면 호출된다: 
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
			infoView.setText("게임시작을 눌려주세요.");
			exitButton.setEnabled(false);
		}
	}
//	■■■■■■■■■■■■■■■■■■■■■■■■■■■클라이언트 GUI E■■■■■■■■■■■■■■■■■■■■■■■■■■■
	
//	■■■■■■■■■■■■■■■■■■■■■■■■■■■클라이언트 Thread S■■■■■■■■■■■■■■■■■■■■■■■■
	public void run() {
		// 서버로 부터 받은 메세지를 저장시킴.
		String server_message;
		try {
			while ((server_message = reader.readLine()) != null) {

				if (server_message.startsWith("[STONE]")) { // 상대편이 놓은 돌의 좌표
					String temp = server_message.substring(7);
					int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
					int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
					board.putOpponent(x, y); // 상대편의 돌을 그린다.
					board.setEnable(true); // 사용자가 돌을 놓을 수 있도록 한다.
				}

				else if (server_message.startsWith("room_msg")) { // 방에 입장
					if (!server_message.equals("room_msg0")) { // 대기실이 아닌 방이면

						exitButton.setEnabled(true);
						infoView.setText(server_message.substring(8) + "번 방에 입장하셨습니다.");
					} else
						infoView.setText("대기실에 입장하셨습니다.");

					roomNumber = Integer.parseInt(server_message.substring(8)); // 방 번호 지정
					
					if (board.isRunning()) { // 게임이 진행중인 상태이면
						board.stopGame(); // 게임을 중지시킨다.
					}
				}

				else if (server_message.startsWith("[FULL]")) { // 방이 찬 상태이면
					infoView.setText("방이 차서 입장할 수 없습니다.");

				}

				else if (server_message.startsWith("[PLAYERS]")) { // 방에 있는 사용자 명단
					nameList(server_message.substring(9));
				}

				else if (server_message.startsWith("[ENTER]")) { // 손님 입장

					pList.add(server_message.substring(7));
					playersInfo();
					msgView.append("[" + server_message.substring(7) + "]님이 입장하였습니다.\n");

				} else if (server_message.startsWith("[EXIT]")) { // 손님 퇴장
					pList.remove(server_message.substring(6)); // 리스트에서 제거
					playersInfo(); // 인원수를 다시 계산하여 보여준다.
					msgView.append("[" + server_message.substring(6) + "]님이 다른 방으로 입장하였습니다.\n");
					if (roomNumber != 0)
						endGame("상대가 나갔습니다.");
				}

				else if (server_message.startsWith("[DISCONNECT]")) { // 손님 접속 종료
					pList.remove(server_message.substring(12));
					playersInfo();
					msgView.append("[" + server_message.substring(12) + "]님이 접속을 끊었습니다.\n");
					if (roomNumber != 0)
						endGame("상대가 나갔습니다.");
				}

				else if (server_message.startsWith("[COLOR]")) { // 돌의 색을 부여받는다.
					String color = server_message.substring(7);
					board.startGame(color); // 게임을 시작한다.(흑독부터, 흰돌은 대기)
					
					if (color.equals("BLACK"))
						infoView.setText("흑돌을 잡았습니다.");
					else
						infoView.setText("백돌을 잡았습니다.");
					board.setEnable(true);//
					msgView.append("게임시작!"+color+"\n");//
					stopButton.setEnabled(true); // 기권 버튼 활성화
				}
				else if(server_message.startsWith("[RESTART]"))//다른 한 유저가 restartall을 눌렀을 때 이 유저는 restart된다.
				{
					String color=server_message.substring(9);
					endGame(color+"유저 기권!");
					writer.println("[START]");
				}
				else if (server_message.startsWith("[DROPGAME]")) // 상대가 기권하면
					endGame("상대방 기권!");

				else if (server_message.startsWith("[WIN]")) // 이겼으면
					endGame("당신의 승리!");

				else if (server_message.startsWith("[LOSE]")) // 졌으면
					endGame("당신의 패배!");

				// 약속된 메시지가 아니면 메시지 영역에 보여준다.
				else
					msgView.append(server_message + "\n");
			}
		} catch (IOException ie) {
			msgView.append(ie + "\n");
		}
		msgView.append("접속해체");
	}

	private void endGame(String server_message) { // 게임의 종료시키는 메소드
		infoView.setText(server_message);
		startButton.setEnabled(false);
		stopButton.setEnabled(false);

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		} // 2초간 대기

		if (board.isRunning())
			board.stopGame();
		if (pList.getItemCount() == 2)
			startButton.setEnabled(true);
	}

	private void playersInfo() { // 방에 있는 접속자의 수를 보여준다.
		int count = pList.getItemCount();
		if (roomNumber == 0)
			pInfo.setText("접속자: " + count + "명");
		else {
			pInfo.setText(roomNumber + " 번 방: " + count + "명");

		}
		// 대국 시작 버튼의 활성화 상태를 점검한다.
		if (count == 2 && roomNumber != 0)
			startButton.setEnabled(true);
		else
			startButton.setEnabled(false);
	}

	// 사용자 리스트에서 사용자들을 추출하여 pList에 추가한다.
	private void nameList(String server_message) {
		pList.removeAll();
		StringTokenizer st = new StringTokenizer(server_message, "\t");
		while (st.hasMoreElements())
			pList.add(st.nextToken());
		playersInfo();
	}

	private void connect() { // 연결
		try {
			msgView.append("서버 연결 중~\n");
			CS = new Socket("127.0.0.1", 7777);
			msgView.append("서버 연결 성공!\n");
			msgView.append("아이디을 입력하세요.\n");
			reader = new BufferedReader(new InputStreamReader(CS.getInputStream()));
			writer = new PrintWriter(CS.getOutputStream(), true);
			new Thread(this).start();
			board.setWriter(writer);//board와 채팅창의 outputStream 설정
		} catch (Exception e) {
			msgView.append(e + "\n\n연결 실패..\n");
		}
	}
}
