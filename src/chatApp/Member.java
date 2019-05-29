 package chatApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

import javax.json.Json;

public class Member {

	public static void main(String[] args) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter username and port number:");
		String[] setupValues = bufferedReader.readLine().split(" ");
		LobbyThread lobbyThread = new LobbyThread(setupValues[1]);
		lobbyThread.start();
		new Member().updateListenToMembers(bufferedReader, setupValues[0], lobbyThread);
	}
	public void updateListenToMembers(BufferedReader bufferedReader, String username, LobbyThread lobbyThread) throws Exception{
		System.out.println("Enter hostname and port number of members you want to receive messages from: ");
		String input = bufferedReader.readLine();
		String[] inputValues = input.split(" ");
		if (!input.equals("q")) for (int i=0; i<inputValues.length;i++) {
			String[] address = inputValues[i].split(":");
			Socket socket = null;
			try {
				socket = new Socket(address[0], Integer.valueOf(address[1]));
				new MemberThread(socket).start();
			} catch(Exception e){
				if(socket != null) socket.close();
				else System.out.println("Wrong input...Moving to next step.");
			}
		}
		communicate(bufferedReader, username, lobbyThread);
	}
	public void communicate(BufferedReader bufferedReader, String username, LobbyThread lobbyThread) {
		try {
			System.out.println("You can now chat! Enter e to exit and c to change who you want to talk to.");
			boolean flag = true;
			while(flag) {
				String message = bufferedReader.readLine();
				if(message.equals("e")) {
					flag = false;
					break;
				}else if (message.equals("c")) {
					updateListenToMembers(bufferedReader, username, lobbyThread);
				} else {
					StringWriter stringWriter= new StringWriter();
					Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
													.add("username", username)
													.add("message", message)
													.build());
					lobbyThread.sendMessage(stringWriter.toString());
				}

				
			}
			System.exit(0);
		}catch(Exception e) {}
	}
}
