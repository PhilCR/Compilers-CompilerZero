package compilerZero;

public class Main {

    public static void main( String []args ) {
        char []input = "2002 * ( (20 + 5) * 7 ) + 5 * 2".toCharArray();
        
        Compiler compiler = new Compiler();
        
        compiler.compile(input);
        
        compiler.genCode();

    }

}