package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.parse.GrammarArithm.*;
import static br.ufc.compiler.parse.GrammarLogic.*;
import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarRelational {

	private static Stack<Token> balanceParentheses = new Stack<>();

	static {

		balanceParentheses.push(new Token(OTHER, "$", "end-of-stack marking", null, currentSymbol.getLine()));

	}

	private static boolean openParentheses() {

		// verifica se a expressão contém parenteses
		if (currentSymbol.getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			balanceParentheses.push(currentSymbol);// empilha o parenteses
			nextToken(); // avança para o próximo token
			return true;
		}
		return false;
	}

	private static boolean closeParentheses() {
		// verifica se após ler algum ID ou valor ele fecha paranteses , exempo
		// (a) ..
		if (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			balanceParentheses.pop();// desempilha o parenteses
			nextToken(); // avança para o próximo token
			return true;
		}
		return false;
	}

	public static void opRel() {

		if (currentSymbol.getKind().equals(OP_REL)) {
			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();
			openParentheses();

			if (currentSymbol.getKind().equals(ID) || currentSymbol.getKind().equals(INT)
					|| currentSymbol.getKind().equals(FLOAT)) {
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				closeParentheses();

				if (currentSymbol.getLexeme().equals("==") || currentSymbol.getKind().equals(OP_LOG)) {
					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();
					openParentheses();

					if (currentSymbol.getKind().equals(ID) || currentSymbol.getKind().equals(INT)
							|| currentSymbol.getKind().equals(FLOAT)) {
						System.out.print(currentSymbol.getLexeme() + " ");
						nextToken();
						opRel();
					}

				} else {

					if (currentSymbol.getLexeme().equals(";")) {
						System.out.print(currentSymbol.getLexeme() + " ");
						nextToken();
						return;
					} else if (currentSymbol.getLexeme().equals(","))
						return;
					else {

						throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
								+ currentSymbol.getLexeme() + "\n expected: ==, op logic , or ;");

					}
				}
			}

		} else {

			throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
					+ currentSymbol.getLexeme() + "\n expected: op relational");

		}
	}

	// operacao relacional inserindo um parâmentro para análise semântica
	public static void opRelIf(Kind k) {

		if (currentSymbol.getKind().equals(OP_REL)) {

			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();
			openParentheses();

			if ((currentSymbol.getKind().equals(INT) && currentSymbol.getKind().equals(k))
					|| (currentSymbol.getKind().equals(FLOAT) && currentSymbol.getKind().equals(k))
					|| (currentSymbol.getKind().equals(ID) && currentSymbol.getIdKind().equals(k))) {

				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				closeParentheses();
				// podemos ler uma soma exemplo: id < id + id
				// e quem sabe podemos ler um operador lógico
				// se sim, a funçao expArithmAfterRel() ja retorna a partir do operador lógico
				if (currentSymbol.getKind().equals(OP_ARITHM)) {
					expArithmAfterRel(k);
					closeParentheses();
				}

				// se for operador lógico mesmo sem ler uma exp com soma ex: id < id
				// e ler um operador lógico, ok, chama o metodo abaixo para tratar
				boolean isRead = false;
				if (currentSymbol.getKind().equals(OP_LOG)) {
					opLogIf(k);
					isRead = true;
				}

				// se ler algum simbolo relacional
				// se eu não ler operador lógico então isRead fica falso, negado fica true então
				// emite o erro.
				if (currentSymbol.getKind().equals(OP_REL) && !isRead) {

					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: op logic");

				}

				// como podemos não ler nenhum dos acima, e ser apenas id < id.
				if (balanceParentheses.peek().getLexeme().equals("$")) return;
				else {
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: ')'");
				}
			} else {

				if (!currentSymbol.getKind().equals(k) || !currentSymbol.getIdKind().equals(k)) {

					throw new RuntimeException("\nSemantic error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: Kind " + k);

				} else {

					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: identifier ");

				}
			}
		}

	}

}
