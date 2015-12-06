
class Bag {
	String senderResponse, destinationResponse;
	Session senderSession, destinationSession;
	public Bag(String resp, String dest, Session senderSess, Session destSess){
		senderResponse = resp;
		destinationResponse = dest;
		senderSession = senderSess;
		destinationSession = destSess;
	}
	String getSenderResponse(){
		return senderResponse;
	}
	String getDestinationResponse(){
		return destinationResponse;
	}
	Session getSenderSession(){
		return senderSession;
	}
	Session getDestinationSession(){
		return destinationSession;
	}
}