package net.codejava;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientsManager {

	public static void main(String[] args) {
		
		String jdbcURL = "jdbc:mysql://localhost:3306/clients";
		String username = "root";
		String password = "";
		
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, username, password);
			
			if(connection == null) {
				System.out.println("An error occurred while connection to db");
			}else {
				System.out.println("Connected");
				try {
					ServerSocket serverSocket = new ServerSocket(6565);
					int eleccion;

					do {

						Socket socketCliente = serverSocket.accept();
						DataInputStream dis = new DataInputStream(socketCliente.getInputStream());
						DataOutputStream dos = new DataOutputStream(socketCliente.getOutputStream());
						
						String welcome = "Welcome to clients manager, what would you like to do?\n"
								+ "1. INSERT A NEW CLIENT\n"
								+ "2. SEE ALL CLIENTS\n"
								+ "3. UPDATE A CLIENT\n"
								+ "4. DELETE A CLIENT\n";
						
						dos.writeUTF(welcome);
						
						eleccion = dis.read();

						switch(eleccion) {
						case 1:
							dos.writeUTF("You selected inserting an user");
							break;
						case 2:
							dos.writeUTF(getAllClients(connection));
							
						}
			
						// Cerrar conexiones y streams
						dis.close();
						socketCliente.close();
						
					} while (!(eleccion == 5));

					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}
	
	private static String getAllClients(Connection con) {
		String allClients ="";
		String sql = "SELECT * FROM cliente";
		try {
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(sql);
			while(res.next()) {
				allClients+= res.getString("id") + ". " + res.getString("nombre") + " " +res.getString("apellido1")
				+ " " + res.getString("apellido2") + " EDAD: " + res.getString("edad") + " AÑO DE NACIMIENTO: " + res.getString("nacimiento") + "\n";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			allClients = "null";
		}
		
		return allClients;
	
	}

}
