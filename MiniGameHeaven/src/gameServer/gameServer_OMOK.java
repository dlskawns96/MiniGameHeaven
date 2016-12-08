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
	// ������ ������ ��� ���� SS����
	private ServerSocket SS;
	// Ŭ���̾�Ʈ�� ����� �����带 ������� ThreadSoket ����
	private Socket TS;
	private TempMessage OM = new TempMessage(); // �޽��� �޼��� ��ü
	private Random rnd = new Random(); // ��� ���� ���Ƿ� ���ϱ� ���� ����
	private Omok_Thread OT;
	
	/*��¥��� ����*/
	private SimpleDateFormat fm = new SimpleDateFormat("yyyy�� MM�� dd�� HH��MM�� ss��");//��¥���� ���� ��ü ����
	private tempDBinformation TDI = new tempDBinformation();//���� ��� �����͸� �����ϱ� ���� ��ü
	private long startTime,endTime;//���۽ð�, ���ð�
	
	/*JDBC������ ���� ����*/
	private MySQL ms = new MySQL();//JDBC connect �� query������ ���� ��ü
	private Hashtable insertDate = new Hashtable(); //���� ������� �����ϱ� ���� collection
	private Vector userList=new Vector();//user ���� �����ϱ� ���� ����
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
	void startServer() { // ������ ����
		try {
			// ���� port�� ��Ʈ��ȣ�� ������ ServerSocket ��ü�� ����
			SS = new ServerSocket(7777);
			
			
			while (true) {

				// �����ڿ� ����� �����带 ����
				TS = SS.accept();

				// ������ ����
				OT = new Omok_Thread(TS);

				// ������ ����
				OT.start();

				// OM�� �����带 �߰��Ѵ�.
				OM.add(OT);

				System.out.println("������ ��: " + OM.size());
				
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	// Ŭ���̾�Ʈ�� ����ϴ� ������ innerŬ����
	class Omok_Thread extends Thread {
		
		//private int Rnum = -1;
		private int Rnum=-1;
		// ������� �̸��� �����ϴ� ����
		private String ID = null; // ����� �̸�
		// �� ������� ������ �����ϴ� ����
		private Socket socket; // ����

		// ������� �غ� ���¸� üũ�ϴ� ����
		// �뱹 ������ �������� true
		// �뱹 ������ ������ �ʾ����� false
		private boolean ready_state = false;

		private BufferedReader reader; // �Է� ��Ʈ��
		private PrintWriter writer; // ��� ��Ʈ��

		Omok_Thread(Socket socket) {
			this.socket = socket;
		}

		// ���� getter
	
		Socket getSocket() {
			return socket;
		}

		// ���ȣ getter
		int getRnum() {
			return Rnum;
		}

		// ID getter
		String getID() {
			return ID;
		}

		// ������� �غ� ���� getter
		boolean isready_state() {
			return ready_state;
		}

		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
				
				String msg; // Ŭ���̾�Ʈ�� �޽���

				while ((msg = reader.readLine()) != null) {
					
					// msg�� "[NAME]"���� ���۵Ǵ� �޽����̸�
					if (msg.startsWith("[NAME]")) {
						ID = msg.substring(6); // ID�� ���Ѵ�.
					}

					// msg�� "room_msg"���� ���۵Ǹ� �� ��ȣ�� ���Ѵ�.
					else if (msg.startsWith("room_msg")) {
						if(msg.substring(8).startsWith("_fromWatcher"))//watcher ������ �� �����
						{
							int roomNum=Integer.parseInt(msg.substring(20));
						}
						int roomNum = Integer.parseInt(msg.substring(8));
						
						if (!OM.isFull(roomNum)) { // ���� �� ���°� �ƴϸ�

							// ���� ���� �ٸ� ��뿡�� ������� ������ �˸���.
							if (Rnum != -1)
								OM.sendToOthers(this, "[EXIT]" + ID);

							// ������� �� �� ��ȣ�� �����Ѵ�.
							Rnum = roomNum;

							// ����ڿ��� �޽����� �״�� �����Ͽ� ������ �� ������ �˸���.
							writer.println(msg);

							// ����ڿ��� �� �濡 �ִ� ����� �̸� ����Ʈ�� �����Ѵ�.
							writer.println(OM.getNamesInRoom(Rnum));
//�ڡڡڡڡڡڡڡڼ����� ������ �������̵�
							System.out.println(OM.getNamesInRoom(1));
							handler.CON_Play(OM.getNamesInRoom(1));
							if(ID_set!=null){
								ID_set.append(OM.getNamesInRoom(1));
								ID_set.invalidate();
							}

							// �� �濡 �ִ� �ٸ� ����ڿ��� ������� ������ �˸���.
							OM.sendToOthers(this, "[ENTER]" + ID);
						} else
							writer.println("[FULL]"); // ����ڿ� ���� á���� �˸���.
					}

					// "[STONE]" �޽����� ������� �����Ѵ�.
					else if (Rnum >= 1 && msg.startsWith("[STONE]")){
						OM.sendToOthers(this, msg);
//�ڡڡڡڡڡڡڡڵ��� ��ġ
						
						if(doll==0){
							handler.Game_XY("�浹��ǥ"+msg);doll=1;
							String temp = msg.substring(7);
							
							int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
							int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
							

//							���� �׷�����. x,y�� ���� ��ǥ
							doll=1;
						}
						else{
							handler.Game_XY("�鵹��ǥ"+msg);
							String temp = msg.substring(7);
							
							int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
							int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
							

//							���� �׷�����. x,y�� ���� ��ǥ
							
							doll=0;
						}
					}
					// ��ȭ �޽����� �濡 �����Ѵ�.
					else if (msg.startsWith("[MSG]")){
						OM.sendToRoom(Rnum, "[" + ID + "]: " + msg.substring(5));
						handler.CHAT("[" + ID + "]: " + msg.substring(5));
					}
					// "[START]" �޽����̸�
					else if (msg.startsWith("[START]")) {
						ready_state = true; // ������ ������ �غ� �Ǿ���.

						// Rnum���� �ٸ� ����ڵ� ������ ������ �غ� �Ǿ����� ���ӽ��� ��������
						if (OM.isReady(Rnum)) {
							TDI.setStartDate(fm.format(new Date()));//����ڵ��� ���� �غ� �Ϸ�Ǹ� ���� �ð��� set
							
							/*�ð����� ����ϱ� ���� currentTime*/
							try {
								startTime = System.currentTimeMillis();//���� �ð��� ������ ����.
							} catch (Exception e) {
								e.printStackTrace();
							}
							//****************************************************
							
							// ��� ���� ���ϰ� ����ڿ� ������� �����Ѵ�.
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

					// ����ڰ� ������ �����ϴ� �޽����� ������
					else if (msg.startsWith("[STOPGAME]")){
						ready_state = false;
						handler.CHAT("������ �����Ǿ����ϴ�.");
					}
					// ����ڰ� ������ ����ϴ� �޽����� ������
					else if (msg.startsWith("[DROPGAME]")) {
						TDI.setEndDate(fm.format(new Date()));//������ ����Ǵ� ����
						String gameMsg = ID+"�� ���ó��";//���ó���� ��� ������� �޽����� ID�� ����.
						TDI.setWinner(gameMsg);//����� ��� ���ó�� ����ڸ� ����.
						
						/*�ð����� ����ϱ� ���� currentTiem*/
						try {
							endTime = System.currentTimeMillis();//��Ⱑ ������ �� �ð��� ������ ����.
						} catch (Exception e) {
							e.printStackTrace();
						}
						/*�ð� ��� �۾�*/
						long sumTime = endTime-startTime;//���۽ð��� ���ð��� ���� ���
						long MTime = (sumTime / 1000) / 60;//�� ���
						long STime = (sumTime / 1000) % 60;//�� ���
						String playdate = Integer.toString((int)MTime)+"��"+Integer.toString((int)STime)+"��";
						//���� ���� �̿��� *�� *�ʰ� ����Ǿ����� String���� ����.
						
						insertDate.put("winner",TDI.getWinner());//collection�� �̿��� ���� �����͸� ����
						insertDate.put("startdate",TDI.getStartDate());
						insertDate.put("enddate",TDI.getEndDate());
						insertDate.put("palydate",playdate);
						
						ms.insertGameResult(insertDate);//����� collection�� ������ ���� �Լ��� ���ڰ����� �ش�.
						
						
				
						 
					}
					else if(msg.startsWith("[RESTARTALL]")){
						OM.sendToOthers(this, "[RESTART]");
					}
					
					// ����ڰ� �̰�ٴ� �޽����� ������
					else if (msg.startsWith("[WIN]")) {
						
						TDI.setEndDate(fm.format(new Date()));//������ ����Ǵ� ����
						TDI.setWinner(ID);//������ ���̵� ����
						
						/*�ð����� ����ϱ� ���� currentTiem*/
						try {
							endTime = System.currentTimeMillis();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						/*�ð� ��� �۾�*/
						long sumTime = endTime-startTime;
						long MTime = (sumTime/ 1000)/60;
						long STime = (sumTime/1000)%60;
						String playdate = Integer.toString((int)MTime)+"��"+Integer.toString((int)STime)+"��";
						
						insertDate.put("winner",TDI.getWinner());
						insertDate.put("startdate",TDI.getStartDate());
						insertDate.put("enddate",TDI.getEndDate());
						insertDate.put("palydate",playdate);
						
						ms.insertGameResult(insertDate);

						
						
						ready_state = false;
						// ����ڿ��� �޽����� ������.
						writer.println("[WIN]");

						// ������� ������ �˸���.
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
					
					System.out.println(ID + "�� ����");
					handler.CHAT(ID + "�� ����");
					System.out.println("���� " + OM.size() + "�� ���� �� �Դϴ�.");
					handler.CHAT("���� " + OM.size() + "�� ���� �� �Դϴ�.");
					/*for(int i=0;i<OM.size();i++)
					{
						System.out.print(" [�����"+i+"]"+OM.elementAt(i));
					}*/
					// ����ڰ� ������ �������� ���� �濡 �˸���.
					OM.sendToRoom(Rnum, "[DISCONNECT]" + ID);
				} catch (Exception e) {
				}
			}
		}
	}

}
