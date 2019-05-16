package br.ufc.compiler.parse;

import br.ufc.compiler.lexicon.Token.Kind;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.GrammarExpressions.*;
import static br.ufc.compiler.parse.Parser.*;

public class GrammarKind {

	static {
		InitParser();
	}

	/* Reconhece declarocoes do tipo: int v,xd=f,a=9; */

	public static void kind() {

		/*
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
					//nextToken();
					//GrammarExpressions.expressionArithms();
					declaration();
				}



			} else {
				System.out.println("Erro sintático -> Linha " + currentSymbol.getLine());
			}
		}
		*/

		expressionIf();

		//if(currentSymbol.getKind().equals(IF))
		//	 GrammarIf.commandIf();
		

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
				nextToken();
				kind();
				return;
			}
				if(currentSymbol.getLexeme().equals("=")) {

					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();
					expression();

					if (currentSymbol.getLexeme().equals(",")) {
						declaration();
					} else
						if (currentSymbol.getLexeme().equals(";"))
						return;
                    else
					if(currentSymbol.getKind().equals(ID) ||
							currentSymbol.getKind().equals(INT) ||
							currentSymbol.getKind().equals(FLOAT) ||
							currentSymbol.getKind().equals(LETTER)){
 
						//nextToken();
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
