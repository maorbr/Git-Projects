package com.hit.util;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;



public class CLI implements Runnable {

	private PrintWriter outToUser;
	private Scanner inFromUser;
	private String command = null;
	private PropertyChangeSupport pcs;
	private static String selectedAlgo = null;
	private static int capacityAlgo = 0;

	public CLI(InputStream in, OutputStream out) {
		inFromUser = new Scanner(new InputStreamReader(in));
		outToUser = new PrintWriter(new OutputStreamWriter(out));
		pcs = new PropertyChangeSupport(this);

	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);

	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}

	public void write(String string) {
		outToUser.println(string);
		outToUser.flush();
	}

	public String getCommand() {
		return this.command;
	}
	
	public void setCommand(String newCommand) {
		String oldCommand = this.command;
		this.command = newCommand;
		if(!newCommand.equals(oldCommand))
		this.pcs.firePropertyChange("command", oldCommand, newCommand);
	}

	public void run() {
		String commandFromUser = null;
		write("Please enter your command [start/shutdown]:");
		while (true) {

			commandFromUser = inFromUser.nextLine();

			switch (commandFromUser) {
			case "start":				
				algoInputScanner();
				write("Trying to start server...");
				this.setCommand(commandFromUser);
				synchronized (pcs) {
					pcs.notifyAll();
				}
				break;
			case "shutdown":
				write("Trying to shutdown server...");
				this.setCommand(commandFromUser);
				synchronized (pcs) {
					pcs.notifyAll();
				}
				break;
			default:
				write("\nPlease enter your command [start/shutdown]:");
				break;
			}
		}
	}
	
	private void algoInputScanner() {

		System.out.println("Enter algo's name [LRU/MRU/Random]:");
		setSelectedAlgo(inFromUser.nextLine());

		while (!getSelectedAlgo().equals("LRU") && !getSelectedAlgo().equals("MRU") && !getSelectedAlgo().equals("Random")) {
			System.out.println("wrong input!, try again");
			setSelectedAlgo(inFromUser.nextLine());
		}

		System.out.println("Enter capacity [1 - 999]:");
		setCapacityAlgo(inFromUser.nextInt());

		while ((getCapacityAlgo() < 1) || (getCapacityAlgo() > 999)) {
			System.out.println("wrong input!, try again");
			setCapacityAlgo(inFromUser.nextInt());
		}
	}
	
	public static String getSelectedAlgo() {
		return selectedAlgo;
	}

	public static void setSelectedAlgo(String selectedAlgo) {
		CLI.selectedAlgo = selectedAlgo;
	}

	public static int getCapacityAlgo() {
		return capacityAlgo;
	}

	public static void setCapacityAlgo(int capacityAlgo) {
		CLI.capacityAlgo = capacityAlgo;
	}

}
