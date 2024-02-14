package net.codejava;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
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
				try (ServerSocket serverSocket = new ServerSocket(6562)){
					
					
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
							break;
						case 3:
							int userId = dis.read();
							UpdateClient(dos,dis,connection, userId);
							break;
						case 4:
							int idToDelete = dis.read();
							dos.writeUTF(DeleteClient(connection, idToDelete));
							
							
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
		try {
			PreparedStatement stmnt = con.prepareStatement(sql);
			stmnt.setString(1, dataSplit[0]);
			stmnt.setString(2, dataSplit[1]);
			stmnt.setString(3, dataSplit[2]);
			stmnt.setInt(4, Integer.parseInt(dataSplit[3]));
			stmnt.setString(5, dataSplit[4]);
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
	
	private static void UpdateClient(DataOutputStream dos,DataInputStream dis, Connection con, int id) {
		try {
			if(CheckClientExistance(con, id)) {
				int electChange = dis.read();
				String newValue = dis.readUTF();
				switch(electChange){
					case 1:
						String name = "UPDATE cliente SET nombre = ? WHERE id = ?";
						PreparedStatement stmntName = con.prepareStatement(name);
						stmntName.setString(1, newValue);
						stmntName.setInt(2, id);
						int rowsName = stmntName.executeUpdate();
						
						if(rowsName > 0) {
							dos.writeUTF("Update succesful");
						}else {
							dos.writeUTF("An error occurred while updating");
						}
						break;
					case 2:
						String sName1 = "UPDATE cliente SET apellido1 = ? WHERE id = ?";
						PreparedStatement stmntSName1 = con.prepareStatement(sName1);
						stmntSName1.setString(1, newValue);
						stmntSName1.setInt(2, id);
						int rowsSName1 = stmntSName1.executeUpdate();
					
						if(rowsSName1 > 0) {
							dos.writeUTF("Update succesful");
						}else {
							dos.writeUTF("An error occurred while updating");
						}
						break;
					case 3:
						String sName2 = "UPDATE cliente SET apellido2 = ? WHERE id = ?";
						PreparedStatement stmntSName2 = con.prepareStatement(sName2);
						stmntSName2.setString(1, newValue);
						stmntSName2.setInt(2, id);
						int rowsSName2 = stmntSName2.executeUpdate();
					
						if(rowsSName2 > 0) {
							dos.writeUTF("Update succesful");
						}else {
							dos.writeUTF("An error occurred while updating");
						}
						break;
					case 4:
						String age = "UPDATE cliente SET edad = ? WHERE id = ?";
						PreparedStatement stmntAge = con.prepareStatement(age);
						stmntAge.setInt(1, Integer.valueOf(newValue));
						stmntAge.setInt(2, id);
						int rowsAge = stmntAge.executeUpdate();
					
						if(rowsAge > 0) {
							dos.writeUTF("Update succesful");
						}else {
							dos.writeUTF("An error occurred while updating");
						}
						break;
					case 5:
						String date = "UPDATE cliente SET nacimiento = ? WHERE id = ?";
						PreparedStatement stmntDate= con.prepareStatement(date);
						stmntDate.setString(1, newValue);
						stmntDate.setInt(2, id);
						int rowsDate= stmntDate.executeUpdate();
					
						if(rowsDate > 0) {
							dos.writeUTF("Update succesful");
						}else {
							dos.writeUTF("An error occurred while updating");
						}
						break;
				}
			}else {
					
					dos.writeUTF("ntf");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String DeleteClient(Connection con, int id) {
		try {
			String sql = "DELETE FROM cliente WHERE id = ?";
			PreparedStatement stmntDel = con.prepareStatement(sql);
			stmntDel.setInt(1, id);
			Boolean success = stmntDel.execute();
			
			if(!success) {
				return "Succesfully deleted";
				
			}else {
				return "There was an error while deleting";
			}
			
		} catch (SQLException e) {
			return "There was an e deleting";
		}
		
		
		 
			
	}
	
	private static boolean CheckClientExistance(Connection con, int id) {
		String sql = "SELECT id FROM cliente WHERE id = ?";
		try {
			PreparedStatement stmnt = con.prepareStatement(sql);
			stmnt.setInt(1, id);
			
			ResultSet res = stmnt.executeQuery();
			if(res.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	

}
