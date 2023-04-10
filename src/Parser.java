import Nodes.*;

import java.util.ArrayList;

/**
 * Parser Object
 */
public class Parser {
	
	/**
	 *
	 */
	private int lineCounter = 1;
	
	private TokenList tokenList;
	
	/**
	 * Custom Collection of functionNodes (for now)
	 */
	private ProgramNode program;
	
	/**
	 * Constructor for Parser. Starts parse() on Parser. Program on initialization;
	 * @param tokens tokenList Object which is essentially an arraylist of tokens
	 * @throws Exception can be the following exceptions
	 *	 * @throws ParserErrorException if there is an error parsing the function
	 * 	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 * 	 * @throws NodeErrorException if there is an error creating a node for the function
	 */
	public Parser( TokenList tokens) throws Exception {
		this.tokenList = tokens;
		try {
			program = parse();
		} catch (Exception E){
			throw new Exception(E+" | At Line: "+lineCounter);
		}
	}
	
	/**
	 * parse calls function() until tokenList has nothing in it
	 * @return ProgramNode object
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 * @throws NodeErrorException if there is an error creating a node for the function
	 */
	private ProgramNode parse() throws ParserErrorException, NodeErrorException, SyntaxErrorException {
		ProgramNode parseProgram = new ProgramNode();
		tokenList.removeIf(token -> token.getType() == Token.tokenType.COMMENT);
		while (tokenList.size() > 0){
			parseProgram.addFunction(function());
		}
		return parseProgram;
	}
	
