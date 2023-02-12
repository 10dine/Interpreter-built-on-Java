import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author ishty
 *
 * Main Shank file. Asks for the name of the file and then uses a Lexer object to print all the tokens.
 */
public class Shank {
    public static void main(String[] args) {

        System.out.println("This program does not accept any symbols currently. It will say if there are any symbols and terminate when they are found.");
        System.out.println("**Another note: please input filename without extension**");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the filename: ");
        String filename = scanner.nextLine();

        Lexer lexer = new Lexer(filename);
        System.out.println("Here are all the tokens: \n"+ lexer);
    }
}
