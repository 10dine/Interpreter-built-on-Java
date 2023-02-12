/**
 * @author ishty
 */

public class Token {

    /**
     * enum to keep Token types
     */
    public enum tokenType {
        INDENTIFIER,
        INDENT,
        DEDENT,

        NUMBER,
        STRINGLITERAL,
        CHARACTERLITERAL,
        BOOLEAN,
        ARRAY,
        REAL,
        
        VARAIABLES,
        CONSTANT,
        
        ASSIGNMENT_COLON_EQUALS,
        ASSIGNMENT_EQUALS,
        ASSIGNMENT_COLON,
        
        COMPARATOR_EQUALS,
        COMPARATOR_NOT_EQUAL,
        COMPARATOR_LESS,
        COMPARATOR_LESS_OR_EQUALS,
        COMPARATOR_GREATER,
        COMPARATOR_GREATER_OR_EQUALS,
        
        DEFINE,
        WRITE,
        
        FOR,
        WHILE,
        REPEAT_UNTIL,
        
        PLUS,
        MINUS,
        MULTPILY,
        DIVIDE,
        MOD,
        
        NOT,
        AND,
        OR,
        
        INDEX,
        COMMENT,
        PARAMETERS,
        
        ENDOFLINE}

    /**
     * Variable to contain Token type.
     */
    private final tokenType type;
    /**
     * Variable to contain value of Token.
     */
    private final String value;

    /**
     * Token class constructor.
     * @param type Variable to contain Token type.
     * @param value Variable to contain value of Token.
     */
    public Token(tokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     *Returns value of Token Type
     * @return type of token
     */
    public tokenType getType() {
        return type;
    }

    /**
     *Returns value of token
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @return String concatanation of token type and value
     */
    public String toString() {
        return ("" + getType() + "(" + getValue() + ")");
    }

}
