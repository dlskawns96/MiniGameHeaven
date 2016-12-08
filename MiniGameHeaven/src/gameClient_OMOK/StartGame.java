package gameClient_OMOK;

import gameClient_OMOK.loginOMOK;
import gameClient_OMOK.StartGame;
import gameClient_OMOK.ClientOMOK_player;
import gameClient_OMOK.ClientOMOK_watcher;

public class StartGame {

	loginOMOK loginView;
	ClientOMOK_player clientview;
	private loginOMOK loginview;//서버,클라이언트 양쪽에 loginFrame생성
	
	public static void main(String [] args){
		StartGame main = new StartGame();
		main.loginview=new loginOMOK(main);
		
	}
}
