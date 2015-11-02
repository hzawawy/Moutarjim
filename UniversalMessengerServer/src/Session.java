import java.net.InetAddress;


class Session{
	InetAddress IPAddress;
	int port;
	public Session(InetAddress ipAddress, int Port){
		IPAddress = ipAddress;
		port = Port;
	}
	InetAddress getIPAddress(){
		return IPAddress;
	}
	int getPort(){
		return port;
	}
}