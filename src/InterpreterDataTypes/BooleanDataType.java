package InterpreterDataTypes;

public class BooleanDataType extends InterpreterDataType{
	private boolean data;
	
	public BooleanDataType(boolean data){
		this.data = data;
	}
	
	public boolean getData() {
		return data;
	}
	
	public void setData(boolean data) {
		this.data = data;
	}
	
	
	
	@Override
	public String ToString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) {data = Boolean.parseBoolean(input);}
}
