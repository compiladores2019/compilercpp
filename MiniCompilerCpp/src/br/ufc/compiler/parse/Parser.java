package br.ufc.compiler.parse;

import java.util.ArrayList;
import java.util.List;
import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.LexiconAnalyzer;

public class Parser {

	protected static Token currentSymbol;
	
	public static List<Token> array; 
	public static int position = 0;
	
	public Parser(){
		Parser.array = new ArrayList<Token>();
        array.addAll(LexiconAnalyzer.getSymbolTable());
	}
	
	public static void nextToken(){
		if(position < array.size())
			position++;
	}
	
	public static Token currentToken(){
		currentSymbol = array.get(position);
		return currentSymbol;

	}
	
	
	//void paser(){
		
	//}

}
