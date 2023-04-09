package Nodes;

import InterpreterDataTypes.IntegerDataType;
import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;
import java.lang.Math;

public class BuiltInSquareRoot extends FunctionNode{
	public BuiltInSquareRoot() {
		super();
		this.setName("GetRandom");
		this.setParameterList(new ArrayList<VariableNode>(){{
			add(new VariableNode("output", VariableNode.type.integar, true));
			add(new VariableNode("output", VariableNode.type.integar));
		}});
	}
	
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		if(args.get(0).getClass().getName().equals("InterpreterDataTypes.IntegerDataType")){
			args.get(1).FromString((Math.sqrt(((IntegerDataType)args.get(0)).getData()))+"");
		} else {
			throw new Exception("Incorrect Variable Type for SquareRoot! Expected: Integer");
		}
		
	}
}
