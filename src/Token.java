/**
 * @author ishty
 */

public class Token {

    /**
     * enum to keep Token types
     */
    public enum tokenType {
        IDENTIFIER,            //was WORD
        INDENT,                // should be taken care of in lex function
        DEDENT,
		COMMA,// ``
        
        STRINGTYPE,
        STRINGLITERAL,          //hyrbid
        CHARACTERTYPE,
        CHARACTERLITERAL,       //hybrid
        INTEGER,                //Was NUMBER; //hybrid
        INTEGERTYPE,
        REAL,                   //hyrbid
        REALTYPE,
        BOOLEAN,                //must be taken care of using Hashmap
        BOOLEANTYPE,
        ARRAY,                  //must be taken care of using Hashmap
        ARRAYTYPE,
        
        
        VARAIABLES,             //must be taken care of using Hashmap
        VAR,
        CONSTANT,               //must be taken care of using Hashmap
        
        ASSIGNMENT_COLON_EQUALS,//must be taken care of in lex function
        ASSIGNMENT_EQUALS,      //must be taken care of in lex function
        ASSIGNMENT_COLON,       //must be taken care of in lex function
        SEMI_COLON,
        
        COMPARATOR_EQUALS,      //must be taken care of using Hashmap
        COMPARATOR_NOT_EQUAL,   //must be taken care of using Hashmap
        COMPARATOR_LESS,        //must be taken care of using Hashmap
        COMPARATOR_LESS_OR_EQUALS,//must be taken care of using Hashmap
        COMPARATOR_GREATER,     //must be taken care of using Hashmap
        COMPARATOR_GREATER_OR_EQUALS,//must be taken care of using Hashmap
        
        DEFINE,                 //must be taken care of using Hashmap
        WRITE,                  //must be taken care of using Hashmap
        
        OF,                     //must be taken care of using Hashmap
        FROM,                   //must be taken care of using Hashmap
        TO,                     //must be taken care of using Hashmap
        
        FOR,                    //must be taken care of using Hashmap
        WHILE,                  //must be taken care of using Hashmap
        REPEAT,
        UNTIL,           		//must be taken care of using Hashmap
        
        PLUS,                   //must be taken care of using Hashmap
        MINUS,                  //must be taken care of using Hashmap
        MULTPILY,               //must be taken care of using Hashmap
        DIVIDE,                 //must be taken care of using Hashmap
        MOD,                    //must be taken care of using Hashmap
        
        NOT,                    //must be taken care of using Hashmap
        AND,                    //must be taken care of using Hashmap
        OR,                     //must be taken care of using Hashmap
        
        //INDEX,                  //must be taken care of in lex function
        COMMENT,                //must be taken care of in lex function
        PARAMETERS_START,       //must be taken care of in lex function
        PARAMETERS_END,			//must be taken care of in lex function
        INDEX_START,
        INDEX_END,
        
        ABYSS,
        
        ENDOFLINE
	}

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
