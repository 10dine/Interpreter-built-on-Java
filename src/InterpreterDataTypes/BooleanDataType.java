package InterpreterDataTypes;

public class BooleanDataType extends InterpreterDataType{
	
	private boolean data;
	
	public BooleanDataType(boolean data, boolean cState){
		this.data = data;
		this.cState = cState;
	}
	
	public BooleanDataType(boolean data){
		this.data = data;
	}
	
	public BooleanDataType() {}
	
	public boolean getData() {
		return data;
	}
	
	public void setData(boolean data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) {data = Boolean.parseBoolean(input);}
}
