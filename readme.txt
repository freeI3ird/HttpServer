/*************************************************\
	Author  : Manish Rathi
	College : National Institute of Technology
	Contact :  +91 8368701293,+91 9958814432
\************************************************/

Project :Implementation of a socket server accepting HTTP requests.(JAVA BASED)
Requirements :
	1. Use should JDK and JRE installed in your system(JRE 1.8 preferable)
	2. set the path of your system to "bin folder of JDK"
		e.g  set path=C:\Program Files\Java\jdk1.8.0_91\bin;

Lets Start :
1. go to the folder "httpServer.zip-->httpServer"(folder exactly where you find this readme file)
2. this folder contains three things a)dipper (contains class files) b) src(source files) c) readme
3. Now open the cmd in this directory (you can simply type "cmd" on Address bar of current folder or open cmd and change directory to current folder).

4. type command : java dipper.Server (It will Start the Server,running on port 80)
5. Open your Browser(Use Chrome) :type the URl : "http://www.localhost:80/api/request?connId=19&timeout=80" 
   this was the first task - Exposes a GET API as "api/request?connId=19&timeout=80":
   "This API will keep the request running for provided time on the server side. 
   After the successful completion of the provided time it should return {"status":"ok"}"

=>Second task :- Exposes a GET API as "api/serverStatus" :
   "This API returns all the running requests on the server with their time left for completion.
   E.g {"2":"15","8":"10"} where 2 and 8 are the connIds and 15 and 10 is the time remaining for the requests to complete (in seconds)."
   type the URl : "http://www.localhost:80/api/serverStatus"

=>Third task :Exposes a PUT API as "api/kill" with payload as {"connId":12} :
   "This API will finish the running request with provided connId, so that the finished request returns {"status":"killed"} 
    and the current request will return {"status":"ok"}.
    If no running request found with the provided connId on the server then the current request should 
    return "status":"invalid connection Id : <connId>"}"
   For this PUT request - open another command prompt in same folder
   - type command : "java dipper.PutClient 12"   (to make a put request to the Server,12 is connId you have to give through command line)

 Note : I have not hard coded the connection id,timeout you can give any value of your choice.Waiting for your httpResponse :)