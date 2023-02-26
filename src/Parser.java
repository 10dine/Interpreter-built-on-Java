import Nodes.*;

public class Parser {
	
	private TokenList tokenList;
	
	public Parser(TokenList tokenList){
		this.tokenList = tokenList;
	}
	
	public Node parse(){
		return Expression();
	}
	
	public expression()
	
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
