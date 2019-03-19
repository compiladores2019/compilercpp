package br.ufc.compiler.lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class LexiconAnalyzer {

	private LinkedHashSet<Token> hm = new LinkedHashSet<>();
	private StringBuilder sb = new StringBuilder();
	private SymbolConsumer sc = new SymbolConsumer(hm);

	protected boolean commentActivate = false;
	
	@SuppressWarnings("resource")
	public void builderSymbolTable(String path) throws IOException {

		File file = new File(path);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		int j = 0;
		
		if (!file.exists()) {
			throw new IOException("File not exists!");
		}

		while (br.ready()) {
			String readLine = br.readLine();
			collectLines(readLine, ++j);
		}

		fr.close();
		br.close();
	}

	private void collectLines(String line, int row) {

		String ch="";

		for (int i = 0; i < line.length(); i++) {

			char c = line.charAt(i);
			ch = String.valueOf(c);
			
			if (!Character.isSpaceChar(c) && !commentActivate){

				if(ch.matches("/"))
					i = sc.treatmentComment(this, c, line, i, row);
				else
				if (Util.isOpLogic(ch)) {		
					i = sc.treatmentRelational(c, line, i, row);
					verifyLexeme(row);
					sb.setLength(0);
					
				}else if (Util.isOpArithm(ch)) {
		
					sc.treatmentArithms(c, row);
		            verifyLexeme(row);
					sb.setLength(0);
				}else if (Util.isDelimiter(ch)) {

					sc.treatmentDelimiter(c, row);
					verifyLexeme(row);
					sb.setLength(0);
				}
				
			if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '.')
				sb.append(c);
	   
		 }else if(!commentActivate) 
			      verifyLexeme(row);
			   
			   if(commentActivate)
			   i = sc.treatmentComment(this, c, line, i, row);
		}
		  if(!commentActivate)
		    verifyLexeme(row);   
		  
		 
	}

	private void verifyLexeme(int row) {
		
		 if(Util.isReservedWord(sb.toString()))
		    	sc.treatmentRW(sb, row);
		    else if(Util.isModifier(sb.toString()))
		    	sc.treamentmentModifier(sb, row);
		    else
			if(Util.isIdentifier(sb.toString()))
			    sc.treatmentIndetifier(sb, row);
			else 
				sc.treatmentNumbers(sb, row);	
	}
	
	@Override
	public String toString() {
		return "LexiconAnalyzer [hm= " + hm + "]";
	}

}