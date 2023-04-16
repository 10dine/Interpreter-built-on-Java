package Nodes;

import InterpreterDataTypes.IntegerDataType;
import InterpreterDataTypes.InterpreterDataType;
import InterpreterDataTypes.StringDataType;

import java.util.ArrayList;

public class BuiltInRight extends FunctionNode{
	public BuiltInRight() {
		super();
		this.setName("Right");
		this.setParameterList(new ArrayList<VariableNode>(){{
			add(new VariableNode("input", VariableNode.type.string, true));
			add(new VariableNode("index", VariableNode.type.integar, true));
			add(new VariableNode("output", VariableNode.type.string));
		}});
	}
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		var input = (StringDataType)args.get(1);
		int len = ((StringDataType) args.get(0)).getData().length();
		args.get(2).FromString(input.toString().substring(((IntegerDataType) args.get(1)).getData(), len-1));
	}
	
}
