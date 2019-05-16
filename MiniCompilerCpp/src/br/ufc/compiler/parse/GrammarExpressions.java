package br.ufc.compiler.parse;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import static br.ufc.compiler.lexicon.Token.Kind.*;

import static br.ufc.compiler.parse.Parser.*;

public class GrammarExpressions {

    private static Stack<Token> balanceParentheses = new Stack<>();

    //controle de precedencia de simbolos lógicos e relacionais
    private  static Stack<Token> controlOperator = new Stack<>();

    static {
        balanceParentheses.push(new Token(OTHER, "$", "end-of-stack marking", currentSymbol.getLine()));
        controlOperator.push(new Token(OTHER, "#", "end-of-stack marking", currentSymbol.getLine()));
    }

    private static boolean openParentheses() {

        // verifica se a expressão contém parenteses
        if (currentSymbol.getLexeme().equals("(")) {
            System.out.print(currentSymbol.getLexeme() + " ");
            balanceParentheses.push(currentSymbol);// empilha o parenteses
            nextToken(); // avança para o próximo token
            return true;
        }
        return false;
    }

    private static boolean closeParentheses() {
        // verifica se após ler algum ID ou valor ele fecha paranteses , exempo
        // (a) ..
        if (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
            System.out.print(currentSymbol.getLexeme() + " ");
            balanceParentheses.pop();// desempilha o parenteses
            nextToken(); // avança para o próximo token
            return true;
        }
        return false;
    }

    /*
     * Expressoes Aritméticas ainda não terminada, esta um exemplo simples sem
     * parenteses, lembrando que quando ele ler , ou ; em uma expressão, ele
     * precisa abortar,pois no caso de , se torna uma pipeline de declarações
     * (atribuições) e se for ; denota fim de comando. COMPLETA, MAS NÃO
     * ABRANGE TUDO
     */
    public static void expressionArithms() {

        openParentheses();

        if (currentSymbol.getLexeme().equals(",")
                || currentSymbol.getLexeme().equals(";") && balanceParentheses.peek().getLexeme().equals("$")) {
            // erro sintático
            System.out.println("abortando..");
            nextToken();
            return;
        }

        if (currentSymbol.getKind().equals(ID) ||
                currentSymbol.getKind().equals(INT) ||
                currentSymbol.getKind().equals(FLOAT)) {

            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();

            closeParentheses();

            if (currentSymbol.getKind().equals(OP_ARITHM)) {
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();

                openParentheses();

                expressionArithms();

            } else {


                if (currentSymbol.getLexeme().matches("[,|;]")) {

                    if (currentSymbol.getLexeme().equals(";") && balanceParentheses.peek().getLexeme().equals("$")) {
                        nextToken();
                        return;
                    } else {

                        if (currentSymbol.getLexeme().equals(",")
                                && balanceParentheses.peek().getLexeme().equals("$")) {
                            return;

                        } else {
                            // lançar uma exceção aqui
                            System.out.println("Syntax error line -> " + currentSymbol.getLine()
                                    + "\n cause by: missing ')' before " + currentSymbol.getLexeme());
                            return;
                        }
                    }
                } else

                    // apos todos os parenteses aninhados podemos ter outra operacao
                    // ex: (id + (a + id)) + (id..
                    if (currentSymbol.getKind().equals(OP_ARITHM)) {
                        System.out.print(currentSymbol.getLexeme() + " ");
                        nextToken();
                        expressionArithms();

                    } else if (currentSymbol.getLexeme().equals(")")) {

                        while (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
                            if (closeParentheses())
                                continue;
                            else
                                nextToken();
                        }

                        if (currentSymbol.getLexeme().matches("[,|;]")
                                && balanceParentheses.peek().getLexeme().equals("$")) {
                            if (currentSymbol.getLexeme().equals(";")) {
                                nextToken();
                                return;
                            } else
                                return;
                        } else {

                            if (currentSymbol.getKind().equals(OP_ARITHM)) {
                                System.out.print(currentSymbol.getLexeme() + " ");
                                nextToken();
                                expressionArithms();

                            } else {

                                if (currentSymbol.getKind().equals(OP_LOG)
                                        || currentSymbol.getKind().equals(OP_REL)) {
                                    nextToken();
                                    expressionArithms();
                                } else {

                                    System.out.println("Syntax error line -> " + currentSymbol.getLine()
                                            + "\n cause by: missing operator arithmetic before "
                                            + currentSymbol.getLexeme());
                                    return;
                                }
                            }
                        }
                    } else {

                        if (currentSymbol.getKind().equals(OP_REL)) {

                            controlOperator.push(currentSymbol);
                            // empilha a precedencia
                            // pois não pode haver id + id && .. pois não é algo relacional
                            //mas id [<,>,<=,>=,==,!=] id && é valido, ou seja quando ler o token [||,&&] ele verifica se tem
                            // esse simbolo na pilha, se sim, desempilha, se não, erro de sintaxe
                            System.out.print(currentSymbol.getLexeme() + " ");
                            nextToken();
                            expressionArithms();

                        } else {

                            if(currentSymbol.getKind().equals(OP_LOG)){
                                System.out.print("peek: "+controlOperator.peek().getLexeme());
                                if(controlOperator.peek().getKind().equals(OP_REL)) {
                                    controlOperator.pop();
                                    expressionArithms();
                                }
                            }else
                            System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                                    + currentSymbol.getLexeme() + "\n expected: , or ; or op arithm");
                            return;
                        }
                    }
            }
        }

    }
