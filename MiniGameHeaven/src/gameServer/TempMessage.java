package gameServer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import gameServer.gameServer_OMOK.Omok_Thread;

@SuppressWarnings("serial")
class TempMessage extends Vector<Object> { // �޽����� �����ϴ� Ŭ����
	StringBuffer sb;
	void add(Omok_Thread ot) { // �����带 �߰��Ѵ�.
		super.add(ot);
	}

	void remove(Omok_Thread ot) { // �����带 �����Ѵ�.
		super.remove(ot);
	}

	Omok_Thread getOT(int i) { // i��° �����带 ��ȯ�Ѵ�.
		return (Omok_Thread) elementAt(i);
	}

	Socket getSocket(int i) { // i��° �������� ������ ��ȯ�Ѵ�.
		return getOT(i).getSocket();
	}

	// i��° ������� ����� Ŭ���̾�Ʈ���� �޽����� �����Ѵ�.
	void sendTo(int i, String msg) {
		try {
			PrintWriter pw = new PrintWriter(
					getSocket(i).getOutputStream(), true);
			pw.println(msg);
		} catch (Exception e) {
		}
	}

	int getRnum(int i) { // i��° �������� �� ��ȣ�� ��ȯ�Ѵ�.
		return getOT(i).getRnum();
	}

	synchronized boolean isFull(int roomNum) { // ���� á���� �˾ƺ���.
		if (roomNum == 0)
			return false; // ������ ���� �ʴ´�.

		// �ٸ� ���� 2�� �̻� ������ �� ����.
		int count = 0;
		for (int i = 0; i < size(); i++)
			if (roomNum == getRnum(i))
				count++;
		if (count >= 2)
			return true;
		return false;
	}

	// ���ӹ濡 msg�� �����Ѵ�.
	void sendToRoom(int roomNum, String msg) {
		for (int i = 0; i < size(); i++)
			if (roomNum == getRnum(i))
				sendTo(i, msg);
	}

	// ot�� ���� �濡 �ִ� �ٸ� ����ڿ��� msg�� �����Ѵ�.
	void sendToOthers(Omok_Thread ot, String msg) {
		for (int i = 0; i < size(); i++)
			if (getRnum(i) == ot.getRnum() && getOT(i) != ot)
				sendTo(i, msg);
	}

	// ������ ������ �غ� �Ǿ��°��� ��ȯ�Ѵ�.
	// �� ���� ����� ��� �غ�� �����̸� true�� ��ȯ�Ѵ�.
	synchronized boolean isReady(int roomNum) {
		int count = 0;
		for (int i = 0; i < size(); i++)
			if (roomNum == getRnum(i) && getOT(i).isready_state())
				count++;
		if (count == 2)
			return true;
		return false;
	}

	// roomNum�濡 �ִ� ����ڵ��� �̸��� ��ȯ�Ѵ�.
	String getNamesInRoom(int roomNum) {
		sb = new StringBuffer("[PLAYERS]");
		for (int i = 0; i < size(); i++)
			if (roomNum == getRnum(i))
				sb.append(getOT(i).getID() + "\t");
		return sb.toString();
	}
}