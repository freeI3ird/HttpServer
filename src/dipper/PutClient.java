package dipper;

import java.net.*;
import java.io.*;
public class PutClient {
	Socket socket ;
	public PutClient(int connId) {
		String requestUrl="http://localhost:80/api/kill";
		String payload="{\"connId\":"+connId+"}";
		sendPuttRequest(requestUrl,payload);
	}		
	public static void sendPuttRequest(String requestUrl, String payload) {
    try {
	        URL url = new URL(requestUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("PUT");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	       	OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	        writer.write(payload);
	       	writer.close();
	       	BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	       	String line;
	       	while ((line = br.readLine()) != null) {
       		         System.out.println(line);
        	}
        	br.close();
        	connection.disconnect();
    	} catch (Exception e) {
            System.out.println("Exception occured during Put method:"+e);
    	}
	}
	public static void main(String[] args) {
		if(args.length==0)
		{
			System.out.println("Please Enter the ConnId and try again");
			System.exit(0);
		}
		int connid=Integer.parseInt(args[0]);
		new PutClient(connid);
	}

}
