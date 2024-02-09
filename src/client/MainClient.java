package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("192.168.128.240", 6565);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			Scanner sc = new Scanner(System.in);

			System.out.print("Introduce el mensaje para el server: ");
			dos.writeUTF(sc.nextLine());

			System.out.println("Respuesta: " + dis.readUTF());

			sc.close();
			dos.close();
			socket.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
