package br.ufc.compiler.parse;

import java.util.Stack;


import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

import static br.ufc.compiler.lexicon.Token.Kind.*;

import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.parse.GrammarKind.*;
import static br.ufc.compiler.parse.GrammarRelational.*;
import static br.ufc.compiler.parse.GrammarLogic.*;
import static br.ufc.compiler.parse.GrammarArithm.*;


public class GrammarExpressions {

	private static Stack<Token> balanceParentheses = new Stack<>();
	private static Stack<Token> balanceParenthesesIf = new Stack<>();

	
	
	static {


		balanceParentheses.push(new Token(OTHER, "$", "end-of-stack marking", null,currentSymbol.getLine()));
		balanceParenthesesIf.push(new Token(OTHER, "$", "end-of-stack marking",null, currentSymbol.getLine()));

	}

	// limpa a pilha de uma expressão finalizada
	static void clearStackParentheses() {
		
		if(!balanceParentheses.empty()) {
		balanceParentheses.clear();
		}
	}

	// limpa a pilha de uma expressão if finalizada
	static void clearStackParenthesesIf() {
		
		if(!balanceParenthesesIf.empty()) {
		   balanceParenthesesIf.clear();
		}
	}

	public static boolean openParentheses() {

		// verifica se a expressão contém parenteses
		if (currentSymbol.getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			balanceParentheses.push(currentSymbol);// empilha o parenteses
			nextToken(); // avança para o próximo token
			return true;
		}
		return false;
	}

	public static boolean closeParentheses() {
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

	public static boolean openParenthesesIf() {

		// verifica se a expressão contém parenteses
		if (currentSymbol.getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			balanceParenthesesIf.push(currentSymbol);// empilha o parenteses
			nextToken(); // avança para o próximo token
			return true;
		}
		return false;
	}

	public static boolean closeParenthesesIf() {
		// verifica se após ler algum ID ou valor ele fecha paranteses , exempo
		// (a) ..
		if (currentSymbol.getLexeme().equals(")") && balanceParenthesesIf.peek().getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			balanceParenthesesIf.pop();// desempilha o parenteses
			nextToken(); // avança para o próximo token
			return true;
		}
		return false;
	}

	private static void validateExpression() {

		while (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
			System.out.print(currentSymbol.getLexeme() + " ");
			if (closeParentheses())
				continue;
			else
				nextToken();
		}

		if (currentSymbol.getLexeme().equals(";")) {
			System.out.print(currentSymbol.getLexeme() + " ");

			if (balanceParentheses.peek().getLexeme().equals("$")) {
				nextToken();
				return;
			} else {

				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
						+ currentSymbol.getLexeme() + "\n expected: ')'");
				
			}
		} else if (currentSymbol.getLexeme().equals(",")) {
			declaration();// leu vírgula, então é uma declaração? esperamos que smim
		}
	}

