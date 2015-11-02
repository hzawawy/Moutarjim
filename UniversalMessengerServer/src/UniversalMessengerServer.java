import java.net.*; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Define the format of the message sent by the sender:
 * action,userid,...
 * 
 * NOTE: no comma allowed in any username, password, text, etc.. replace comma's by something else
 * 
 * Request:																Response to sender										Response to receiver
 * senderuserid,signup,password,language,firstname,lastname				serverid,signupsuccess/signupfailure					N/A
 * senderuserid,signin,password											serverid,signinsuccess/signinfailure					N/A
 * senderuserid,send,text between quotes --> no comma inside text		serverid,sendsuccess/sendfailure						serverid,send,serverid,senderid,text with no quotes						
 * senderuserid,search,otheruserid										serverid,searchsucess/searchfound						N/A							
 * senderuserid,list													serverid,listsuccess,friend1userid,friend2userid,... serverid,listfailure    N/A
 * senderuserid,addfriend,friendid										serverid,addsucess/addfailure
 * 	option 1: sender sends userId as the first keyword --> for now use this
 * 	option 2: sender always sends a secret hashed id --> safest
 * 	option 3: sender does not need to send his id.. we identify the sender by the IP adderss and port after they established session
 * 
 * @author hamzehzawawy
 *
 */

public class UniversalMessengerServer {    
	String ERROR = Messages.getString("UniversalMessengerServer.error"); //$NON-NLS-1$
	Map<String, User> users = new HashMap<String, User>();
	Map<String, Session> sessions = new HashMap<String, Session>();
	
	private DatagramSocket serverSocket;
	int timeout = 8000;
	
	public static void main(String[] args){
		UniversalMessengerServer client = new UniversalMessengerServer();
		client.runEchoServerOnComputer();
	}
	
