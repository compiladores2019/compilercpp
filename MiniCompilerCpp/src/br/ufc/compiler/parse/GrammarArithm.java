package br.ufc.compiler.parse;

import static br.ufc.compiler.lexicon.Token.Kind.*;
import static br.ufc.compiler.parse.Parser.currentSymbol;
import static br.ufc.compiler.parse.Parser.nextToken;
import static br.ufc.compiler.parse.Parser.*;
import java.util.Stack;

import br.ufc.compiler.lexicon.Token;

public class GrammarArithm {

	
	private static final int EXP = 1; //esse modificador indica que na limpeza de pareteses qual método chamar
	private static final int EXPIF = 0; //esse modificador indica que na limpeza de pareteses qual método chamar
	
	private static Stack<Token> balanceParentheses = new Stack<>();
	private static int numberArgs = 0; 
	
	static {
		
		//InitParser();
		balanceParentheses.push(new Token(OTHER, "$", "end-of-stack marking", null,currentSymbol.getLine()));

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
	
	
	public static void expArithm() {
		
		openParentheses();
		
		if(currentSymbol.getKind().equals(INT) || 
		   currentSymbol.getKind().equals(FLOAT) ||
		   currentSymbol.getKind().equals(ID)) {
			
			System.out.print(currentSymbol.getLexeme() + " ");
			numberArgs++;
			nextToken();
			closeParentheses();
			
			if(currentSymbol.getLexeme().matches("[,|;]")) {
				System.out.print(currentSymbol.getLexeme() + " ");
				if(balanceParentheses.peek().getLexeme().equals("$")) return;
				else {
					
					 System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
							+ currentSymbol.getLexeme() + "\n expected: ')'");
					 return;
				}
			}else {
				
				if(currentSymbol.getKind().equals(OP_ARITHM)) {
					System.out.print(currentSymbol.getLexeme() + " ");
					nextToken();
					openParentheses();
					expArithm();
					
				}else {
					cleanParentheses(1);
				}
				
			}
			
		}else {
			
			//verifico se existe algum identificador na expressão
			//exemplo a = 2; para evitar aceitar isso a = ;
			
			if(numberArgs > 0) {
				
				//se na recursão ele não ler um id, pode ser que leia , ou ; e alem disso
				// preciso verificar a pilha de parenteses, mas caso ele não leia , ou ;, pode ser parenteses aninhados,
				// para isso chamo a função cleanParenthenses que verifica tudo, até o caso de ler tudo e estar OK e já 
				// encadear outra operação aritmética em sequência
				if(currentSymbol.getLexeme().matches("[,|;]")) {
					
					if(balanceParentheses.peek().getLexeme().equals("$")) return;
								
				}else {
	
					cleanParentheses(1);
				}
				
			}else {
				
				 System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
							+ currentSymbol.getLexeme() + "\n expected: identifier ");
					 return;
			}
		}
		
	}
	

	//expressao aritmietica pro if para ser usada antes de relacionais:
	 //exemplo id (+,-,/,*) id .. (<|>|>=|<=)
    public static void expArithmBeforeRel() {

		if(currentSymbol.getKind().equals(OP_ARITHM)) {
			
			System.out.print(currentSymbol.getLexeme() + " ");
			nextToken();
			openParentheses();
		
			
			if(currentSymbol.getKind().equals(INT) || 
		       currentSymbol.getKind().equals(FLOAT) ||
		       currentSymbol.getKind().equals(ID)) {
				
				numberArgs++;
				System.out.print(currentSymbol.getLexeme() + " ");
				nextToken();
				expArithmBeforeRel();
				
				closeParentheses();
				
			}else {
				 System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
							          + currentSymbol.getLexeme() + "\n expected: identifier ");
					 return;
			}
		
		}else{
			
			
			
			if(currentSymbol.getKind().equals(INT) || 
		       currentSymbol.getKind().equals(FLOAT) ||
		       currentSymbol.getKind().equals(ID) || numberArgs == 0) {
				
				System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
				          + currentSymbol.getLexeme() + "\n expected: op artithm");
		        return;
			
			}else {
			
				if(currentSymbol.getKind().equals(OP_REL) && numberArgs > 0) {
					
					if(balanceParentheses.peek().getLexeme().equals("$")) {
						///System.out.print(currentSymbol.getLexeme() + " ");
						//relOP();
						return;
					}else {
						
						System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
						          + currentSymbol.getLexeme() + "\n expected: ')'");
				        return;
						
					}
					
				}else{
					
					if(currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")) {
						
						cleanParentheses(0);
						
						if(currentSymbol.getKind().equals(OP_ARITHM)) {
							System.out.print(currentSymbol.getLexeme() + " ");
							expArithmBeforeRel();
						}
						
						if(currentSymbol.getKind().equals(OP_REL)) {
							//System.out.print(currentSymbol.getLexeme() + " ");
							//Op_rel();
						}
						
						
					}
					
				}
				
			}		
		}
			
	}
	
    
    //nessa expressão temos de verificar se estamos lendo a expressão aritmética após o relacional,
    //exemplo ... id < id + id .. o proximo simbolo pode ser nenhum, pode ser a continuação da soma ou apenas um operador lógico.
    //por isso separo em duas expressoes diferentes, para tratar casos particulares
    public static void expArithmAfterRel() {
    	
    	if(currentSymbol.getKind().equals(OP_ARITHM)) {
    		
    		System.out.print(currentSymbol.getLexeme() + " ");
    		nextToken();
    		openParentheses();
    		
    		if(currentSymbol.getKind().equals(INT) || 
		       currentSymbol.getKind().equals(FLOAT) ||
		       currentSymbol.getKind().equals(ID)) {
    		
    			numberArgs++;
    			System.out.print(currentSymbol.getLexeme() + " ");
    			
    			nextToken();
    			
    			expArithmAfterRel();
    			
    			closeParentheses();
    		}
    		
    	}else{
    		
    		if(numberArgs > 0) {
    			
    			if(currentSymbol.getKind().equals(OP_REL)) {
    				
    				System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
					          + currentSymbol.getLexeme() + "\n expected: op  Arithm or op logic");
    		
    				return;
    				
    			}else {
    				
    				if(currentSymbol.getKind().equals(OP_LOG)) {
    					numberArgs = 0;
    					return;
    				}
    				
    			}
    			
    		}else {
    			
    			System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n caused by: "
				          + currentSymbol.getLexeme() + "\n expected: identifier");
		
				return;
    			
    		}
    		
    	}
    	
    }
    
	
    private static void cleanParentheses(int op) {
    	
    	while(currentSymbol.getLexeme().equals(")") && balanceParentheses.peek().getLexeme().equals("(")){
    		
    		if(closeParentheses()) continue;
    		else
    			nextToken();	
    	}
    	
    	if(balanceParentheses.peek().getLexeme().equals("$")) return;	
    	else {
    		
    		 System.out.println("\nSyntax error line -> " + currentSymbol.getLine() + "\n cause by: "
						+ currentSymbol.getLexeme() + "\n expected: ')'");
				 return;
    	}
    	
    }
    
}
