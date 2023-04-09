import InterpreterDataTypes.*;
import Nodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class Interpreter {
	ProgramNode mainAST;
	
	public Interpreter(ProgramNode programNode) throws NodeErrorException, InterpreterErrorException {
		this.mainAST = programNode;
		interpretFunction(mainAST.getFunction("start)"));
	}
	
	public void interpretFunction(FunctionNode function) throws InterpreterErrorException {
		HashMap<String, InterpreterDataType> local = new HashMap<String, InterpreterDataType>();
		for(VariableNode param: function.getParameterList()){
			local.put(param.getName(), expression(local, param));
		}
		for(VariableNode variables: function.getVariableList()){
			local.put(variables.getName(), expression(local, variables));
		}
		interpretBlock(local, function.getStatementList());
	}
	
	public void interpretBlock(HashMap<String, InterpreterDataType> local, ArrayList<StatementNode> block) throws InterpreterErrorException {
		for(Node statement: block){
			if(statement instanceof AssignmentNode){
				AssignmentNode assignmentNode = (AssignmentNode) statement;
				if(local.containsKey(((AssignmentNode) statement).getTargetVariableName())){
					local.put(assignmentNode.getTargetVariableName().getVariableName(), expression(local, assignmentNode.getValue()));
				} else {
					throw new InterpreterErrorException(String.format(
							"[Error in interpretBlock(): %s does not exist in local!]",
							assignmentNode.getTargetVariableName()
					));
				}
			} else if (statement instanceof IfNode){
				interpretIfNode(local, (IfNode) statement);
			} else if (statement instanceof ForNode){
				interpretForNode(local, (ForNode) statement);
			} else if (statement instanceof RepeatNode){
				interpretRepeatNode(local, (RepeatNode) statement);
			} else if (statement instanceof WhileNode) {
				interpretWhileNode(local, (WhileNode) statement);
			}
		}
	}
	
	public void interpretIfNode(HashMap<String, InterpreterDataType> local, IfNode ifNode) throws InterpreterErrorException {
		IfNode next = (IfNode) ifNode.getNextIf();
		if(boolCompare(local, (BooleanCompareNode)ifNode.getCondition())){
			interpretBlock(local, ifNode.getStatementList());
		} else if (next != null) {
			interpretIfNode(local, next);
		}
	}
	
	public void interpretForNode(HashMap<String, InterpreterDataType> local, ForNode forNode) throws InterpreterErrorException {
		
		InterpreterDataType initial = expression(local, forNode.getFrom());
		if(!(initial instanceof IntegerDataType)){
			throw new InterpreterErrorException(String.format
					("[For Node cannot be processed: %s]", forNode)
			);
		}
		int initialInt = ((IntegerDataType) initial).getData();
		
		InterpreterDataType End = expression(local, forNode.getTo());
		if(!(End instanceof IntegerDataType)){
			throw new InterpreterErrorException(String.format
					("[For Node cannot be processed: %s]", forNode)
			);
		}
		int endInt = ((IntegerDataType) End).getData()+1;
		
		if(local.get((forNode.getVariable().getVariableName())) != null){
			throw new InterpreterErrorException("[Error in interpretForNode: Variable already in use!]");
		}
		
		for(int i = initialInt; i < endInt; i++){
			local.put((forNode.getVariable().getVariableName()), new IntegerDataType(initialInt));
			interpretBlock(local, forNode.getStatementList());
		}
		
		local.remove((forNode.getVariable().getVariableName()));
	}
	
	public void interpretRepeatNode(HashMap<String, InterpreterDataType> local, RepeatNode repeatNode) throws InterpreterErrorException {
		do{
			interpretBlock(local, repeatNode.getStatementList());
		} while (boolCompare(local, repeatNode.getCondition()));
	}
	
	public void interpretWhileNode(HashMap<String, InterpreterDataType> local, WhileNode whileNode) throws InterpreterErrorException {
		while (boolCompare(local, whileNode.getCondition())){
			interpretBlock(local, whileNode.getStatementList());
		}
	}
	
	public boolean boolCompare(HashMap<String, InterpreterDataType> local,BooleanCompareNode booleanCompareNode) throws InterpreterErrorException {
		InterpreterDataType left = expression(local, booleanCompareNode.getLeft());
		BooleanCompareNode.boolType comparator = booleanCompareNode.getComparator();
		InterpreterDataType right = expression(local, booleanCompareNode.getRight());
		
		try{
			switch(comparator){
				case COMPARATOR_EQUAL -> {
					if (left instanceof IntegerDataType){
						if (right instanceof IntegerDataType){
							return ((IntegerDataType) left).getData() == ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((IntegerDataType) left).getData() == ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((IntegerDataType) left).getData() == ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType){
							return ((RealDataType) left).getData() == ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((RealDataType) left).getData() == ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((RealDataType) left).getData() == ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType){
						if (right instanceof IntegerDataType){
							return ((CharacterDataType) left).getData() == ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((CharacterDataType) left).getData() == ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((CharacterDataType) left).getData() == ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case COMPARATOR_NOT_EQUAL -> {
					if (left instanceof IntegerDataType){
						if (right instanceof IntegerDataType){
							return ((IntegerDataType) left).getData() != ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((IntegerDataType) left).getData() != ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((IntegerDataType) left).getData() != ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType){
							return ((RealDataType) left).getData() != ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((RealDataType) left).getData() != ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((RealDataType) left).getData() != ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType){
						if (right instanceof IntegerDataType){
							return ((CharacterDataType) left).getData() != ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((CharacterDataType) left).getData() != ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((CharacterDataType) left).getData() != ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case COMPARATOR_LESS -> {
					if (left instanceof IntegerDataType) {
						if (right instanceof IntegerDataType) {
							return ((IntegerDataType) left).getData() < ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType) {
							return ((IntegerDataType) left).getData() < ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType) {
							return ((IntegerDataType) left).getData() < ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType) {
						if (right instanceof IntegerDataType) {
							return ((RealDataType) left).getData() < ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType) {
							return ((RealDataType) left).getData() < ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType) {
							return ((RealDataType) left).getData() < ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType) {
						if (right instanceof IntegerDataType) {
							return ((CharacterDataType) left).getData() < ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType) {
							return ((CharacterDataType) left).getData() < ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType) {
							return ((CharacterDataType) left).getData() < ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case COMPARATOR_LESS_OR_EQUALS -> {
					if (left instanceof IntegerDataType){
						if (right instanceof IntegerDataType){
							return ((IntegerDataType) left).getData() <= ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((IntegerDataType) left).getData() <= ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((IntegerDataType) left).getData() <= ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType){
							return ((RealDataType) left).getData() <= ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((RealDataType) left).getData() <= ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((RealDataType) left).getData() <= ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType){
						if (right instanceof IntegerDataType){
							return ((CharacterDataType) left).getData() <= ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((CharacterDataType) left).getData() <= ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((CharacterDataType) left).getData() <= ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case COMPARATOR_GREATER -> {
					if (left instanceof IntegerDataType){
						if (right instanceof IntegerDataType){
							return ((IntegerDataType) left).getData() > ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((IntegerDataType) left).getData() > ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((IntegerDataType) left).getData() > ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType){
							return ((RealDataType) left).getData() > ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((RealDataType) left).getData() > ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((RealDataType) left).getData() > ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType){
						if (right instanceof IntegerDataType){
							return ((CharacterDataType) left).getData() > ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((CharacterDataType) left).getData() > ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((CharacterDataType) left).getData() > ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case COMPARATOR_GREATER_OR_EQUALS -> {
					if (left instanceof IntegerDataType){
						if (right instanceof IntegerDataType){
							return ((IntegerDataType) left).getData() >= ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((IntegerDataType) left).getData() >= ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((IntegerDataType) left).getData() >= ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType){
							return ((RealDataType) left).getData() >= ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((RealDataType) left).getData() >= ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((RealDataType) left).getData() >= ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType){
						if (right instanceof IntegerDataType){
							return ((CharacterDataType) left).getData() >= ((IntegerDataType) right).getData();
						} else if (left instanceof RealDataType){
							return ((CharacterDataType) left).getData() >= ((RealDataType) right).getData();
						} else if (left instanceof CharacterDataType){
							return ((CharacterDataType) left).getData() >= ((CharacterDataType) right).getData();
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
			}
		} catch (Exception e) {
			throw new InterpreterErrorException(
					String.format(
							"[Error in boolCompare: Incomparable Left Node(%s) and Right Node(%s)]"
							, left.ToString(), right.ToString()
					)
			);
		}
		return false;
	}
	
	public InterpreterDataType getVariableFromReference(HashMap<String, InterpreterDataType> local, VariableReferenceNode variableReferenceNode) throws InterpreterErrorException {
		try{
			return local.get(((VariableReferenceNode) variableReferenceNode).getVariableName());
		} catch (NullPointerException e){
			throw new InterpreterErrorException("[Error in expression: Variable not found in local!]");
		}
	}
	
	public InterpreterDataType expression(HashMap<String, InterpreterDataType> local, Node expressionNode) throws InterpreterErrorException {
		InterpreterDataType left = null;
		MathOpNode.operations operation = null;
		InterpreterDataType right = null;
		if (expressionNode instanceof VariableReferenceNode){
			try{
				left = local.get(((VariableReferenceNode) expressionNode).getVariableName());
				return left;
			} catch (NullPointerException e){
				throw new InterpreterErrorException("[Error in expression: Variable not found in local!]");
			}
		} else if(expressionNode instanceof MathOpNode){
			left = expression(local, ((MathOpNode) expressionNode).getLeftSide());
			operation = ((MathOpNode) expressionNode).getOperator();
			right = expression(local, ((MathOpNode) expressionNode).getRightSide());
		} else if (expressionNode instanceof IntegerNode){
			left = new IntegerDataType(((IntegerNode) expressionNode).getElement());
		} else if (expressionNode instanceof RealNode){
			left = new RealDataType(((RealNode) expressionNode).getElement());
		} else if (expressionNode instanceof StringNode){
			left = new StringDataType(((StringNode) expressionNode).getElement());
		} else if (expressionNode instanceof CharNode){
			left = new CharacterDataType(((CharNode) expressionNode).getElement());
		} else if (expressionNode instanceof BoolNode){
			left = new BooleanDataType(((BoolNode) expressionNode).getState());
		} else {
			throw new InterpreterErrorException("[Error in expression: Invalid Node(check parser)]");
		}
		
		if(operation == null){
			return left;
		}
		try {
			switch (operation) {
				case ADD -> {
					if (left instanceof IntegerDataType) {
						if (right instanceof IntegerDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() + ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((IntegerDataType) left).getData() + ((RealDataType) right).getData());
						} else if (right instanceof StringDataType) {
							return new StringDataType(((IntegerDataType) left).getData() + ((StringDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() + ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType) {
							return new RealDataType(((RealDataType) left).getData() + ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((RealDataType) left).getData() + ((RealDataType) right).getData());
						} else if (right instanceof StringDataType) {
							return new StringDataType(((RealDataType) left).getData() + ((StringDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new RealDataType(((RealDataType) left).getData() + ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof StringDataType){
						if (right instanceof IntegerDataType) {
							return new StringDataType(((StringDataType) left).getData() + ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new StringDataType(((StringDataType) left).getData() + ((RealDataType) right).getData());
						} else if (right instanceof StringDataType) {
							return new StringDataType(((StringDataType) left).getData() + ((StringDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new StringDataType(((StringDataType) left).getData() + ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType){
						if (right instanceof IntegerDataType) {
							return new IntegerDataType((((CharacterDataType) left).getData() + ((IntegerDataType) right).getData()));
						}else if (right instanceof CharacterDataType) {
							return new IntegerDataType((((CharacterDataType) left).getData() + ((CharacterDataType) right).getData()));
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
					
				}
				case SUBTRACT -> {
					if (left instanceof IntegerDataType) {
						if (right instanceof IntegerDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() - ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((IntegerDataType) left).getData() - ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() - ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType) {
							return new RealDataType(((RealDataType) left).getData() - ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((RealDataType) left).getData() - ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new RealDataType(((RealDataType) left).getData() - ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof CharacterDataType){
						if (right instanceof IntegerDataType) {
							return new IntegerDataType((char) (((CharacterDataType) left).getData() - ((IntegerDataType) right).getData()));
						}else if (right instanceof CharacterDataType) {
							return new IntegerDataType((char) (((CharacterDataType) left).getData() - ((CharacterDataType) right).getData()));
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case MULTIPLICATION -> {
					if (left instanceof IntegerDataType) {
						if (right instanceof IntegerDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() * ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((IntegerDataType) left).getData() * ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() * ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType) {
							return new RealDataType(((RealDataType) left).getData() * ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((RealDataType) left).getData() * ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new RealDataType(((RealDataType) left).getData() * ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case DIVISION -> {
					if (left instanceof IntegerDataType) {
						if (right instanceof IntegerDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() / ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((IntegerDataType) left).getData() / ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() / ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType) {
							return new RealDataType(((RealDataType) left).getData() / ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((RealDataType) left).getData() / ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new RealDataType(((RealDataType) left).getData() / ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
				case MOD -> {
					if (left instanceof IntegerDataType) {
						if (right instanceof IntegerDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() % ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((IntegerDataType) left).getData() % ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new IntegerDataType(((IntegerDataType) left).getData() % ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else if (left instanceof RealDataType){
						if (right instanceof IntegerDataType) {
							return new RealDataType(((RealDataType) left).getData() % ((IntegerDataType) right).getData());
						} else if (right instanceof RealDataType) {
							return new RealDataType(((RealDataType) left).getData() % ((RealDataType) right).getData());
						} else if (right instanceof CharacterDataType) {
							return new RealDataType(((RealDataType) left).getData() % ((CharacterDataType) right).getData());
						} else {
							throw new InterpreterErrorException("");
						}
					} else {
						throw new InterpreterErrorException("");
					}
				}
			}
		} catch (Exception e){
			throw new InterpreterErrorException(
					String.format(
							"[Error in expression: Invalid Operation - %s %s %s (%s %s %s)]",
							left.getClass().getName(), operation, right.getClass().getName(),
							left.ToString(), operation, right.ToString()
					));
		}
		return left;
	}
	
	
}

