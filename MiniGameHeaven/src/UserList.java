import java.util.HashMap;
import java.util.Iterator;
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

	public String keySet() {
		// Iterator<String> iterator = userList.keySet().iterator();
		// while (iterator.hasNext()) {
		// String key = (String) iterator.next();
		System.out.println(userList.keySet().toString() + "아이디 뿌리는거\n");
		//
		return userList.keySet().toString();
	}

}
