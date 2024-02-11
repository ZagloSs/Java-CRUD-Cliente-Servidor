package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 6564);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			Scanner sc = new Scanner(System.in);

			
			System.out.println("Welcome to clients manager, what would you like to do?\n"
					+ "1. INSERT A NEW CLIENT\n"
					+ "2. SEE ALL CLIENTS\n"
					+ "3. UPDATE A CLIENT\n"
					+ "4. DELETE A CLIENT\n");
			
			//Asumo que el cliente no va a introducir algo que no sea un numero entero
			int elect = sc.nextInt();
			sc.nextLine();
			dos.write(elect);
			switch(elect) {
			case 1:
				System.out.print("Full name of the client with 2 surnames: ");
				String fName = sc.nextLine();
				String cName = String.join(",", fName.split(" "));
				System.out.print("Edad: ");
				int age = sc.nextInt();
				String toServer = cName + "," + age;
				dos.writeUTF(toServer);
				System.out.println(dis.readUTF());
				break;
			case 2: 
				System.out.println(dis.readUTF());
				break;
			}
			
			
			
			
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
