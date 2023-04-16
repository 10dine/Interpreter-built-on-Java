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
	public InterpreterDataType clone() {
		return new StringDataType(this.data, this.cState);
	}
	
	@Override
	public String toString() {
		return data;
	}
	
	@Override
	public void FromString(String input) throws Exception {
		try{
			this.data = input;
		} catch (Exception e) {
			throw new Exception(String.format("[This IDT(%s) does not accept: (%s) as valid input -> Error: (%s)]", this.getClass().getName(), input, e));
		}
	}
}
