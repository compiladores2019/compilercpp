package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.GrammarRelational.*;
import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarArithm {

	private static Stack<Token> balanceParentheses = new Stack<>();
	private static int numberArgs = 0;

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

	// expressao aritmietica pro if para ser usada antes de relacionais:
	// exemplo id (+,-,/,*) id .. (<|>|>=|<=)
	// passando um novo parâmetro para análise semântica
	public static void expArithmBeforeRel(Kind k) {

		if (currentSymbol.getKind().equals(OP_ARITHM)) {

			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();
			openParentheses();

			if ((currentSymbol.getKind().equals(INT) && currentSymbol.getKind().equals(k)) ||
			    (currentSymbol.getKind().equals(FLOAT) &&  currentSymbol.getKind().equals(k))|| 
				(currentSymbol.getKind().equals(ID) && currentSymbol.getIdKind().equals(k))) {

				numberArgs++;
				System.out.print(currentSymbol.getLexeme() + "  ");
				nextToken();
				expArithmBeforeRel(k);

				closeParentheses();

			} else {
				
				if(!currentSymbol.getKind().equals(k) || !currentSymbol.getIdKind().equals(k)) {
					
					throw new RuntimeException("\nSemantic error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: Kind "+k);
			
				}else {
					
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: identifier ");
			
				}
			}

		} else {

			if (currentSymbol.getKind().equals(INT) || currentSymbol.getKind().equals(FLOAT)
					|| currentSymbol.getKind().equals(ID) || numberArgs == 0) {

				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						+ currentSymbol.getLexeme() + "\n expected: op artithm");
			

			} else {

				if (currentSymbol.getKind().equals(OP_REL) && numberArgs > 0) {

					if (balanceParentheses.peek().getLexeme().equals("$")) {
						
						opRelIf(k);
						return;
						
					} else {

						throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
								+ currentSymbol.getLexeme() + "\n expected: ')'");
			
					}

				} else {

					if (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {

						   cleanParentheses();

						if (currentSymbol.getKind().equals(OP_ARITHM)) {
							System.out.print(currentSymbol.getLexeme() + " ");
							expArithmBeforeRel(k);
						}

						if (currentSymbol.getKind().equals(OP_REL)) {
							opRelIf(k);
						}

					}

				}

			}
		}

	}

	// nessa expressão temos de verificar se estamos lendo a expressão aritmética
	// após o relacional,
	// exemplo ... id < id + id .. o proximo simbolo pode ser nenhum, pode ser a
	// continuação da soma ou apenas um operador lógico.
	// por isso separo em duas expressoes diferentes, para tratar casos particulares
	//adicionado o parâmetro de tipo para analise semântica
	public static void expArithmAfterRel(Kind k) {

		
		
		if (currentSymbol.getKind().equals(OP_ARITHM)) {

			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();
			openParentheses();

			if ((currentSymbol.getKind().equals(INT) && currentSymbol.getKind().equals(k)) ||
				(currentSymbol.getKind().equals(FLOAT) &&  currentSymbol.getKind().equals(k))|| 
			    (currentSymbol.getKind().equals(ID) && currentSymbol.getIdKind().equals(k))) {

				numberArgs++;
				System.out.print(currentSymbol.getLexeme() + " ");

				nextToken();

				expArithmAfterRel(k);

				closeParentheses();
			}else {
				
				if(!currentSymbol.getKind().equals(k) || !currentSymbol.getIdKind().equals(k)) {
					
					throw new RuntimeException("\nSemantic error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: Kind "+k);
				
				}else {
					
				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						+ currentSymbol.getLexeme() + "\n expected: identifier ");
			
				}
				
			}

		} else {

			if (numberArgs > 0) {

				if (currentSymbol.getKind().equals(OP_REL)) {

					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: op  Arithm or op logic");
					
		
				} else {

					if (currentSymbol.getKind().equals(OP_LOG)) {
						numberArgs = 0;
						return;
					}else {
						//não leu nada, apenas parenteses
						cleanParentheses();
						if(balanceParentheses.peek().getLexeme().equals("$")) {
							return;
						}
						
					}

				}

			} else {

				
				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						+ currentSymbol.getLexeme() + "\n expected: identifier");
		
			}

		}

	}

	private static void cleanParentheses() {

		while (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {

			if (closeParentheses())
				continue;
			else
				nextToken();
		}

		if (balanceParentheses.peek().getLexeme().equals("$"))
			return;
		
		else {

			throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
					+ currentSymbol.getLexeme() + "\n expected: ')'");
	
		}

	}

}