	public void runEchoServerOnComputer(){
		
//		TODO's
//		in case someone is signing up:
//			- if new id, then register this id
//			- else find him in the list and say already registered
//		is case someone is signin up:
//			- search for them, if not found, say not found
//			- else, sign them which means open a session for them to send and receive: 
//					like give them a token to use
		
		if (serverSocket==null) {
			try {
				serverSocket = new DatagramSocket(9876);  
				serverSocket.setReuseAddress(true);
//				serverSocket.setSoTimeout(timeout);
			} catch(SocketException se){
				se.printStackTrace();
			}
		}
		byte[] receiveData = new byte[1024];             
		byte[] sendData = new byte[1024];             
		DatagramPacket receivePacket;
		DatagramPacket sendPacket;
		while(true) {  
			receivePacket = new DatagramPacket(receiveData, receiveData.length); 
			try {
				serverSocket.receive(receivePacket);  
			} catch (Exception e){
				System.out.println(e.getMessage());
				break;
			}
			String sentenceRecvd = new String(receivePacket.getData());    
			InetAddress senderIPAddress = receivePacket.getAddress();                   
			int senderPort = receivePacket.getPort();                   
			Bag bag = handleRequest(sentenceRecvd, senderIPAddress, senderPort);
			if (bag.getSenderResponse()!=null){
				sendData = ("serverid,"+bag.getSenderResponse()).getBytes();             
				sendPacket = new DatagramPacket(sendData, sendData.length, bag.getSenderSession().getIPAddress(), bag.getSenderSession().getPort());                   
				try {
					serverSocket.send(sendPacket);
				} catch(Exception e){
					System.out.println(e.getMessage());
				}
				// if applicable, forward to destination
				if (bag.getDestinationResponse()!=null){
					sendData = ("serverid,"+bag.getDestinationResponse()).getBytes();             
					sendPacket = new DatagramPacket(sendData, sendData.length, bag.getDestinationSession().getIPAddress(), bag.getDestinationSession().getPort());                   
					try {
						serverSocket.send(sendPacket);
					} catch(Exception e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
		serverSocket.close();
	}
	
	/**
	 * @param request
	 * @param IPAddress
	 * @param port
	 * @return
	 */
	private Bag handleRequest(String request, InetAddress IPAddress, int port){
		if (request==null) {
			return new Bag(ERROR, null, new Session(IPAddress, port), null);
		}
		StringTokenizer tokenizer = new StringTokenizer(request, ",");
		
		 /* Incoming: signup,senderuserid,password,language,firstname,lastname				
		 * Outgoing to sender: signupsuccess/signupfailure,serverid
		 * Outgoing to other: N/A*/

		if (request.contains(Messages.getString("UniversalMessengerServer.signup"))){
			String userId = tokenizer.nextToken();
			tokenizer.nextToken();//signup keyword
			String password = tokenizer.nextToken();
			String language = tokenizer.nextToken();
			String first = tokenizer.nextToken();
			String last = tokenizer.nextToken();
			Session senderSession = null;
			if (first==null || last==null || password==null){
				if (userId!=null){
					senderSession = sessions.get(userId);
				} else {
					senderSession = new Session(IPAddress, port);
				}
				return new Bag(ERROR, null, senderSession, null);
			} else if (users.containsKey(userId)){
				return new Bag(Messages.getString("UniversalMessengerServer.alreadyRegistered"), 
						null, new Session(IPAddress, port), null); //$NON-NLS-1$
			} else {// signing up new user
				List<String> friends = new ArrayList<String>();
				users.put(userId, new User(first, last, language, userId, password, friends));
				senderSession = new Session(IPAddress, port);
				sessions.put(userId, senderSession);
				return new Bag(Messages.getString("UniversalMessengerServer.successfulsignup"), 
						null, sessions.get(userId), null); //$NON-NLS-1$
			}
		} 
		 /* Received: signin,senderuserid,password											
		  * Sent back: signinsuccess/usernotfound,serverid
		  * Sent to other: N/A
		  */

		else if (request.contains(Messages.getString("UniversalMessengerServer.signin"))){// e.g. signin,hz8395,password123 //$NON-NLS-1$
			String userId = tokenizer.nextToken();
			tokenizer.nextToken();//signin keyword
			String password = tokenizer.nextToken();
			Session session;
			if (userId==null || !users.containsKey(userId)){
				session = new Session(IPAddress, port);
				return new Bag(Messages.getString("UniversalMessengerServer.usernotfound"), null, session, null); //$NON-NLS-1$
			} else {
				session = new Session(IPAddress, port);
				sessions.put(userId, session);
				return new Bag(Messages.getString("UniversalMessengerServer.successfulsignin"), null, session, null); //$NON-NLS-1$
			}	
		} 
		// list friends of a user
		else if (request.contains(Messages.getString("UniversalMessengerServer.list"))){// e.g. list,sendersessionID,hz8395 //$NON-NLS-1$
			String userId = tokenizer.nextToken();// TODO: what if there is no comma
			tokenizer.nextToken();//list keyword
			Session senderSession = null;
			if (sessions.get(userId)==null){
				senderSession = new Session(IPAddress, port);
				sessions.put(userId, senderSession);
			} else {
				senderSession = sessions.get(userId);
			}
			StringBuilder builder = new StringBuilder(); 
			if (userId!=null && users.get(userId)!=null){
				User user = users.get(userId);
				if (user!=null){
					List<String> friends = user.friends;
					if (friends!=null && friends.size()>0){
						for (int i=0; i<friends.size(); i++){
							if (i>0 && i<friends.size()-1){
								builder.append(","); //$NON-NLS-1$
							}
							builder.append(friends.get(i));
						}
					}
				}
				return new Bag(builder.toString(), null, senderSession, null);
			} else {
				return new Bag(ERROR, null, senderSession, null);
			}
		/* senderuserid,send,destinationuserid,text between quotes --> no comma inside text		
		 * serverid,sendsuccess/sendfailure						
		 * serverid,send,senderid,text with no quotes						
		 */
		} else if (request.contains(Messages.getString("UniversalMessengerServer.send"))){// e.g. send,sendersessionID,target12,hello there //$NON-NLS-1$
			String userid = tokenizer.nextToken();
			tokenizer.nextToken();// that is for send keyword
			String destinationid = tokenizer.nextToken();
			String sentence = tokenizer.nextToken();
			if (destinationid!=null && users.get(destinationid)!=null){
				// TODO: check if destination is online first, then send them a message
				// TODO: start a new session to the other phone: every phone that is online has to send its IP address
				//		otherwise it is considered offline
				Session senderSession = (Session)sessions.get(userid);
				if (senderSession==null){
					return new Bag(null, null, null, null);											
				}
				Session destinationSession = (Session)sessions.get(destinationid);
				if (destinationSession!=null){// if destination user is not signed in or not even signed up (wrong destination name)
					String responsetoSender = Messages.getString("UniversalMessengerServer.successfullsend");
					return new Bag(responsetoSender, userid+","+sentence, senderSession, destinationSession); // response is same as request data, but new destination
				} else {// tell the sender that destination can't be found
					return new Bag(Messages.getString("UniversalMessengerServer.destinationnotfound"), null, senderSession, null);						
				}
			}
		} 
		// search for an id
		// response includes if user is online of offline
		else if (request.contains(Messages.getString("UniversalMessengerServer.search"))){// e.g. search,sendersessionID,target12 //$NON-NLS-1$
			String userid = tokenizer.nextToken();
			tokenizer.nextToken();// that is for search keyword
			String destination = tokenizer.nextToken();
			Session senderSession = (Session)sessions.get(userid);
			if (senderSession==null){
				return new Bag(null, null, null, null);											
			}
			if (destination==null){
				return new Bag(Messages.getString("UniversalMessengerServer.ERROR"), null, new Session(IPAddress, port), null);
			} else {
				String response = null;
				if (users.containsKey(destination)){
					Session destinationSession = (Session)sessions.get(destination);
					if (destinationSession!=null){
						response = Messages.getString("UniversalMessengerServer.searchsuccess")+",online";
					} else {
						response = Messages.getString("UniversalMessengerServer.searchsuccess")+",offline";
					}
					return new Bag(response, null, senderSession, null);
				} else {
					return new Bag( Messages.getString("UniversalMessengerServer.searchfailure"), null, senderSession, null);
				}
			}
		}
		return new Bag(Messages.getString("UniversalMessengerServer.ERROR"), null, new Session(IPAddress, port), null);
	}
} 
			