package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.INT;
import static br.ufc.compiler.lexicon.Token.Kind.MAIN;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;

public class GrammarMain {

	public static void expressionMain() {

		if (currentSymbol.getKind().equals(MAIN)) {
			nextToken();

			if (currentSymbol.getLexeme().equals("(")) {
				nextToken();

				if (currentSymbol.getLexeme().equals(")")) {
					nextToken();

					if (currentSymbol.getLexeme().equals("{")) {
						nextToken();

						/*
						 * 
						 * 
						 * todas as outras expressões aqui.
						 * 
						 *
						 */

						if (currentSymbol.getLexeme().equals("}"))
							return;

						else {
							System.out.println("Falta fechar chave");
						}
					}

					else {
						System.out.println("Falta abrir a chave");
					}

				} else {
					System.out.println("Falta fechar parenteses");
				}
			} else {
				System.out.println("Falta o abre parenteses");
			}
		} else {
			System.out.println("Falta o main");
		}

	}

}
