package Nodes;

import InterpreterDataTypes.IntegerDataType;
import InterpreterDataTypes.InterpreterDataType;
import InterpreterDataTypes.StringDataType;

import java.util.ArrayList;

public class BuiltInSubstring extends FunctionNode{
	public BuiltInSubstring() {
		super();
		this.setName("Substring");
		this.setParameterList(new ArrayList<VariableNode>(){{
			add(new VariableNode("output", VariableNode.type.string, true));
			add(new VariableNode("output", VariableNode.type.integar, true));
			add(new VariableNode("output", VariableNode.type.integar, true));
			add(new VariableNode("output", VariableNode.type.string));
		}});
	}
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		var input = ((StringDataType)args.get(0)).getData();
		var start = ((IntegerDataType)args.get(1)).getData();
		var end = ((IntegerDataType)args.get(2)).getData();
		try{
			args.get(3).FromString(input.substring(start, end));
		} catch (Exception e) {
			throw new Exception(e+" Failed at Built In Method Substring");
		}
	}
}
