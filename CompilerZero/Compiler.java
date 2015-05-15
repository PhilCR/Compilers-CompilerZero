/*
Author:
    Philippe César Ramos RA 380415
    Jonas


Compiler 0 v2.0

As seen in class, we will build a compiler for the grammar LE:

Expr ::= Term ExprPrime
ExprPrime ::= + Term ExprPrime | ø
Term ::= Factor TermPrime
TermPrime ::= * Factor TermPrime | ø
Factor ::= Number | '(' Expr ')'
Number ::= ’0’ | ’1’ | ... | ’9{0 | ... | 9}'

*/

package compilerZero;


public class Compiler {
    
    private char token;
    private int  tokenPos;
    private char []input;
    
    //Expression's String
    private String output;
    //private char []output;
    
    //Expression's Integer value
    private int eval;
    
    //Compiler's Writer
    private String writer;
    //private char []writer;
    
    public void compile( char []p_input ) {

        input = p_input;
        
        output = "";
        eval = 0;

        tokenPos = 0;
        nextToken();
        
    	eval = expr();

    	if ( token  != '\0' )
            error();
    }

    //Expr ::= Term ExprPrime
    private int expr() {

        return term() + exprPrime();

    }
        
    //ExprPrime ::= + Term ExprPrime | ø
    private int exprPrime(){

        if(token == '+'){

            //Writes token into Expression's output
            output += token;

            nextToken();
            
            return term() + exprPrime();

        }

        return 0; //0 (zero) is addition's nullity term
    }
        
    //Term ::= Factor TermPrime
    private int term(){

        return factor() * termPrime();

    }
        
    //TermPrime ::= * Factor TermPrime | ø
    private int termPrime(){

        if (token == '*'){

            //Writes token into Expression's output
            output += token;

            nextToken();
            
            return factor() * termPrime();

        }

        return 1; //1 (one) is addition's null term
    }
        
    //Factor ::= Number | '(' Expr ')'
    private int factor(){

        //Eval value from internal expression Expr
        int e;

        //Analises '(' Expr ')' occurance first
        if( token == '(' ){

            //Writes token into Expression's output
            output += token;

            nextToken();
            
            e = expr();

            //Condition to close brackets
            if( token == ')' ){
                
                //Writes token into Expression's output
                output += token;

                nextToken();
                return e;

            }else{
                error();
                return 0;
            }

        }else
            return number();
    }

    //Number ::= ’0’ | ’1’ | ... | ’9{0 | ... | 9}'
    private int number() {

        int nInt = 0;
        String nString = "";

        if( token >= '0' && token <= '9' ){

            //Consums a numerical digits chain
            while( token >= '0' && token <= '9' ){
                output += token;

                nString += token;

                nextToken();
            }

            nInt = Integer.parseInt( (nString + "\0").trim() ); //Parsing a String (token + "\0") into int

            //return nInt; Does not need to return here since there's one at the end

        } else
            error(); 

        return nInt;

    }

    private void nextToken() {

        while (  tokenPos < input.length && input[tokenPos] == ' ' )
            tokenPos++;

        if ( tokenPos < input.length ) {
            token = input[tokenPos];
            tokenPos++;

        } else
            token = '\0';
    }

    private void error() {

        if ( tokenPos == 0 )
            tokenPos = 1;

        else
            if ( tokenPos >= input.length )
                tokenPos = input.length;

        String strInput = new String( input,
                 tokenPos - 1, input.length - tokenPos + 1 );
        String strError = "Error at \"" + strInput + "\"";
        System.out.println( strError );
           // throws an exception - the program is terminated
        throw new RuntimeException(strError);
    }

    //Generate Compiler's code in C
    public void genCode(){

        writer = "/* CompilerZero */\n";

        writer += "#include <stdlib.h>\n#include <stdio.h>\n\nint main(){\n\n\tprintf( \" ";

        writer += output + " : ";

        writer += "%d \", ";

        writer += eval + '\0';

        writer += ");\n\n\treturn 0;\n}\n";

        System.out.print(writer);
    }
}