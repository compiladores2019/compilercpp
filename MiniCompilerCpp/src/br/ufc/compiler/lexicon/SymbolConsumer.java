package br.ufc.compiler.lexicon;

import java.util.LinkedHashSet;

import br.ufc.compiler.lexicon.Token.Kind;

public class SymbolConsumer {

	public SymbolConsumer(LinkedHashSet<Token> hm){
		this.hm = hm;
	}

	private LinkedHashSet<Token> hm;

    protected void treatmentRW(StringBuilder sb,int line){
    	
		String op = sb.toString();

		switch (op) {

		case "for":
			hm.add(new Token(Kind.FOR, "FOR", line));
			break;
			
		case "while":
			hm.add(new Token(Kind.WHILE, "WHILE", line));
			break;
			
		case "if":
			hm.add(new Token(Kind.IF, "IF", line));
			break;
			
		case "else":
			hm.add(new Token(Kind.ELSE, "ELSE", line));
			break;
			
		case "main":
			hm.add(new Token(Kind.MAIN, "MAIN", line));
			break;
			
		case "int":
			hm.add(new Token(Kind.INT, "INT", line));
			break;
			
		case "char":
			hm.add(new Token(Kind.CHAR, "CHAR", line));
			break;
			
		case "float":
			hm.add(new Token(Kind.FLOAT, "FLOAT", line));
			break;
			
		default:
			break;
		}
		sb.setLength(0);
    }
	
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
			
		default:
			hm.add(new Token(Kind.OTHER, "UNKNOW", line));
			break;
		}
	}

	protected void treamentmentModifier(StringBuilder sb, int line) {

		String op = sb.toString();

		if (op.equals("public"))
			hm.add(new Token(Kind.PUBLIC, "PUBLIC", line));
		else if (op.equals("private"))
			hm.add(new Token(Kind.PRIVATE, "PRIVATE", line));

		sb.setLength(0);
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
			break;
		}

		case '=': {
			if (operator.equals("==")) {
				hm.add(new Token(Kind.OP_REL, "==", row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OTHER, "ATRIB", row));
            break;
		}

		case '<': {
			if (operator.equals("<=")) {
				hm.add(new Token(Kind.OP_REL, "<=", row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_REL, "<", row));
             break;
		}

		case '!': {
			if (operator.equals("!=")) {
				hm.add(new Token(Kind.OP_REL, "!=", row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_LOG, "!", row));
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
	
		sb.setLength(0);
	}
	
	protected void treatmentIndetifier(StringBuilder sb,int row) {
			
		    hm.add(new Token(Kind.ID,sb.toString(),"ID",row));	
		    sb.setLength(0);
	}

	protected int treatmentComment(LexiconAnalyzer ln,char c,String com,int i, int row) {
		
		String str = String.valueOf(c);
	
		if((i + 1) < com.length())
			str = str.concat(String.valueOf(com.charAt(i + 1)));
		
		switch (c) {
		
		case '/':
		{
			if(str.equals("//")) return com.length() - 1;
			else if(str.equals("/*")) {
				ln.commentActivated = true;
		   }else if(str.equals("/"))
			     hm.add(new Token(Kind.OP_ARITHM,"DIV_OP",row));
			
			return i + 1;
		}
		case '*':
		{    
			  if(str.equals("*/")) {
				ln.commentActivated = false;
				 return i + 1;
			  }else if(str.contains("*"))
				  	  hm.add(new Token(Kind.OP_ARITHM,"MULT_OP",row));
			  
			  return i + 1;	
		}	
		default:
		} 
		return i;
    }
}