package br.ufc.compiler.lexicon;

import java.util.LinkedHashSet;

import br.ufc.compiler.lexicon.Token.Kind;

public class SymbolConsumer {

	public SymbolConsumer(LinkedHashSet<Token> hm) {
		this.hm = hm;
	}

	private LinkedHashSet<Token> hm;

	protected void treatmentDelimiter(char c, int line) {

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
			hm.add(new Token(Kind.OTHER, "@", line));
			break;
		case '#':
			hm.add(new Token(Kind.OTHER, "#", line));
			break;
		case '$':
			hm.add(new Token(Kind.OTHER, "$", line));
			break;
		default:

		}

	}

	protected void treatmentArithms(char c, int line) {

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

	protected int treatmentRelational(char c, String str, int i, int row) {

		String operator = String.valueOf(c);
		if ((i + 1) < str.length())
			operator = operator.concat(String.valueOf(str.charAt(i + 1)));

		switch (c) {

		case '>': {
			if (operator.equals(">=")) {
				hm.add(new Token(Kind.OP_REL, ">=", row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_REL, ">", row));

			return i;
		}

		case '=': {

			if (operator.equals("==")) {
				hm.add(new Token(Kind.OP_REL, "==", row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OTHER, "ATRIB", row));

			return i;
		}

		case '<': {
			if (operator.equals("<=")) {
				hm.add(new Token(Kind.OP_REL, "<=", row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_REL, "<", row));

			return i;
		}

		case '!': {

			if (operator.equals("!=")) {
				hm.add(new Token(Kind.OP_REL, "!=", row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_REL, "!", row));

			return i;
		}
		default:

		}
		return i;
	}

	protected void treatmentNumbers(StringBuilder sb, int row) {

		if (Util.isNumberFloat(sb.toString()))
			hm.add(new Token(Kind.FLOAT, Double.parseDouble(sb.toString()), "FLOAT", row));
		else if (Util.isNumberInteger(sb.toString()))
			hm.add(new Token(Kind.INT, Integer.parseInt(sb.toString()), "INT", row));
	}

}
