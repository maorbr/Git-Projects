package gcov.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileTolistConvertor {

	public static List<String> convetFileToList(String fileName) throws IOException {

		BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fileName));
		return bufferedReader.lines().collect(Collectors.toList());
	}

	public static void printListToFile(String path, List<String> list) throws IOException {
		File file = new File(path);
		Path out = Paths.get(file.getAbsolutePath());
		Files.write(out,list,Charset.defaultCharset());
	}
	
	public static void cleanFile(String path) throws IOException {
		FileOutputStream writer = new FileOutputStream(path);
		writer.write(("").getBytes());
		writer.close();	
	}
}
