package com.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import can.beans.Data;
import can.beans.Message;
import can.utils.BitsConvertor;

public class CSVSinfferEditor {
	public void writeHeaderToNewFile(String inputFilePath, String outputFilePath, String[] headers) throws IOException {

		File file = new File(inputFilePath);
		FileReader inputFile = new FileReader(file);
		CSVReader csvReader = new CSVReader(inputFile);
		List<String[]> data = new ArrayList<>();
		data = csvReader.readAll();
		data.remove(0);
		data.remove(0);

		inputFile.close();
		csvReader.close();

		file = new File(outputFilePath);
		FileWriter outputFile = new FileWriter(file);
		CSVWriter writer = new CSVWriter(outputFile);
		writer.writeNext(headers);
		writer.writeAll(data);
		writer.close();
	}

	public List<Message> parseCSVToBean(String filePath) throws IOException {

		Reader reader = Files.newBufferedReader(Paths.get(filePath));
		CSVReader csvReader = new CSVReader(reader);

		List<String[]> records = csvReader.readAll();
		records.remove(0);
		List<Message> messages = new ArrayList<>();

		for (String[] record : records) {
			Message message = new Message(record[0], record[1],
					new Data(record[2], record[3], record[4], record[5], record[6], record[7], record[8], record[9]));

			String headerBinary = BitsConvertor.HexToBinary(message.getHeader());

			String priorityIndexBinary = headerBinary.substring(1, 4);
			String h_uBinary = headerBinary.substring(4, 5);
			String classTypeBinary = headerBinary.substring(5, 7);
			String addressBinary = headerBinary.substring(7, 13);
			String dataItemTypeBinary = headerBinary.substring(13, 16);
			String dataItemNumberBinary = headerBinary.substring(16, 23);
			String counterBinary = headerBinary.substring(26, 29);
			String attBinary = headerBinary.substring(29, 30);

			message.setPriority_Index(BitsConvertor.binaryToDecimal(priorityIndexBinary));
			message.setH_U(BinaryDecoder.h_u(h_uBinary));
			message.setClass_Type(BinaryDecoder.classType(classTypeBinary));
			message.setAddress(BitsConvertor.binaryToDecimal(addressBinary));
			message.setData_Item_Type(BinaryDecoder.dataItemType(dataItemTypeBinary));
			message.setData_Item_Number(BitsConvertor.binaryToDecimal(dataItemNumberBinary));
			message.setCounter(BitsConvertor.binaryToDecimal(counterBinary));
			message.setAtt(attBinary);

			messages.add(message);
		}

		csvReader.close();

		File file = new File(filePath);
		file.delete();

		return messages;
	}

	public void parseBeanToCSV(String filePath, List<Message> messages, String[] headers) throws IOException {

		File file = new File(filePath);
		FileWriter writer = new FileWriter(file);

		CSVWriter csvWriter = new CSVWriter(writer, ',');
		List<String[]> data = this.toStringArray(messages, headers);

		csvWriter.writeAll(data);

		csvWriter.close();
	}

	private List<String[]> toStringArray(List<Message> messages, String[] headers) {
		List<String[]> records = new ArrayList<String[]>();

		records.add(headers);

		Iterator<Message> it = messages.iterator();
		while (it.hasNext()) {
			Message msg = it.next();
			records.add(new String[] { msg.getTime(), msg.getHeader(), msg.getData().getHalf0_0(),
					msg.getData().getHalf0_1(), msg.getData().getHalf0_2(), msg.getData().getHalf0_3(),
					msg.getData().getHalf1_3(), msg.getData().getHalf1_2(), msg.getData().getHalf1_1(),
					msg.getData().getHalf1_0(), msg.getPriority_Index(), msg.getH_U(), msg.getClass_Type(),
					msg.getAddress(), msg.getData_Item_Type(), msg.getData_Item_Number(), msg.getCounter(),
					msg.getAtt() });
		}
		return records;
	}
}
