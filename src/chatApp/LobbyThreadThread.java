package chatApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LobbyThreadThread extends Thread{
	private LobbyThread lobbyThread;
	private Socket socket;
	private PrintWriter printWriter;
	public LobbyThreadThread(Socket socket, LobbyThread lobbyThread) {
		this.lobbyThread = lobbyThread;
		this.socket=socket;
	}
	
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.printWriter = new PrintWriter(socket.getOutputStream(), true);
			while(true) lobbyThread.sendMessage(bufferedReader.readLine());
		}catch(Exception e) {
			lobbyThread.getLobbyThreadThreads().remove(this);
		}
	}
	
	public PrintWriter getPrintWriter() {
		return printWriter;
		}
}
