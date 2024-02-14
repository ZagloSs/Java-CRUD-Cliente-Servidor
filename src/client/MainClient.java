package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 6562);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			Scanner sc = new Scanner(System.in);

			
			do {
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
						System.out.print("Age: ");
						int age = sc.nextInt();
						sc.nextLine();
						System.out.print("Year: ");
						String nacimiento = sc.nextLine();
						String toServer = cName + "," + age + "," + nacimiento;
						dos.writeUTF(toServer);
						System.out.println(dis.readUTF());
						break;
					case 2: 
						System.out.println(dis.readUTF());
						break;
					case 3:
						System.out.println("Please enter the ID of the user you want to update");
						int idElect = sc.nextInt();
						dos.write(idElect);
							System.out.println("What you want to update?:\n1.Name\n2.(1)Surname\n3.(2)Surname\n4.Age\n5.Birthday\n");
							int decision = sc.nextInt();
							sc.nextLine();
							dos.write(decision);
							System.out.print("What is the new value?: ");
							String newValue = sc.nextLine();
							dos.writeUTF(newValue);
							System.out.println(dis.readUTF());
						break;
					case 4:
						System.out.println("Please enter the ID of the user you want to delete");
						int idToDelete = sc.nextInt();
						sc.nextLine();
						dos.write(idToDelete);
						
						System.out.println(dis.readUTF());
				}
				if(elect == 5) {
					sc.close();
					dos.close();
					socket.close();
				}
			}while(!socket.isClosed());
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
