package br.ufc.compiler.lexicon;

public class Token {

	public enum Kind {
		FOR, IF, WHILE, ELSE, ID, MAIN, PRIVATE, PUBLIC, INT, CHAR, FLOAT, DEL, OP_LOG, OP_ARITHM, OP_REL, LETTER, STRING, OTHER
	}

	private Kind kind;
	private int line;
	private Object value;
	private String lexeme;
	private Kind idKind;
	private String describe;

	public Token(Kind kind, String lexeme, String describe,Kind idKind, int line) {
		this.kind = kind;
		this.lexeme = lexeme;
		this.line = line;
		this.idKind = idKind;
		this.describe = describe;
	}

	public Token(Kind kind, Object value, String lexeme, String describe,Kind idKind, int line) {

		this.kind = kind;
		this.value = value;
		this.lexeme = lexeme;
		this.describe = describe;
		this.idKind = idKind;
		this.line = line;
	}

	public Kind getKind() {
		return kind;
	}

	public void setIdKind(Kind kind) {
		this.idKind = kind;
	}

	public Kind getIdKind() {
		return idKind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String toString() {
		return "Token [kind=" + kind + ", line=" + line + ", value=" + value + ", lexeme=" + lexeme + ", idKind="
				+ idKind + ", describe=" + describe + "]";
	}


}
