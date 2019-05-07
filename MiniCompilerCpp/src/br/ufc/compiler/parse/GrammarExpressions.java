package br.ufc.compiler.parse;

import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarExpressions {


	//Expressoes Aritméticas ainda não terminada
	public static void expressionArithms() {
		
		boolean parOpen = false; // marca abertura de parenteses
		if(Parser.currentSymbol.getLexeme().equals("(")) { 
			parOpen = true;
		    Parser.nextToken();
		}
		
		if( Parser.currentSymbol.getKind().equals(Kind.ID) ||
			Parser.currentSymbol.getKind().equals(Kind.INT) ||
			Parser.currentSymbol.getKind().equals(Kind.FLOAT)) {
			
			Parser.nextToken();
		
			if(Parser.currentSymbol.getKind().equals(Kind.OP_ARITHM)) {
				
				Parser.nextToken();
				expressionArithms();
				if(Parser.currentSymbol.getLexeme().equals(")")) 
					parOpen = false;
				
			}else {
				System.out.println("Syntax error line " + Parser.currentSymbol.getLine() + 
						      "\ncause by: "+ Parser.currentSymbol.getLexeme() + "\nexpected symbol: [ + , - , / , * ]");
			    return;
			}
			
			//if(Parser.currentSymbol.getLexeme().equals(")")) 
				//parOpen = false;
			
			if(parOpen) 
				System.out.println("Syntax error line "+Parser.currentSymbol.getLine() + "- expected symbol" + ")");
		
		}
	
		
	}
	
}
