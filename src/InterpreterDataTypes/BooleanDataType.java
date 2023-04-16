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
	
	public void setData(boolean data) throws Exception {
		if(cState) {
			throw new Exception("[This Variable is constant!]");
		} else {
			this.data = data;
		}
	}
	
	@Override
	public InterpreterDataType clone() {
		return new BooleanDataType(this.data, this.cState);
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
				this.data = Boolean.parseBoolean(input);
			}
		} catch (Exception e) {
			throw new Exception(String.format("[This IDT(%s) does not accept: (%s) as valid input -> Error: (%s)]", this.getClass().getName(), input, e));
		}
	}
}