	/**
	 * Parses the function definition and returns a FunctionNode object.
	 *
	 * There's essentially four stages
	 *  1 - Get function name
	 *  2 - Get Parameters (as a list of VariableNodes)
	 *  3 - Get Variables (as another list of VariableNodes)
	 *  4 - Get Statements(Right now a list of AssignmentNodes)
	 *  		Calls statements()
	 *
	 * @return the FunctionNode object representing the parsed function
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 * @throws NodeErrorException if there is an error creating a node for the function
	 */
	private FunctionNode function() throws ParserErrorException, SyntaxErrorException, NodeErrorException {
		
		String functionName = null;
		ArrayList<VariableNode> paramNodes = null;
		ArrayList<VariableNode> variableNodes = new ArrayList<>();
		ArrayList<StatementNode> statementsList;
		
		Node arrayIndex = null;
		
		//Stage 1 starts here
		if (matchAndRemove(Token.tokenType.DEFINE) != null) {
			if (peekType() == Token.tokenType.IDENTIFIER) {
				functionName = matchAndRemove(Token.tokenType.IDENTIFIER).getValue();
				
				//Stage 2 starts here
				paramNodes = parameterDeclarations();
				expectEndsOfLine();
			} else {
				throw new SyntaxErrorException("No name for function!");
			}
		}
		
		//Stage 3 starts here
		enum state{
			start,
			constant,
			variables,
			end,
			
			startVariables,
			typeAndSet,
			array
		}
		state parsingVariables = state.start;
		boolean constantState = false;
		
		while (parsingVariables != state.end){
			
			switch (parsingVariables){
				
				case start:
					if (matchAndRemove(Token.tokenType.CONSTANT) != null){
						constantState = true;
						parsingVariables = state.constant;
					} else if (matchAndRemove(Token.tokenType.VARAIABLES) != null){
						constantState = false;
						parsingVariables = state.variables;
					} else if (peekType() == Token.tokenType.INDENT){
						parsingVariables = state.end;
					} else {
						throw new SyntaxErrorException("Unexpected token!");
					}
					break;
					
				case constant:
					String constantIdentifier;
					if (peekType() != Token.tokenType.IDENTIFIER){
						throw new SyntaxErrorException(String.format("Expected identifier token. Got: %s", tokenList.get(0)));
					}
					constantIdentifier = matchAndRemove(Token.tokenType.IDENTIFIER).getValue();
					if(matchAndRemove(Token.tokenType.ASSIGNMENT_EQUALS) == null){
						throw new SyntaxErrorException(String.format("Expected '=' token. Got: %s", tokenList.get(0)));
					}
					Node tempConstantNode;
					switch (peekType()){
						case INTEGER -> tempConstantNode = new IntegerNode(Integer.valueOf(tokenList.pop().getValue()));
						case REAL -> tempConstantNode = new RealNode(Float.valueOf(tokenList.pop().getValue()));
						case STRINGLITERAL -> tempConstantNode = new StringNode(tokenList.pop().getValue());
						case CHARACTERLITERAL -> tempConstantNode = new CharNode(tokenList.pop().getValue().charAt(0));
						case BOOLEAN -> tempConstantNode = new BoolNode(Boolean.valueOf(tokenList.pop().getValue()));
						case ARRAY -> tempConstantNode = new StringNode();
						default -> throw new SyntaxErrorException("Invalid type!");
					}
					variableNodes.add(new VariableNode(constantIdentifier, tempConstantNode, constantState));
					
					expectEndsOfLine();
					constantState = false;
					
					parsingVariables = state.start;
					break;
					
				case variables:
					TokenList variableNameList = new TokenList();
					state variableMachine = state.startVariables;
					while (variableMachine != state.end){
						switch (variableMachine){
							case startVariables:
								if (peekType() == Token.tokenType.IDENTIFIER) {
									//throw new SyntaxErrorException(String.format("Expected identifier token. Got: %s", tokenList.get(0)));
									variableNameList.add(tokenList.pop());
								} else if (matchAndRemove(Token.tokenType.COMMA) != null) {
									//do nothing
								} else if (matchAndRemove(Token.tokenType.ASSIGNMENT_COLON) != null){
									variableMachine = state.typeAndSet;
								}
								break;
							
							case typeAndSet:
								VariableNode.type tempType;
								switch (peekType()){
									case INTEGERTYPE -> tempType = VariableNode.type.integar;
									case REALTYPE -> tempType = VariableNode.type.real;
									case STRINGTYPE -> tempType = VariableNode.type.string;
									case CHARACTERTYPE -> tempType = VariableNode.type.character;
									case BOOLEANTYPE -> tempType = VariableNode.type.bool;
									case ARRAYTYPE -> tempType = VariableNode.type.array;
									default -> throw new SyntaxErrorException("Invalid type in function params! ");
								}
								if(tempType == VariableNode.type.array){
									variableMachine = state.array;
									break;
								}
								tokenList.pop();
								if (matchAndRemove(Token.tokenType.FROM) != null) {
									Node left = expression();
									if (matchAndRemove(Token.tokenType.TO) == null) {
										throw new SyntaxErrorException("No 'to' token");
									}
									Node right = expression();
									
									for (Token token : variableNameList) {
										variableNodes.add(new VariableNode(token.getValue(), tempType, constantState, left, right));
									}
									variableNameList.clear();
									
								} else {
									for(Token token: variableNameList){
										variableNodes.add(new VariableNode(token.getValue(), tempType, constantState));
									}
									variableNameList.clear();
								}
								
								if (matchAndRemove(Token.tokenType.ENDOFLINE) == null){
									throw new SyntaxErrorException(
											String.format("Unexpected Token in parameter declaration. Token: %s", tokenList.pop())
									);
								}
								lineCounter++;
								variableMachine = state.end;
								break;
							
							case array:
								tokenList.pop();
								if(matchAndRemove(Token.tokenType.OF) == null){
									throw new SyntaxErrorException("No 'of' token");
								}
								switch (peekType()){
									case INTEGERTYPE -> tempType = VariableNode.type.arrayInt;
									case REALTYPE -> tempType = VariableNode.type.arrayFlt;
									case STRINGTYPE -> tempType = VariableNode.type.arrayStr;
									default -> throw new SyntaxErrorException("Invalid Type for array!");
								}
								tokenList.pop();
								if (matchAndRemove(Token.tokenType.FROM) != null) {
									Node left = expression();
									if (matchAndRemove(Token.tokenType.TO) == null) {
										throw new SyntaxErrorException("No 'to' token");
									}
									Node right = expression();
									arrayIndex = new MathOpNode(MathOpNode.operations.SUBTRACT, left, right);
									
									for (Token token : variableNameList) {
										variableNodes.add(new VariableNode(token.getValue(), tempType, constantState, arrayIndex));
									}
									variableNameList.clear();
									
								} else {
									for(Token token: variableNameList){
										variableNodes.add(new VariableNode(token.getValue(), tempType, constantState));
									}
									variableNameList.clear();
								}
								constantState = true;
								if (matchAndRemove(Token.tokenType.ENDOFLINE) == null){
									throw new SyntaxErrorException(
											String.format("Unexpected Token in parameter declaration. Token: %s", tokenList.pop())
									);
								}
								lineCounter++;
								variableMachine = state.end;
								break;
							
						}
					}
					parsingVariables = state.start;
					break;
			}
			
		}
		//Stage 4 starts here
		statementsList = statements();
		return new FunctionNode(functionName, paramNodes, variableNodes, statementsList);
	}
	
