package Nodes;

import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;

public class BuiltInWrite extends FunctionNode{
	
	/**
	 * built in write function
	 */
	public BuiltInWrite(){
		super("Write", new ArrayList<VariableNode>());
	}
	
	/**
	 * Prints all the InterpreterDataType objects inputted.
	 * @param args array list of InterpreterDataType
	 */
	public void execute(ArrayList<InterpreterDataType> args){
		for(InterpreterDataType data : args){
			System.out.print(data.ToString()+" ");
		}
	}
}
