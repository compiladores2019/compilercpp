package br.ufc.compiler.parse;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import static br.ufc.compiler.lexicon.Token.Kind.*;

import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.parse.GrammarKind.*;
import static br.ufc.compiler.parse.GrammarRelational.*;
import static br.ufc.compiler.parse.GrammarLogic.*;

public class GrammarExpressions {

    private static Stack<Token> balanceParentheses = new Stack<>();
    private static Stack<Token> balanceParenthesesIf = new Stack<>();


    static {
        balanceParentheses.push(new Token(OTHER, "$", "end-of-stack marking", currentSymbol.getLine()));
        balanceParenthesesIf.push(new Token(OTHER, "$", "end-of-stack marking", currentSymbol.getLine()));
    }

    //limpa a pilha de uma expressão finalizada
    static void clearStackParentheses(){
        balanceParentheses.clear();
    }

    //limpa a pilha de uma expressão if finalizada
    static void clearStackParenthesesIf(){
        balanceParenthesesIf.clear();
    }

    public static boolean openParentheses() {

        // verifica se a expressão contém parenteses
        if (currentSymbol.getLexeme().equals("(")) {
            System.out.print(currentSymbol.getLexeme() + " ");
            balanceParentheses.push(currentSymbol);// empilha o parenteses
            nextToken(); // avança para o próximo token
            return true;
        }
        return false;
    }

    public static boolean closeParentheses() {
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

    public static boolean openParenthesesIf() {

        // verifica se a expressão contém parenteses
        if (currentSymbol.getLexeme().equals("(")) {
            System.out.print(currentSymbol.getLexeme() + " ");
            balanceParenthesesIf.push(currentSymbol);// empilha o parenteses
            nextToken(); // avança para o próximo token
            return true;
        }
        return false;
    }

    public static boolean closeParenthesesIf() {
        // verifica se após ler algum ID ou valor ele fecha paranteses , exempo
        // (a) ..
        if (currentSymbol.getLexeme().equals(")") && balanceParenthesesIf.peek().getLexeme().equals("(")) {
            System.out.print(currentSymbol.getLexeme() + " ");
            balanceParenthesesIf.pop();// desempilha o parenteses
            nextToken(); // avança para o próximo token
            return true;
        }
        return false;
    }


    private static void validateExpression(){

        while(currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("("))
        {
            if(closeParentheses()) continue;
            else
                nextToken();
        }

        if(currentSymbol.getLexeme().equals(";")){

            if(balanceParentheses.peek().getLexeme().equals("$")){
                nextToken();
                return;
            }else{

                System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                        + currentSymbol.getLexeme() + "\n expected: ')'");
                  return;
            }
        }
        else
        if(currentSymbol.getLexeme().equals(",")) {
           declaration();// leu vírgula, então é uma declaração? esperamos que smim
        }
    }

