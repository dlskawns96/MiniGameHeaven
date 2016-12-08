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
	// ���������������������������Ἥ�� GUI S����������������������������
	private TextArea msgView = new TextArea("", 1, 1, 1); // �޽����� �����ִ� ����:�������
	private TextField sendBox = new TextField(""); // ���� �޽����� ���� ����:id �Է�

	// �濡 ������ �ο��� ���� �����ִ� ���̺�
	private Label pInfo = new Label("������:  ��");

	private String Peaple = "not";
	private String xy = "not";
	private java.awt.List pList = new java.awt.List(); // ����� ����� �����ִ� ����Ʈ
	private Button serverStartButton = new Button("���� ����"); // �뱹 ���� ��ư
	private Button gameDataButton = new Button("������� ����");// �������
	private Vector gameRecord; // JDBC�޼��忡�� ������ ���� �����ϱ� ���� collection
	// ���� ������ �����ִ� ���̺�
	private Label infoView = new Label("��Ʈ��ũ ���� ���� ver 5.1", 1);
	private OmokPan pan; // ������ ��ü
	private Dialog di;
	private static TextArea gameResult;
	private Button closeDi;

	public OmokmanagerStart(String title) { // ������
		super(title);
		pan = new OmokPan();
		setLayout(null); // ���̾ƿ��� ������� �ʴ´�.
		// ���� ������Ʈ�� �����ϰ� ��ġ�Ѵ�.
		msgView.setEditable(false);
		// Ÿ��Ʋ ����
		infoView.setBounds(10, 60, 470, 30);
		infoView.setBackground(new Color(200, 200, 255));
		pan.setLocation(10, 70);
		add(infoView);
		add(pan);
		// �α���
		Panel p = new Panel();
		p.setBackground(new Color(200, 255, 255));
		p.setLayout(new GridLayout(3, 3));
		p.add(new Label("������", 1));
		p.setBounds(500, 60, 250, 30);
		
		
		/* ��������� �������� dialog ���� �� ���� */
		di = new Dialog(this, "�������", true);// ���̾�α� ����
		gameResult = new TextArea("", 50, 120);// ������� Viewâ
		gameResult.setEditable(false);
		closeDi = new Button("�ݱ�");// ���̾�α׸� �ݱ� ���� ��ư
		closeDi.addActionListener(this);
		gameDataButton.addActionListener(this);
		di.setLayout(new BorderLayout());

		di.add(gameResult, BorderLayout.CENTER);
		di.add(closeDi, BorderLayout.SOUTH);
		di.pack();
		// *************************************//
		// ������
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
		// ä��
		Panel p3 = new Panel();
		p3.setBackground(new Color(200, 255, 255));
		p3.setLayout(new BorderLayout());
		p3.add(msgView, "Center");
		p3.add(sendBox, "South");
		p3.setBounds(500, 205, 250, 320);

		add(p);
		add(p2);
		add(p3);

		// �̺�Ʈ �����ʸ� ����Ѵ�.
		sendBox.addActionListener(this);
		serverStartButton.addActionListener(this);

		// ������ �ݱ� ó��
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
			// ���� ���� ����
			infoView.setText("������ �����߽��ϴ�.");
			serverStartButton.setEnabled(false);
			server.startServer();

		}else if(ae.getSource() == gameDataButton){//������� ���� ��ư�̸�
			
			MySQL CDB = new MySQL();//JDBC Ŭ���� ����
			gameRecord = CDB.printResult();//JDBCŬ���� ������ �޼��带 �����ϰ� return�� �����ϱ� ���� vector
			Enumeration e = gameRecord.elements();//Enumeration��ü�� �̿��� vector�� elements�� ��� ����
			
			gameResult.setText("���� ���....");//gameResultâ�� ���� ���
			gameResult.append("\n");
			
			
			int cnt = 1;
			while(e.hasMoreElements()){//�ݺ����� ���� vector�� ����� ���� �ϳ��� ���.
				gameResult.append("\n");
				gameResult.append("[��� ��ȣ] : ");
				gameResult.append((String) e.nextElement());
				gameResult.append("\n");
				gameResult.append("[����] : ");
				gameResult.append((String) e.nextElement()+" ");
				gameResult.append("[���۽ð�] : ");
				gameResult.append((String) e.nextElement()+" ");
				gameResult.append("[����ð�] : ");
				gameResult.append((String) e.nextElement()+" ");
				gameResult.append("[����ð�] : ");
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
			
			
			di.setVisible(true);//���̾�α� View true
			
		}
		else if(ae.getSource() == closeDi){ // ���̾�α� ���� ��û ��ư�̸�.
			di.setVisible(false);//���̾�α� View false			
		}
	}

	public static void main(String[] args) {
		// ������ gui ����
		OmokmanagerStart OC = new OmokmanagerStart("OMOKserver");
		OC.setSize(770, 550);
		OC.setVisible(true);
		// ���񼭹� ��ü ����
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
	// ���������������������������Ἥ�� GUI E����������������������������
}
