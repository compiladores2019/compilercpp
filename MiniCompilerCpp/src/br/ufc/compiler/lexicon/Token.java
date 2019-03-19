package br.ufc.compiler.lexicon;

public class Token {

	public enum Kind {
		
		FOR, IF, WHILE, ELSE, ID,MAIN,PRIVATE, PUBLIC, INT, CHAR, FLOAT, DEL, OP_LOG, OP_ARITHM, OP_REL, OTHER
		
	}

	private Kind kind;
	private int line;
	private Object value;
	private String describe;

	public Token(Kind kind, String describe, int line) {
		this.kind = kind;
		this.line = line;
		this.describe = describe;
	}

	public Token(Kind kind, Object value, String describe, int line) {

		this.kind = kind;
		this.value = value;
		this.describe = describe;
		this.line = line;
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

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	@Override
	public String toString() {
		return "Token [kind= " + kind + ", line= " + line + ", value= " + value + ", describe= " + describe + "]\n";
	}



}
