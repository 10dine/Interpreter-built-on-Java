package Nodes;

import InterpreterDataTypes.ArrayDataType;
import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;

public class BuiltInEnd extends FunctionNode{
	
	/**
	 * Built in function takes in an array data type and var interepreterdatatype
	 */
	public BuiltInEnd(){
		super();
		this.setName("End");
		this.setParameterList(new ArrayList<VariableNode>());
	}
	
	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		if (args.size() == 2){
			if(args.get(0).getClass().equals("InterpreterDataTypes.ArrayDataType")) {
				ArrayDataType array = (ArrayDataType) args.get(0);
				int len = array.getData().size();
				args.get(1).FromString(String.valueOf(array.getData().get(len-1)));
			} else {
				throw new Exception("");
			}
		}
	}
}
