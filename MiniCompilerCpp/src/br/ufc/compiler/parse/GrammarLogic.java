package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.ID;
import static br.ufc.compiler.lexicon.Token.Kind.OP_LOG;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.GrammarExpressions.*;


public class GrammarLogic {

    public static void opLog(){

        if(currentSymbol.getKind().equals(OP_LOG)){

            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();
            openParentheses();

            if(currentSymbol.getKind().equals(ID)) {
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();
                closeParentheses();

                opLog(); // chama novamente o procedimento

                if(currentSymbol.getLexeme().equals(";") || currentSymbol.getLexeme().equals(",")) {
                    return;
                }

            }else{
                System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                        + currentSymbol.getLexeme() + "\n expected: identifier or , or ;");
                return;
            }

        }else{

            if(currentSymbol.getLexeme().equals(";")){
                System.out.print(currentSymbol.getLexeme() + " ");
                //nextToken();
                return;
            } else
            if(currentSymbol.getLexeme().equals(",")) return;
            else {
                if(currentSymbol.getLexeme().equals(")")) return;
                else {
                    System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                            + currentSymbol.getLexeme() + "\n expected: op logic");
                    return;
                }
            }
        }

    }

    public static void opLogIf() {

        if (currentSymbol.getKind().equals(OP_LOG)) {

            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();
            openParenthesesIf();

            if (currentSymbol.getKind().equals(ID)) {
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();
                closeParenthesesIf();

                opLogIf(); // chama novamente o procedimento

            } else {
                System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                        + currentSymbol.getLexeme() + "\n expected: identifier or (");
                return;
            }

        } else {

            if (currentSymbol.getLexeme().equals(")")) return;
            else {

                if(currentSymbol.getKind().equals(OP_LOG) || currentSymbol.getLexeme().equals("=="))
                return;
                else {
                    System.out.println("\nSyntax error line -> " + currentSymbol.getLine() +
                            "\n cause by: "
                            + currentSymbol.getLexeme() + "\n expected: op logic");
                    return;
                }

            }
        }
    }

}
