package chatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class LobbyThread extends Thread{
	private ServerSocket serverSocket;
	private Set<LobbyThreadThread> lobbyThreadThreads = new HashSet<LobbyThreadThread>();
	public LobbyThread(String portNumb) throws IOException{
		serverSocket = new ServerSocket(Integer.valueOf(portNumb));
	}
	public void run() {
		try {
			while(true) {
				LobbyThreadThread lobbyThreadThread = new LobbyThreadThread(serverSocket.accept(), this);
				lobbyThreadThreads.add(lobbyThreadThread);
				lobbyThreadThread.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	void sendMessage(String message) {
		try {
			lobbyThreadThreads.forEach(t -> t.getPrintWriter().println(message));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Set<LobbyThreadThread> getLobbyThreadThreads(){ return lobbyThreadThreads; }
}
