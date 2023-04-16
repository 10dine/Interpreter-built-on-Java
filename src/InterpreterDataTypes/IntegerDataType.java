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
	
	public void setData(int data) throws Exception {
		if(cState) {
			throw new Exception("[This Variable is constant!]");
		} else {
			this.data = data;
		}
	}
	
	@Override
	public InterpreterDataType clone() {
		return new IntegerDataType(this.data, this.cState);
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
				data = Integer.parseInt(input);
			}
		} catch (Exception e) {
			throw new Exception(String.format("[This IDT(%s) does not accept: (%s) as valid input -> Error: (%s)]", this.getClass().getName(), input, e));
		}
	}
}
