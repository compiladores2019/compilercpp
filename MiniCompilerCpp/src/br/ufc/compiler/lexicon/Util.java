package br.ufc.compiler.lexicon;

public class Util {

	public static boolean isNumberFloat(String number) {
		return number.matches("[0-9][0-9]*.[0-9][0-9]*");
	}
	
	public static boolean isNumberInteger(String number) {
		return number.matches("[0-9][0-9]*");
	}
	
	public static boolean isOther(String other) {
		return other.matches("#|@|$|&|!|-");
	}
	
	public static boolean isOpArithm(String op) {
		return op.matches("[+,-,*,/]");
	}
	
	public static boolean isDelimiter(String simbol) {
		return simbol.matches("[(,),{,},;,@,#,$]");
	}
		
	public static boolean isIdentifier(String id) {
		return id.matches("(_+([0-9]*|[a-zA-Z]*)*)|[a-zA-Z]+([0-9]*|[a-zA-Z]*)*|(_|[a-zA-Z]*)|(_|[0-9]*)|(_|[a-z][A-Z]*)|(_|[a-z][A-Z]*[0-9])"); 
	}
	
	public static boolean isOpLogic(String opLogic) {
		return opLogic.matches("<|>|<=|>=|==|!=|!|=");
	}
	
}
