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
		
		balanceKey.push(new Token(OTHER,"$","end-marking-stack",null,currentSymbol.getLine()));
		
	}
	
	public static void commandIf() {
		ifCommand();
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
    		  System.out.println(currentSymbol.getLexeme() + " ");
    		  balanceKey.pop();
    		  nextToken();
    		  return true;
    	  }
      
    	  return false;
	}
	
      
    //para a regra do if, é obrigatorio o uso do else
	public static void ifCommand() {

		if (currentSymbol.getKind().equals(IF)) {
			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();

			if (currentSymbol.getLexeme().equals("(")) {
				 System.out.print(currentSymbol.getLexeme() + " ");
				 nextToken();
				 expressionIf();
				 
				if (currentSymbol.getLexeme().equals(")")) {
					
					System.out.print(currentSymbol.getLexeme() + "  ");
					nextToken();

					if (currentSymbol.getLexeme().equals("{")) {
				
						openKey();
			
						GrammarMain.controlMain();
					     
					       if(currentSymbol.getKind().equals(IF)) 
					    	   ifCommand(); 
					  
					       
					else {
					    
					    	 //para caso não ler else logo de cara, vai que tem if(){ if(){ }} else..
					    	 parenthesesRemove();
						    
						    if(currentSymbol.getKind().equals(ELSE)) {
						    	System.out.print(currentSymbol.getLexeme() + " ");
						    	nextToken();
						      if(currentSymbol.getLexeme().equals("{")) {
						    	  
						    	  openKey();
						    	  GrammarMain.controlMain();
						    	  
						    	  if(currentSymbol.getKind().equals(IF)) {
						    		  ifCommand();
						              
						    	  }
						    	  
						    	  if(currentSymbol.getLexeme().equals("}")) {
						  
						    		  parenthesesRemove();
						    	  }else {
						    		  throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
					                            + currentSymbol.getLexeme() + "\n expected: }");
						    	  }
						    	  
						      }else throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
			                            + currentSymbol.getLexeme() + "\n expected: {");
						      
						    }else 
						    	throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
			                            + currentSymbol.getLexeme() + "\n expected: else");			   
					   }
					       
					}else
						throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                            + currentSymbol.getLexeme() + "\n expected: {");
					
				}else 
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                        + currentSymbol.getLexeme() + "\n expected: )");
			}else {
				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
                        + currentSymbol.getLexeme() + "\n expected: (");
			}
		}
		if(balanceKey.peek().getLexeme().equals("$")) return;
		else {
			previousToken();
			
			throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
					+ currentSymbol.getLexeme() + "\n expected: missing '}' block key");
	
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