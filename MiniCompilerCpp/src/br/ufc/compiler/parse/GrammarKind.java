package br.ufc.compiler.parse;

import br.ufc.compiler.lexicon.Token.Kind;


public class GrammarKind {

	static {
		Parser.InitParser();
	}

	/* Reconhece declarocoes do tipo: int v,xd=f,a=9; */

	public static void kind() {

		if (Parser.currentSymbol.getKind().equals(Kind.INT) ||
				Parser.currentSymbol.getKind().equals(Kind.CHAR) ||
				Parser.currentSymbol.getKind().equals(Kind.FLOAT)) {

			Parser.nextToken();

			//exemplo adaptado para testar as expreões aritméticas
			//pretendo usar a expressão dentro de declaration
			if (Parser.currentSymbol.getKind().equals(Kind.ID)) {

				Parser.nextToken();
				if(Parser.currentSymbol.getLexeme().equals("="))
					Parser.nextToken();
				GrammarExpressions.expressionArithms();
				//declaration();

			} else {
				System.out.println("Erro sintático -> Linha " + Parser.currentSymbol.getLine());
			}
		}

	}

	private static void declaration() {

		if (Parser.currentSymbol.getLexeme().equals(",")){

			Parser.nextToken();

			if (Parser.currentSymbol.getKind().equals(Kind.ID)) {

				Parser.nextToken();
				declaration();
			}else {
				System.out.println("Syntax error line -> " + Parser.currentSymbol.getLine() +
						"\ncause by: "+ Parser.currentSymbol.getLexeme());
				return;
			}
		}else{
			if (Parser.currentSymbol.getLexeme().equals(";")) return;

			else {

				if(Parser.currentSymbol.getLexeme().equals("=")) {
					Parser.nextToken();

					if(Parser.currentSymbol.getKind().equals(Kind.ID)  ||
							Parser.currentSymbol.getKind().equals(Kind.INT) ||
							Parser.currentSymbol.getKind().equals(Kind.FLOAT) ||
							Parser.currentSymbol.getKind().equals(Kind.LETTER)){

						Parser.nextToken();
						declaration();

					}else{
						System.out.println("Syntax error line -> " + Parser.currentSymbol.getLine() +
								"\ncause by: "+ Parser.currentSymbol.getLexeme());
						return;
					}

				}
			}

		}
	}


	private static void nextID() {
		Parser.nextToken();

		if (Parser.currentSymbol.getLexeme().equals(";")) {
			System.out.println(Parser.currentSymbol);
			return;
		}

		System.out.println(Parser.currentSymbol.getLexeme());

		if (Parser.currentSymbol.getLexeme().equals(",") || Parser.currentSymbol.getLexeme().equals("=")) {

			nextID();
		}

		else if (Parser.currentSymbol.getKind().equals(Kind.ID) || Parser.currentSymbol.getLexeme().equals(Kind.INT)) {
			nextID();
		}

		else
			System.out.println("Erro sitatico!s");

	}

}
