package dipper;
import java.net.*;
import java.util.*;
public class Server {
	ArrayList<Socket> aList=new ArrayList<Socket>();
	ServerSocket ss;
	Socket socket;
	Map<Socket,MyPair> map=new HashMap<Socket, MyPair>();
	public Server() {
		try {
			ss= new ServerSocket(80);
			System.out.println("Server Started");
			while(true)
			{
				socket=ss.accept();
				System.out.println("Client Connected");
				aList.add(socket);
				Runnable runnable=new ClientThread(socket,aList,map);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		} catch (Exception e) {
			System.out.println("Exception inside Server Constructor :"+e);
		}
	}				
	public static void main(String[] args) {
		new Server();
	}
}
