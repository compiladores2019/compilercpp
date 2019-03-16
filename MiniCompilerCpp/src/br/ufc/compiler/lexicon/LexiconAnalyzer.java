package br.ufc.compiler.lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

import br.ufc.compiler.lexicon.Token.Kind;

public class LexiconAnalyzer {

	private LinkedHashSet <Token> hm = new LinkedHashSet<>();
		
    @SuppressWarnings("resource")
	public void builderSymbolTable(String path) throws IOException {
    
    	File file = new File(path);
    	FileReader fr = new FileReader(file);
    	BufferedReader br = new BufferedReader(fr);
    	int j = 0;
    	if(!file.exists()) {
			throw new IOException("File not exists!");
		}
    	
    	while(br.ready()) {
            j++;
    		collectLines(br.readLine(),j);
    	}
    	  	
    	fr.close();
    	br.close();
    }
	
    private void collectLines(String line,int row) {
    	
    	StringBuilder sb = new StringBuilder();
    	
    	for(int i = 0; i < line.length() - 1; i++) {
    	     
    		char c = line.charAt(i);
    	 
    		if(!Character.isSpaceChar(c)) {
    		    
    			String st = String.valueOf(c);
    			
    			if(st.matches("[>|<|!|=]")) {
    				
    				if(st.equals(">") && line.charAt(i + 1) == '=')
    					hm.add(new Token(Kind.OP_REL,">=",row));
    				else if(st.equals(">")) 
    					  hm.add(new Token(Kind.OP_REL,">",row)); 
    				
    				if(st.equals("=") && line.charAt(i + 1) == '=')
    					hm.add(new Token(Kind.OP_REL,"==",row));
    				else if(st.equals("="))
    					hm.add(new Token(Kind.OP_REL,"ATRIB",row)); 
    					
    				
    				if(st.equals("<") && line.charAt(i + 1) == '=')
    					hm.add(new Token(Kind.OP_REL,"<=",row));
    				else if(st.equals("<"))
    					hm.add(new Token(Kind.OP_REL,"<",row));
    					 			
    				if(st.equals("!") && line.charAt(i + 1) == '=')
    					hm.add(new Token(Kind.OP_REL,"!=",row));
    				else if(st.equals("!"))
    					hm.add(new Token(Kind.OP_REL,"!",row));
    				     	
    			}else {
    			
    			treatmentDelimiter(c, row);
                treatmentArithms(c, row);
    			}
    	
	    		if(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == '.') 
	    		    sb.append(c);
	    		else {
	    			
	    		  if(Util.isNumberFloat(sb.toString())) 
	    			   hm.add(new Token(Kind.FLOAT,Double.parseDouble(sb.toString()),"FLOAT",row));
	    		  else if(Util.isNumberInteger(sb.toString())) 
	    			   hm.add(new Token(Kind.INT,Integer.parseInt(sb.toString()),"INT",row));
	    		  
	    		  sb.setLength(0);
	    		}  		
	    	 } //else //b.setLength(0);
      }
 }
    
	private void treatmentDelimiter(char c,int line) {
    	
    	switch(c) {
    	
    	case '(':
    		hm.add(new Token(Kind.DEL,"PARLFT",line));
    		break;
    	case ')':
    		hm.add(new Token(Kind.DEL,"PARRGH",line));
    		break;
    	case '{':
    		hm.add(new Token(Kind.DEL,"KEYLFT",line));
    		break;	
    	case '}':
    		hm.add(new Token(Kind.DEL,"KEYRGH",line));
    		break;	
       	case '[':
    		hm.add(new Token(Kind.DEL,"BKTLFT",line));
    		break;	
    	case ']':
    		hm.add(new Token(Kind.DEL,"BKTRGH",line));
    		break;		
    	case ';':
    		hm.add(new Token(Kind.DEL,"COMMA",line));
    		break;
    	default:
    		hm.add(new Token(Kind.OTHER,"SMB_ESP",line));
    	}
    	
    }
    
	private void treatmentArithms(char c,int line) {
    	
    	switch(c) {
    	
    	case '+':
    		hm.add(new Token(Kind.OP_ARITHM,"SUM_OP",line));
    		break;
    	case '-':
    		hm.add(new Token(Kind.OP_ARITHM,"SUB_OP",line));
    		break;
    	case '/':
    		hm.add(new Token(Kind.OP_ARITHM,"DIV_OP",line));
    		break;	
    	case '*':
    		hm.add(new Token(Kind.OP_ARITHM,"MULT_OP",line));
    		break;	
    	default:
 
    	}
    	
    }

	@Override
	public String toString() {
		return "LexiconAnalyzer [hm=" + hm + "]";
	}
    
    
}
