package com.example.messenger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.AsyncTask;

class ConnectionManager extends AsyncTask<String, Void, Void> {

	private String action;
	private byte[] sendData = new byte[1024];       
	private byte[] receiveData = new byte[1024];  

	protected Void doInBackground(String... urls) {  
		try {
			DatagramSocket clientSocket = new DatagramSocket(); 
//			clientSocket.setSoTimeout(30000);
			InetAddress IPAddress = InetAddress.getByName("192.168.0.105");//67");////"172.31.99.158");//
			
			if (action.equals("signup")){
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);    
				clientSocket.send(sendPacket);       
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
				clientSocket.receive(receivePacket);   
				receiveData = receivePacket.getData();
				// 
			} else if (action.equals("signin")){
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);       
				clientSocket.send(sendPacket);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket); 
				receiveData = receivePacket.getData();
				//
			} else if (action.equals("send")){
				// do nothing for now
			} else if (action.equals("search")){
				// do nothing for now
			} else if (action.equals("list")){
				// do nothing for now
			} else if (action.equals("addfriend")){
				// do nothing for now
			}
			clientSocket.close(); 
		} catch(SocketException so) {
			so.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return null;
		}

	public void setAction(String action) {
		this.action = action;
	}

	public void setSendData(byte[] send) {
		this.sendData = send;
	}

	public byte[] getReceivedData() {
		return receiveData;
	}
}
