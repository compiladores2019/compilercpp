package br.ufc.compiler.lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class LexiconAnalyzer {

	private LinkedHashSet <Token> hm = new LinkedHashSet<>();
		
    @SuppressWarnings("resource")
	public void builderSymbolTable(FileInputStream in,String path) throws IOException {
    
    	File file = new File(path);
    	FileReader fr = new FileReader(file);
    	BufferedReader br = new BufferedReader(fr);
    	
    	if(!file.exists()) {
			throw new IOException("File not exists!");
		}
    	
    	if(br.ready()) {
  
    		collectLines(br.readLine());
    	}
    	  	
    	fr.close();
    	br.close();
    }
	
    private void collectLines(String line) {
    	
    	StringBuilder sb = new StringBuilder();
    	
    	for(int i = 0; i < line.length(); i++) {
    	
    		if(!Character.isSpaceChar(line.charAt(i))) 
    		    sb.append(line.charAt(i));
    		else {
    			
    			if(Util.isNumber(sb.toString()))
    				//hm.add(e)
    			
    			sb.delete(0, sb.toString().length() - 1);
    		}
    		
    	}
    	
    }
    
    
}
