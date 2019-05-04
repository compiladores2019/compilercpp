package br.ufc.compiler.lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

import br.ufc.compiler.lexicon.Token.Kind;

public class LexiconAnalyzer {

	private static LinkedHashSet<Token> hm = new LinkedHashSet<>();
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
					hm.add(new Token(Kind.OTHER, ch, "OTHER", row));
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
						hm.add(new Token(Kind.OTHER, ch, "UNKNOW", row));
						
					verifyLexeme(row);
				}

			} else if (isText && c != '"') {
				textBuilder.append(c);
			} else if (c == '"') {
				isText = false;
				textBuilder.append('"');
				hm.add(new Token(Kind.STRING, textBuilder.toString(), '"' + "abc" + '"', "STRING", row));
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
		else if (Util.isNotIdentifier(lexeme) || Util.isIdentifier(lexeme) || Util.isLetter(lexeme))
			sc.treatmentIndetifier(sb, row);
		else
			sc.treatmentNumbers(sb, row);
	}

	public static LinkedHashSet<Token> getSymbolTable() {
		return hm;
	}

	@Override
	public String toString() {
		return "LexiconAnalyzer [hm= " + hm + "]";
	}
}