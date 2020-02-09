package com.hit.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CacheUnitClient {
	private Socket clientSocket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	public CacheUnitClient() {
	}

	public String send(String request) {
		String response = "Request timed out, no response from server.";
		try {
			clientSocket = new Socket("localhost", 12345);
			outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			inputStream = new ObjectInputStream(clientSocket.getInputStream());

			System.out.println("Sending request:\n\n" + request);
			outputStream.writeObject(request);

			try {
				response = (String) inputStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Server Message: " + response);

			clientSocket.close();
			outputStream.close();
			inputStream.close();

		} catch (IOException e) {
		}
		return response;
	}
}
