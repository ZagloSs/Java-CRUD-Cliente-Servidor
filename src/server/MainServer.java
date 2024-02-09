package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(6565);
			String mensaje;

			do {

				Socket socketCliente = serverSocket.accept();
				DataInputStream dis = new DataInputStream(socketCliente.getInputStream());
				DataOutputStream dos = new DataOutputStream(socketCliente.getOutputStream());
				mensaje = dis.readUTF();

				// Leemos el mensaje y lo ponemos por consola
				System.out.println("Mensaje de " + socketCliente.getInetAddress().getHostName() + " recibido: " + mensaje);
	

				// Cerrar conexiones y streams
				dis.close();
				socketCliente.close();
			} while (!mensaje.equals("exit"));

			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
