package Nodes;

import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;
import java.util.Scanner;

public class BuiltInRead extends FunctionNode{
	public BuiltInRead(){
		super("Read", new ArrayList<VariableNode>());
		setVariadic(true);
	}
	public void execute(ArrayList<InterpreterDataType> args, Scanner log) throws Exception {
		String[] input = log.nextLine().split(" ");
		
		if (input.length  != args.size()){
			throw new Exception("[Error in BuiltInRead: Not Enough Inputs]");
		}
		int inputIndex = 0;
		
		try {
			for (InterpreterDataType data : args) {
				data.FromString(input[inputIndex++]);
			}
		} catch (Exception e){
			throw new Exception(String.format("[Error in BuiltInRead: Parsing Failed -> (%s) ]", e));
		}
	}
}
