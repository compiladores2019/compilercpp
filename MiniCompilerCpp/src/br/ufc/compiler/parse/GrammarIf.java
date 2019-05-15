package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.ELSE;
import static br.ufc.compiler.lexicon.Token.Kind.IF;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.Parser.size;
import static br.ufc.compiler.parse.Parser.getPosition;

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
			nextToken();

			if (currentSymbol.getLexeme().equals("(")) {
				System.out.println(currentSymbol.getLexeme() + " ");
				nextToken();

				// expressionForIf();
				if (currentSymbol.getLexeme().equals(")")) {
					System.out.println(currentSymbol.getLexeme() + " ");
					nextToken();

					if (currentSymbol.getLexeme().equals("{")) {
						System.out.println(currentSymbol.getLexeme() + " ");
						nextToken();

						balanceKey.push(new Token(Kind.DEL, "{", "closing-key-to-this-line", currentSymbol.getLine()));

						// declaration()?
						// Attbibr()?
						// if?

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

									// declaration()?
									// Attbibr()?
									// if?
									if (currentSymbol.getKind().equals(IF))
										ifError();

									if (currentSymbol.getLexeme().equals("}")) {
										nextToken();
										balanceKey.pop();

									} else {
										System.out.println("Chave else  fecha!");

									}
								} else {
									System.out.println("chave else fecha");
								}
							}
						} else {
							System.out.println("Chave if fecha!");
						}
					} else {
						System.out.println("Chave if abre!");
					}
				} else {

					System.out.println("parenteses if fecha!");
				}
			} else {
				System.out.println("parenteses  if abre!");
			}
		}

		return balanceKey.isEmpty();
	}
}