package com.hit.sapiens.riskassessment.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class CSVUtils  {

	public static void writeHeaderToFile(String filePath, String[] Headers) throws IOException {

		File file = new File(filePath);
		FileReader inputFile = new FileReader(file);
		CSVReader csvReader = new CSVReader(inputFile);
		List<String[]> data = new ArrayList<>();
		data = csvReader.readAll();

		inputFile.close();
		csvReader.close();
		file.delete();

		file = new File(filePath);
		FileWriter outputFile = new FileWriter(file);
		CSVWriter writer = new CSVWriter(outputFile);
		writer.writeNext(Headers);
		writer.writeAll(data);
		writer.close();
	}

	@SuppressWarnings("deprecation")
	public static <T> List<T> parseCSVToBeanList(Class<T> type,String filePath, String[] headers, String[] fields)
			throws IOException {

		HeaderColumnNameTranslateMappingStrategy<T> beanStrategy = new HeaderColumnNameTranslateMappingStrategy<>();
		beanStrategy.setType(type);

		Map<String, String> columnMapping = new HashMap<String, String>();

		for (int i = 0; i < headers.length; i++) {

			columnMapping.put(headers[i], fields[i]);
		}

		beanStrategy.setColumnMapping(columnMapping);

		CsvToBean<T> csvToBean = new CsvToBean<>();
		CSVReader reader = new CSVReader(new FileReader(filePath));
		List<T> objects = csvToBean.parse(beanStrategy, reader);
		
		return objects;
	}
	
	public static void removeHeaderFromFile(String filePath) throws IOException {

		File file = new File(filePath);
		FileReader inputFile = new FileReader(file);
		CSVReader csvReader = new CSVReader(inputFile);
		List<String[]> data = new ArrayList<>();
		data = csvReader.readAll();
		inputFile.close();
		csvReader.close();
		file.delete();
		
		file = new File(filePath);
		FileWriter outputFile = new FileWriter(file);
		CSVWriter writer = new CSVWriter(outputFile);
		
		data.remove(0);
		writer.writeAll(data);
		writer.close();
	}
	
	@SafeVarargs
	public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
		final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

		return t -> {
			final List<?> keys = Arrays.stream(keyExtractors).map(ke -> ke.apply(t)).collect(Collectors.toList());

			return seen.putIfAbsent(keys, Boolean.TRUE) == null;
		};
	}
	
}
