import Nodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;

public class SemanticAnalysis {
	ProgramNode mainAST;
	
	/**
	 * Semantic Analysis chechs through each functions statementlist. Most if not all type mismatchs are handled already in interpeter.
	 * @param mainAST
	 * @throws Exception
	 */
	public SemanticAnalysis(ProgramNode mainAST) throws Exception{
		this.mainAST = mainAST;
		Iterator<FunctionNode> listAST = mainAST.getFunctionHashMap().values().iterator();
		
		ArrayList<Exception> exceptions = new ArrayList<>();
		while(listAST.hasNext()){
			checkStatements(listAST.next().getStatementList());
		}
	}
	
	
	public void checkStatements(ArrayList<StatementNode> statements) throws Exception {
		for(StatementNode statement: statements){
			/*if (statement instanceof FunctionCallNode) {
				FunctionCallNode calledFunctionStatement = (FunctionCallNode) statement;
				ArrayList<ParameterNode> paramsCalled = calledFunctionStatement.getParametertList();
				
				FunctionNode calledFunction = mainAST.getFunction(calledFunctionStatement.getName());
				ArrayList<VariableNode> paramsExpected = calledFunction.getParameterList();
				if(!calledFunction.isVariadic()){
					
					if (paramsExpected.size() != paramsCalled.size()) {
						throw new InterpreterErrorException(String.format("\n[Error in interpretFunctionCall (parameters inputted are incorrect):{" +
										" Expected (%d) parameters, types (%s)} - {Actual (%d) parameters}\n\tStatement - (%s)]",
								paramsExpected.size(), paramsExpected.stream().map(VariableNode::getType).collect(Collectors.toList()),
								paramsCalled.size(),
								statement
						)
						);
					}
				
					for(int i = 0; i < paramsCalled.size();i++){
						switch (paramsExpected.get(i).getType()){
							case integar -> {
								int finalI = i;
								Optional<VariableNode> theVariable = variableList.stream()
										.filter(variable -> variable.getName() == paramsCalled.get(finalI).getVariable().getVariableName()).findFirst();
								if(paramsCalled.get(i).getVar().getClass().getName() != "Nodes.IntegerNode"){
									throw new Exception(String.format("\n\t[Function (%s) not called properly (parameters inputted are incorrect):{" +
													" Expected (%d) parameters, types (%s)} - {Actual (%s) (%d) parameters}\n\tStatement - (%s)]",
											calledFunction.getName(),
											paramsExpected.size(),
											paramsExpected.stream().map(VariableNode::getType).collect(Collectors.toList()),
											paramsCalled.stream().map(ParameterNode::getVar).collect(Collectors.toList()),
											paramsCalled.size(),
											statement
									));
								}
							}
							case real -> {
								if(paramsCalled.get(i).getVar().getClass().getName() != "Nodes.RealNode"){
									throw new Exception(String.format("[Function (%s) not called properly (parameters inputted are incorrect):{" +
													" Expected (%d) parameters, types (%s)} - {Actual (%s) (%d) parameters}]",
											calledFunction.getName(),
											paramsExpected.size(),
											paramsExpected.stream().map(VariableNode::getType).collect(Collectors.toList()),
											paramsCalled.stream().map(ParameterNode::getVar).collect(Collectors.toList()),
											paramsCalled.size()
									));
								}
							}
							case bool -> {
								if(paramsCalled.get(i).getVar().getClass().getName() != "Nodes.BoolNode"){
									throw new Exception(String.format("[Function (%s) not called properly (parameters inputted are incorrect):{" +
													" Expected (%d) parameters, types (%s)} - {Actual (%s) (%d) parameters}]",
											calledFunction.getName(),
											paramsExpected.size(),
											paramsExpected.stream().map(VariableNode::getType).collect(Collectors.toList()),
											paramsCalled.stream().map(ParameterNode::getVar).collect(Collectors.toList()),
											paramsCalled.size()
									));
								}
							}
							case character -> {
								if(paramsCalled.get(i).getVar().getClass().getName() != "Nodes.CharNode"){
									throw new Exception(String.format("[Function (%s) not called properly (parameters inputted are incorrect):{" +
													" Expected (%d) parameters, types (%s)} - {Actual (%s) (%d) parameters}]",
											calledFunction.getName(),
											paramsExpected.size(),
											paramsExpected.stream().map(VariableNode::getType).collect(Collectors.toList()),
											paramsCalled.stream().map(ParameterNode::getVar).collect(Collectors.toList()),
											paramsCalled.size()
									));
								}
							}
							case string -> {
								if(paramsCalled.get(i).getVar().getClass().getName() != "Nodes.StringNode"){
									throw new Exception(String.format("[Function (%s) not called properly (parameters inputted are incorrect):{" +
													" Expected (%d) parameters, types (%s)} - {Actual (%s) (%d) parameters}]",
											calledFunction.getName(),
											paramsExpected.size(),
											paramsExpected.stream().map(VariableNode::getType).collect(Collectors.toList()),
											paramsCalled.stream().map(ParameterNode::getVar).collect(Collectors.toList()),
											paramsCalled.size()
									));
								}
							}
							case array -> {
							}
							case arrayInt -> {
							}
							case arrayStr -> {
							}
							case arrayFlt -> {
							}
							case abyss -> {
							}
						}
					}
				}
			} else */if (statement instanceof WhileNode){
				checkStatements(((WhileNode) statement).getStatementList());
			} else if (statement instanceof ForNode){
				checkStatements(((ForNode) statement).getStatementList());
			} else if (statement instanceof IfNode){
				checkStatements(((IfNode) statement).getStatementList());
				if(((IfNode) statement).getNextIf() != null){
					checkStatements(((IfNode)(( (IfNode) statement).getNextIf())).getStatementList());
				}
			} else if (statement instanceof RepeatNode){
				checkStatements(((RepeatNode) statement).getStatementList());
			}
		}
	}
	
}
