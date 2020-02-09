package com.hit.server;

import com.hit.util.CLI;

public class CacheUnitServerDriver {
	
	
	public CacheUnitServerDriver() {
	}

	public static void main(java.lang.String[] args) {
		CLI cli = new CLI(System.in, System.out);
		Server server = new Server();
		cli.addPropertyChangeListener(server);
		new Thread(cli).start(); 
	}
}
