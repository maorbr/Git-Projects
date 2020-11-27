package com.core;

public class BinaryDecoder {

	public static String h_u(String binaryValue) {
		switch (binaryValue) {
		case "0":
			return "Host(0)";
		default:
			return "Unit(1)";
		}
	}

	public static String classType(String binaryValue) {
		switch (binaryValue) {
		case "00":
			return "Bus_Configuration(0)";
		case "10":
			return "Status(2)";
		case "01":
			return "I/O_Configuration(1)";
		default:
			return "I/O_Data(3)";
		}
	}

	public static String dataItemType(String binaryValue) {
		switch (binaryValue) {
		case "000":
			return "Status_Message_Type(0) / Version_Message_Number(0)";
		case "001":
			return "Bit_Message_Number(1)";
		case "010":
			return "Set_Timestamp_Message_Number(2)";
		case "011":
			return "Set_TOD_Message_Number(3)";
		case "100":
			return "Statistics_1_Message_Number(4)";
		case "101":
			return "Statistics_2_Message_Number(5)";
		case "110":
			return "Statistics_3_Message_Number(6)";
		case "111":
			return "Statistics_4_Message_Number(7)";
		default:
			return "Statistics_5_Message_Number(8)";
		}
	}

}
