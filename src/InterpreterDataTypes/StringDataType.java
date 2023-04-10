package InterpreterDataTypes;

public class StringDataType extends InterpreterDataType{
	
	private String data;
	
	public StringDataType(String data, boolean cState) {
		this.data = data;
		this.cState = cState;
	}
	
	public StringDataType(String data) {
		this.data = data;
	}
	
	public StringDataType(boolean cState){this.cState = cState;}
	
	public StringDataType() {}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data;
	}
	
	@Override
	public void FromString(String input) {this.data = input;}
}
