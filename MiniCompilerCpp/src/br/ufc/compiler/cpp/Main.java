package br.ufc.compiler.cpp;

import java.io.IOException;

import br.ufc.compiler.exception.CommentException;
import br.ufc.compiler.lexicon.LexiconAnalyzer;
import br.ufc.compiler.lexicon.Token;

public class Main {

	public static void main(String[] args) throws IOException, CommentException {
		
     LexiconAnalyzer ln = new LexiconAnalyzer();
    
     ln.builderSymbolTable("C:\\Users\\Michael\\Desktop\\test.cpp");
     
     for(Token t: ln.getSymbolTable())
    	 System.out.print(t);
    	
	}
	
}
