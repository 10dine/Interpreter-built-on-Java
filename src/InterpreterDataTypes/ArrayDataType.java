package InterpreterDataTypes;

import Nodes.VariableNode;

import java.util.ArrayList;

public class ArrayDataType extends InterpreterDataType{
	
	private ArrayList<InterpreterDataType> data;
	private VariableNode.type arrayType; //
	
	public ArrayDataType(ArrayList<InterpreterDataType> data, VariableNode.type arrayType){
		this.data = data;
		this.arrayType = arrayType;
	}
	
	public ArrayList<InterpreterDataType> getData() {
		return data;
	}
	
	public void setData(ArrayList<InterpreterDataType> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
	@Override
	public void FromString(String input) {
	
	}
}
