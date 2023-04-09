package InterpreterDataTypes;

public class StringDataType extends InterpreterDataType{
	private String data;
	
	public StringDataType(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String ToString() {
		return null;
	}
	
	@Override
	public void FromString(String input) {this.data = input;}
}
