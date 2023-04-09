package InterpreterDataTypes;

public class IntegerDataType extends InterpreterDataType{
	private int data;
	
	public IntegerDataType(int data){
		this.data = data;
	}
	
	public int getData() {
		return data;
	}
	
	public void setData(int data) {
		this.data = data;
	}
	
	@Override
	public String ToString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) {
		data = Integer.parseInt(input);
	}
}
