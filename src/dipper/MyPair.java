package dipper;
public class MyPair {
	int timeout;
	int connId;
	Long startTime;
	MyPair(int a,int b,Long c)
	{
		startTime=c;
		timeout= b;
		connId=a;
	}
}
