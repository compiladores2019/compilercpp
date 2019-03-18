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
			j++;
			String readLine = br.readLine();
			collectLines(readLine, j);
		}

		fr.close();
		br.close();
	}

	private void collectLines(String line, int row) {
	
		String ch;
		
		for (int i = 0; i < line.length(); i++) {

			 char c = line.charAt(i);
			 	 
		   if (!Character.isWhitespace(c) || !Character.isSpaceChar(c)){
			   
			   ch = String.valueOf(c);
			   
			    if(Util.isOpLogic(ch)) 
		            i = sc.treatmentRelational(c,line, i, row);
			    else if(Util.isOpArithm(ch)) 
					sc.treatmentArithms(c, row);
				else if(Util.isDelimiter(ch)) 
					sc.treatmentDelimiter(c, row);
					   
				if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '.') {
					sb.append(c);	
					
				}else {
					sc.treatmentNumbers(sb, row);
					sb.setLength(0);
				}
				  
			}else{
				  sc.treatmentNumbers(sb, row);
				  sb.setLength(0);
			}
		}
	}
	
	@Override
	public String toString() {
		return "LexiconAnalyzer [hm= " + hm + "]";
	}

}
