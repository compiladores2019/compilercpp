package br.ufc.compiler.parse;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarExpressions {


	private static Stack<Token> balanceParentheses = new Stack<>();

	static {
		balanceParentheses.push(new Token(Kind.OTHER,"$","end-of-stack marking",Parser.currentSymbol.getLine()));
	}


	private static void openParentheses(){

        //verifica se a expressão contém parenteses
        if(Parser.currentSymbol.getLexeme().equals("(")) {
            balanceParentheses.push(Parser.currentSymbol);// empilha o parenteses
            Parser.nextToken(); // avança para o próximo token
        }
    }

	private static void closeParentheses(){
        //verifica se após ler algum ID ou valor ele fecha paranteses , exempo (a) ..
        if(Parser.currentSymbol.getLexeme().equals(")")) {
            balanceParentheses.pop();// desempilha o parenteses
            Parser.nextToken(); // avança para o próximo token
        }
    }

	/*Expressoes Aritméticas ainda não terminada, esta um exemplo simples sem parenteses, lembrando que quando ele
	ler , ou ; em uma expressão, ele precisa abortar,pois no caso de , se torna uma pipeline de declarações (atribuições) e
	se for ; denota fim de comando. COMPLETA, MAS NÃO ABRANGE TUDO */
	public static void expressionArithms() {

	            openParentheses();

		//o token autal precisa ser um desses
		if(Parser.currentSymbol.getKind().equals(Kind.ID) ||
				Parser.currentSymbol.getKind().equals(Kind.INT) ||
				Parser.currentSymbol.getKind().equals(Kind.FLOAT)) {

			System.out.print(Parser.currentSymbol.getLexeme()+" ");
			Parser.nextToken(); //o proximo termo precisa ser uma opreação? não necessariamente

                  closeParentheses();

			if(Parser.currentSymbol.getKind().equals(Kind.OP_ARITHM)){
				System.out.print(Parser.currentSymbol.getLexeme()+" ");
				Parser.nextToken();

                 openParentheses(); // verifica se abre outro parenteses ex: ... id + (

				expressionArithms();/* chama a função recursiva para verificar se na proxima chamada será um ID ou numero
                                        ou para que encontre um marcador de final de expressão */



			}else //se o termo for de final de comando ou de pipe de declarações
                closeParentheses();// verifica se antes de terminar a expressão contém algum parenteses para fechar
				if(Parser.currentSymbol.getLexeme().equals(",") ||
                        Parser.currentSymbol.getLexeme().equals(";")){

				    if(!balanceParentheses.empty() && balanceParentheses.peek().getLexeme().equals("$")){
                           Parser.nextToken();
                           return;
                    }else{
				        Parser.previousToken();
                        System.out.println("Syntax error line " + Parser.currentSymbol.getLine() +
                                "\ncause by: missing ')' after"+ Parser.currentSymbol.getLexeme());
                    }
				}else
				    // se não for nem operador e nem um marcador, então erro sintático
                if(balanceParentheses.peek().getLexeme().equals("$")) {
                    System.out.println("\n\nSyntax error line " + Parser.currentSymbol.getLine() +
                            "\ncause by: missing ; or , ");
                    return;
                }
		}


	}

}
