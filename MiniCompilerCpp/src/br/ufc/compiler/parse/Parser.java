package br.ufc.compiler.parse;

import java.util.ArrayList;
import java.util.List;
import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.lexicon.Token.Kind.*;
import br.ufc.compiler.lexicon.LexiconAnalyzer;

import static br.ufc.compiler.lexicon.Token.Kind.OTHER;
import static br.ufc.compiler.parse.Parser.currentSymbol;

public class Parser {

	public static Token currentSymbol;

	private static List<Token> array;
	private static int position = 0;

	public static void destroy() {
		array.clear();
		position = 0;
	}

	public static void InitParser() {

		array = new ArrayList<Token>();
		array.addAll(LexiconAnalyzer.getSymbolTable());
		array.add(new Token(OTHER,"$","end-marking-stack",null,0));
		currentSymbol = array.get(position);
		position++;

	}

	public static void nextToken() {

	
		if (position < array.size()) {
			currentSymbol = array.get(position);
			position++;
		}
	}

	public static void previousToken() {
		position -= 1;
		currentSymbol = array.get(position);

	}

	public static int size() {
		return array.size();
	}

	public static int getPosition() {
		return position;
	}


}
