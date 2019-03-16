package br.ufc.compiler.cpp;

import java.io.IOException;

import br.ufc.compiler.lexicon.LexiconAnalyzer;
import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Util;

public class Main {

	public static void main(String[] args) throws IOException {
		
     LexiconAnalyzer ln = new LexiconAnalyzer();
     
     ln.builderSymbolTable("/home/rafael/Documents/test.txt");
     
     System.out.println(ln.toString());
     
	}

}
