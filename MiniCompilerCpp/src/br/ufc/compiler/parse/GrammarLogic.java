package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.parse.GrammarArithm.*;
import static br.ufc.compiler.parse.GrammarRelational.*;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind;

public class GrammarLogic {

private static Stack<Token> balanceParentheses = new Stack<>();
	
	static {

		balanceParentheses.push(new Token(OTHER, "$", "end-of-stack marking",null, currentSymbol.getLine()));

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
	
	public static void opLog() {

		if (currentSymbol.getKind().equals(OP_LOG)) {

			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();
			openParentheses();

			if (currentSymbol.getKind().equals(ID)) {
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				closeParentheses();

				opLog(); // chama novamente o procedimento

				if (currentSymbol.getLexeme().equals(";") || currentSymbol.getLexeme().equals(",")) {
					return;
				}

			} else {
				
				throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
						+ currentSymbol.getLexeme() + "\n expected: identifier or , or ;");
			
			}

		} else {

			if (currentSymbol.getLexeme().equals(";")) {
				System.out.print(currentSymbol.getLexeme() + " ");
				return;
				
			} else if (currentSymbol.getLexeme().equals(","))
				return;
			
			else {
				if (currentSymbol.getLexeme().equals(")"))
					return;
				else {
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
							+ currentSymbol.getLexeme() + "\n expected: op logic");
				
				}
			}
		}

	}

	//operaccao logica inserindo um novo parâmetro para anáslie semântica
	public static void opLogIf(Kind k) {

	  if(currentSymbol.getKind().equals(OP_LOG)) {
		  
		  System.out.print(currentSymbol.getLexeme() + " ");
		  nextToken();
		  openParentheses();
		  
		  if((currentSymbol.getKind().equals(INT) && currentSymbol.getKind().equals(k)) ||
		     (currentSymbol.getKind().equals(FLOAT) &&  currentSymbol.getKind().equals(k))|| 
		     (currentSymbol.getKind().equals(ID) && currentSymbol.getIdKind().equals(k))) {
		  
			  closeParentheses();
			  
			  if(currentSymbol.getKind().equals(INT)||
		         currentSymbol.getKind().equals(FLOAT)) {
				  
				  System.out.print(currentSymbol.getLexeme() + " ");
				  nextToken();
				  
				  
				  while(currentSymbol.getKind().equals(OP_ARITHM)) {
					  expArithmBeforeRel(k);
					  closeParentheses();
				  }
			 
				
				  if(currentSymbol.getKind().equals(OP_REL)){ 	 
					   opRelIf(k);  
					   closeParentheses();
					   
				  if(currentSymbol.getKind().equals(OP_LOG)) { 
					  opLogIf(k);
					  closeParentheses();
				  }
				
				  
				  }else{
					  //erro? 
					  throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
								+ currentSymbol.getLexeme() + "\n expected: op relational after INT or FLOAT");
					
				  }
				  
			  }else {
				  
				    //é um id
				    System.out.print(currentSymbol.getLexeme() + " ");
				    nextToken();
				    closeParentheses();
			    
				    while(currentSymbol.getKind().equals(OP_ARITHM)) {
						  expArithmBeforeRel(k);
						  closeParentheses();
					  }
				    
				    if(currentSymbol.getKind().equals(OP_REL)){ 	 
						   opRelIf(k); 
						   closeParentheses();
				    }
				    
				    if(currentSymbol.getKind().equals(OP_LOG)) {
				    	opLogIf(k);
				    	closeParentheses();
				    	
				    }else{
				    	
				    	if(balanceParentheses.peek().getLexeme().equals("$")) return;
				    	else {
				    		
				    		throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
									+ currentSymbol.getLexeme() + "\n expected: ')'");
				    	}	
				    }
			  }

		  }else {
			  
				if(!currentSymbol.getKind().equals(k) || !currentSymbol.getIdKind().equals(k)) {
					
					throw new RuntimeException("\nSemantic error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: Kind "+k);
				
				}else {
					
					throw new RuntimeException("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							+ currentSymbol.getLexeme() + "\n expected: identifier ");
		
				}
	
		  }
	  }
		
	}
}
