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
	
	public void setData(float data) throws Exception {
		if(cState) {
			throw new Exception("[This Variable is constant!]");
		} else {
			this.data = data;
		}
	}
	
	@Override
	public InterpreterDataType clone() {
		return new RealDataType(this.data, this.cState);
	}
	
	@Override
	public String toString() {
		return data+"";
	}
	
	@Override
	public void FromString(String input) throws Exception {
		try{
			if(cState) {
				throw new Exception("[This Variable is constant!]");
			} else {
				this.data = Float.parseFloat(input);
			}
		} catch (Exception e) {
			throw new Exception(String.format("[This IDT(%s) does not accept: (%s) as valid input -> Error: (%s)]", this.getClass().getName(), input, e));
		}
		
	}
}
