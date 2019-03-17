package br.ufc.compiler.lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

import br.ufc.compiler.lexicon.Token.Kind;

public class LexiconAnalyzer {

	private LinkedHashSet<Token> hm = new LinkedHashSet<>();

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

		StringBuilder sb = new StringBuilder();
        String ch; 
		for (int i = 0; i < line.length(); i++) {

			char c = line.charAt(i);
            
            
		   if (!Character.isWhitespace(c) || !Character.isSpaceChar(c)) {
			 
			    ch = String.valueOf(c);
			   
			    if(ch.matches("[<|>|=|!]")){
			    	//consertar esse método, ele não está capturando os relacionais
			      treatmentRelational(line, 0, row);
			    }else
				if(Util.isOpArithm(ch)) 
					treatmentArithms(c, row);
				else
				if(Util.isDelimiter(ch)) 
					treatmentDelimiter(c, row);
				else
				if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '.') {
					sb.append(c);	
				}else {
					treatmentNumbers(sb, row);
					sb.setLength(0);
				}
				  
			}else {
				  treatmentNumbers(sb, row);
				  sb.setLength(0);
			}
		}
	}

	private void treatmentDelimiter(char c, int line) {

		switch (c) {

		case '(':
			hm.add(new Token(Kind.DEL, "PARLFT", line));
			break;
		case ')':
			hm.add(new Token(Kind.DEL, "PARRGH", line));
			break;
		case '{':
			hm.add(new Token(Kind.DEL, "KEYLFT", line));
			break;
		case '}':
			hm.add(new Token(Kind.DEL, "KEYRGH", line));
			break;
		case '[':
			hm.add(new Token(Kind.DEL, "BKTLFT", line));
			break;
		case ']':
			hm.add(new Token(Kind.DEL, "BKTRGH", line));
			break;
		case ';':
			hm.add(new Token(Kind.DEL, "COMMA", line));
			break;	
		case '@':
			hm.add(new Token(Kind.OTHER, "SMB_ESP", line));
			break;
		case '#':
			hm.add(new Token(Kind.OTHER, "SMB_ESP", line));
			break;
		case '$':
			hm.add(new Token(Kind.OTHER, "SMB_ESP", line));
			break;
		default:
			
		}

	}

	private void treatmentArithms(char c, int line) {

		switch (c) {

		case '+':
			hm.add(new Token(Kind.OP_ARITHM, "SUM_OP", line));
			break;
		case '-':
			hm.add(new Token(Kind.OP_ARITHM, "SUB_OP", line));
			break;
		case '/':
			hm.add(new Token(Kind.OP_ARITHM, "DIV_OP", line));
			break;
		case '*':
			hm.add(new Token(Kind.OP_ARITHM, "MULT_OP", line));
			break;
		default:

		}

	}

	private void treatmentRelational(String str,int i, int row) {
		
		if((i + 1) < str.length()) {
			
			 if (str.equals(">") && str.charAt(i + 1) == '=') hm.add(new Token(Kind.OP_REL, ">=", row));
	            else 
	        	  if (str.equals(">"))
					hm.add(new Token(Kind.OP_REL, ">", row));
	          else
			     if (str.equals("=") && str.charAt(i + 1) == '=') hm.add(new Token(Kind.OP_REL, "==", row));
				else 
				  if (str.equals("="))
					 hm.add(new Token(Kind.OTHER, "ATRIB", row));
			 else
	            if (str.equals("<") && str.charAt(i + 1) == '=') hm.add(new Token(Kind.OP_REL, "<=", row));
			 else 
				 if (str.equals("<"))
					hm.add(new Token(Kind.OP_REL, "<", row));
			else
				 if (str.equals("!") && str.charAt(i + 1) == '=') 
					 hm.add(new Token(Kind.OP_REL, "!=", row));	
			else 
				 if (str.equals("!")) 
					 hm.add(new Token(Kind.OP_REL, "!", row));	
			
		}else {
			 if (str.equals(">"))
					hm.add(new Token(Kind.OP_REL, ">", row));
			 else
			 if (str.equals("="))
				 hm.add(new Token(Kind.OTHER, "ATRIB", row));
			 else
			 if (str.equals("<"))
					hm.add(new Token(Kind.OP_REL, "<", row));
			 else
			 if (str.equals("!")) 
				 hm.add(new Token(Kind.OP_REL, "!", row));
		}
		
	}

	private void treatmentNumbers(StringBuilder sb, int row) {

		if (Util.isNumberFloat(sb.toString()))
			hm.add(new Token(Kind.FLOAT, Double.parseDouble(sb.toString()), "FLOAT", row));
		else if (Util.isNumberInteger(sb.toString()))
			hm.add(new Token(Kind.INT, Integer.parseInt(sb.toString()), "INT", row));	
	}

	@Override
	public String toString() {
		return "LexiconAnalyzer [hm= " + hm + "]";
	}

}
