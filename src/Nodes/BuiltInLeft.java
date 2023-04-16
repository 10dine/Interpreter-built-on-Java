package Nodes;

import InterpreterDataTypes.IntegerDataType;
import InterpreterDataTypes.InterpreterDataType;
import InterpreterDataTypes.StringDataType;

import java.util.ArrayList;

public class BuiltInLeft extends FunctionNode{
	public BuiltInLeft() {
		super();
		this.setName("Left");
		this.setParameterList(new ArrayList<VariableNode>(){{
			add(new VariableNode("input", VariableNode.type.string, true));
			add(new VariableNode("index", VariableNode.type.integar, true));
			add(new VariableNode("output", VariableNode.type.string));
		}});
	}
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		var input = (StringDataType)args.get(1);
		args.get(2).FromString(input.toString().substring(0, ((IntegerDataType) args.get(1)).getData()));
	}
}
