package br.ufc.compiler.parse;

import br.ufc.compiler.lexicon.Token.Kind;


/**/
public class GrammarKind {

	static void kind(){
		
		if(Parser.currentSymbol.equals(Kind.INT)){
			Parser.nextToken();
			Parser.currentToken();
		
			
			if(Parser.currentSymbol.equals(Kind.ID)){
				   Parser.nextToken();
				   Parser.currentToken();
				if(Parser.currentSymbol.getLexeme().equals(",")){
					
				}
				
			}
		
			//int id;
			//int id,id,id;
			//int id = exp;
		}
		
		
		
	}
	
	
}
