package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.INT;
import static br.ufc.compiler.lexicon.Token.Kind.MAIN;
import static br.ufc.compiler.parse.GrammarIf.commandIf;
import static br.ufc.compiler.parse.GrammarKind.attribuition;
import static br.ufc.compiler.parse.GrammarKind.kind;
import static br.ufc.compiler.parse.Parser.InitParser;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarMain {

	public static Stack<List<String>> scopeElement = new Stack<List<String>>();

	static {

		InitParser(); // aloca a tabela de simbolos para análise
						// sintática/semântica
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

							scopeElement.push(new ArrayList<String>());
							controlMain();

							if (currentSymbol.getLexeme().equals("}")) {
								System.out.println(currentSymbol.getLexeme() + " ");
							} else {
								throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine()
										+ "\n caused by: " + currentSymbol.getLexeme() + "\n expected: }");
							}

						} else {
							throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine()
									+ "\n caused by: " + currentSymbol.getLexeme() + "\n expected: {");
						}
					} else {
						throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine()
								+ "\n caused by: " + currentSymbol.getLexeme() + "\n expected: )");
					}
				} else {
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: (");
				}
			} else {
				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						+ currentSymbol.getLexeme() + "\n expected: main");
			}

		} else {
			throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
					+ currentSymbol.getLexeme() + "\n expected: int");
		}

	}

	public static void controlMain() {

		Kind k = currentSymbol.getKind();

		switch (k) {

		case IF: {

			commandIf();
			k = currentSymbol.getKind();
			controlMain();

			break;

		}
		case ID: {

			attribuition();
			nextToken();
			k = currentSymbol.getKind();

			controlMain();

			break;

		}
		case INT: {

			kind();
			k = currentSymbol.getKind();
			controlMain();
			break;

		}
		case FLOAT: {

			kind();
			k = currentSymbol.getKind();
			controlMain();
			break;

		}
		case CHAR: {

			kind();
			k = currentSymbol.getKind();
			controlMain();
			break;
		}

		default:
			break;

		}

	}

}
