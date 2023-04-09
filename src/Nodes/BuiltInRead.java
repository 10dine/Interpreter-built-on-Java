package Nodes;

import InterpreterDataTypes.InterpreterDataType;

import java.util.ArrayList;
import java.util.Scanner;

public class BuiltInRead extends FunctionNode{
	public BuiltInRead(){
		super("Read", new ArrayList<VariableNode>());
		//this.setName("Write");
		//this.setParameterList(new ArrayList<VariableNode>());
	}
	public void execute(ArrayList<InterpreterDataType> args){
		Scanner log = new Scanner(System.in);
		String[] input = log.nextLine().split(" ");
		int inputIndex = 0;
		
		for (InterpreterDataType data: args){
			data.FromString(input[inputIndex++]);
		}
		log.close();
	}
}
