package net.codejava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}

}
