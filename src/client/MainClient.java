package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 6565);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			Scanner sc = new Scanner(System.in);

			System.out.println(dis.readUTF());
			
			//Asumo que el cliente no va a introducir no va a introducir algo que no sea un entero
			int elect = sc.nextInt();
			dos.write(elect);
			System.out.println(dis.readUTF());
			
			
			
			if(elect == 5) {
				sc.close();
				dos.close();
				socket.close();
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
