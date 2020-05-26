package gcov.utils;

import java.io.PrintWriter;


public class CmdExecutor {

	public static void createGcdaFiles(String comPort, String baud) {
		Process p;
		try {
			p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd .//gcovLib && gcovGET COM" + comPort + " " + baud);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("\n\n");
			stdin.close();
			int exitVal = p.waitFor();
			System.out.println("Exited with error code " + exitVal);
		} catch (Exception e) {
			System.out.println("Something was wrong");
			e.printStackTrace();
		}
	}

	public static void createReport(String SourceFilePath, String gcnoFilePath, String gcdaFilePath) {
		try {
		String[] command =
		    {
		        "cmd",
		    };
			Process p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("cd .//gcovLib && gcovReport " + SourceFilePath + " " + gcnoFilePath + " " + gcdaFilePath);
			stdin.close();
			int exitVal = p.waitFor();
			System.out.println("Exited with error code " + exitVal);
		} catch (Exception e) {
			System.out.println("Something was wrong");
			e.printStackTrace();
		}
	}
	
	public static void openDeviceManager() {
		try {
		String[] command =
		    {
		        "cmd",
		    };
			Process p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			stdin.println("control hdwwiz.cpl");
			stdin.close();
			int exitVal = p.waitFor();
			System.out.println("Exited with error code " + exitVal);
		} catch (Exception e) {
			System.out.println("Something was wrong");
			e.printStackTrace();
		}
	}

}
