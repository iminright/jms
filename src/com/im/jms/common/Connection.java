package com.im.jms.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.im.jms.common.ServerConfig;

public class Connection {
	
	private String id ;
	
	private Socket socket ;
	
	public Connection(String id){
		this.setId(id) ;
		socket = connect() ;
	}
	
	private Socket connect(){
		Socket socket = null ;
		try {
			socket = new Socket(ServerConfig.IP, ServerConfig.PORT) ;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket ;
	}
	
	public void recieve(){
		DataInputStream is = null ;
		try {
			is = new DataInputStream(socket.getInputStream()) ;
			while(true){
				if(is.available() != 0){
					String msg = is.readUTF() ;
					System.out.println(this.id + "收到信息:\n" + msg) ;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void send(String msg){
		DataOutputStream os = null ;
		try {
			os = new DataOutputStream(socket.getOutputStream()) ;
			os.writeUTF(msg);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
