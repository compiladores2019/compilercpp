package br.ufc.compiler.parse;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

import static br.ufc.compiler.parse.Parser.*;

public class GrammarExpressions {


    private static Stack<Token> balanceParentheses = new Stack<>();

    static {
        balanceParentheses.push(new Token(Kind.OTHER,"$","end-of-stack marking", currentSymbol.getLine()));
    }


    private static boolean openParentheses(){

        //verifica se a expressão contém parenteses
        if(currentSymbol.getLexeme().equals("(")) {
            balanceParentheses.push(currentSymbol);// empilha o parenteses
            nextToken(); // avança para o próximo token
            return true;
        }
        return false;
    }

    private static boolean closeParentheses(){
        //verifica se após ler algum ID ou valor ele fecha paranteses , exempo (a) ..
        if(currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
            balanceParentheses.pop();// desempilha o parenteses
            nextToken(); // avança para o próximo token
            return true;
        }
        return false;
    }

    /*Expressoes Aritméticas ainda não terminada, esta um exemplo simples sem parenteses, lembrando que quando ele
    ler , ou ; em uma expressão, ele precisa abortar,pois no caso de , se torna uma pipeline de declarações (atribuições) e
    se for ; denota fim de comando. COMPLETA, MAS NÃO ABRANGE TUDO */
    public static void expressionArithms() {



        openParentheses();
        //aborto a operação se encontrar =, ou =(, que é um erro e obvio a pilha tem de estar zerada
        // o que significa que se em algum nível de recursão isso coincidir, temo uma situação válida
        //por exemplo (id + id), parenteses balanceados e a marca de fim de expressão,mas isso nunca será
        //verificado no final, apenas no início
        if(currentSymbol.getLexeme().equals(",") && balanceParentheses.peek().getLexeme().equals("$")){
            //erro sintático
            System.out.println("abortando..");
            nextToken();
            return;
        }
        //o token autal precisa ser um desses
        if(currentSymbol.getKind().equals(Kind.ID) ||
                currentSymbol.getKind().equals(Kind.INT) ||
                currentSymbol.getKind().equals(Kind.FLOAT)) {

            System.out.print(currentSymbol.getLexeme() + " ");
            nextToken();//o proximo termo precisa ser uma opreação? não necessariamente
            closeParentheses();

            if (currentSymbol.getKind().equals(Kind.OP_ARITHM)){
                System.out.print(currentSymbol.getLexeme() + " ");
                nextToken();

                openParentheses(); // verifica se abre outro parenteses ex: ... id + (

                expressionArithms();/* chama a função recursiva para verificar se na proxima chamada será um ID ou numero
                                       ou para que encontre um marcador de final de expressão */

            }else{
                //se o termo for de final de comando ou de pipe de declarações
                // ele só entra aqui quando o token for esses delimitadores de expressão
                if (currentSymbol.getLexeme().matches("[,|;]")) {
                    //closeParentheses();
                    if (currentSymbol.getLexeme().equals(";") && balanceParentheses.peek().getLexeme().equals("$")) {
                        nextToken();
                        return;
                    } else {

                        if (currentSymbol.getLexeme().equals(",") && balanceParentheses.peek().getLexeme().equals("$")) {
                            //nextToken();
                            return;
                        } else {
                            //lançar uma exceção aqui
                            System.out.println("Syntax error line -> " + currentSymbol.getLine() +
                                    "\n cause by: missing ')' before " + currentSymbol.getLexeme());
                            return;
                        }
                    }
                } else
                    //apos todos os parenteses aninhados podemos ter outra operacao  ex: (id + (a + id)) + (id..
                    if (currentSymbol.getKind().equals(Kind.OP_ARITHM)) {
                        System.out.print(currentSymbol.getLexeme() + " ");
                        nextToken();
                        expressionArithms();

                    } else if(currentSymbol.getLexeme().equals(")")) {

                        while (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
                            if (closeParentheses()) continue;
                            else
                                nextToken();
                        }
                        if (currentSymbol.getLexeme().matches("[,|;]") && balanceParentheses.peek().getLexeme().equals("$")){
                              if(currentSymbol.getLexeme().equals(";")) {
                                  nextToken();
                                  return;
                              }else return;
                        } else {

                            if (currentSymbol.getKind().equals(Kind.OP_ARITHM)) {
                                System.out.print(currentSymbol.getLexeme() + " ");
                                nextToken();
                                expressionArithms();

                            } else {

                                System.out.println("Syntax error line -> " + currentSymbol.getLine() +
                                        "\n cause by: missing operator arithmetic before " + currentSymbol.getLexeme());
                                return;
                            }
                        }
                    }else{
                        //lançar uma exeção aqui
                        System.out.println("\nSyntax error line -> " + currentSymbol.getLine() +
                                "\n cause by: " + currentSymbol.getLexeme() + "\n expect: , or ;");
                        return;
                    }
            }
        }

    }

}
