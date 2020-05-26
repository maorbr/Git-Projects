package gcov.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DirectoryUtils {
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

	public static void walkForGcnaFile(String path, List<File> listOfFiles) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walkForGcnaFile(f.getAbsolutePath(), listOfFiles);
			} else {
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

	public static Path createNewDirectoryAndCopyGcovFilesToIt(String i_path) throws IOException {
		Path path = Paths.get(i_path + "\\GCOV_" + LocalDate.now() + "_" + Calendar.getInstance().getTimeInMillis());
		if (!Files.exists(path)) {
			Files.createDirectories(path);
			List<File> allGcovFiles = new ArrayList<>();
			DirectoryUtils.walkForGcovFile(i_path, allGcovFiles);
			for (File file : allGcovFiles) {

				CopyFileToDirectory(file.getAbsolutePath(), path + "\\" + file.getName());

			}
		}
		return path;
	}

	public static void createNewGcovMergerDirectoryIfNotExisted(String mergePath, String pathLastGcov)
			throws IOException {
		Path path = Paths.get(mergePath);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
			List<File> allGcovFiles = new ArrayList<>();
			DirectoryUtils.walkForGcovFile(pathLastGcov, allGcovFiles);
			for (File file : allGcovFiles) {
				CopyFileToDirectory(file.getAbsolutePath(), path + "\\" + file.getName());
			}
		}
	}

	public static void CopyFileToDirectory(String sourcePath, String targetPath) throws IOException {
		Path sourceDirectory = Paths.get(sourcePath);
		Path targetDirectory = Paths.get(targetPath);

		Files.copy(sourceDirectory, targetDirectory);

	}

}
