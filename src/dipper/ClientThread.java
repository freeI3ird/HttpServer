package dipper;
import java.io.*;
import java.net.*;
import java.util.*;
public class ClientThread implements Runnable {
	Socket socket;
	ArrayList<Socket> alList;
	Map<Socket,MyPair> map;
	static PrintStream pout;
	static BufferedReader br;
	public ClientThread(Socket socket,ArrayList<Socket> aList,Map<Socket, MyPair> map) {
		this.socket=socket;
		this.alList=aList;
		this.map=map;
	}
	public void run() {
		try {
			Long startTime=System.currentTimeMillis();
			InputStream is = socket.getInputStream();
			InputStreamReader isReader = new InputStreamReader(is);
			br = new BufferedReader(isReader);
			String headerLine= br.readLine();
			System.out.println(headerLine);
			String[] tokens =headerLine.split("[\\s]+");
			if(tokens[1].equals("/"))
				errorResponse(socket);// error 404 no resource
			else{
				String[] uritoken=tokens[1].split("[/?]+");
				if(uritoken.length==3)//second and third cases
				{
					if(!uritoken[1].equals("api"))
						errorResponse(socket);
					else if(uritoken[2].equals("serverStatus"))
					{
						String response= currentStatus();
						closeConnection(socket, response);
					}
					else if (uritoken[2].equals("kill"))
					{
						//put method
						int connectionId= putMethod();
						int flag=0;// flag shows if there is some specified connId or not
						for(Iterator<Map.Entry<Socket, MyPair>> it = map.entrySet().iterator(); it.hasNext(); ) {
						     Map.Entry<Socket, MyPair> entry = it.next();
						     MyPair pair=entry.getValue();
						     if(pair.connId==connectionId)
						     {
						    	  //kill the process
						    	 flag=1;
						    	 Socket sc= entry.getKey();
						    	 Map<String,String> mymap = new HashMap<String,String>();
						    	 mymap.put("\"status\"", "\"killed\"");
						    	 closeConnection(sc, mymap);
						    	 Map<String,String> mymap2 = new HashMap<String,String>();
						    	 mymap2.put("\"status\"", "\"OK\"");
						    	 closeConnection(socket,mymap2);
						    	 break;
						     }   
						 }
						if(flag==0)
						{
							String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" ;
							pout=new PrintStream(socket.getOutputStream());
							pout.print(httpResponse);
							String response="{\"status\":\"invalid connection Id :"+connectionId +" \"}";
							pout.println(response);
							if(alList.contains(socket))
								alList.remove(socket);
							if(map.containsKey(socket))
								map.remove(socket);
							pout.close();
							socket.close();
						}
					}
					else {
						errorResponse(socket);// 404 resource not found
					}
				}
				else if(uritoken.length==4)//case 1
				{
					//read parameters
					if(!uritoken[1].equals("api")||!uritoken[2].equals("request")||(!headerLine.contains("?")))
						errorResponse(socket);
					else {
						String[] param=uritoken[3].split("[&]+");
						int connId=-1,timeout=-1;
						for (String parameter : param)
						{
							String[] pairStrings=parameter.split("[=]+");
							if(pairStrings.length!=2)
								errorResponse(socket);
							String key= pairStrings[0];
							String value= pairStrings[1];
							if(key.equals("connId"))
								connId=Integer.parseInt(value);
							else if(key.equals("timeout"))
								timeout=Integer.parseInt(value);
						}
						if(connId==-1||timeout==-1)
							errorResponse(socket);
						map.put(socket, new MyPair(connId,timeout,startTime));				
						Thread.sleep(timeout*1000);
						Map<String,String> mymap = new HashMap<String,String>();
						mymap.put("\"status\"", "\"ok\"");
						closeConnection(socket,mymap);
					}		
				}
				else
				{
					errorResponse(socket);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception occured in Client_Thread:"+e);
		}
	}
	public void closeConnection(Socket socket,Object ob)throws IOException {
		String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" ;
		pout=new PrintStream(socket.getOutputStream());
		pout.print(httpResponse);
		pout.println(ob);
		if(alList.contains(socket))
			alList.remove(socket);
		if(map.containsKey(socket))
			map.remove(socket);
		pout.close();
		socket.close();
	}
	public String currentStatus()
	{
		Iterator<Socket> itr=alList.iterator();
		int len= alList.size();
		String response="{";
		int counter=1;
		while(itr.hasNext())
		{
			Socket sc=(Socket)itr.next();
			if(!map.containsKey(sc))
			{
				counter++;continue;
			}
			MyPair value= map.get(sc);
			Long currentTime=System.currentTimeMillis();
			Long timeLeft= value.timeout*1000-(currentTime-value.startTime);
			if(counter==len-1)
				response+="\"" + value.connId + "\":\""+timeLeft/1000+"\"";
			else
				response+="\"" + value.connId + "\":\""+timeLeft/1000+"\",";
			counter++;
		
		}
		response+="}";
		return response;
	}
	public int putMethod()throws IOException {
		//code to read and print headers
		String headerLine = null;
		while((headerLine = br.readLine()).length() != 0){
		//	System.out.println(headerLine);
		}
		//code to read the post payload data
		StringBuilder payload = new StringBuilder();
		while(br.ready()){
			payload.append((char) br.read());
		}
		String string=payload.toString();
		String[] tokens= string.split("[:]");
		String[] param= tokens[1].split("[}]");
		return Integer.parseInt(param[0]);
	}
	public void errorResponse(Socket socket)throws IOException {
		//HTTP/1.1 404 Not Found
		String httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n" ;
		pout=new PrintStream(socket.getOutputStream());
		pout.print(httpResponse);
		if(alList.contains(socket))
			alList.remove(socket);
		if(map.containsKey(socket))
			map.remove(socket);
		pout.close();
		socket.close();
	}
}
