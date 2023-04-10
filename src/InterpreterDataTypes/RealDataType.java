package InterpreterDataTypes;

public class RealDataType extends InterpreterDataType{
	
	private float data;
	
	public RealDataType(float data, boolean cState) {
		this.data = data;
		this.cState = cState;
	}
	
	public RealDataType(float data) {
		this.data = data;
	}
	
	public RealDataType(boolean cState){this.cState = cState;}
	
	public RealDataType() {}
	
	public float getData() {
		return data;
	}
	
	public void setData(float data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) {
		this.data = Float.parseFloat(input);
	}
}
