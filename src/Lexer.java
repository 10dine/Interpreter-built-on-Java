import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * @author ishty
 * Lexer object. Currently, it is an object that contains three variables. A java files Path object,
 * a List for the all the lines and an arraylist extended class object TokenList to caontain all the
 * tokens it produces.
 */
public class Lexer {
	
	//variables and object intergral to the lexer object
	private final Path thePath;
	private List<String> lines;
	private final TokenList tokens = new TokenList();
	
	//variables and objects required for the lex function
	private StringBuilder accumulator = new StringBuilder();                //renamed from tokenString to accumulator
	private int indentLevel = 0;                                            //for tracking indent
	private int lineCount = 0;
	private lexState charState = lexState.scanning;
	HashMap<String, Token.tokenType> knownWords = new HashMap<String, Token.tokenType>() {{  //for easier token assignment
		//put("", tokenType.);
		
		put("variables", Token.tokenType.VARAIABLES);
		put("constant", Token.tokenType.CONSTANT);
		
		put("string", Token.tokenType.STRINGLITERAL);
		put("character", Token.tokenType.CHARACTERLITERAL);
		put("integer", Token.tokenType.INTEGER);
		put("real", Token.tokenType.REAL);
		put("boolean", Token.tokenType.BOOLEAN);
		put("array", Token.tokenType.ARRAY);
		
		put("of", Token.tokenType.OF);
		put("from", Token.tokenType.FROM);
		put("to", Token.tokenType.TO);
		
		put(":=", Token.tokenType.ASSIGNMENT_COLON_EQUALS);
		put("=", Token.tokenType.ASSIGNMENT_EQUALS);
		put(":", Token.tokenType.ASSIGNMENT_COLON);
		
		//put("=", Token.tokenType.COMPARATOR_EQUALS);
		put("<>", Token.tokenType.COMPARATOR_NOT_EQUAL);
		put("<", Token.tokenType.COMPARATOR_LESS);
		put("<=", Token.tokenType.COMPARATOR_LESS_OR_EQUALS);
		put(">", Token.tokenType.COMPARATOR_GREATER);
		put(">=", Token.tokenType.COMPARATOR_GREATER_OR_EQUALS);
		
		put("define", Token.tokenType.DEFINE);
		put("write", Token.tokenType.WRITE);
		
		put("for", Token.tokenType.FOR);
		put("while", Token.tokenType.WHILE);
		put("repeat", Token.tokenType.REPEAT);
		put("until", Token.tokenType.UNTIL);
		
		put("+", Token.tokenType.PLUS);
		put("-", Token.tokenType.MINUS);
		put("*", Token.tokenType.MULTPILY);
		put("/", Token.tokenType.DIVIDE);
		put("mod", Token.tokenType.MOD);
		
		put("not", Token.tokenType.NOT);
		put("and", Token.tokenType.AND);
		put("or", Token.tokenType.OR);
		
		put("{", Token.tokenType.COMMENT);
		put("}", Token.tokenType.COMMENT);
		put("(", Token.tokenType.PARAMETERS_START);
		put(")", Token.tokenType.PARAMETERS_END);
		put("[", Token.tokenType.INDEX_START);
		put("]", Token.tokenType.INDEX_END);
		
		put(",", Token.tokenType.COMMA);
		
	}};
	//States for state machine
	//scanning is used for scanning the type of token being made currently.
	//word is the state at which the machines is making a WORD type token.
	//number is the state at which the machines is making a NUMBER type token.
	//floatNUmber is the state at which the machines is making a NUMBER type token with periods.
	enum lexState{
		scanning,
		identifierMode,
		
		tabMode,
		commentMode,
		
		word,
		floatNumber,
		number,
		stringLiteral,
		characterLiteral
		
	}
	// special boolean variable for looping a char element in the for loop until it is assigned to token type.
	boolean loopState;
	
	//Constructor of lexer object
	public Lexer(String path) {
		this.thePath = Paths.get(path + ".shank");
		stringArr();
		linesToToken();
	}
	
