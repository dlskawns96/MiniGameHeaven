import java.util.HashMap;
import java.util.Vector;

public class UserList {

	private HashMap<String, User> userList = new HashMap<String, User>();

	public void addUserList(String id, User user) {
		userList.put(id, user);
	}

	public boolean check(String id) {
		if (userList.containsKey(id.toString()) == false)
			return true;
		return false;
	}
	/*public void  dd{
		System.out.println(userList.);
	}*/
}
