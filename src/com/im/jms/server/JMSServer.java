package com.im.jms.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.im.jms.common.ServerConfig;

/**
 * 
 * @author wanhaofan
 */
public class JMSServer {
	private Queue<String> msgs = new ArrayBlockingQueue<String>(200) ;
	
	private Map<String, Socket> clients = new HashMap<String, Socket>(5) ;
	
	public static void main(String args[]){
		JMSServer server = new JMSServer() ;
		server.start();
	}
	
	public void listenMessage(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					if(!msgs.isEmpty()){
						String msg = msgs.poll() ;
						String to = msg.split("\\$")[1] ;
						Socket socket = clients.get(to) ;
						if(socket == null || socket.isClosed()){
							msgs.add(msg) ;
							continue ;
						}
						DataOutputStream os = null ;
						try {
							os = new DataOutputStream(socket.getOutputStream()) ;
							os.writeUTF(msg.split("\\$")[2]);
							os.flush();
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}).start();
	}
	
	public void start(){
		ServerSocket serverSocket = null ;
		listenMessage() ;
		try {
			serverSocket = new ServerSocket(ServerConfig.PORT) ;
			while(true){
				Socket client = serverSocket.accept() ;
				new Thread(new Processor(client)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class Processor implements Runnable{
		
		private Socket socket ;
		
		public Processor(Socket socket){
			this.socket = socket ;
		}
		
		@Override
		public void run() {
			String msg ;
			DataInputStream dis = null ;
			try {
				dis = new DataInputStream(socket.getInputStream()) ;
				msg = dis.readUTF() ;
				synchronized (clients) {
					clients.put(msg.split("\\$")[0], socket) ;
				}
				msgs.add(msg) ;
				System.out.println(msg) ;
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
		
	}
	
}
