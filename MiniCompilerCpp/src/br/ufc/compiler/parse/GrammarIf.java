package br.ufc.compiler.parse;

import static br.ufc.compiler.parse.Parser.*;
import static br.ufc.compiler.lexicon.Token.Kind.*;

public class GrammarIf {

	public static void commandIf(){
		
      if(currentSymbol.getKind().equals(IF)){
    	  System.out.println(currentSymbol.getLexeme() + " ");
    	   nextToken();
           if(currentSymbol.getLexeme().equals("("))
           {
        	   System.out.println(currentSymbol.getLexeme() + " ");
        	   nextToken();
        	   //expressionForIf();
        	
        	   if(currentSymbol.getLexeme().equals(")")){
        		   System.out.println(currentSymbol.getLexeme() + " ");
        		   nextToken();
        		   if(currentSymbol.getLexeme().equals("{")){
        			   System.out.println(currentSymbol.getLexeme() + " ");
        			  nextToken();
        			   //declaration()?
        			   //Attbibr()?
        			   //if?
        			   
        			  if(currentSymbol.getLexeme().equals("}"))
        			  {
        				  System.out.println(currentSymbol.getLexeme() + " ");
        				  nextToken();
        				  
        				  if(currentSymbol.getKind().equals(ELSE)){
        					  System.out.println(currentSymbol.getLexeme() + " ");
        					   nextToken();
        					  if(currentSymbol.getLexeme().equals("{")){
        						  System.out.println(currentSymbol.getLexeme() + " ");
        	        			  nextToken();
        	        			  
        	        			   if(currentSymbol.getKind().equals(IF))
        	        				   commandIf();
        	        			   
        	        			       //declaration()?
        	        			       //Attbibr()?
        	        			       //if?
        	        			   
        	        			   if(currentSymbol.getLexeme().equals("}")){
        	        				   System.out.println(currentSymbol.getLexeme() + " ");
        	        				   nextToken();
        	        				   //return;
        	        			   }else{
        	        				   System.out.println("Chave else fecha!");
        	        				   return;
        	        			   }
        					  }
        				  }
        				  
        		
        			  }else{
        				  System.out.println("Chave if fecha!");
        			  }
        		   }else{
        			   System.out.println("Chave if abre!");
        		   }
        		   
 		   
        	   }else{
        		   
        		   System.out.println("parenteses if fecha!");
        	   }
           }else{
        	   System.out.println("parenteses if abre!");
           }
        	  
        	   
    	  
    	  
      }
		
	}

}
