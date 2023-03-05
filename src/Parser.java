import Nodes.*;

import java.util.HashMap;

public class Parser {
	
	private TokenList tokenList;
	
	private HashMap<String, FunctionNode> functionNodeHashMap;
	
	Node root;
	
	public Parser() throws ParserErrorException {
	}
	
	public Node parse() throws ParserErrorException {
		return expression();
	}
	
	public Node expression() throws ParserErrorException {
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
		
		return termOne;
		
	}
	
	public Node term() throws ParserErrorException {
		
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
		return new MathOpNode(operator, factorOne, factorTwo);
		
	}
	
	public Node factor() throws ParserErrorException {
		
		if (peek(0).getType() == Token.tokenType.PARAMETERS_START) {
			
			matchAndRemove(Token.tokenType.PARAMETERS_START);
			Node paramMathNode = expression();
			
			if(matchAndRemove(Token.tokenType.PARAMETERS_END) != null){
				return paramMathNode;
			} else {
				throw new ParserErrorException("No parameter end found!");
			}

		} else if (peek(0).getType() == Token.tokenType.INTEGER) {
			return new IntegerNode(Integer.valueOf(matchAndRemove(Token.tokenType.INTEGER).getValue()));
			
		} else if (peek(0).getType() == Token.tokenType.REAL) {
			return new RealNode(Float.valueOf(matchAndRemove(Token.tokenType.REAL).getValue()));
			
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
		}
	}
	
	private Token peek(int index){
		return tokenList.get(index);
	}
	
	private Token.tokenType peekType(int index){
		return tokenList.get(index).getType();
	}
	
	void setTokenList(TokenList tokenList) throws ParserErrorException {
		this.tokenList = tokenList;
		root = parse();
	}
	
	public Node getRoot() {
		return root;
	}
	
	public Node createNumberNode() {
		if (peek(0).getType() == Token.tokenType.INTEGER) {
			return new IntegerNode(Integer.valueOf(matchAndRemove(Token.tokenType.INTEGER).getValue()));
			
		} else if (peek(0).getType() == Token.tokenType.REAL) {
			return new RealNode(Float.valueOf(matchAndRemove(Token.tokenType.REAL).getValue()));
			
		}
		return null;
	}
}
