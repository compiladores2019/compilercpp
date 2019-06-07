package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.Parser.InitParser;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.parse.GrammarArithm.*;
import static br.ufc.compiler.parse.GrammarRelational.*;

import java.util.Stack;

import br.ufc.compiler.lexicon.Token;

public class GrammarLogic {

private static Stack<Token> balanceParentheses = new Stack<>();
	
	static {
	
		//InitParser();
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
				System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
						+ currentSymbol.getLexeme() + "\n expected: identifier or , or ;");
				return;
			}

		} else {

			if (currentSymbol.getLexeme().equals(";")) {
				System.out.print(currentSymbol.getLexeme() + " ");
				// nextToken();
				return;
			} else if (currentSymbol.getLexeme().equals(","))
				return;
			else {
				if (currentSymbol.getLexeme().equals(")"))
					return;
				else {
					System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
							+ currentSymbol.getLexeme() + "\n expected: op logic");
					return;
				}
			}
		}

	}

	public static void opLogIf() {

	  if(currentSymbol.getKind().equals(OP_LOG)) {
		  
		  System.out.print(currentSymbol.getLexeme() + " ");
		  nextToken();
		  openParentheses();
		  
		  if(currentSymbol.getKind().equals(ID) || 
		     currentSymbol.getKind().equals(INT)||
		     currentSymbol.getKind().equals(FLOAT)) {
		  
			  closeParentheses();
			  
			  if(currentSymbol.getKind().equals(INT)||
		         currentSymbol.getKind().equals(FLOAT)) {
				  
				  System.out.print(currentSymbol.getLexeme() + " ");
				  nextToken();
				  
				  
				  while(currentSymbol.getKind().equals(OP_ARITHM)) {
					  expArithmBeforeRel();
					  closeParentheses();
				  }
			 
				
				  if(currentSymbol.getKind().equals(OP_REL)){ 	 
					   opRelIf();  
					   closeParentheses();
					   
				  if(currentSymbol.getKind().equals(OP_LOG)) { 
					  opLogIf();
					  closeParentheses();
				  }
				
				  
				  }else{
					  //erro? 
						System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
								+ currentSymbol.getLexeme() + "\n expected: op relational after INT or FLOAT");
						return;
				  }
				  
			  }else {
				  
				    //é um id
				    System.out.print(currentSymbol.getLexeme() + " ");
				    nextToken();
				    closeParentheses();
			    
				    while(currentSymbol.getKind().equals(OP_ARITHM)) {
						  expArithmBeforeRel();
						  closeParentheses();
					  }
				    
				    if(currentSymbol.getKind().equals(OP_REL)){ 	 
						   opRelIf(); 
						   closeParentheses();
				    }
				    
				    if(currentSymbol.getKind().equals(OP_LOG)) {
				    	opLogIf();
				    	closeParentheses();
				    	
				    }else{
				    	
				    	if(balanceParentheses.peek().getLexeme().equals("$")) return;
				    	else {
				    		System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
									+ currentSymbol.getLexeme() + "\n expected: ')'");
							return;
				    	}	
				    }
			  }

		  }else {
			  System.out.println("erorooo");
	
		  }
	  }
		
	}
}