	/**
	 * Uses a state machine to creates a list of parameter of Variable Nodes;
	 *__state machine__
	 * state set to state.start
	 * 1. while != state.end
	 * 2. if state.start check for "(" and switches to state.identifier
	 * 3. checks for
	 * 	var -  sets constantState,
	 * 	identifier - gets name of variables,
	 * 	comma - ignores commas,
	 * 	semi-colon - switch to typeAndSet
	 * in that order.
	 * 4. (On typeAndSet) Now checks for variable type and exits loop if ")" is found by setting state = state.end;
	 * @return ArrayList of VariableNode s
	 * @throws SyntaxErrorException throws parameter declaration syntax error
	 */
	public ArrayList<VariableNode> parameterDeclarations() throws SyntaxErrorException, ParserErrorException {
		
		ArrayList<VariableNode> variableList = new ArrayList<VariableNode>();
		TokenList variableNameList = new TokenList();
		boolean constantState = true;
		Node arrayIndex = null;
		
		enum parameterDeclarationState {
			start,
			identifier,
			typeAndSet,
			array,
			end
		}
		
		VariableNode.type tempType = VariableNode.type.abyss;
		
		parameterDeclarationState paramDeclarationState = parameterDeclarationState.start;
		
		while(paramDeclarationState != parameterDeclarationState.end) {
				switch (paramDeclarationState) {
					case start:
						if (matchAndRemove(Token.tokenType.PARAMETERS_START) != null) {
							if(matchAndRemove(Token.tokenType.PARAMETERS_END) != null){
								return variableList;
							}
							paramDeclarationState = parameterDeclarationState.identifier;
						} else {
							throw new SyntaxErrorException(String.format("Expected \"(\" token. Got: %s", tokenList.get(0)));
						}
						break;
						
					case identifier:
						if (matchAndRemove(Token.tokenType.VAR) != null){
							constantState = false;
						} else if (peekType() == Token.tokenType.IDENTIFIER) {
							variableNameList.add(tokenList.pop());
						} else if (matchAndRemove(Token.tokenType.ASSIGNMENT_COLON) != null){
							paramDeclarationState = parameterDeclarationState.typeAndSet;
						} else if(matchAndRemove(Token.tokenType.COMMA) == null) {
							throw new SyntaxErrorException(String.format("Unexpected Token in parameter declaration. Token: %s", tokenList.pop()));
						}
						break;
						
					case typeAndSet:
						switch (peekType()){
							case INTEGERTYPE -> tempType = VariableNode.type.integar;
							case REALTYPE -> tempType = VariableNode.type.real;
							case STRINGTYPE -> tempType = VariableNode.type.string;
							case CHARACTERTYPE -> tempType = VariableNode.type.character;
							case BOOLEANTYPE -> tempType = VariableNode.type.bool;
							case ARRAYTYPE -> tempType = VariableNode.type.array;
							default -> throw new SyntaxErrorException("Invalid type in function params! ");
						}
						
						if(tempType == VariableNode.type.array){
							paramDeclarationState = parameterDeclarationState.array;
							break;
						}
						
						tokenList.pop();
						for(Token token: variableNameList){
							variableList.add(new VariableNode(token.getValue(), tempType, constantState));
						}
						variableNameList.clear();
						constantState = true;
						if (peekType() == Token.tokenType.SEMI_COLON){
							tokenList.pop();
							paramDeclarationState = parameterDeclarationState.identifier;
						} else if (peekType() == Token.tokenType.PARAMETERS_END){
							matchAndRemove(Token.tokenType.PARAMETERS_END);
							paramDeclarationState = parameterDeclarationState.end;
						} else {
							throw new SyntaxErrorException(String.format("Unexpected Token in parameter declaration. Token: %s", tokenList.pop()));
						}
						break;
						
					case array:
						tokenList.pop();
						if(matchAndRemove(Token.tokenType.OF) == null){
							throw new SyntaxErrorException("No 'of' token");
						}
						switch (peekType()){
							case INTEGERTYPE -> tempType = VariableNode.type.arrayInt;
							case REALTYPE -> tempType = VariableNode.type.arrayFlt;
							case STRINGTYPE -> tempType = VariableNode.type.arrayStr;
							default -> throw new SyntaxErrorException("Invalid Type for array!");
						}
						
						tokenList.pop();
						
						if (matchAndRemove(Token.tokenType.FROM) != null) {
							Node left = expression();
							if (matchAndRemove(Token.tokenType.TO) == null) {
								throw new SyntaxErrorException("No 'to' token");
							}
							Node right = expression();
							arrayIndex = new MathOpNode(MathOpNode.operations.SUBTRACT, left, right);
							
							for (Token token : variableNameList) {
								variableList.add(new VariableNode(token.getValue(), tempType, constantState, arrayIndex));
							}
							variableNameList.clear();
							
						} else {
							for(Token token: variableNameList){
								variableList.add(new VariableNode(token.getValue(), tempType, constantState));
							}
							variableNameList.clear();
						}
						
						constantState = true;
						if (peekType() == Token.tokenType.SEMI_COLON){
							tokenList.pop();
							paramDeclarationState = parameterDeclarationState.identifier;
						} else if (peekType() == Token.tokenType.PARAMETERS_END){
							matchAndRemove(Token.tokenType.PARAMETERS_END);
							paramDeclarationState = parameterDeclarationState.end;
						} else {
							throw new SyntaxErrorException(String.format("Unexpected Token in parameter declaration. Token: %s", tokenList.pop()));
						}
						
						
				}
			
		}
		
		return variableList;
	}
	
