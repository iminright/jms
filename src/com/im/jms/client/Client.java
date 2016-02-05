package com.im.jms.client;

import com.im.jms.common.Connection;

public class Client {
	
	public static void main(String args[]){
		final Connection order = new Connection("order") ;
		final Connection finance = new Connection("finance") ;
//		final Connection member = new Connection("member") ;
		order.send("order$finance$order-->finance发送的消息:aaa");
		finance.send("finance$order$finance-->order发送的消息:bbb");
//		for(int i=0; i<100; i++){
//			if(i < 30){
//				order.send("order|finance|order-->order发送的消息:" + i);
//			}else{
//				order.send("order|member|order-->order发送的消息:" + i);
//			}
//		}
		new Thread(new Runnable(){

			@Override
			public void run() {
				finance.recieve();
			}
			
		}).start();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				order.recieve();
			}
			
		}).start();
		
	}
}
