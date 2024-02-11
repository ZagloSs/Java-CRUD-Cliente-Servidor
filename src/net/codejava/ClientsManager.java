package net.codejava;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
					ServerSocket serverSocket = new ServerSocket(6564);
					int eleccion;

					do {

						Socket socketCliente = serverSocket.accept();
						DataInputStream dis = new DataInputStream(socketCliente.getInputStream());
						DataOutputStream dos = new DataOutputStream(socketCliente.getOutputStream());
						
						eleccion = dis.read();
						switch(eleccion) {
						case 1:
							String data = dis.readUTF();
							dos.writeUTF(insertNewClient(connection, data));
							
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
			
			e.printStackTrace();
			allClients = "null";
		}
		
		return allClients;
	
	}
	
	private static String insertNewClient(Connection con, String data) {
		String[] dataSplit = data.split(",");
		String sql = "INSERT INTO cliente (nombre, apellido1, apellido2, edad, nacimiento) VALUES(?,?,?,?,?)";
		Date defDate = new Date(999945843);
		try {
			PreparedStatement stmnt = con.prepareStatement(sql);
			stmnt.setString(1, dataSplit[0]);
			stmnt.setString(2, dataSplit[1]);
			stmnt.setString(3, dataSplit[2]);
			stmnt.setInt(4, Integer.parseInt(dataSplit[3]));
			stmnt.setDate(5, defDate);
			int rows = stmnt.executeUpdate();
			
			if(rows > 0) {
				return "A new user has been inserted";
			}else {
				return "A problem occurred while trying to insert new user";
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return "A problem occurred while trying to insert new user";
		}
	}
	
	

}
