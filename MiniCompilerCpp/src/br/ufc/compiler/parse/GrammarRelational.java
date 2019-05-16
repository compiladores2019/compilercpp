package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.lexicon.Token.Kind.FLOAT;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.GrammarExpressions.*;

public class GrammarRelational {

    //na leitura ele não precisa let id antes, apenas algum relacional vai ativa a chamada do método
    public static void opRel(){

        if(currentSymbol.getKind().equals(OP_REL)){
            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();
            openParentheses();

            if(currentSymbol.getKind().equals(ID) ||
                    currentSymbol.getKind().equals(INT) ||
                    currentSymbol.getKind().equals(FLOAT)) {
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();
                closeParentheses();

                if(currentSymbol.getLexeme().equals("==") || currentSymbol.getKind().equals(OP_LOG)){
                    System.out.print(currentSymbol.getLexeme() + " ");
                    nextToken();
                    openParentheses();

                    if(currentSymbol.getKind().equals(ID) ||
                            currentSymbol.getKind().equals(INT) ||
                            currentSymbol.getKind().equals(FLOAT)){
                        System.out.print(currentSymbol.getLexeme() + " ");
                        nextToken();
                        opRel();
                    }

                }else {

                    if(currentSymbol.getLexeme().equals(";")){
                        System.out.print(currentSymbol.getLexeme() + " ");
                        nextToken();
                        return;
                    }else
                    if(currentSymbol.getLexeme().equals(",")) return;
                    else {
                        System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                                + currentSymbol.getLexeme() + "\n expected: ==, op logic , or ;");
                        return;
                    }
                }
            }

        }else{

            System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                    + currentSymbol.getLexeme() + "\n expected: op relational");
            return;
        }
    }

    public static void opRelIf() {

        if (currentSymbol.getKind().equals(OP_REL)) {

            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();
            openParenthesesIf();

            if (currentSymbol.getKind().equals(ID) ||
                    currentSymbol.getKind().equals(INT) ||
                    currentSymbol.getKind().equals(FLOAT)) {
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();
                closeParenthesesIf();

                if (currentSymbol.getLexeme().equals("==") || currentSymbol.getKind().equals(OP_LOG)) {
                    System.out.print(currentSymbol.getLexeme() + " ");
                    nextToken();
                    openParenthesesIf();

                    if (currentSymbol.getKind().equals(ID) ||
                            currentSymbol.getKind().equals(INT) ||
                            currentSymbol.getKind().equals(FLOAT)) {
                        System.out.print(currentSymbol.getLexeme() + " ");
                        nextToken();
                        opRel();
                    }else{
                        System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                                + currentSymbol.getLexeme() + "\n expected: '(' or identifier");
                        return;
                    }

                } else {

                    if(currentSymbol.getLexeme().equals(")")){
                        System.out.print(currentSymbol.getLexeme() + " ");
                        return;
                    }
                    else {

                        if(currentSymbol.getKind().equals(ID) ||
                                currentSymbol.getKind().equals(INT)||
                                currentSymbol.getKind().equals(FLOAT)) {
                            //pode ser que (5 + 6) < ? pode ser um id? encerro.
                            return;
                        }else{
                            System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                                    + currentSymbol.getLexeme() + "\nexpected: op relational");
                            return;
                        }
                    }
                }
            }

        }
    }

}