/*
    public static void expressionForIf() {

        openParentheses();
        // aborto a operação se encontrar =, ou =(, ou =; ou =(; que é um
        // erro e obvio a pilha tem de estar zerada
        // o que significa que se em algum nível de recursão isso coincidir,
        // temo uma situação válida
        // por exemplo (id + id), parenteses balanceados e a marca de fim de
        // expressão,mas isso nunca será
        // verificado no final, apenas no início
        if (currentSymbol.getLexeme().equals(",")
                || currentSymbol.getLexeme().equals(";") && balanceParentheses.peek().getLexeme().equals("$")) {
            // erro sintático
            System.out.println("abortando..");
            nextToken();
            return;
        }
        // o token autal precisa ser um desses
        if (currentSymbol.getKind().equals(Kind.ID) || currentSymbol.getKind().equals(Kind.INT)
                || currentSymbol.getKind().equals(Kind.FLOAT)) {

            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();// o proximo termo precisa ser uma opreação? não
            // necessariamente
            closeParentheses();

            if (currentSymbol.getKind().equals(Kind.OP_ARITHM)) {
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();

                openParentheses(); // verifica se abre outro parenteses ex: ...
                // id + (

                expressionArithms();

            } else {
                // se o termo for de final de comando ou de pipe de
                // declarações
                // ele só entra aqui quando o token for esses delimitadores de
                // expressão
                if (currentSymbol.getLexeme().matches("[,|;]")) {
                    // closeParentheses();
                    if (currentSymbol.getLexeme().equals(";") && balanceParentheses.peek().getLexeme().equals("$")) {
                        nextToken();
                        return;
                    } else {

                        if (currentSymbol.getLexeme().equals(",")
                                && balanceParentheses.peek().getLexeme().equals("$")) {
                            // nextToken();
                            return;
                        } else {
                            // lançar uma exceção aqui
                            System.out.println("Syntax error line -> " + currentSymbol.getLine()
                                    + "\n cause by: missing ')' before " + currentSymbol.getLexeme());
                            return;
                        }
                    }
                } else
                    // apos todos os parenteses aninhados podemos ter outra operacao
                    // ex: (id + (a + id)) + (id..
                    if (currentSymbol.getKind().equals(Kind.OP_ARITHM)) {
                        System.out.print(currentSymbol.getLexeme() + " ");
                        nextToken();
                        expressionArithms();

                    } else if (currentSymbol.getLexeme().equals(")")) {

                        while (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
                            if (closeParentheses())
                                continue;
                            else
                                nextToken();
                        }
                        if (currentSymbol.getLexeme().matches("[,|;]")
                                && balanceParentheses.peek().getLexeme().equals("$")) {
                            if (currentSymbol.getLexeme().equals(";")) {
                                nextToken();
                                return;
                            } else
                                return;
                        } else {

                            if (currentSymbol.getKind().equals(Kind.OP_ARITHM)) {
                                System.out.print(currentSymbol.getLexeme() + " ");
                                nextToken();
                                expressionArithms();

                            } else {

                                if (currentSymbol.getKind().equals(Kind.OP_LOG)
                                        || currentSymbol.getKind().equals(Kind.OP_REL)) {
                                    nextToken();
                                    expressionArithms();
                                } else {

                                    System.out.println("Syntax error line -> " + currentSymbol.getLine()
                                            + "\n cause by: missing operator arithmetic before "
                                            + currentSymbol.getLexeme());
                                    return;
                                }
                            }
                        }
                    } else {
                        // lançar uma exeção aqui
                        if (currentSymbol.getKind().equals(Kind.OP_LOG) || currentSymbol.getKind().equals(Kind.OP_REL)) {
                            System.out.print(currentSymbol.getLexeme() + " ");
                            nextToken();
                            expressionArithms();
                        } else {
                            System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                                    + currentSymbol.getLexeme() + "\n expect: , or ;");
                            return;
                        }
                    }
            }
        }

    } */


}
