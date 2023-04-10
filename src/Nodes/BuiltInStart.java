package Nodes;

import InterpreterDataTypes.ArrayDataType;
import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;

public class BuiltInStart extends FunctionNode{
	public BuiltInStart(){
		super();
		this.setName("Start");
		this.setParameterList(new ArrayList<VariableNode>());
	}
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		if (args.size() == 2){
			if(args.get(0).getClass().equals("InterpreterDataTypes.ArrayDataType")) {
				ArrayDataType array = (ArrayDataType) args.get(0);
				args.get(1).FromString(String.valueOf(array.getData().get(0)));
			} else {
				throw new Exception("");
			}
		}
	}
}
