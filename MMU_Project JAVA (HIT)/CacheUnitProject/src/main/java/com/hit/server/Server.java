package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hit.services.CacheUnitController;
import com.hit.server.HandleRequest;

public class Server implements PropertyChangeListener, Runnable {

	private ServerSocket serverSocket = null;
	private static final int PORT = 12345;
	private final int poolSize;
	private final ExecutorService pool;
	private boolean serverStatus = false;
	private CacheUnitController<String> cacheUnitController;
	private Socket client = null;

	public Server() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		poolSize = Runtime.getRuntime().availableProcessors();
		pool = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String commandStatus = evt.getNewValue().toString();

		switch (commandStatus) {
		case "start":

			if (serverStatus) {
				System.out.println("Server already up");
				break;
			} else {
				System.out.println("Server start successfully");	
				serverStatus = true;
				this.run();
				break;
			}
			
		case "shutdown":
			if (serverStatus) {
				System.out.println("Server shutdown successfully, press [Enter].");
				try {
					serverSocket.close();

					pool.shutdown();
					while (!pool.isTerminated()) {
					}

					serverStatus = false;
					break;

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Server already down, press [Enter].");
				break;
			}
		default:
			System.out.println("Not a valid commnand");
			break;
		}
	}

	@Override
	public void run() {

		cacheUnitController = new CacheUnitController<String>();
		while (serverStatus) {
			System.out.println("wait to client connection...");

			try {
				client = serverSocket.accept();				
				System.out.println("client " + client + " connected to server");
				HandleRequest<String> handlerRequester = new HandleRequest<String>(client, cacheUnitController);
				pool.execute(handlerRequester);
				

			} catch (IOException e) {
				System.out.println(e);
				pool.shutdown();
				while (!pool.isTerminated()) {
				}
			}
		}
	}
	


	


}
