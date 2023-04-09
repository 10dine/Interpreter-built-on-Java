package InterpreterDataTypes;

public class RealDataType extends InterpreterDataType{
	private float data;
	
	public RealDataType(float data) {
		this.data = data;
	}
	
	public float getData() {
		return data;
	}
	
	public void setData(float data) {
		this.data = data;
	}
	
	@Override
	public String ToString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) {
		this.data = Float.parseFloat(input);
	}
}
