import Nodes.*;

public class Parser {
	
	private TokenList tokenList;
	
	public Parser(TokenList tokenList){
		this.tokenList = tokenList;
	}
	
	public Node parse(){
		return expression();
	}
	
	public Node expression(){
		Node termOne = term();
		if(peek(0).getType() == Token.tokenType.PLUS){
			Node termTwo = term();
			return new MathOpNode(MathOpNode.operations.ADD, termOne, termTwo);
		} else if (peek(0).getType() == Token.tokenType.MINUS){
			Node termTwo = term();
			return new MathOpNode(MathOpNode.operations.SUBTRACT, termOne, termTwo);
		} else {
			return termOne;
		}
		
	}
	
	public Node term(){
		Node factorOne = factor();
		if(peek(0).getType() == Token.tokenType.DIVIDE){
			matchAndRemove(Token.tokenType.DIVIDE);
			Node factorTwo = factor();
			return new MathOpNode(MathOpNode.operations.MULTIPLICATION, factorOne, factorTwo);
		} else if (peek(0).getType() == Token.tokenType.MULTPILY){
			matchAndRemove(Token.tokenType.MULTPILY);
			Node factorTwo = factor();
			return new MathOpNode(MathOpNode.operations.MULTIPLICATION, factorOne, factorTwo);
		} else {
			return factorOne;
		}
		
		
	}
	
	public Node factor() {
		
		enum mode {
			negetive,
			regular
		}
		mode current = mode.regular;
		
		if (peek(0).getType() == Token.tokenType.MINUS) {
			matchAndRemove(Token.tokenType.MINUS);
			current = mode.negetive;
		} else if (peek(0).getType() == Token.tokenType.INTEGER) {
			return new IntegerNode(Integer.getInteger(matchAndRemove(Token.tokenType.INTEGER).getValue()));
		}
		
		if (current == mode.negetive) {
			if (peek(0).getType() == Token.tokenType.INTEGER) {
				return new IntegerNode(Integer.getInteger(matchAndRemove(Token.tokenType.INTEGER).getValue())*-1);
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
		}
	}
	
	private Token peek(int index){
		return tokenList.get(index);
	}
}
