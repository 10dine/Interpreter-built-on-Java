package InterpreterDataTypes;

import Nodes.VariableNode;

public class ArrayDataType extends InterpreterDataType{
	private InterpreterDataType[] data;
	private VariableNode.type arrayType; //
	
	public ArrayDataType(IntegerDataType[] data){
		this.data = data;
	}
	
	public InterpreterDataType[] getData() {
		return data;
	}
	
	public void setData(InterpreterDataType[] data) {
		this.data = data;
	}
	
	@Override
	public String ToString() {
		return data.toString();
	}
	
	@Override
	public void FromString(String input) {
	
	}
}