	/**
	 * Expects indent and then loops till dedent while calling statement()
	 * @return ArrayList<StatementNode>
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 */
	public ArrayList<StatementNode> statements() throws SyntaxErrorException, ParserErrorException, NodeErrorException {
		ArrayList<StatementNode> statementsList = new ArrayList<StatementNode>();
		if(matchAndRemove(Token.tokenType.INDENT) == null){
			throw new SyntaxErrorException("Expected indent!");
		}
		while(matchAndRemove(Token.tokenType.DEDENT) == null){
			statementsList.add(statement());
			expectEndsOfLine();
			if(tokenList.size()==0){
				tokenList.add(new Token(Token.tokenType.DEDENT, ""));
			}
		}
		return statementsList;
	}
	
	/**
	 * Currently calls assignment()
	 * @return Currently only AssignmentNodes
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 */
	public StatementNode statement() throws ParserErrorException, SyntaxErrorException, NodeErrorException {
		
		switch (peekType()){
			case IDENTIFIER -> {
				AssignmentNode assignmentNode;
				if((assignmentNode = assignment()) != null){
					return assignmentNode;
				}
				FunctionCallNode functionCallNode;
				if((functionCallNode = parseFunctionCall()) != null){
					return functionCallNode;
				}
			}
			case IF -> {
				return parseIf();
			}
			case FOR -> {
				return parseFor();
			}
			case REPEAT -> {
				return parseRepeat();
			}
			case WHILE -> {
				return parseWhile();
			}
			case ENDOFLINE -> expectEndsOfLine();
			default -> {
				throw new ParserErrorException("Unsupported statement!");
			}
		}
		return null;
	}
	
