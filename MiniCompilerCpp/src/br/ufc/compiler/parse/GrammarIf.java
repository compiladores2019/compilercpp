package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.GrammarExpressions.*;
import static br.ufc.compiler.parse.Parser.*;
import java.util.Stack;

import br.ufc.compiler.lexicon.Token;

public class GrammarIf {

	private static Stack<Token> balanceKey = new Stack<>();

	static {
		//InitParser();
		balanceKey.push(new Token(OTHER,"$","end-marking-stack",null,currentSymbol.getLine()));
		
	}
	
	public static void commandIf() {
		ifError();
	}


	private static void openKey() {
		
		if(currentSymbol.getLexeme().equals("{")) {
			System.out.println(currentSymbol.getLexeme() + " ");
			balanceKey.push(currentSymbol);
			nextToken();
		}
		
	}
	
      private static boolean closeKey() {
    	  
    	  if(currentSymbol.getLexeme().equals("}") && balanceKey.peek().getLexeme().equals("{")) {
    		  System.out.print(currentSymbol.getLexeme() + " ");
    		  balanceKey.pop();
    		  nextToken();
    		  return true;
    	  }
      
    	  return false;
	}
	
	public static void ifError() {

		if (currentSymbol.getKind().equals(IF)) {
			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();

			if (currentSymbol.getLexeme().equals("(")) {
				 System.out.print(currentSymbol.getLexeme() + " ");
				 nextToken();
				 expressionIf();
			     
				if (currentSymbol.getLexeme().equals(")")) {
					
					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();

					if (currentSymbol.getLexeme().equals("{")) {
						 
						openKey();
						
						//System.out.println();
					    GrammarKind.kind();
					      
					       if(currentSymbol.getKind().equals(IF)) 
					    	     ifError(); 
					  
					       
					else {
					    
					    	 //para caso não ler else logo de cara, vai que tem if(){ if(){ }} else..
					    	 parenthesesRemove();
						    
						    if(currentSymbol.getKind().equals(ELSE)) {
						    	System.out.print(currentSymbol.getLexeme() + " ");
						    	nextToken();
						      if(currentSymbol.getLexeme().equals("{")) {
						    	  
						    	  openKey();
						    	  System.out.println();
						    	  
						    	  if(currentSymbol.getKind().equals(IF)) {
						    		  ifError();
						    		  //nextToken();
						    	     //comand
						    	    //atribs
						    	   //other
						    	  }
						    	  
						    	  if(currentSymbol.getLexeme().equals("}")) {
						    		  ///System.out.print("ao menos entra aqui?");
						    		  parenthesesRemove();
						    	  }
						    	  
						      }
						      
						    }
						   
					     }
					
					}					
				}
			}
		}
		if(balanceKey.peek().getLexeme().equals("$")) return;
		else {
			previousToken();
			System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
					+ currentSymbol.getLexeme() + "\n expected: missing '}' block key");
			return;
		}
    }
	
	private static void parenthesesRemove() {
	
		   while(currentSymbol.getLexeme().equals("}") && balanceKey.peek().getLexeme().equals("{")) {
			    
		    	if(closeKey()) continue;
		    	else 
		    		nextToken();     
		    }  
	}
}