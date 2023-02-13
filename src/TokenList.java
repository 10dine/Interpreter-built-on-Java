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

    /**
     * toString function overrriden so that all tokens are printed line by line which is marked by the
     * ENDOFLINE token.
     * @return String of all Tokens.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Token element : this) {
            switch (element.getType()){
                case ENDOFLINE:
                    builder.append(element);
                    builder.append("\n");
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