	/**
	 *  1 - uses variableReference()
	 *  2 - Looks for assignment token
	 *  3 - Gets right side expression of assignment
	 * @return AssignmentNode
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 */
	public AssignmentNode assignment() throws SyntaxErrorException, ParserErrorException {
		
		Node tempTarget = variableReference();
		
		if(tempTarget == null){
			return null;
		}
		
		if(matchAndRemove(Token.tokenType.ASSIGNMENT_COLON_EQUALS) == null){
			return null;
		}
		
		Node value = boolCompare();
		
		if(tokenList.isEmpty()){
			return new AssignmentNode ((VariableReferenceNode) tempTarget, value);
		}
		
		//if (matchAndRemove(Token.tokenType.ENDOFLINE) == null){
		//	throw new ParserErrorException("Exception expected EOF");
		//}
		lineCounter++;
		return new AssignmentNode ((VariableReferenceNode) tempTarget, value);
	}
	
	public FunctionCallNode parseFunctionCall() throws ParserErrorException, SyntaxErrorException, NodeErrorException {
		FunctionCallNode parseFunctionCallNode = new FunctionCallNode();
		ArrayList<ParameterNode> parseParameterNodelist = new ArrayList<ParameterNode>();
		String functionCallName;
		
		FunctionNode calledFunction;
		int paramCounter = 0;
		
		if(program.containsKey(peek().getValue())){
			functionCallName = matchAndRemove(Token.tokenType.IDENTIFIER).getValue();
			calledFunction = program.getFunction(functionCallName);
		}
		
		enum functionCallState{
			start,
			parameter,
			var,
			end
		}
		
		functionCallState state = functionCallState.start;
		
		while (state != functionCallState.end){
			switch (state){
				case start -> {
					if (matchAndRemove(Token.tokenType.ENDOFLINE) != null){
						lineCounter++;
						state = functionCallState.end;
					} else if (matchAndRemove(Token.tokenType.VAR) != null){
						state = functionCallState.var;
					} else if (matchAndRemove(Token.tokenType.COMMA) != null){
						state = functionCallState.start;
					} else {
						state = functionCallState.parameter;
					}
				}
				case parameter -> {
					try {
						parseParameterNodelist.add(new ParameterNode(boolCompare()));
					} catch (Exception e) {
						throw new SyntaxErrorException("Expected parameter for function. | At line: " + lineCounter);
					}
					state =functionCallState.start;
				}
				case var -> {
					try {
						parseParameterNodelist.add(new ParameterNode(variableReference(), true));
					} catch (Exception e) {
						throw new SyntaxErrorException("Expected parameter for function. | At line: " + lineCounter);
					}
					state =functionCallState.start;
				}
			}
		}
		
		parseFunctionCallNode.setStatementList(parseParameterNodelist);
		return parseFunctionCallNode;
		
	}
	
	public ForNode parseFor() throws SyntaxErrorException, ParserErrorException, NodeErrorException {
		
		ForNode parseForNode = new ForNode();
		if(matchAndRemove(Token.tokenType.FOR) == null){
			return null;
		}
		
		if (peekType() == Token.tokenType.IDENTIFIER){
			parseForNode.setVariable(new VariableReferenceNode(matchAndRemove(Token.tokenType.IDENTIFIER).getValue()));
		} else {
			throw new SyntaxErrorException("No iterator in for loop." + lineCounter);
		}
		
		if (matchAndRemove(Token.tokenType.FROM) == null){
			throw new SyntaxErrorException("No from token in for loop." + lineCounter);
		}
		parseForNode.setFrom(expression());
		
		if (matchAndRemove(Token.tokenType.TO) == null){
			throw new SyntaxErrorException("No to token in for loop." + lineCounter);
		}
		parseForNode.setTo(expression());
		
		expectEndsOfLine();
		
		parseForNode.setStatementList(statements());
		
		return parseForNode;
	}
	