	/**
	 * Uses java.io.files.Readalllines to assign a List<String> into lines variable.
	 */
	private void stringArr(){
		try {
			this.lines = Files.readAllLines(thePath, StandardCharsets.UTF_8);
		} catch (java.io.IOException e) {
			System.out.println("Hey, sorry to inform you but the the file name gave me is a bit wonky. I will now terminate! Also here's the error message: \n" + e.getMessage() + "\n");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * function that's calls lex function on all lines
	 */
	private void linesToToken(){
		for (String str: lines) {
			lineCount++;
			lex(str);
		}
	}
	
	/**
	 * Main state machine
	 *
	 *
	 * @param line
	 */
	public void lex(String line) {
		
		if (charState != lexState.commentMode){
			charState = lexState.tabMode;
		}
		
		int indentLevelInLex = 0;
		
		for (char ch: line.toCharArray()) {
			//(1) starts here
			switch (charState) {
				
				case tabMode:
					if (isTabs(ch)) {
						indentLevelInLex++;
					} else {
						if (indentLevelInLex > indentLevel) {
							for(int i = indentLevelInLex-indentLevel; i > 0; i--){
								indentLevel++;
								tokens.add(tokens.size(),new Token(Token.tokenType.INDENT, ""));
							}
							charState = lexState.scanning;
							scanningOutsideOfLex(ch);
						} else if (indentLevelInLex < indentLevel) {
							for(int i = indentLevel-indentLevelInLex; i > 0; i--){
								indentLevel--;
								tokens.add(tokens.size(),new Token(Token.tokenType.DEDENT, ""));
							}
							charState = lexState.scanning;
							scanningOutsideOfLex(ch);
						} else {
							charState = lexState.scanning;
							scanningOutsideOfLex(ch);
						}
					}
					
					break;
				
				case scanning:
					if (isNumber(ch)) {
						accumulator.append(ch);
						charState = lexState.number;
						
					} else if (isLetter(ch)) {
						accumulator.append(ch);
						charState = lexState.word;
						
					} else if (isDoubleQuotes(ch)){
						charState = lexState.stringLiteral;
						
					} else if (isSingleQuote(ch)){
						charState = lexState.characterLiteral;
						
					} else if (isFloat(ch)) {
						accumulator.append(ch);
						charState = lexState.floatNumber;
						//Previous while loop condition
					} else if (isBrace(ch)) {
						charState = lexState.commentMode;
						
					} else if (knownWords.containsKey(""+ch)) {
						
						accumulator.append(ch);
						charState = lexState.identifierMode;
						
					} else {
						try {
							isIllegalChar(ch);
						} catch (SyntaxErrorException e) {
							System.out.println(e.getMessage() + " " + ch + ". " + "These are all the Token made so far: " + tokens.toString());System.exit(1);
						}
					}
					break;
				//(2) starts here
				case word:
					
					if (isNumber(ch) || isLetter(ch)) {
						accumulator.append(ch);
						//Previous while loop condition
					} else if (knownWords.containsKey(""+accumulator.toString()+ch)) {
						tokens.add(tokens.size(), new Token(knownWords.get(""+accumulator.toString()+ch), ""+accumulator.toString()+ch));
						accumulator.setLength(0);
						charState = lexState.scanning;
					} else if (knownWords.containsKey(""+accumulator.toString())) {
						tokens.add(tokens.size(), new Token(knownWords.get(""+accumulator.toString()), accumulator.toString()));
						accumulator.setLength(0);
						scanningOutsideOfLex(ch);
					} else {
						tokens.add(tokens.size(), new Token(Token.tokenType.IDENTIFIER, accumulator.toString()));
						accumulator.setLength(0);
						charState = lexState.scanning;
						scanningOutsideOfLex(ch);
						
					}
					
					break;//(3) starts here
				case number:
					
					if (isNumber(ch)){
						accumulator.append(ch);
						//Previous while loop condition
					} else if (isFloat(ch)) {
						accumulator.append(ch);
						charState = lexState.floatNumber;
						//Previous while loop condition
					} else {
						tokens.add(tokens.size(), new Token(Token.tokenType.INTEGER, accumulator.toString()));
						accumulator.setLength(0);
						
						charState = lexState.scanning;
						scanningOutsideOfLex(ch);
					}
					
					break;//(4) starts here
				case floatNumber:
					
					if (isNumber(ch)){
						accumulator.append(ch);
						//Previous while loop condition
					} else if (isLetter(ch)) {
						tokens.add(tokens.size(), new Token(Token.tokenType.REAL, accumulator.toString()));
						//System.out.println("Token added succesfully: " + tokens.toString());
						accumulator.setLength(0);
						scanningOutsideOfLex(ch);
					} else if (isFloat(ch)) {
						tokens.add(tokens.size(), new Token(Token.tokenType.REAL, accumulator.toString()));
						//System.out.println("Token added succesfully: " + tokens.toString());
						accumulator.setLength(0);
						scanningOutsideOfLex(ch);
					} else {
						
						tokens.add(tokens.size(), new Token(Token.tokenType.REAL, accumulator.toString()));
						accumulator.setLength(0);
						charState = lexState.scanning;
						identifierOutsideOfLex(ch);
					}
					break;
					
				case identifierMode:
					if (knownWords.containsKey(""+accumulator.toString()+ch)){
						tokens.add(tokens.size(), new Token(knownWords.get(""+accumulator.toString()+ch), ""+accumulator.toString()+ch));
						accumulator.setLength(0);
						charState = lexState.scanning;
					} else  if (knownWords.containsKey(""+accumulator.toString())){
						tokens.add(tokens.size(), new Token(knownWords.get(""+accumulator.toString()), accumulator.toString()));
						accumulator.setLength(0);
						
						charState = lexState.scanning;
						scanningOutsideOfLex(ch);
					} else {
						scanningOutsideOfLex(ch);
					}
					break;
				
				case stringLiteral:
					if (isDoubleQuotes(ch)) {
						tokens.add(tokens.size(), new Token(Token.tokenType.STRINGLITERAL, accumulator.toString()));
						accumulator.setLength(0);
					} else {
						accumulator.append(ch);
					}
					break;
					
				case characterLiteral:
					if (isSingleQuote(ch)) {
						tokens.add(tokens.size(), new Token(Token.tokenType.CHARACTERLITERAL, accumulator.toString()));
						accumulator.setLength(0);
					} else {
						accumulator.append(ch);
					}
					break;
					
				case commentMode:
					if (ch == '}') {
						tokens.add(tokens.size(), new Token((Token.tokenType.COMMENT), accumulator.toString()));
						accumulator.setLength(0);
						charState = lexState.tabMode;
					} else {
						accumulator.append(ch);
					}
					break;
			}
		}
		
		//This switch statement is used to handle a bug where if the line ends while a token is being made and for the
		//end of the line token.
		switch (charState){
			case number:
				tokens.add(new Token(Token.tokenType.INTEGER,accumulator.toString()));
				accumulator.setLength(0);
				
				tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
				
				break;
			
			case floatNumber:
				tokens.add(new Token(Token.tokenType.REAL,accumulator.toString()));
				accumulator.setLength(0);
				
				tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
				break;
			
			case word:
				tokens.add(new Token(Token.tokenType.IDENTIFIER,accumulator.toString()));
				accumulator.setLength(0);
				
				tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
				break;
			
				
			case identifierMode:
				identifierOutsideOfLex(accumulator.charAt(accumulator.length()-1));
				accumulator.setLength(0);
				tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
				break;
				
			default:
				tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
				break;
		}
		//System.out.println("Token added succesfully: " + tokens.toString());
	}
	
	private void scanningOutsideOfLex(char ch){
		
		if (isNumber(ch)) {
			accumulator.append(ch);
			charState = lexState.number;
			
		} else if (isLetter(ch)) {
			accumulator.append(ch);
			charState = lexState.word;
			
		} else if (isDoubleQuotes(ch)){
			charState = lexState.stringLiteral;
			
		} else if (isSingleQuote(ch)){
			charState = lexState.characterLiteral;
			
		} else if (isFloat(ch)) {
			accumulator.append(ch);
			charState = lexState.floatNumber;
			//Previous while loop condition
		} else if (isBrace(ch)) {
			charState = lexState.commentMode;
		} else if (knownWords.containsKey(""+ch)) {
			accumulator.append(ch);
			charState = lexState.identifierMode;
		} else {
			try {
				isIllegalChar(ch);
			} catch (SyntaxErrorException e) {
				System.out.println(e.getMessage() + " " + ch + ". " + "These are all the Token made so far: " + tokens.toString());System.exit(1);
			}
		}
	}
	private void identifierOutsideOfLex(char ch){
		
		if (knownWords.containsKey(""+accumulator.toString()+ch)){
			tokens.add(tokens.size(), new Token(knownWords.get(""+accumulator.toString()+ch), ""+accumulator.toString()+ch));
			accumulator.setLength(0);
			charState = lexState.scanning;
		} else  if (knownWords.containsKey(""+accumulator.toString())){
			tokens.add(tokens.size(), new Token(knownWords.get(""+accumulator.toString()), accumulator.toString()));
			accumulator.setLength(0);
			charState = lexState.scanning;
			scanningOutsideOfLex(ch);
		} else {
			scanningOutsideOfLex(ch);
		}
	}

	
	/**
	 * Sees if input is a char of 0-9
	 * @param input char type
	 * @return boolean
	 */
	private boolean isNumber(char input) {
		return ((input >= 48) && (input <= 57));
	}
	
	/**
	 * Returns true if char is A-Z,a-z, and 0-9
	 * @param input char type
	 * @return boolean
	 */
	private boolean isLetter(char input) {
		if  ((input >= 65)&&(input <= 90))
			return true;
		else return (input >= 97) && (input <= 122);
	}
	
	/**
	 * Returns id the parameter is the character period or not.
	 * @param input char type
	 * @return boolean
	 */
	private boolean isFloat(char input) {
		return input == 46;
	}
	
	private boolean isTabs(char input) {return input == '\t'; }
	
	private boolean isDoubleQuotes(char input) {return input =='"';}
	
	private boolean isSingleQuote(char input) {return input == 39;}
	
	private boolean isColon(char input) {return input == 58; }
	
	private boolean isLess(char input) {return input == 60; }
	
	private boolean isGreater(char input) {return input == 62; }
	
	private boolean isEquals(char input) {return input == 61; }
	
	private boolean isBrace(char input) {return input == '{'; }
	/**
	 * Throws IllegalCharacterException if the parameter input is a space(32) character.
	 * @param input Char value
	 * @return true or IllegalCharacterException
	 * @throws SyntaxErrorException
	 */
	private boolean isIllegalChar(char input) throws SyntaxErrorException {
		if (input != 32)
			throw new SyntaxErrorException("Illegal characters have been found. The character:");
		else
		
		return true;
	}
	
	private void isIllegalChar() throws SyntaxErrorException {
		throw new SyntaxErrorException("Illegal characters have been found. The character:");
	}
	
	/**
	 * @return String of Tokens in List of Tokens.
	 */
	@Override
	public String toString(){
		return tokens.toString();
	}
}
