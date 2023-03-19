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
	 * Constructor for Parser. Starts parse() on Parser.program on initialization;
	 * @param tokens tokenList Object which is essentially an arraylist of tokens
	 * @throws Exception
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
	 * @return
	 * @throws ParserErrorException
	 * @throws NodeErrorException
	 * @throws SyntaxErrorException
	 */
	private ProgramNode parse() throws ParserErrorException, NodeErrorException, SyntaxErrorException {
		ProgramNode parseProgram = new ProgramNode();
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
		ArrayList<VariableNode> variableNodes = new ArrayList<VariableNode>();
		ArrayList<StatementNode> statementsList;
		
		//Stage 1 starts here
		if (matchAndRemove(Token.tokenType.DEFINE) != null) {
			if (peekType(0) == Token.tokenType.IDENTIFIER) {
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
			typeAndSet
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
					} else if (peekType(0) == Token.tokenType.INDENT){
						parsingVariables = state.end;
					}
					break;
					
				case constant:
					String constantIdentiffier;
					if (peekType(0) != Token.tokenType.IDENTIFIER){
						throw new SyntaxErrorException(String.format("Expected identifier token. Got: %s", tokenList.get(0)));
					}
					constantIdentiffier = matchAndRemove(Token.tokenType.IDENTIFIER).getValue();
					if(matchAndRemove(Token.tokenType.ASSIGNMENT_EQUALS) == null){
						throw new SyntaxErrorException(String.format("Expected '=' token. Got: %s", tokenList.get(0)));
					}
					Node tempConstantNode;
					switch (peekType(0)){
						case INTEGER -> tempConstantNode = new IntegerNode(Integer.valueOf(tokenList.pop().getValue()));
						case REAL -> tempConstantNode = new RealNode(Float.valueOf(tokenList.pop().getValue()));
						case STRINGLITERAL -> tempConstantNode = new StringNode(tokenList.pop().getValue());
						case CHARACTERLITERAL -> tempConstantNode = new CharNode(tokenList.pop().getValue().charAt(0));
						case BOOLEAN -> tempConstantNode = new BoolNode(Boolean.valueOf(tokenList.pop().getValue()));
						case ARRAY -> tempConstantNode = new StringNode();
						default -> throw new SyntaxErrorException("Invalid type!");
					}
					variableNodes.add(new VariableNode(constantIdentiffier, tempConstantNode, constantState));
					
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
								if (peekType(0) == Token.tokenType.IDENTIFIER) {
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
								switch (peekType(0)){
									case INTEGERTYPE -> tempType = VariableNode.type.integar;
									case REALTYPE -> tempType = VariableNode.type.real;
									case STRINGTYPE -> tempType = VariableNode.type.string;
									case CHARACTERTYPE -> tempType = VariableNode.type.character;
									case BOOLEANTYPE -> tempType = VariableNode.type.bool;
									case ARRAYTYPE -> tempType = VariableNode.type.array;
									default -> throw new SyntaxErrorException("Invalid type in function params! ");
								}
								tokenList.pop();
								for(Token token: variableNameList){
									variableNodes.add(new VariableNode(token.getValue(), tempType, constantState));
								}
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
	 * 2. if state.start check for "(" and switchs to state.identifier
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
	public ArrayList<VariableNode> parameterDeclarations() throws SyntaxErrorException {
		
		ArrayList<VariableNode> variableList = new ArrayList<VariableNode>();
		TokenList variableNameList = new TokenList();
		boolean constantState = true;
		
		enum parameterDeclarationState {
			start,
			identifier,
			typeAndSet,
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
						} else if (peekType(0) == Token.tokenType.IDENTIFIER) {
							variableNameList.add(tokenList.pop());
						} else if (matchAndRemove(Token.tokenType.ASSIGNMENT_COLON) != null){
							paramDeclarationState = parameterDeclarationState.typeAndSet;
						} else if(matchAndRemove(Token.tokenType.COMMA) == null) {
							throw new SyntaxErrorException(String.format("Unexpected Token in parameter declaration. Token: %s", tokenList.pop()));
						}
						break;
						
					case typeAndSet:
						switch (peekType(0)){
							case INTEGERTYPE -> tempType = VariableNode.type.integar;
							case REALTYPE -> tempType = VariableNode.type.real;
							case STRINGTYPE -> tempType = VariableNode.type.string;
							case CHARACTERTYPE -> tempType = VariableNode.type.character;
							case BOOLEANTYPE -> tempType = VariableNode.type.bool;
							case ARRAYTYPE -> tempType = VariableNode.type.array;
							default -> throw new SyntaxErrorException("Invalid type in function params! ");
						}
						tokenList.pop();
						for(Token token: variableNameList){
							variableList.add(new VariableNode(variableNameList.pop().getValue(), tempType, constantState));
						}
						constantState = true;
						if (peekType(0) == Token.tokenType.SEMI_COLON){
							tokenList.pop();
							paramDeclarationState = parameterDeclarationState.identifier;
						} else if (peekType(0) == Token.tokenType.PARAMETERS_END){
							matchAndRemove(Token.tokenType.PARAMETERS_END);
							paramDeclarationState = parameterDeclarationState.end;
						} else {
							throw new SyntaxErrorException(String.format("Unexpected Token in parameter declaration. Token: %s", tokenList.pop()));
						}
						break;
				}
			
		}
		
		return variableList;
	}
	
	/**
	 * Expects indent and then loops till dedent while calling statement()
	 * @return ArrayList<StatementNode>
	 * @throws SyntaxErrorException
	 * @throws ParserErrorException
	 */
	public ArrayList<StatementNode> statements() throws SyntaxErrorException, ParserErrorException {
		ArrayList<StatementNode> statementsList = new ArrayList<StatementNode>();
		if(matchAndRemove(Token.tokenType.INDENT) == null){
			throw new SyntaxErrorException("Expected indent!");
		}
		while(matchAndRemove(Token.tokenType.DEDENT) == null){
			statementsList.add(statement());
			if(tokenList.size()==0){
				tokenList.add(new Token(Token.tokenType.DEDENT, ""));
			}
		}
		return statementsList;
	}
	
	/**
	 * Currently calls assignment()
	 * @return Currently only AssignmentNodes
	 * @throws ParserErrorException
	 * @throws SyntaxErrorException
	 */
	public StatementNode statement() throws ParserErrorException, SyntaxErrorException {
		return assignment();
	}
	
	/**
	 *  1 - uses variableReference()
	 *  2 - Looks for assignment token
	 *  3 - Gets right side expression of assignment
	 * @return AssignmentNode
	 * @throws SyntaxErrorException
	 * @throws ParserErrorException
	 */
	public AssignmentNode assignment() throws SyntaxErrorException, ParserErrorException {
		
		Node tempTarget = variableReference();
		
		if(matchAndRemove(Token.tokenType.ASSIGNMENT_COLON_EQUALS) == null){
			throw new ParserErrorException("Exception expected :=");
		}
		
		Node value = boolCompare();
		
		if (matchAndRemove(Token.tokenType.ENDOFLINE) == null){
			throw new ParserErrorException("Exception expected EOF");
		}
		lineCounter++;
		return  new AssignmentNode ((VariableReferenceNode) tempTarget, value);
	}
	
	/**
	 *
	 * @return
	 * @throws ParserErrorException
	 * @throws SyntaxErrorException
	 */
	public Node boolCompare() throws ParserErrorException, SyntaxErrorException {
		Node left = expression();
		BooleanCompareNode.boolType actualComperatorType;
		Token.tokenType comperatorType = peekType(0);
		switch (comperatorType){
			case COMPARATOR_EQUALS -> {
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
	
	private Node variableReference() throws ParserErrorException, SyntaxErrorException {
		VariableReferenceNode temporaryVarRef;
		Token tempToken;
		if((tempToken=matchAndRemove(Token.tokenType.IDENTIFIER)) == null){
			throw new ParserErrorException("Can't parse assignment1 (varRef())");
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
	 * @return
	 * @throws ParserErrorException
	 * @throws SyntaxErrorException
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
			
			if(peek(0).getType() == Token.tokenType.PLUS){
				tokenList.pop();
				operator = MathOpNode.operations.ADD;
			} else if (peek(0).getType() == Token.tokenType.MINUS){
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
		
		return termOne;
		
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
		
		if(peek(0).getType() == Token.tokenType.DIVIDE){
			tokenList.pop();
			operator = MathOpNode.operations.DIVISION;
		} else if (peek(0).getType() == Token.tokenType.MULTPILY){
			tokenList.pop();
			operator = MathOpNode.operations.MULTIPLICATION;
		} else if (peek(0).getType() == Token.tokenType.MOD) {
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
			
			if(peek(0).getType() == Token.tokenType.DIVIDE){
				tokenList.pop();
				operator = MathOpNode.operations.DIVISION;
			} else if (peek(0).getType() == Token.tokenType.MULTPILY){
				tokenList.pop();
				operator = MathOpNode.operations.MULTIPLICATION;
			} else if (peek(0).getType() == Token.tokenType.MOD) {
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
	 * @return
	 * @throws ParserErrorException
	 * @throws SyntaxErrorException
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

		} else if (peek(0).getType() == Token.tokenType.INTEGER) {
			return new IntegerNode(Integer.valueOf(matchAndRemove(Token.tokenType.INTEGER).getValue())*negator);
			
		} else if (peek(0).getType() == Token.tokenType.REAL) {
			return new RealNode(Float.valueOf(matchAndRemove(Token.tokenType.REAL).getValue())*negator);
			
		} else if (peek(0).getType() == Token.tokenType.IDENTIFIER){
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
		if (tokenType == peek(0).getType()){
			return tokenList.pop();
		} else {
			return null;
		}
	}
	
	private void expectEndsOfLine() throws SyntaxErrorException {
		while (peek(0).getType() == Token.tokenType.ENDOFLINE){
			matchAndRemove(Token.tokenType.ENDOFLINE);
			lineCounter++;
		}
	}
	
	private Token peek(int index){
		return tokenList.get(index);
	}
	
	private Token.tokenType peekType(int index){
		return tokenList.get(index).getType();
	}
	
	@Override
	public String toString() {
		return "Parser{" +
				"\n\tlineCounter = " + lineCounter +
				"\n\ttokenList = " + tokenList +
				"\n\tprogram = " + program +
				"\n}";
	}
	
	void setTokenList(TokenList tokenList) throws ParserErrorException, NodeErrorException, SyntaxErrorException {
		this.tokenList = tokenList;
	}
	
	
	public Node createNode() {
		return switch (peek(0).getType()) {
			case INTEGER -> new IntegerNode(Integer.valueOf(matchAndRemove(Token.tokenType.INTEGER).getValue()));
			case REAL -> new RealNode(Float.valueOf(matchAndRemove(Token.tokenType.REAL).getValue()));
			default -> throw new IllegalStateException("Unexpected value: " + peek(0).getType());
		};
	}
	
//
}