	public IfNode parseIf() throws SyntaxErrorException, ParserErrorException, NodeErrorException {
		
		IfNode parseIfNode = new IfNode();
		
		boolean ifElse = false;
		
		if(peekType() != Token.tokenType.IF && peekType() != Token.tokenType.ELSEIF && peekType() != Token.tokenType.ELSE){
			return null;
		}
		
		if ((matchAndRemove(Token.tokenType.IF) != null || matchAndRemove(Token.tokenType.ELSEIF) != null)){
		
		} else if (matchAndRemove(Token.tokenType.ELSE) != null) {
			ifElse = true;
		} else {
			throw new SyntaxErrorException("Expected if, elseif, else, token" + lineCounter);
		}
		
		if(ifElse){
			
			expectEndsOfLine();
			parseIfNode.setNextIf(null);
			parseIfNode.setStatementList(statements());
			
		} else {
			
			parseIfNode.setCondition((BooleanCompareNode) boolCompare());
			if(matchAndRemove(Token.tokenType.THEN) == null){
				throw new SyntaxErrorException("Expected {then} token" + lineCounter);
			}
			expectEndsOfLine();
			parseIfNode.setStatementList(statements());
			
			expectEndsOfLine();
			parseIfNode.setNextIf(parseIf());
			
		}
		
		return parseIfNode;
		
	}
	
	public RepeatNode parseRepeat() throws SyntaxErrorException, ParserErrorException, NodeErrorException {
		
		RepeatNode parseRepeatNode = new RepeatNode();
		
		if(matchAndRemove(Token.tokenType.REPEAT) == null){
			return null;
		}
		if(matchAndRemove(Token.tokenType.UNTIL) == null){
			throw new SyntaxErrorException("[Expected 'until' token.]");
		}
		
		parseRepeatNode.setCondition((BooleanCompareNode) boolCompare());
		expectEndsOfLine();
		
		parseRepeatNode.setStatementList(statements());
		return parseRepeatNode;
		
	}
	
	public WhileNode parseWhile() throws ParserErrorException, SyntaxErrorException, NodeErrorException {
		WhileNode pasreWhileNode = new WhileNode();
		
		if(matchAndRemove(Token.tokenType.WHILE) == null){
			return null;
		}
		
		try {
			pasreWhileNode.setCondition((BooleanCompareNode) boolCompare());
		} catch(Exception e) {
		
		}
		expectEndsOfLine();
		pasreWhileNode.setStatementList(statements());
		return pasreWhileNode;
	}
	
	/**
	 * 1 - Uses expression() to handle expressions and variable references and stores as left Node
	 * 2 - Checks for Comparator
	 * 		If none are found returns left
	 * 3 - Uses expression() to handle expressions and variable references for right Side
	 * 4- returns BoolCompNode
	 * @return Nodes of type BoolCompareNode, MathOpNode, VariableReferenceNode, or any basic Node types.
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 */
	public Node boolCompare() throws ParserErrorException, SyntaxErrorException {
		Node left = expression();
		if(tokenList.isEmpty()){
			return left;
		}
		BooleanCompareNode.boolType actualComperatorType;
		Token.tokenType comperatorType = peekType();
		switch (comperatorType){
			case ASSIGNMENT_EQUALS -> {
				actualComperatorType = BooleanCompareNode.boolType.COMPARATOR_EQUAL;
			}
			case COMPARATOR_NOT_EQUAL -> {
				actualComperatorType = BooleanCompareNode.boolType.COMPARATOR_NOT_EQUAL;
			}
			case COMPARATOR_LESS -> {
				actualComperatorType = BooleanCompareNode.boolType.COMPARATOR_LESS;
			}
			case COMPARATOR_GREATER -> {
				actualComperatorType = BooleanCompareNode.boolType.COMPARATOR_GREATER;
			}
			case COMPARATOR_LESS_OR_EQUALS -> {
				actualComperatorType = BooleanCompareNode.boolType.COMPARATOR_LESS_OR_EQUALS;
			}
			case COMPARATOR_GREATER_OR_EQUALS -> {
				actualComperatorType = BooleanCompareNode.boolType.COMPARATOR_GREATER_OR_EQUALS;
			}
			default ->{
				return left;
			}
		}
		tokenList.pop(); // Consume the operator token
		Node right = expression();
		return new BooleanCompareNode(left, right, actualComperatorType);
	}
	