	public static void validateExpressionIf() {

		while (currentSymbol.getLexeme().equals(")") && balanceParenthesesIf.peek().getLexeme().equals("(")) {
			System.out.println(currentSymbol.getLexeme() + " ");
			if (closeParenthesesIf())
				continue;
			else
				nextToken();
		}

		if (currentSymbol.getLexeme().equals(")")) {
			if (balanceParenthesesIf.peek().getLexeme().equals("$")) {
				// se a pilha estiver o cifrão então ok,o proximo ) será do if
				return;
			} else {
				
				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
						+ currentSymbol.getLexeme() + "\n expected: ')'");
				
			}

		} else {

			if (currentSymbol.getKind().equals(OP_REL) || currentSymbol.getKind().equals(OP_LOG)) {
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				expressionIf();
				// verifico se não tem algum parenteses sem fechar, se Ok e o proximo simbolo
				// não for ) pq? pq seria o parenteses de final if, como não é, ele vai ver se
				// tem outra expressão if
				// mas para isso ele precisa ler algum relacional ou operador lógico
			}
		}

	}

	//adicionando novo parâmetro para análise semântica 
	public static void expression(Kind k) {

		if(k == null) {
	
			throw new RuntimeException("\nSemantic error line -> " + currentSymbol.getLine() + "\n caused by: "
					+ currentSymbol.getLexeme() + "\n expected: identifier no was declared : "+k);
		}
			
		openParentheses();

		if ((currentSymbol.getKind().equals(INT) && currentSymbol.getKind().equals(k)) ||
		    (currentSymbol.getKind().equals(FLOAT) &&  currentSymbol.getKind().equals(k))|| 
		    (currentSymbol.getKind().equals(ID) && currentSymbol.getIdKind().equals(k))) {

			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();

			closeParentheses();

			if (currentSymbol.getKind().equals(OP_ARITHM)) {
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();

				openParentheses();

				expression(k);

			} else {

				if (currentSymbol.getLexeme().matches("[,|;]")) {
						
					if (currentSymbol.getLexeme().equals(";") && balanceParentheses.peek().getLexeme().equals("$")) {
						System.out.println(currentSymbol.getLexeme() + " ");
	                     
						return;
						
					} else {

						if (currentSymbol.getLexeme().equals(",")
								&& balanceParentheses.peek().getLexeme().equals("$")) {
							
							return;

						} else {
						
							throw new RuntimeException("Syntax error line -> " + currentSymbol.getLine()
							     + "\n cause by: missing ')' before " + currentSymbol.getLexeme());
						}
					}
				} else

				// apos todos os parenteses aninhados podemos ter outra operacao
				// ex: (id + (a + id)) + (id..
				if (currentSymbol.getKind().equals(OP_ARITHM)) {
					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();
					expression(k);

				} else if (currentSymbol.getLexeme().equals(")")) {
                           
					while (currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
						if (closeParentheses())
							continue;
						else
							nextToken();
					}

					if (currentSymbol.getLexeme().matches("[,|;]") && balanceParentheses.peek().getLexeme().equals("$")) {
						
						if (currentSymbol.getLexeme().equals(";")) {
							System.out.println(currentSymbol.getLexeme() + " ");
				
							 nextToken();
							return;
						} else
							return;
					} else {

						if (currentSymbol.getKind().equals(OP_ARITHM)) {
							System.out.print(currentSymbol.getLexeme() + " ");
							nextToken();
							expression(k);

						} else {

							if (currentSymbol.getKind().equals(OP_LOG)) {

								// previousToken(); // para começar de id &&
								opLog();
								validateExpression();

							} else {

								throw new RuntimeException("Syntax error line -> " + currentSymbol.getLine()
								+ "\n cause by: missing operator arithmetic before "
								+ currentSymbol.getLexeme());
							
							}
						}
					}
				} else {

					if (currentSymbol.getKind().equals(OP_REL)) {

						opRel();
						expression(k);

					} else {

						if (currentSymbol.getKind().equals(OP_LOG)) {
							System.out.print(currentSymbol.getLexeme() + " ");
							opLog();
							validateExpression();

						} else {
							
							throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
									+ currentSymbol.getLexeme() + "\n expected: , or ; or op arithm");
									
						}
					}
				}
			}
		}else {
			
			throw new RuntimeException("\nSemantic error line -> " + currentSymbol.getLine() + "\n caused by: "
									+ currentSymbol.getLexeme() + "\n expected kind: "+k);

		}

	}

	public static void expressionIf() {

		
		if(currentSymbol.getKind().equals(FLOAT) ||
		   currentSymbol.getKind().equals(INT) ||
		   currentSymbol.getKind().equals(ID)) {
			
			 System.out.print(currentSymbol.getLexeme() + " ");

			Kind idKind = currentSymbol.getIdKind();
		    nextToken();
			controlExpressionIf(idKind,currentSymbol.getKind());
		
		}
		
	}
	
	private static void controlExpressionIf(Kind kindId,Kind op) {
		
	
		switch(op) {
		
		case OP_LOG:{
			
			opLogIf(kindId);
	
			controlExpressionIf(kindId,currentSymbol.getKind());
			break;
		}
		case OP_REL:{

			opRelIf(kindId);
			controlExpressionIf(kindId,currentSymbol.getKind());
			break;
		}
		
		case OP_ARITHM:{
		
			expArithmBeforeRel(kindId);
			controlExpressionIf(kindId,currentSymbol.getKind());
			
			break;
		}
			
		default:
			break;
		
		
		}
		
	}
	
}