    private static void validateExpressionIf(){

        while(currentSymbol.getLexeme().equals(")") && balanceParenthesesIf.peek().getLexeme().equals("("))
        {
            if(closeParenthesesIf()) continue;
            else
                nextToken();
        }

        if(currentSymbol.getLexeme().equals(")")){
            if(balanceParenthesesIf.peek().getLexeme().equals("$")){
                nextToken();
                return;
            }else{
                System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                        + currentSymbol.getLexeme() + "\n expected: ')'");
                return;
            }

        }else{

            if(currentSymbol.getKind().equals(OP_REL) || currentSymbol.getKind().equals(OP_LOG)) {
                nextToken();
                expressionIf();// verifico se não tem algum parenteses sem fechar, se Ok e o proximo simbolo
                // não for ) pq? pq seria o parenteses de final if, como não é, ele vai ver se tem outra expressão if
                // mas para isso ele precisa ler algum relacional ou operador lógico
            }
        }

    }


    public static void expression() {

        openParentheses();

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

                expression();

            } else {

                if (currentSymbol.getLexeme().matches("[,|;]")) {

                    if (currentSymbol.getLexeme().equals(";") && balanceParentheses.peek().getLexeme().equals("$")) {
                        nextToken();
                        return;
                    } else {

                        if (currentSymbol.getLexeme().equals(",")
                                && balanceParentheses.peek().getLexeme().equals("$")) {
                            clearStackParentheses();
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
                        expression();

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
                                clearStackParentheses();
                                nextToken();
                                return;
                            } else
                                return;
                        } else {

                            if (currentSymbol.getKind().equals(OP_ARITHM)) {
                                System.out.print(currentSymbol.getLexeme() + " ");
                                nextToken();
                                expression();

                            } else {

                                if (currentSymbol.getKind().equals(OP_LOG)) {

                                    //previousToken(); // para começar de id &&
                                    opLog();
                                    validateExpression();

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

                            opRel();
                            expression();

                        } else {

                            if(currentSymbol.getKind().equals(OP_LOG)){
                                System.out.print(currentSymbol.getLexeme() + " ");
                                    opLog();
                                   validateExpression();

                            }else {
                                System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                                        + currentSymbol.getLexeme() + "\n expected: , or ; or op arithm");
                                return;
                            }
                        }
                    }
            }
        }

    }

    public static void expressionIf() {

        openParentheses();

        if (currentSymbol.getKind().equals(ID) ||
                currentSymbol.getKind().equals(INT) ||
                currentSymbol.getKind().equals(FLOAT)) {

            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();// o proximo termo precisa ser uma opreação? não
            closeParentheses();

            //caso para if(id).. expressão unitária, para algum bool
            if (balanceParenthesesIf.peek().getLexeme().equals("$") && currentSymbol.getLexeme().equals(")")) {
                return;
            } else

                  if (currentSymbol.getKind().equals(OP_ARITHM)) {
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();

                openParentheses(); // verifica se abre outro parenteses ex: ...// id + (

                expressionIf();

            } else {
                // apos todos os parenteses aninhados podemos ter outra operacao
                // ex: (id + (a + id)) + (id..
                if (currentSymbol.getKind().equals(OP_ARITHM)) {
                    System.out.print(currentSymbol.getLexeme() + " ");
                    nextToken();
                    expressionIf();

                } else if (currentSymbol.getLexeme().equals(")")) {

                    while (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
                        if (closeParentheses())
                            continue;
                        else
                            nextToken();
                    }
                    if (currentSymbol.getKind().equals(OP_ARITHM)) {
                        System.out.print(currentSymbol.getLexeme() + " ");
                        nextToken();
                        expressionIf();

                    } else {

                        if (currentSymbol.getKind().equals(OP_LOG)
                                || currentSymbol.getKind().equals(OP_REL)) {
                            nextToken();
                            expressionIf();
                        } else {

                            System.out.println("Syntax error line -> " + currentSymbol.getLine()
                                    + "\n cause by: missing operator arithmetic before "
                                    + currentSymbol.getLexeme());
                            return;
                        }
                    }

                } else {

                    if (currentSymbol.getKind().equals(OP_REL)) {
                        System.out.print(currentSymbol.getLexeme() + " ");
                        opRelIf();

                        System.out.println("retornei aqui!");

                        //quando terminar o metodo opRel ele verifica se o token atual é um desses, se for ele chama expressãoIf
                        //caso contrario ele verifica a expressao e valida a mesma ou não, caso faltou algo, em OpRel terá erro de sintaxe
                        if(currentSymbol.getKind().equals(ID) ||
                                currentSymbol.getKind().equals(INT)||
                                currentSymbol.getKind().equals(FLOAT)) {
                            nextToken();
                            expressionIf();
                        }else
                             validateExpressionIf();
                           //preciso validar, parenteses e a construção
                          //expressionIf();
                    } else {
                        System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
                                + currentSymbol.getLexeme() + "\nexpect: op logic or ");
                                  return;
                    }
                }
            }


        }
    }

}
