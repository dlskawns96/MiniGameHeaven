package gameClient_OMOK;

import gameClient_OMOK.loginOMOK;
import gameClient_OMOK.StartGame;
import gameClient_OMOK.ClientOMOK_player;
import gameClient_OMOK.ClientOMOK_watcher;

public class StartGame {

	loginOMOK loginView;
	ClientOMOK_player clientview;
	private loginOMOK loginview;//����,Ŭ���̾�Ʈ ���ʿ� loginFrame����
	
	public static void main(String [] args){
		StartGame main = new StartGame();
		main.loginview=new loginOMOK(main);
		
	}
}
