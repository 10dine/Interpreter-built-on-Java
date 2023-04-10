package InterpreterDataTypes;

public class IntegerDataType extends InterpreterDataType{
	
	private int data;
	
	public IntegerDataType(int data, boolean cState){
		this.data = data;
		this.cState = cState;
	}
	
	public IntegerDataType(int data){
		this.data = data;
	}
	
	public IntegerDataType(boolean cState){this.cState = cState;}
	
	public IntegerDataType() {}
	
	public int getData() {
		return data;
	}
	
	public void setData(int data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) {
		data = Integer.parseInt(input);
	}
}
