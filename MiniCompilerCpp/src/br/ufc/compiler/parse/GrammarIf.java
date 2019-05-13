package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.ELSE;
import static br.ufc.compiler.lexicon.Token.Kind.IF;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarIf {

	private static Stack<Token> balanceKey = new Stack<>();

	public static void commandIf() {

		if (currentSymbol.getKind().equals(IF)) {
			System.out.println(currentSymbol.getLexeme() + " ");
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
						// declaration()?
						// Attbibr()?
						// if?

						if (currentSymbol.getLexeme().equals("}")) {
							System.out.println(currentSymbol.getLexeme() + " ");
							nextToken();

							if (currentSymbol.getKind().equals(ELSE)) {
								System.out.println(currentSymbol.getLexeme() + " ");
								nextToken();
								if (currentSymbol.getLexeme().equals("{")) {
									System.out.println(currentSymbol.getLexeme() + " ");
									nextToken();

									if (currentSymbol.getKind().equals(IF)) {

										balanceKey.push(new Token(Kind.DEL, "}", "closing-key-to-this-line",
												currentSymbol.getLine()));

										commandIf();
										
										while (!balanceKey.isEmpty() && currentSymbol.getLexeme().equals("}")) {
											balanceKey.pop();
											nextToken();

										}
										
										
									}

									// declaration()?
									// Attbibr()?
									// if?

									if (currentSymbol.getLexeme().equals("}") && !balanceKey.isEmpty()) {

										nextToken();
										
										if (currentSymbol.getLexeme().equals("}"))System.out.println("erro");
										
										return;
											
									} else {
										System.out.println("Chave else fecha!");
										return;
									}
								}

								else {
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

	}

}
