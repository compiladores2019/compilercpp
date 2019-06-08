package br.ufc.compiler.lexicon;

import static br.ufc.compiler.lexicon.Token.Kind.CHAR;
import static br.ufc.compiler.lexicon.Token.Kind.FLOAT;
import static br.ufc.compiler.lexicon.Token.Kind.ID;
import static br.ufc.compiler.lexicon.Token.Kind.INT;
import static br.ufc.compiler.lexicon.Token.Kind.OTHER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import br.ufc.compiler.lexicon.Token.Kind;

public class LexiconAnalyzer {

	private static Set <Token> hm = new LinkedHashSet<>();
	private static Map<String,Kind> idents = new HashMap<>(); 
	private StringBuilder sb = new StringBuilder();
	private StringBuilder textBuilder = new StringBuilder();
	private SymbolConsumer sc = new SymbolConsumer(hm);

	private int commentRow = -1;
	private int stringUnboundedRow = -1;
	protected boolean commentActivated = false;
	private boolean isText = false;
	private boolean isAlpha, isNumber, isUnderScore, isDot, isApostrophe = false;

	// O(2n) = O(n)
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

		if (commentActivated)
			System.out.println("Comment on line " + commentRow + " not closed!");

		if (isText)
			System.out.println("string couldn't find end of text in line: " + stringUnboundedRow);
	
		if(!commentActivated && !isText) {
		     setKindIdentifiers();
		}
		
	}

	private void collectLines(String line, int row) {

		String ch = null;

		for (int i = 0; i < line.length(); i++) {

			char c = line.charAt(i);

			if (!Character.isSpaceChar(c) && !commentActivated && !isText) {

				ch = String.valueOf(c);

				if (c == '"') {
					textBuilder.append(c);
					isText = true;
					this.stringUnboundedRow = row;
					sb.setLength(0);
				} else if (ch.matches("/") || ch.matches("[*]")) {

					i = sc.treatmentComment(this, c, line, i, row);
					this.commentRow = row;
					sb.setLength(0);
				} else if (Util.isOpLogic(ch)) {
					verifyLexeme(row);
					i = sc.treatmentRelational(c, line, i, row);
				} else if (Util.isOpArithm(ch)) {
					verifyLexeme(row);
					sc.treatmentArithms(c, row);
				} else if (Util.isDelimiter(ch)) {
					verifyLexeme(row);
					sc.treatmentDelimiter(c, row);
				} else if (Util.isOther(ch)) {
					hm.add(new Token(Kind.OTHER, ch, "OTHER",null, row));
					sb.setLength(0);
				}else 
		
				isAlpha = Character.isLetter(c);
				isNumber = Character.isDigit(c);
				isUnderScore = (c == '_') ? true : false;
				isDot = (c == '.') ? true : false;
				isApostrophe = ch.matches("'");

				if (isAlpha || isNumber || isUnderScore || isDot || isApostrophe) {
					sb.append(c);
					isAlpha = isNumber = isUnderScore = isDot = isApostrophe = false;
				}else{

					if(Util.isUnknow(ch) && c !='"') 
						hm.add(new Token(Kind.OTHER, ch, "UNKNOW",null, row));
						
					verifyLexeme(row);
				}

			} else if (isText && c != '"') {
				textBuilder.append(c);
			} else if (c == '"') {
				isText = false;
				textBuilder.append('"');
				hm.add(new Token(Kind.STRING, textBuilder.toString(), '"' + "abc" + '"', "STRING",null, row));
				textBuilder.setLength(0);
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

		if (Util.isReservedWord(lexeme))
			sc.treatmentRW(sb, row);
		else if (Util.isModifier(lexeme))
			sc.treamentmentModifier(sb, row);
		else if(Util.isNumberInteger(lexeme) || Util.isNumberFloat(lexeme))
			sc.treatmentNumbers(sb, row);
		else if (Util.isNotIdentifier(lexeme) || Util.isIdentifier(lexeme) || Util.isLetter(lexeme))
			sc.treatmentIndetifier(sb, row);
	
	}

	private void setKindIdentifiers() {
	
		Set <Token> hmTemp = hm;
		
		Kind kind = null;
		boolean isAttrib = false;
        boolean isKind = false;
		
		for(Token t: hmTemp) {
			
			if((t.getKind().equals(CHAR) ||
			    t.getKind().equals(INT)  ||
			    t.getKind().equals(FLOAT)) && !isKind){
					
				kind = t.getKind();
				isKind = true;
			}
			
			Kind kindId = idents.get(t.getLexeme());
			boolean contains = idents.containsKey(t.getLexeme());
			
			if(contains){
				
				 if(t.getIdKind() == null){
					//System.out.println("antes: "+t.getLexeme() + " "+kindId + " "  +t.getLine());
					 t.setIdKind(kindId);
					 //System.out.println("depois: "+t.getLexeme() + " "+t.getIdKind()+ " " + t.getLine());
				 }
			}
			
			if(!t.getKind().equals(kind)) {
		
				if(t.getLexeme().equals("="))
					isAttrib = true;
				
				if(t.getKind().equals(ID) && !isAttrib) {
				
					t.setIdKind(kind);
					idents.put(t.getLexeme(),kind);
				}				
				
				if(isAttrib){
					
					if(idents.containsKey(t.getLexeme())){
						
						if(t.getIdKind() == null){
							t.setIdKind(idents.get(t.getLexeme()));
						}
						
					}
				}
				
				if(t.getLexeme().equals(",")) {
					isAttrib = false;
					
				}else {
					
					if(t.getLexeme().equals(";")) {
						
						kind = null;
						isKind = false;
						isAttrib = false;
					}
				}
				
			}
		
		}
				
	}
	
	
	public static Set<Token> getSymbolTable() {
		return hm;
	}

	@Override
	public String toString() {
		return "LexiconAnalyzer [hm= " + hm + "]";
	}
}