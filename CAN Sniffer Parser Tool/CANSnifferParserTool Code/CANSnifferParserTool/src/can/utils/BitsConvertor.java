package can.utils;

public class BitsConvertor {
	public static String binaryToDecimal(String binaryValue) {
        int decimal = Integer.parseInt(binaryValue, 2);
        return Integer.toString(decimal);
	}
	
	public static String HexToBinary(String hexValue) {
        int Hex = Integer.parseInt(hexValue, 16);
        return Integer.toBinaryString(Hex);
	}
}
