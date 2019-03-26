package br.ufc.compiler.lexicon;

public class Util {

	public static boolean isNumberFloat(String number) {
		return number.matches("[0-9][0-9]*.[0-9][0-9]*");
	}

	public static boolean isNumberInteger(String number) {
		return number.matches("[0-9][0-9]*");
	}

	public static boolean isOther(String other) {
		return other.matches("#|@");
	}

	public static boolean isOpArithm(String op) {
		return op.matches("[+|/|*]") || op.matches("[-]");
	}

	public static boolean isDelimiter(String symbol) {
		return symbol.matches("[(,),{,},;,@,#,:]") || symbol.matches(",");
	}

	public static boolean isIdentifier(String id) {
		return id.matches("(_+([0-9]*|[a-zA-Z]*)*)|[a-zA-Z]+([0-9]*|[a-zA-Z]*)*|(_)");
	}

	public static boolean isNotIdentifier(String id) {
		return id.matches("([0-9]+([0-9]|[a-zA-Z])+([0-9]|[a-zA-Z])*)");
	}
	
	public static boolean isLetter(String ch) {
		return ch.matches("'+([0-9]|[a-zA-Z])+'");
	}
	
	public static boolean isOpLogic(String opLogic) {
		return opLogic.matches("<|>|<=|>=|==|!=|!|=|&") || opLogic.matches("[|]");
	}

	public static boolean isReservedWord(String rw){
		return rw.matches("while|for|if|else|int|char|float|main");
	}
	
	public static boolean isModifier(String mod) {
		return mod.matches("private|public");
	}

}
