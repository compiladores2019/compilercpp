package br.ufc.compiler.parse;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token.Kind;
import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.GrammarExpressions.*;
import static br.ufc.compiler.parse.Parser.*;
import br.ufc.compiler.lexicon.Token;

public class GrammarKind {

	private static Stack<Token> balance = new Stack<>();

	private static Kind k;

	static {

		balance.push(new Token(OTHER, "$", "end-of-stack marking", null, currentSymbol.getLine()));

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
		// verifica se após ler algum ID ou valor ele fecha paranteses ,
		// exemplo
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

		if (currentSymbol.getKind().equals(INT) || currentSymbol.getKind().equals(CHAR)
				|| currentSymbol.getKind().equals(FLOAT)) {

			k = currentSymbol.getKind();

			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();

			if (currentSymbol.getKind().equals(ID)) {

				if (!GrammarMain.scopeElement.isEmpty()
						&& GrammarMain.scopeElement.peek().contains(currentSymbol.getLexeme()))
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine()
							+ "\ncaused by an existing element: " + currentSymbol.getIdKind());
				else
					GrammarMain.scopeElement.peek().add(currentSymbol.getLexeme());

				System.out.print(currentSymbol.getLexeme() + " ");

				nextToken();

				if (currentSymbol.getLexeme().equals("=")) {

					declaration();

				} else {

					if (currentSymbol.getLexeme().matches("[,|;]")) {
						declaration();
					}
				}

			} else {

				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						+ currentSymbol.getLexeme() + "\n expected: " + k);

			}
		}

	}

	public static void attribuition() {

		if (currentSymbol.getKind().equals(ID)) {

			k = currentSymbol.getIdKind();
			System.out.print(currentSymbol.getLexeme() + " ");

			nextToken();

			if (currentSymbol.getLexeme().equals("=")) {
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				expression(k);

			} else {

				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						+ currentSymbol.getLexeme() + "\n expected: =");

			}

		}

	}

	public static void declaration() {

		if (currentSymbol.getLexeme().equals(",")) {

			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();
			if (currentSymbol.getKind().equals(ID)) {

				if (!GrammarMain.scopeElement.isEmpty()
						&& GrammarMain.scopeElement.peek().contains(currentSymbol.getLexeme()))
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine()
							+ "\ncaused by an existing element: " + currentSymbol.getLexeme() + "\n expected: " + k);
				else
					GrammarMain.scopeElement.peek().add(currentSymbol.getLexeme());

				k = currentSymbol.getIdKind();

				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();

				if (currentSymbol.getLexeme().equals("=")) {

					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();
					expression(k);
					nextToken();

					if (currentSymbol.getLexeme().equals(","))
						declaration();
				} else
					declaration();

			} else {

				throw new RuntimeException(
						"Syntax error line -> " + currentSymbol.getLine() + "\ncause by: " + currentSymbol.getLexeme());

			}
		} else {

			if (currentSymbol.getLexeme().equals(";")) {
				System.out.print(currentSymbol.getLexeme() + " ");
				k = null;
				nextToken();
				return;
			} else if (currentSymbol.getLexeme().equals("=")) {

				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				expression(k);

				if (currentSymbol.getLexeme().equals(",")) {
					declaration();
				} else if (currentSymbol.getLexeme().equals(";")) {

					k = null;
					nextToken();
					return;

				} else if (currentSymbol.getKind().equals(ID) || currentSymbol.getKind().equals(INT)
						|| currentSymbol.getKind().equals(FLOAT) || currentSymbol.getKind().equals(LETTER)) {

					nextToken();
					declaration();

				} else {

					throw new RuntimeException("Declaration error:\nSyntax error line -> " + currentSymbol.getLine()
							+ "\ncause by: " + currentSymbol.getLexeme());

				}

			} else
				throw new RuntimeException("Declaration error:\nSyntax error line -> " + currentSymbol.getLine()
						+ "\ncause by: " + currentSymbol.getLexeme());
		}
	}

}
