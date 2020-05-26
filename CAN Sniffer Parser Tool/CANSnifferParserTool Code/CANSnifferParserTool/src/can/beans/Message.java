package can.beans;

public class Message {
	private String Time;
	private String Header;
	private Data data;
	private String Priority_Index;
	private String H_U;
	private String Class_Type;
	private String Address;
	private String Data_Item_Type;
	private String Data_Item_Number;
	private String Counter;
	private String Att;

	public Message() {
		setTime("");
		setHeader("");
		setData(new Data());
		setPriority_Index("");
		setH_U("");
		setClass_Type("");
		setAddress("");
		setData_Item_Type("");
		setData_Item_Number("");
		setCounter("");
		setAtt("");
	}

	public Message(String time, String header, Data data) {
		this.Time = time;
		this.Header = header;
		this.data = data;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		this.Time = time;
	}

	public String getHeader() {
		return Header;
	}

	public void setHeader(String header) {
		this.Header = header;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public String getPriority_Index() {
		return Priority_Index;
	}
	public void setPriority_Index(String priority_Index) {
		Priority_Index = priority_Index;
	}
	public String getH_U() {
		return H_U;
	}
	public void setH_U(String h_U) {
		H_U = h_U;
	}
	public String getClass_Type() {
		return Class_Type;
	}
	public void setClass_Type(String class_type) {
		Class_Type = class_type;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getData_Item_Type() {
		return Data_Item_Type;
	}
	public void setData_Item_Type(String data_Item_Type) {
		Data_Item_Type = data_Item_Type;
	}
	public String getData_Item_Number() {
		return Data_Item_Number;
	}
	public void setData_Item_Number(String data_Item_Number) {
		Data_Item_Number = data_Item_Number;
	}
	public String getCounter() {
		return Counter;
	}
	public void setCounter(String counter) {
		Counter = counter;
	}
	public String getAtt() {
		return Att;
	}
	public void setAtt(String att) {
		Att = att;
	}
}