	/**
	 * Handles variable references
	 * @return returns a variable reference Node
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 */
	private VariableReferenceNode variableReference() throws ParserErrorException, SyntaxErrorException {
		VariableReferenceNode temporaryVarRef;
		Token tempToken;
		if((tempToken=matchAndRemove(Token.tokenType.IDENTIFIER)) == null){
			return null;
		}
		Node index = null;
		if(matchAndRemove(Token.tokenType.INDEX_START) != null){
			index = expression();
			if (matchAndRemove(Token.tokenType.INDEX_END) == null){
				throw new SyntaxErrorException("\"]\" missing!");
			}
		}
		
		return new VariableReferenceNode(tempToken.getValue(), index);
	}
	
	/**
	 *
	 * @return Node type
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 */
	public Node expression() throws ParserErrorException, SyntaxErrorException {
		Node termOne = term();
		
		if (tokenList.size() == 0){
			return termOne;
		}
		
		MathOpNode.operations operator;
		
		
		if(matchAndRemove(Token.tokenType.PLUS) != null){
			operator = MathOpNode.operations.ADD;
		} else if (matchAndRemove(Token.tokenType.MINUS)!= null){
			operator = MathOpNode.operations.SUBTRACT;
		} else {
			return termOne;
		}
		
		Node termTwo = term();
		
		if (termTwo == null){
			throw new ParserErrorException("No right operand." + tokenList.get(0).toString());
		}
		
		while (matchAndRemove(Token.tokenType.ENDOFLINE) == null) {
			
			termOne = new MathOpNode(operator,termOne, termTwo);
			
			if(peek().getType() == Token.tokenType.PLUS){
				tokenList.pop();
				operator = MathOpNode.operations.ADD;
			} else if (peek().getType() == Token.tokenType.MINUS){
				tokenList.pop();
				operator = MathOpNode.operations.SUBTRACT;
			} else {
				return termOne;
			}
			
			termTwo = term();
			
			if (termTwo == null){
				throw new ParserErrorException("No right operand.");
			}
			
		}
		lineCounter++;
		return new MathOpNode(operator, termOne, termTwo);
	}
	
	/**
	 *
	 * @return
	 * @throws ParserErrorException
	 * @throws SyntaxErrorException
	 */
	public Node term() throws ParserErrorException, SyntaxErrorException {
		
		Node factorOne = factor();
		MathOpNode.operations operator;
		
		if(peek().getType() == Token.tokenType.DIVIDE){
			tokenList.pop();
			operator = MathOpNode.operations.DIVISION;
		} else if (peek().getType() == Token.tokenType.MULTPILY){
			tokenList.pop();
			operator = MathOpNode.operations.MULTIPLICATION;
		} else if (peek().getType() == Token.tokenType.MOD) {
			tokenList.pop();
			operator = MathOpNode.operations.MOD;
		} else {
			return factorOne;
		}
		
		Node factorTwo = factor();
		
		if (factorTwo == null){
			throw new ParserErrorException("No right operand.");
		}
		
		while (matchAndRemove(Token.tokenType.ENDOFLINE) == null) {
			
			factorOne = new MathOpNode(operator, factorOne, factorTwo);
			
			if(peek().getType() == Token.tokenType.DIVIDE){
				tokenList.pop();
				operator = MathOpNode.operations.DIVISION;
			} else if (peek().getType() == Token.tokenType.MULTPILY){
				tokenList.pop();
				operator = MathOpNode.operations.MULTIPLICATION;
			} else if (peek().getType() == Token.tokenType.MOD) {
				tokenList.pop();
				operator = MathOpNode.operations.MOD;
			} else {
				return factorOne;
			}
			
			factorTwo = factor();
			
			if (factorTwo == null){
				throw new ParserErrorException("No right operand.");
			}
		}
		lineCounter++;
		return new MathOpNode(operator, factorOne, factorTwo);
		
	}
	
