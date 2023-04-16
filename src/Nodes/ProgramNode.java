package Nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ProgramNode holds collection of FunctionNodes
 */
public class ProgramNode extends Node{
	
	private HashMap<String, FunctionNode> functionHashMap = new HashMap<String, FunctionNode>();
	
	public ProgramNode() throws NodeErrorException {
		this.addFunction(new BuiltInWrite());
		this.addFunction(new BuiltInRead());
		this.addFunction(new BuiltInLeft());
		this.addFunction(new BuiltInRight());
		this.addFunction(new BuiltInSubstring());
		this.addFunction(new BuiltInGetRandom());
		this.addFunction(new BuiltInIntegerToReal());
		this.addFunction(new BuiltInSquareRoot());
		this.addFunction(new BuiltInRealToInteger());
		this.addFunction(new BuiltInStart());
		this.addFunction(new BuiltInEnd());
	}
	
	public boolean containsKey(String name){
		return functionHashMap.containsKey(name);
	}
	
	public void addFunction(FunctionNode function) throws NodeErrorException {
		if (functionHashMap.containsKey(function.getName())){
			throw new NodeErrorException(String.format("No function named \"%s\" found!", function.getName()));
		} else {
			functionHashMap.put(function.getName(), function);
		}
	}
	
	public FunctionNode getFunction(String functionName) throws NodeErrorException {
		if (functionHashMap.containsKey(functionName)){
			return functionHashMap.get(functionName);
		} else {
			throw new NodeErrorException(String.format("No function named \"%s\" found!", functionName));
		}
	}
	
	public Iterator<FunctionNode> iterator(){
		return new Iterator<FunctionNode>() {
			
			ArrayList<FunctionNode> functionList = (ArrayList<FunctionNode>) functionHashMap.values();
			
			@Override
			public boolean hasNext() {
				if(functionList.size()>0){
					return true;
				} else {
					return false;
				}
			}
			
			@Override
			public FunctionNode next() {
				return functionList.remove(0);
			}
		};
	}
	
	public HashMap<String, FunctionNode> getFunctionHashMap() {
		return functionHashMap;
	}
	
	public void setFunctionHashMap(HashMap<String, FunctionNode> functionHashMap) {
		this.functionHashMap = functionHashMap;
	}
	
	@Override
	public String toString() {
		return "ProgramNode{" +
				"\n\tfunctionHashMap=" + functionHashMap +
				'}';
	}
}
