package gameServer;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import DataBase.MySQL;
import gameServer.OmokPan;

@SuppressWarnings("serial")
public class OmokmanagerStart extends Frame implements ActionListener, Setmessage {
	public static gameServer_OMOK server;
	// ■■■■■■■■■■■■■■■■■■■■■■■■■■■서버 GUI S■■■■■■■■■■■■■■■■■■■■■■■■■■■
	private TextArea msgView = new TextArea("", 1, 1, 1); // 메시지를 보여주는 영역:유저목록
	private TextField sendBox = new TextField(""); // 보낼 메시지를 적는 상자:id 입력

	// 방에 접속한 인원의 수를 보여주는 레이블
	private Label pInfo = new Label("접속자:  명");

	private String Peaple = "not";
	private String xy = "not";
	private java.awt.List pList = new java.awt.List(); // 사용자 명단을 보여주는 리스트
	private Button serverStartButton = new Button("서버 시작"); // 대국 시작 버튼
	private Button gameDataButton = new Button("대전기록 보기");// 대전기록
	private Vector gameRecord; // JDBC메서드에서 가져온 값을 저장하기 위한 collection
	// 각종 정보를 보여주는 레이블
	private Label infoView = new Label("네트워크 오목 게임 ver 5.1", 1);
	private OmokPan pan; // 오목판 객체
	private Dialog di;
	private static TextArea gameResult;
	private Button closeDi;

	public OmokmanagerStart(String title) { // 생성자
		super(title);
		pan = new OmokPan();
		setLayout(null); // 레이아웃을 사용하지 않는다.
		// 각종 컴포넌트를 생성하고 배치한다.
		msgView.setEditable(false);
		// 타이틀 설정
		infoView.setBounds(10, 60, 470, 30);
		infoView.setBackground(new Color(200, 200, 255));
		pan.setLocation(10, 70);
		add(infoView);
		add(pan);
		// 로그인
		Panel p = new Panel();
		p.setBackground(new Color(200, 255, 255));
		p.setLayout(new GridLayout(3, 3));
		p.add(new Label("관리자", 1));
		p.setBounds(500, 60, 250, 30);
		
		
		/* 대전기록을 보기위한 dialog 생성 및 구성 */
		di = new Dialog(this, "대전기록", true);// 다이얼로그 생성
		gameResult = new TextArea("", 50, 120);// 대전기록 View창
		gameResult.setEditable(false);
		closeDi = new Button("닫기");// 다이얼로그를 닫기 위한 버튼
		closeDi.addActionListener(this);
		gameDataButton.addActionListener(this);
		di.setLayout(new BorderLayout());

		di.add(gameResult, BorderLayout.CENTER);
		di.add(closeDi, BorderLayout.SOUTH);
		di.pack();
		// *************************************//
		// 접속자
		Panel p2 = new Panel();
		p2.setBackground(new Color(255, 255, 100));
		p2.setLayout(new BorderLayout());
		Panel p2_1 = new Panel();
		p2_1.add(serverStartButton);
		p2_1.add(gameDataButton);
		p2.add(pInfo, "North");
		p2.add(pList, "Center");
		p2.add(p2_1, "South");
		serverStartButton.setEnabled(true);
		p2.setBounds(500, 100, 250, 90);
		// 채팅
		Panel p3 = new Panel();
		p3.setBackground(new Color(200, 255, 255));
		p3.setLayout(new BorderLayout());
		p3.add(msgView, "Center");
		p3.add(sendBox, "South");
		p3.setBounds(500, 205, 250, 320);

		add(p);
		add(p2);
		add(p3);

		// 이벤트 리스너를 등록한다.
		sendBox.addActionListener(this);
		serverStartButton.addActionListener(this);

		// 윈도우 닫기 처리
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getSource() == serverStartButton) {
			// 오목 서버 시작
			infoView.setText("서버를 시작했습니다.");
			serverStartButton.setEnabled(false);
			server.startServer();

		}else if(ae.getSource() == gameDataButton){//대전기록 보기 버튼이면
			
			MySQL CDB = new MySQL();//JDBC 클래스 생성
			gameRecord = CDB.printResult();//JDBC클래스 내부의 메서드를 실행하고 return값 저장하기 위한 vector
			Enumeration e = gameRecord.elements();//Enumeration객체를 이용해 vector의 elements를 모두 저장
			
			gameResult.setText("대전 기록....");//gameResult창에 내용 출력
			gameResult.append("\n");
			
			
			int cnt = 1;
			while(e.hasMoreElements()){//반복문을 통해 vector에 저장된 값을 하나씩 출력.
				gameResult.append("\n");
				gameResult.append("[경기 번호] : ");
				gameResult.append((String) e.nextElement());
				gameResult.append("\n");
				gameResult.append("[승자] : ");
				gameResult.append((String) e.nextElement()+" ");
				gameResult.append("[시작시간] : ");
				gameResult.append((String) e.nextElement()+" ");
				gameResult.append("[종료시간] : ");
				gameResult.append((String) e.nextElement()+" ");
				gameResult.append("[진행시간] : ");
				gameResult.append((String) e.nextElement()+" ");
				gameResult.append("\n");
				gameResult.append("\n");
				gameResult.append("============================="
						+ "=============================================="
						+ "=============================================="
						+ "===================");
				gameResult.append("\n");
			
				cnt++;
			}
			
			
			di.setVisible(true);//다이얼로그 View true
			
		}
		else if(ae.getSource() == closeDi){ // 다이얼로그 종료 요청 버튼이면.
			di.setVisible(false);//다이얼로그 View false			
		}
	}

	public static void main(String[] args) {
		// 관리자 gui 생성
		OmokmanagerStart OC = new OmokmanagerStart("OMOKserver");
		OC.setSize(770, 550);
		OC.setVisible(true);
		// 오목서버 객체 생성
		server = new gameServer_OMOK(OC);
	}

	@Override
	public void CON_Play(String s) {
		Peaple = s;
		System.out.println("CONPeaple" + s);
		nameList(Peaple);
	}

	public void Game_XY(String z) {
		xy = z;
		System.out.println("xy=" + z);
		xymsg(xy);
	}
	
	public void CHAT(String c){
		CHAT_set(c);
	}

	private void nameList(String ID) {
		pList.removeAll();
		StringTokenizer st = new StringTokenizer(ID, "\t");
		while (st.hasMoreElements())
			pList.add(st.nextToken()+"\n");

	}

	private void xymsg(String xy) {
		StringTokenizer st = new StringTokenizer(xy, "\t");
		while (st.hasMoreElements())
			msgView.append(st.nextToken()+"\n");

	}
	
	private void CHAT_set(String c){
		StringTokenizer st = new StringTokenizer(c, "\t");
		while (st.hasMoreElements())
			msgView.append(st.nextToken()+"\n");
	}
	// ■■■■■■■■■■■■■■■■■■■■■■■■■■■서버 GUI E■■■■■■■■■■■■■■■■■■■■■■■■■■■
}
