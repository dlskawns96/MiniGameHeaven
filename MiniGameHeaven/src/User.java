import java.util.Vector;

public class User {

	private String ID = null;
	private String IP = null;
	private int state = 0;
	// initial value
	public int numOfHeart = 5;

	User() {

	}

	public void setID(String id) {
		this.ID = id;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getID() {
		return ID;
	}

	public String getIP() {
		return IP;
	}

	public void subHeart() {
		numOfHeart--;
	}

	public void addHeart() {
		numOfHeart++;
	}

}
