package br.ufc.compiler.lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;


import br.ufc.compiler.lexicon.Token.Kind;

public class LexiconAnalyzer {

	private LinkedHashSet<Token> hm = new LinkedHashSet<>();
	private StringBuilder sb = new StringBuilder();
	private SymbolConsumer sc = new SymbolConsumer(hm);

	private int commentRow = 0;
	protected boolean commentActivated = false;
	
    //O(2n) = O(n)
	@SuppressWarnings("resource")
	public void builderSymbolTable(String path) throws IOException {

		File file = new File(path);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		int j = 0;
		if (!file.exists()) {
			throw new IOException("File not exists!");
		}

		// O(n)
		while (br.ready()) {
			String readLine = br.readLine();
			collectLines(readLine, ++j);
			
		}

		fr.close();
		br.close();
		
		if(commentActivated)
			System.out.println("Comment on line " + commentRow + " not closed!");
			
			
	}

    //O(n)
	private void collectLines(String line, int row) {

		String ch = null;

		for (int i = 0; i < line.length(); i++) {

			char c = line.charAt(i);

			if (!Character.isSpaceChar(c) && !commentActivated) {
				  
				ch = String.valueOf(c);
				
				if (ch.matches("/") || ch.matches("[*]")){
					i = sc.treatmentComment(this, c, line, i, row);
					this.commentRow = row;
				}
				else if (Util.isOpLogic(ch)) {
					verifyLexeme(row);
					i = sc.treatmentRelational(c, line, i, row);
				}else if (Util.isOpArithm(ch)){
					 verifyLexeme(row);
					sc.treatmentArithms(c, row);
				} else if (Util.isDelimiter(ch)) {
					 verifyLexeme(row);
					 sc.treatmentDelimiter(c, row);
				}else if(Util.isOther(ch)) 
				   hm.add(new Token(Kind.OTHER,ch,"OTHER",row));	 
				else			
				if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '.')
					sb.append(c);
				else
					 verifyLexeme(row);
				
			} else if (!commentActivated)
				verifyLexeme(row);
			else if (commentActivated)
				i = sc.treatmentComment(this, c, line, i, row);
			
		}
		if (!commentActivated)
			verifyLexeme(row);
	}

	private void verifyLexeme(int row) {
		
		String lexeme = sb.toString();
		 
		 if(Util.isReservedWord(lexeme)) 
		    	sc.treatmentRW(sb, row);	    	
		 else if(Util.isModifier(lexeme)) 
		    	sc.treamentmentModifier(sb, row);
		 else if(Util.isIdentifier(lexeme)) 
			   sc.treatmentIndetifier(sb, row);
		 else  
			 sc.treatmentNumbers(sb, row);
		 
		 sb.setLength(0);
	}
	
	public LinkedHashSet<Token> getSymbolTable(){return this.hm;}
	
	@Override
	public String toString() {
		return "LexiconAnalyzer [hm= " + hm + "]";
	}
}