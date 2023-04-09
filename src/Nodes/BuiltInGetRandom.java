package Nodes;

import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;
import java.util.Random;

public class BuiltInGetRandom extends FunctionNode{
	
	public BuiltInGetRandom() {
		super();
		this.setName("GetRandom");
		this.setParameterList(new ArrayList<VariableNode>(){{
			add(new VariableNode("output", VariableNode.type.integar));
		}});
	}
	
	public void execute(ArrayList<InterpreterDataType> args) throws Exception {
		if(args.get(0).getClass().equals("InterpreterDataTypes.IntegerDataType")){
			Random rand = new Random();
			args.get(0).FromString(String.valueOf(rand.nextInt()));
		} else {
			throw new Exception("Incorrect Variable Type for GetRandom! Expected: Integer");
		}
		
	}
}
