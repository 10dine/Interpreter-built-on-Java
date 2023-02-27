import Nodes.*;

public class Parser {
	
	private TokenList tokenList;
	
	public Parser(TokenList tokenList){
		this.tokenList = tokenList;
	}
	
	public Node parse(){
		expression();
	}
	
	public Node expression(){
		term();
	}
	
	public Node term(){
		Node factorOne = factor();
		
		if(peek(0).getType() == Token.tokenType.DIVIDE){
			Node factorTwo = factor();
		} else if (peek(0).getType() == Token.tokenType.MULTPILY){
			Node factorTwo = factor();
		}
	}
	
	public Node factor() {
		
		enum mode {
			negetive,
			regular
		}
		mode current = mode.regular;
		
		if (peek(0).getType() == Token.tokenType.MINUS) {
			current = mode.negetive;
		} else if (peek(0).getType() == Token.tokenType.INTEGER) {
			return new IntegerNode(Integer.getInteger(matchAndRemove(Token.tokenType.INTEGER).getValue()));
		}
		
		if (current == mode.negetive) {
			if (peek(1).getType() == Token.tokenType.INTEGER) {
				return new IntegerNode(Integer.getInteger(matchAndRemove(Token.tokenType.INTEGER).getValue())*-1);
				
			}
		}
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
