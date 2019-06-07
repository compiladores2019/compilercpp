package br.ufc.compiler.lexicon;

import java.util.LinkedHashSet;
import java.util.Set;

import br.ufc.compiler.lexicon.Token.Kind;

public class SymbolConsumer {

	public SymbolConsumer(Set <Token> hm){
		this.hm = hm;
	}

	private Set<Token> hm;

    protected void treatmentRW(StringBuilder sb,int line){
    	
		String op = sb.toString();

		switch (op) {

		case "for":
			hm.add(new Token(Kind.FOR, op,"RSVD_WORD",null, line));
			break;
			
		case "while":
			hm.add(new Token(Kind.WHILE,op,"RSVD_WORD",null, line));
			break;
			
		case "if":
			hm.add(new Token(Kind.IF, op,"RSVD_WORD",null, line));
			break;
			
		case "else":
			hm.add(new Token(Kind.ELSE, op,"RSVD_WORD",null, line));
			break;
			
		case "main":
			hm.add(new Token(Kind.MAIN, op,"RSVD_WORD",null, line));
			break;
			
		case "int":
			hm.add(new Token(Kind.INT, op,"RSVD_WORD",null, line));
			break;
			
		case "char":
			hm.add(new Token(Kind.CHAR,op,"RSVD_WORD",null, line));
			break;
			
		case "float":
			hm.add(new Token(Kind.FLOAT, op,"RSVD_WORD",null, line));
			break;
			
		default:
			break;
		}
		sb.setLength(0);
    }
	
	protected void treatmentDelimiter(char c, int line) {

		switch (c) {

		case '(':
			hm.add(new Token(Kind.DEL,"(", "PARLFT",null, line));
			break;
			
		case ')':
			hm.add(new Token(Kind.DEL,")", "PARRGH",null, line));
			break;
			
		case '{':
			hm.add(new Token(Kind.DEL,"{", "KEYLFT",null, line));
			break;
		case '}':
			hm.add(new Token(Kind.DEL,"}" ,"KEYRGH",null, line));
			break;
			
		case '[':
			hm.add(new Token(Kind.DEL,"[", "BKTLFT",null, line));
			break;
			
		case ']':
			hm.add(new Token(Kind.DEL,"]", "BKTRGH",null, line));
			break;
			
		case ';':
			hm.add(new Token(Kind.DEL,";","DELIMITER",null, line));
			break;
			
		case ',':
			hm.add(new Token(Kind.DEL,",","COMMA",null, line));
			break;	
		
		case ':':
			hm.add(new Token(Kind.DEL,":","TWOPNT",null, line));
			break;	
		
		case '@':
			hm.add(new Token(Kind.OTHER, "@","OTHER",null, line));
			break;
			
		case '#':
			hm.add(new Token(Kind.OTHER, "#", "OTHER",null, line));
			break;
			
		default:
			break;
		}
	}

	protected void treamentmentModifier(StringBuilder sb, int line) {

		String op = sb.toString();

		if (op.equals("public")) {
			hm.add(new Token(Kind.PUBLIC, op, "RSVD_WORD",null,line));
		}else if (op.equals("private")) {
			hm.add(new Token(Kind.PRIVATE, op,"RSVD_WORD",null, line));
		}
	    sb.setLength(0);
	}
	
	protected void treatmentArithms(char c, int line) {

		switch (c) {

		case '+':
			hm.add(new Token(Kind.OP_ARITHM,"+", "SUM_OP",null, line));
			break;
			
		case '-':
			hm.add(new Token(Kind.OP_ARITHM,"-", "SUB_OP",null, line));
			break;
	
		case '/':
			hm.add(new Token(Kind.OP_ARITHM,"/","DIV_OP",null, line));
			break;
			
		case '*':
			hm.add(new Token(Kind.OP_ARITHM,"*", "MULT_OP",null, line));
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
				hm.add(new Token(Kind.OP_REL, ">=","REL_GE",null, row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_REL, ">","REL_G",null, row));
			break;
		}

		case '=': {
			if (operator.equals("==")) {
				hm.add(new Token(Kind.OP_REL, "==","REL_EE",null, row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OTHER, "=","ATRIB",null, row));
            break;
		}

		case '<': {
			if (operator.equals("<=")) {
				hm.add(new Token(Kind.OP_REL, "<=","REL_LE",null, row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_REL, "<", "REL_L",null,row));
             break;
		}

		case '!': {
			if (operator.equals("!=")) {
				hm.add(new Token(Kind.OP_LOG, "!=","REL_NE",null, row));
				return i + 1;
			} else
				hm.add(new Token(Kind.OP_LOG, "!","LOG_NOT",null, row));
			break;
		}
		
		case '&':
		{
			if(operator.equals("&&")) {
				hm.add(new Token(Kind.OP_LOG,operator,"LOG_AND",null,row));
				return i + 1;
			}else
				hm.add(new Token(Kind.OTHER,operator,"UNKNOW",null,row));
			break;
		}
		
		case '|':
		{
			if(operator.equals("||")) {
				hm.add(new Token(Kind.OP_LOG,operator,"LOG_OR",null,row));
				return i + 1;
			}else
				hm.add(new Token(Kind.OTHER,operator,"UNKOW",null,row));	
			break;
		}
		default:
		}
		return i;
	}

	protected void treatmentNumbers(StringBuilder sb, int row) {

		if (Util.isNumberFloat(sb.toString())) { 
			hm.add(new Token(Kind.FLOAT, Double.parseDouble(sb.toString()), "FLOAT","RSVD_WORD",null, row));
			sb.setLength(0);
		}else if (Util.isNumberInteger(sb.toString())) {
			hm.add(new Token(Kind.INT, Integer.parseInt(sb.toString()), "INT","RSVD_WORD",null,row));
			sb.setLength(0);
		}
	}
	
	protected void treatmentIndetifier(StringBuilder sb,int row) {
			
		String lexeme = sb.toString();
	
		if(Util.isLetter(lexeme)) 
			hm.add(new Token(Kind.LETTER,lexeme,"LETTER",null,row));
		else if(!Util.isNotIdentifier(lexeme)) 
		    hm.add(new Token(Kind.ID,lexeme,"ID",null,row));		   
		   
		 sb.setLength(0);
	}

	protected int treatmentComment(LexiconAnalyzer ln,char c,String com,int i, int row) {
		
		String str = String.valueOf(c);
	
		if((i + 1) < com.length()) {
			str = str.concat(String.valueOf(com.charAt(i + 1)));
			System.out.println(str+" na linha "+ row);
		}
		switch (c) {
		
		case '/':
		{
			//System.out.println(str+" na linha "+ row);
			if(str.equals("//")) return com.length() - 1;
			else if(str.equals("/*")) {
				//System.out.println(str);
				ln.commentActivated = true;
				return i + 1;
		   }else if(str.matches("/+([0-9]*|[A-Za-z]*| *)*")) {
			      
			     hm.add(new Token(Kind.OP_ARITHM,"/", "DIV_OP",null,row));
		   }
		   break;	
		}
		case '*':
		{    
			  if(str.equals("*/")) {
				ln.commentActivated = false;
				return i + 1;	
			  }else if(str.matches("[*]+([a-zA-Z]*|[0-9]*)*"));
				  	  hm.add(new Token(Kind.OP_ARITHM,"*","MULT_OP",null,row));
			  
			 break;
		}	
		default:
	  } 
		return i;
    }
}