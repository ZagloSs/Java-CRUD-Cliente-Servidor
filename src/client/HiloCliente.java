package client;

import java.net.Socket;

public class HiloCliente extends Thread{
	public Socket socket;
	
	public HiloCliente(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
		
	}
	
	
}
