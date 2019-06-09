package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.lexicon.Token.Kind;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;

import static br.ufc.compiler.parse.GrammarIf.*;
import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.parse.GrammarKind.*;

public class GrammarMain {

	static {
		InitParser(); // aloca a tabela de simbolos para análise sintática/semântica
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
					
					        controlMain();
					       
							if (currentSymbol.getLexeme().equals("}")) {
								System.out.println(currentSymbol.getLexeme() + " ");
							}else {
								throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						                                     + currentSymbol.getLexeme() + "\n expected: }");
							}
							
						}else {
							throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                                    + currentSymbol.getLexeme() + "\n expected: {");
						}
					}else {
						throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                                + currentSymbol.getLexeme() + "\n expected: )");
					}
				}else {
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                            + currentSymbol.getLexeme() + "\n expected: (");
				}
			}else {
				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                        + currentSymbol.getLexeme() + "\n expected: main");
			}

		}else {
			throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                    + currentSymbol.getLexeme() + "\n expected: int");
		}

	}
	
	public static void controlMain() {
		
		Kind k = currentSymbol.getKind();
		
			switch(k) {	
			
			case IF:{

				commandIf();
				k = currentSymbol.getKind();
				controlMain();
				
				break;
				
			}
			case ID:{
		
				attribuition();		
				nextToken();
				k = currentSymbol.getKind();
				controlMain();
				break;
				
			}
			case INT:{
				
				kind();
				k = currentSymbol.getKind();
				controlMain();
				break;
				
			}
			case FLOAT	:{
		
				kind();
				k = currentSymbol.getKind();
				controlMain();
				break;
				
			}
			case CHAR:{
				
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
