package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.INT;
import static br.ufc.compiler.lexicon.Token.Kind.MAIN;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.GrammarIf.*;
import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.parse.GrammarKind.*;

public class GrammarMain {

	static {
		InitParser();
	}

	// algumas coiasas incompletas
	public static void expressionMain() {

		if (currentSymbol.getKind().equals(INT)) {
			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();

			if (currentSymbol.getKind().equals(MAIN)) {
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();

				if (currentSymbol.getLexeme().equals("(")) {
					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();

					if (currentSymbol.getLexeme().equals(")")) {
						System.out.print(currentSymbol.getLexeme() + " ");
						nextToken();

						if (currentSymbol.getLexeme().equals("{")) {
							System.out.println(currentSymbol.getLexeme() + " ");
							nextToken();
						
							kind();
							attribuition();
							commandIf();

							if (currentSymbol.getLexeme().equals("}")) {
								System.out.println(currentSymbol.getLexeme() + " ");
							}
							
						}
					}
				}
			}

		}

	}
}
