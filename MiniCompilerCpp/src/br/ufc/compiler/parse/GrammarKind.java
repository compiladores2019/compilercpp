package br.ufc.compiler.parse;

import br.ufc.compiler.lexicon.Token.Kind;

import java.util.Stack;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.GrammarExpressions.*;
import static br.ufc.compiler.parse.Parser.*;
import br.ufc.compiler.lexicon.Token;
public class GrammarKind {


	private static Stack<Token> balance = new Stack<>();


	static{
		balance.push(new Token(OTHER, "$", "end-of-stack marking", currentSymbol.getLine()));
	}

	public static boolean openParenthesesAtrib() {

		// verifica se a expressão contém parenteses
		if (currentSymbol.getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			balance.push(currentSymbol);// empilha o parenteses
			nextToken(); // avança para o próximo token
			return true;
		}
		return false;
	}

	public static boolean closeParenthesesAtrib() {
		// verifica se após ler algum ID ou valor ele fecha paranteses , exempo
		// (a) ..
		if (currentSymbol.getLexeme().equals(")") && balance.peek().getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			balance.pop();// desempilha o parenteses
			nextToken(); // avança para o próximo token
			return true;
		}
		return false;
	}


	public static void kind() {

		if (currentSymbol.getKind().equals(INT) ||
				currentSymbol.getKind().equals(CHAR) ||
				currentSymbol.getKind().equals(FLOAT)) {

			System.out.print(currentSymbol.getLexeme()+" ");
			nextToken();

			//exemplo adaptado para testar as expreões aritméticas
			//pretendo usar a expressão dentro de declaration
			if (currentSymbol.getKind().equals(ID)) {
				System.out.print(currentSymbol.getLexeme()+" ");
				nextToken();
				if(Parser.currentSymbol.getLexeme().equals("=")) {
					declaration();
					nextToken();
				}
			} else {
				System.out.println("Erro sintático -> Linha " + currentSymbol.getLine());
			}
		}

	}

	public static void attribuition(){

		if(currentSymbol.getKind().equals(ID)) {

			System.out.print(currentSymbol.getLexeme()+" ");
			nextToken();

		   if(currentSymbol.getLexeme().equals("=")) {
			   System.out.print(currentSymbol.getLexeme() + " ");
			   nextToken();
			   openParenthesesAtrib();

			   if (currentSymbol.getKind().equals(ID) ||
					   currentSymbol.getKind().equals(FLOAT) ||
					   currentSymbol.getKind().equals(INT)) {

				   System.out.print(currentSymbol.getLexeme() + " ");
				   nextToken();
				   closeParenthesesAtrib();

				   if (currentSymbol.getKind().equals(OP_ARITHM)) {
					   System.out.print(currentSymbol.getLexeme() + " ");
					   nextToken();
					   attribuition();

				   } else {

					   if (currentSymbol.getLexeme().equals(";")){
					   	   nextToken();
					   	   return;

					   } else {
						   System.out.println("\nSyntax error line -> " + currentSymbol.getLine() +
								   "\n cause by: "
								   + currentSymbol.getLexeme() + "\n expected: ; or Op arithm");
						   return;
					   }
				   }
			   }else {
				   System.out.println("\nSyntax error line -> " + currentSymbol.getLine() +
						   "\n cause by: "
						   + currentSymbol.getLexeme() + "\n expected: Identifier");
				   return;
			   }
		   }else {
			   System.out.println("\nSyntax error line -> " + currentSymbol.getLine() +
					   "\n cause by: "
					   + currentSymbol.getLexeme() + "\n expected: ( or id");
			   return;
		   }
		}

	}


	public static void declaration() {

		//falta achar uma forma de tratar o caso quando não tem o ; no final
   
		if (currentSymbol.getLexeme().equals(",")){

			System.out.print(currentSymbol.getLexeme()+" ");
			nextToken();
			if (currentSymbol.getKind().equals(ID)) {
				System.out.print(currentSymbol.getLexeme()+" ");
				nextToken();
				if(currentSymbol.getLexeme().equals("=")) {
					System.out.print(currentSymbol.getLexeme()+" ");
				    nextToken();
					expression();
					if(currentSymbol.getLexeme().equals(","))
						declaration();
				}else
				    declaration();

			}else {

				System.out.println("Syntax error line -> " + currentSymbol.getLine() +
						"\ncause by: "+ currentSymbol.getLexeme());
				         return;
			} 
		}else{

			if (currentSymbol.getLexeme().equals(";")){
				//nextToken();
				//kind();
				return;
			}else
				if(currentSymbol.getLexeme().equals("=")) {

					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();
					expression();

					if (currentSymbol.getLexeme().equals(",")) {
						declaration();
					} else
						if (currentSymbol.getLexeme().equals(";")) {
						   //nextToken();
							return;
						}
                    else
					if(currentSymbol.getKind().equals(ID) ||
							currentSymbol.getKind().equals(INT) ||
							currentSymbol.getKind().equals(FLOAT) ||
							currentSymbol.getKind().equals(LETTER)) {

						nextToken();
						declaration();

					}else{
						System.out.println("Declaration error:\nSyntax error line -> " + currentSymbol.getLine() +
								"\ncause by: "+ currentSymbol.getLexeme());
						return;
					}

				}else System.out.println("erroooooo");
		}
	}

}
