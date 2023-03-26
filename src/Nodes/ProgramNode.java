package Nodes;

import java.util.HashMap;

/**
 * ProgramNode holds collection of FunctionNodes
 */
public class ProgramNode extends Node{
	
	private HashMap<String, FunctionNode> functionHashMap = new HashMap<String, FunctionNode>();
	
	public ProgramNode(){
	}
	
	public boolean containsKey(String name){
		return functionHashMap.containsKey(name);
	}
	
	public void addFunction(FunctionNode function) throws NodeErrorException {
		if (functionHashMap.containsKey(function.getName())){
			throw new NodeErrorException(String.format("No function named \" %s \" found!", function.getName()));
		} else {
			functionHashMap.put(function.getName(), function);
		}
	}
	
	public FunctionNode getFunction(String functionName) throws NodeErrorException {
		if (functionHashMap.containsKey(functionName)){
			return functionHashMap.get(functionName);
		} else {
			throw new NodeErrorException(String.format("No function named \" %s \" found!", functionName));
		}
	}
	
	@Override
	public String toString() {
		return "ProgramNode{" +
				"\n\tfunctionHashMap=" + functionHashMap +
				'}';
	}
}
