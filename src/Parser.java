import Nodes.*;

public class Parser {
	
	private TokenList tokenList;
	
	public Parser(TokenList tokenList){
		this.tokenList = tokenList;
	}
	
	public Node parse(){
	
	}
	
	
	private Token matchAndRemove(Token.tokenType tokenType){
		if (tokenType == peek(0).getType()){
			return tokenList.pop();
		} else {
			return null;
		}
	}
	
	private void expectEndsOfLine() throws SyntaxErrorException {
		if(matchAndRemove(Token.tokenType.ENDOFLINE).getType() == Token.tokenType.ENDOFLINE){
		
		} else {
			throw new SyntaxErrorException("No endOfLine token found!");
		}
	}
	
	private Token peek(int index){
		return tokenList.get(index);
	}
}
