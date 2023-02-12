import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import Token.tokentype;

/**
 * @author ishty
 * Lexer object. Currently, it is an object that contains three variables. A java files Path object,
 * a List for the all the lines and an arraylist extended class object TokenList to caontain all the
 * tokens it produces.
 */
public class Lexer {

    //variables made
    private final Path thePath;
    private List<String> lines;
    private final TokenList tokens = new TokenList();
    private StringBuilder tokenString = new StringBuilder();
    Map<String, tokentype> myMap = new HashMap<String, tokentype>() {{
        put("a", );
        put("c", "d");
    }};

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
            lex(str);
        }
    }

    public void lex(String line) {

        //States for state machine
        //scanning is used for scanning the type of token being made currently.
        //word is the state at which the machines is making a WORD type token.
        //number is the state at which the machines is making a NUMBER type token.
        //floatNUmber is the state at which the machines is making a NUMBER type token with periods.
        enum lexState{
            scanning,
            word,
            floatNumber,
            number,

        }

        // special boolean variable for looping a char element in the for loop until it is assigned to token type.
        boolean loopState;

        //state machine state instantiated
        lexState charState = lexState.scanning;


        //main logic of state machine
        //for loop loops through all characters in the string input of the function
        //while loop loops the character at hand until it is assigned accordingly
        //(1)In scanning state if char is not an illegal character and also changes states to number, word and floatingNumber
        //accordingly.
        //(2)In word state all numbers and words are accepted
        //(3)In number state all numbers and a period is accepted
        //(4)In floatingNumber state all numbers are accepted and all other ones either get built as another token or
        //get rejected
        for (char ch: line.toCharArray()) {
            loopState = true;
            while (loopState)
            {//root.98
                //(1) starts here
                switch (charState) {
                    case scanning:

                        if (isNumber(ch)) {
                            tokenString.append(ch);
                            charState = lexState.number;
                            loopState=false;
                        } else if (isLetter(ch)) {
                            tokenString.append(ch);
                            charState = lexState.word;
                            loopState=false;
                        } else if (isFloat(ch)) {
                            tokenString.append(ch);
                            charState = lexState.floatNumber;
                            loopState=false;
                        } else {
                            try {
                                isIllegalChar(ch);
                                loopState=false;
                            } catch (SyntaxErrorException e) {
                                System.out.println(e.getMessage() + " " + ch + ". " + "These are all the Token made so far: " + tokens.toString());
                                System.exit(1);
                            }
                        }

                        break;
                    //(2) starts here
                    case word:

                        if (isNumber(ch) || isLetter(ch)) {
                            tokenString.append(ch);
                            loopState=false;
                        } else if (isFloat(ch)) {
                            tokens.add(tokens.size(), new Token(Token.tokenType.WORD, tokenString.toString()));
                            tokenString.setLength(0);
                            charState = lexState.scanning;
                        } else {
                            try {
                                isIllegalChar(ch);
                                tokens.add(tokens.size(), new Token(Token.tokenType.WORD, tokenString.toString()));
                                tokenString.setLength(0);
                                charState = lexState.scanning;
                                loopState=false;
                            } catch (SyntaxErrorException e) {
                                System.out.println(e.getMessage() + " " + ch + ". " + "These are all the Token made so far: " + tokens.toString());
                                System.exit(1);
                            }
                        }

                        break;
                    //(3) starts here
                    case number:

                        if (isNumber(ch)){
                            tokenString.append(ch);
                            loopState=false;
                        } else if (isFloat(ch)) {
                            tokenString.append(ch);
                            charState = lexState.floatNumber;
                            loopState=false;
                        } else if (isLetter(ch)) {
                            tokens.add(tokens.size(), new Token(Token.tokenType.NUMBER, tokenString.toString()));
                            tokenString.setLength(0);
                            charState = lexState.word;
                        } else {
                            try {
                                isIllegalChar(ch);
                                tokens.add(tokens.size(), new Token(Token.tokenType.NUMBER, tokenString.toString()));
                                tokenString.setLength(0);
                                charState = lexState.scanning;
                                loopState=false;
                            } catch (SyntaxErrorException e) {
                                System.out.println(e.getMessage() + " " + ch + ". " + "These are all the Token made so far: " + tokens.toString());
                                System.exit(1);
                            }
                        }

                        break;
                    //(4) starts here
                    case floatNumber:

                        if (isNumber(ch)){
                            tokenString.append(ch);
                            loopState=false;
                        } else if (isLetter(ch)) {
                            tokens.add(tokens.size(), new Token(Token.tokenType.NUMBER, tokenString.toString()));
                            //System.out.println("Token added succesfully: " + tokens.toString());
                            tokenString.setLength(0);
                            charState = lexState.word;
                        } else if (isFloat(ch)) {
                            tokens.add(tokens.size(), new Token(Token.tokenType.NUMBER, tokenString.toString()));
                            //System.out.println("Token added succesfully: " + tokens.toString());
                            tokenString.setLength(0);
                            charState = lexState.number;
                        } else {
                            try {
                                isIllegalChar(ch);
                                tokens.add(tokens.size(), new Token(Token.tokenType.NUMBER, tokenString.toString()));
                                //System.out.println("Token added succesfully: " + tokens.toString());
                                tokenString.setLength(0);
                                charState = lexState.scanning;
                                loopState=false;
                            } catch (SyntaxErrorException e) {
                                System.out.println(e.getMessage() + " " + ch + ". " + "These are all the Token made so far: " + tokens.toString());
                                System.exit(1);
                            }
                        }
                        break;

                }
            }
        }

        //This switch statement is used to handle a bug where if the line ends while a token is being made and for the
        //end of the line token.
        switch (charState){
            case number:

            case floatNumber:
                tokens.add(new Token(Token.tokenType.NUMBER,tokenString.toString()));
                tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
                break;

            case word:
                tokens.add(new Token(Token.tokenType.WORD,tokenString.toString()));
                tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
                break;

            default:
                tokens.add(new Token(Token.tokenType.ENDOFLINE, ""));
                break;
        }
        //System.out.println("Token added succesfully: " + tokens.toString());
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

    /**
     * Throws IllegalCharacterException if the parameter input is a space(32) character.
     * @param input Char value
     * @return true or IllegalCharacterException
     * @throws SyntaxErrorException
     */
    private boolean isIllegalChar(char input) throws SyntaxErrorException {
        if (input != 32)
            throw new SyntaxErrorException("Illegal characters have been found. The character:");
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
