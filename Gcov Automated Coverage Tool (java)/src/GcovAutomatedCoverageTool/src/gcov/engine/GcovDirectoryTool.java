package gcov.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GcovDirectoryTool {
	public static void walkForSourceFile(String path, List<File> listOfFiles) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walkForSourceFile(f.getAbsolutePath(), listOfFiles);
			} else {
				if (f.getName().substring(f.getName().length() - 2, f.getName().length()).equals(".c"))
					listOfFiles.add(f.getAbsoluteFile());
			}
		}
	}

	public static void walkForGcnoFile(String path, List<File> listOfFiles) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walkForGcnoFile(f.getAbsolutePath(), listOfFiles);
			} else {
				if (f.getName().contains(".gcno")) {
					listOfFiles.add(f.getAbsoluteFile());
				}
			}
		}
	}

	public static void walkForGcdaFile(String path, List<File> listOfFiles) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (!f.isDirectory()) {
				if (f.getName().contains(".gcda_")) {
					listOfFiles.add(f.getAbsoluteFile());
				}
			}
		}
	}

	public static void walkForGcovFile(String path, List<File> listOfFiles) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.getName().contains(".gcov")) {
				listOfFiles.add(f.getAbsoluteFile());
			}
		}
	}

	public static Path createNewDirectoryAndCopyGcovFilesToIt(List<File> allGcdaFilesNeeded, String i_path) throws IOException {
		Path path = Paths.get(i_path + "\\GCOV_" + LocalDate.now() + "_" + Calendar.getInstance().getTimeInMillis());
		if (!Files.exists(path)) {
			Files.createDirectories(path);
			List<File> allGcovFiles = new ArrayList<>();
			
			for (File gcdaFile : allGcdaFilesNeeded) {
				copyAndPasteFileToDirectory(gcdaFile.getAbsolutePath(), path + "\\" + gcdaFile.getName());
			}
			
			GcovDirectoryTool.walkForGcovFile(i_path, allGcovFiles);
			for (File gcovFile : allGcovFiles) {
				for (File gcdafile : allGcdaFilesNeeded) {
					if (gcovFile.getName().substring(0, gcovFile.getName().length() - 5).equals(gcdaFileRename(gcdafile.getName())))

						cutAndPasteFileToDirectory(gcovFile.getAbsolutePath(), path + "\\" + gcovFile.getName().substring(0, gcovFile.getName().length() - 5) + "_" + loadTimeTag(gcdafile.getName()) + ".gcov");		
				}
			}

		}
		return path;
	}
	
	private static String loadTimeTag(String fileName) {
		String[] fullTimeTage;

		fullTimeTage = fileName.split("_");

		if (fullTimeTage[1].matches("-?\\d+(\\.\\d+)?")) {
			return fullTimeTage[1];
		} else {
			return fullTimeTage[2];
		}
	}
	
	public static String gcdaFileRename(String fileName) {
		String[] gcovTiedfileName;
		gcovTiedfileName = fileName.split(".gcda");
		
		return gcovTiedfileName[0];
	}
	
	public static String gcdaFileRenameToGcov(String fileName) {
		String[] gcovTiedfileName;
		gcovTiedfileName = fileName.split(".gcda");
		
		return gcovTiedfileName[0] + gcovTiedfileName[1];
	}
	
	public static String gcovTimeTagFileRenameToGcov(String fileName) {
		String[] fullTimeTage;
		fullTimeTage = fileName.split("_");

		if (fullTimeTage[1].contains(".gcov") && fullTimeTage[1].substring(0, fullTimeTage[1].length() - 5).matches("-?\\d+(\\.\\d+)?")) {
			return fullTimeTage[0];
		} else {
			return fullTimeTage[0] + "_" + fullTimeTage[1];
		}
	}

	public static void createNewGcovMergerDirectoryIfNotExisted(String mergePath)
			throws IOException {
		Path path = Paths.get(mergePath);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
	}

	public static void cutAndPasteFileToDirectory(String sourcePath, String targetPath) throws IOException {
		Path sourceDirectory = Paths.get(sourcePath);
		Path targetDirectory = Paths.get(targetPath);
		Files.copy(sourceDirectory, targetDirectory);
		sourceDirectory.toFile().delete();
	}
	
	public static void copyAndPasteFileToDirectory(String sourcePath, String targetPath) throws IOException {
		Path sourceDirectory = Paths.get(sourcePath);
		Path targetDirectory = Paths.get(targetPath);
		Files.copy(sourceDirectory, targetDirectory);
	}

}
