package br.ufc.compiler.cpp;

import java.io.IOException;
import br.ufc.compiler.lexicon.LexiconAnalyzer;
import br.ufc.compiler.lexicon.Token;

public class Main {

	public static void main(String[] args) throws IOException {
		
     LexiconAnalyzer ln = new LexiconAnalyzer();
    
     ln.builderSymbolTable("/home/rafael/Documents/test.txt");
     
     for(Token t: ln.getSymbolTable())
    	 System.out.print(t);
    	
     
	}

}
