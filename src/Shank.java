
/**
 * @author ishty
 *
 * Main Shank file. Asks for the name of the file and then uses a Lexer object to print all the tokens.
 */
public class Shank {
    public static void main(String[] args) throws Exception {
        
        if(args.length == 1){
            Lexer lexer = new Lexer(args[0]);
            Parser parser = new Parser(lexer.getTokenList());
            System.out.println(parser);
            Interpreter interpreter = new Interpreter(parser.getProgram());
        } else {
            System.out.println("Please use Shank appropriately **\" Java Shank({filename})\"**");
        }
    }
}