	/**
	 *
	 * @return Node type
	 * @throws ParserErrorException if there is an error parsing the function
	 * @throws SyntaxErrorException if there is a syntax error in the function definition
	 */
	public Node factor() throws ParserErrorException, SyntaxErrorException {
		int negator = 1;
		if(matchAndRemove(Token.tokenType.MINUS) != null) {
			negator = -1;
		}
		if (matchAndRemove(Token.tokenType.PARAMETERS_START) != null) {
			
			Node paramMathNode = expression();
			
			if(matchAndRemove(Token.tokenType.PARAMETERS_END) != null){
				return paramMathNode;
			} else {
				throw new ParserErrorException("No parameter end found!");
			}

		} else if (peek().getType() == Token.tokenType.INTEGER) {
			return new IntegerNode(Integer.parseInt(matchAndRemove(Token.tokenType.INTEGER).getValue())*negator);
			
		} else if (peek().getType() == Token.tokenType.REAL) {
			return new RealNode(Float.parseFloat(matchAndRemove(Token.tokenType.REAL).getValue())*negator);
			
		} else if(peek().getType() == Token.tokenType.TRUE) {
			matchAndRemove(Token.tokenType.TRUE);
			return new BoolNode(true);

		} else if(peek().getType() == Token.tokenType.FALSE) {
			matchAndRemove(Token.tokenType.FALSE);
			return new BoolNode(false);
		
		} else if(peek().getType() == Token.tokenType.STRINGLITERAL) {
			return new StringNode(matchAndRemove(Token.tokenType.STRINGLITERAL).getValue());
			
		} else if(peek().getType() == Token.tokenType.CHARACTERLITERAL) {
			return new CharNode(matchAndRemove(Token.tokenType.CHARACTERLITERAL).getValue().charAt(0));
			
		} else if (peek().getType() == Token.tokenType.IDENTIFIER){
			VariableReferenceNode temporaryVarRef;
			Token tempToken = tokenList.pop();
			Node index = null;
			if(matchAndRemove(Token.tokenType.INDEX_START) != null){
				index = expression();
				if (matchAndRemove(Token.tokenType.INDEX_END) == null){
					throw new SyntaxErrorException("\"]\" missing!");
				}
			}
			
			switch (negator){
				case 1 ->{
					temporaryVarRef = new VariableReferenceNode(tempToken.getValue(), index);
					return temporaryVarRef;
				}
				case -1 -> {
					return new MathOpNode(MathOpNode.operations.MULTIPLICATION, new IntegerNode(-1),
							new VariableReferenceNode(tempToken.getValue(), index)
					);
				}
			}
			
		}
		
		return null;
	}
	
	private Token matchAndRemove(Token.tokenType tokenType){
		if (tokenType == peek().getType()){
			return tokenList.pop();
		} else {
			return null;
		}
	}
	
	private void expectEndsOfLine() throws SyntaxErrorException {
		while (peek().getType() == Token.tokenType.ENDOFLINE){
			matchAndRemove(Token.tokenType.ENDOFLINE);
			lineCounter++;
			if(tokenList.size() == 0){
				break;
			}
			
		}
	}
	
	private void expectDedents() throws SyntaxErrorException {
		while (peek().getType() == Token.tokenType.DEDENT){
			matchAndRemove(Token.tokenType.DEDENT);
		}
	}
	
	private Token peek(){
		return tokenList.get(0);
	}
	
	private Token.tokenType peekType(){
		return tokenList.get(0).getType();
	}
	
	@Override
	public String toString() {
		return "Parser{" +
				//"\n\tlineCounter = " + lineCounter +
				//"\n\ttokenList = " + tokenList +
				//"\n\tprogram = +"
				 program +
				"\n}";
	}
	
	public void setTokenList(TokenList tokenList) {
		this.tokenList = tokenList;
	}
	
	public ProgramNode getProgram() {
		return program;
	}
}
