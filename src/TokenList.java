import java.util.ArrayList;

/**
 * @author ishty
 * TokenList is a child class of ArrayList and its custom function is the overriden
 * toString function for readability.
 */
public class TokenList extends ArrayList<Token> {

    @Override
    public void add(int index, Token element) {
        super.add(index, element);
    }
    
    public Token pop(){
        return this.remove(0);
    }
    
    /**
     * toString function overrriden so that all tokens are printed line by line which is marked by the
     * ENDOFLINE token.
     * @return String of all Tokens.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int lineCount = 1;
        builder.append(String.format("Line Count: %d | ",lineCount));
        for (Token element : this) {
            switch (element.getType()){
                case ENDOFLINE:
                    lineCount++;
                    builder.append(element);
                    builder.append(String.format("\nLine Count: %d | ",lineCount));
                    break;
                default:
                    builder.append(element);
                    builder.append(", ");
                    break;
            }
        }
        return builder.toString();
    }

 
}
