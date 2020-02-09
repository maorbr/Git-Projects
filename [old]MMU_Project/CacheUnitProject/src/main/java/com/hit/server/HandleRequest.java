package com.hit.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

public class HandleRequest<T> implements Runnable {

	private Socket socket;
	private CacheUnitController<T> controller;

	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.controller = controller;
		this.socket = s;
	}

	@Override
	public void run() {

		String req = null;
		String action = null;
		boolean result = true;
		DataModel<T>[] requestBody = null;
		ObjectOutputStream outToUser = null;
		ObjectInputStream inFromClient = null;
		String stat = null;
		try {

			outToUser = new ObjectOutputStream(socket.getOutputStream());
			inFromClient = new ObjectInputStream(socket.getInputStream());

			try {
				req = (String) inFromClient.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Type ref = new TypeToken<Request<DataModel<T>[]>>() {}.getType();
			Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);

			try {
				action = request.getHeaders().get("action");

				if (request.getBody() instanceof DataModel<?>[]) {
					requestBody = (DataModel<T>[]) request.getBody();
				}
			} catch (NullPointerException e) {
				action = "NOTHING";
			}

			switch (action) {
			case "GET":
				requestBody = controller.get(request.getBody());			
				break;
			case "DELETE":
				result = controller.delete(request.getBody());
				break;
			case "UPDATE":
				result = controller.update(request.getBody());
				break;
			case "STATISTICS":

				stat = controller.getStatistics();
				if (stat != null)
					result = true;
				break;
			default:
				break;
			}

			String answerCheck;

			if (result == false) {
				answerCheck = "connection failed";
			} else
				answerCheck = "connection succeeded";

			if (action.equals("STATISTICS")) {
				outToUser.writeObject(stat);
			} else if (action.equals("GET")) {

				@SuppressWarnings("unchecked")
				T[] getArray = (T[]) new Object[requestBody.length];
				for (int i = 0; i < requestBody.length; i++) {
					if (requestBody[i] != null)
						getArray[i] = requestBody[i].getContent();
					else {
						getArray[i] = null;
					}
				}

				StringBuilder str = new StringBuilder();
				str.append("GET REQ: [ ");
				for (int i = 0; i < getArray.length; i++) {
					if (i != getArray.length - 1) {
						str.append(getArray[i] + ", ");
					} else {
						str.append(getArray[i] + " ]");
					}
				}

				outToUser.writeObject(str.toString());
			} else {
				outToUser.writeObject(answerCheck);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				inFromClient.close();
				outToUser.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
