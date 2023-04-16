package InterpreterDataTypes;

public abstract class InterpreterDataType {
	boolean cState = false;
	
	public boolean iscState() {
		return cState;
	}
	
	public void setcState(boolean cState) {
		this.cState = cState;
	}
	
	public abstract InterpreterDataType clone();
	
	public abstract String toString();
	
	public abstract void FromString(String input) throws Exception;
}
