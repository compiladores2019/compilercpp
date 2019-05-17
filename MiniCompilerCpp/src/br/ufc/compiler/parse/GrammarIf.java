package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.ELSE;
import static br.ufc.compiler.lexicon.Token.Kind.IF;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.GrammarExpressions.*;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarIf {

	private static Stack<Token> balanceKey = new Stack<>();

	public static void commandIf() {
		if (!ifError())
			System.out.println(" error");

	}

	private static boolean ifError() {

		if (currentSymbol.getKind().equals(IF)) {
			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();

			if (currentSymbol.getLexeme().equals("(")) {
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				expressionIf();
				//nextToken();

				if (currentSymbol.getLexeme().equals(")")) {
					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();

					if (currentSymbol.getLexeme().equals("{")) {
						System.out.print(currentSymbol.getLexeme() + " ");
						nextToken();
						balanceKey.push(new Token(Kind.DEL, "{", "closing-key-to-this-line", currentSymbol.getLine()));

						GrammarKind.kind();

						if (currentSymbol.getKind().equals(IF))
							ifError();

						if (currentSymbol.getLexeme().equals("}")) {
							System.out.println(currentSymbol.getLexeme() + " ");
							balanceKey.pop();
							nextToken();

							if (currentSymbol.getKind().equals(ELSE)) {
								System.out.println(currentSymbol.getLexeme() + " ");
								nextToken();

								if (currentSymbol.getLexeme().equals("{")) {
									System.out.println(currentSymbol.getLexeme() + " ");
									nextToken();

									balanceKey.push(new Token(Kind.DEL, "{", "closing-key-to-this-line",
											currentSymbol.getLine()));



									if (currentSymbol.getKind().equals(IF))
										ifError();

									if (currentSymbol.getLexeme().equals("}") && balanceKey.peek().getLexeme().equals("{")) {
										nextToken();
										balanceKey.pop();
									}
								}
							}
						}
					}
				}
			}
		}

		return balanceKey.isEmpty();
	}
}