package Nodes;

import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;

public class BuiltInRealToInteger extends FunctionNode{
	public BuiltInRealToInteger(){
		super();
		this.setName("RealToInteger");
		this.setParameterList(new ArrayList<VariableNode>(){{
			add(new VariableNode("input", VariableNode.type.real, true));
			add(new VariableNode("output", VariableNode.type.integar));
		}});
	}
	
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		if(args.get(0).getClass().getName().equals("InterpreterDataTypes.RealDataType") && args.get(1).getClass().getName().equals("InterpreterDataTypes.IntegerDataType"))
			args.get(1).FromString(String.valueOf(Integer.parseInt((args.get(0).toString()))));
		else
			throw new Exception("Incorrect Variable Type for GetRandom! Expected: Integer");
	}
}
