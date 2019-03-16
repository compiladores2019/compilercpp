package br.ufc.compiler.lexicon;

public class Util {

	public static boolean isNumber(String number) {
		return number.matches("[0-9][0-9]*|[0-9][0-9].[0-9][0-9]*");
	}
	
	public static boolean isOther(String other) {
		return other.matches("#|@|$|&|!|-");
	}
	
	public static boolean isOpArithm(String op) {
		return op.matches("[+,-,*,/]");
	}
	
	public static boolean isOpLogic(String opLogic) {
		return opLogic.matches("<|>|<=|>=|==|!=|!");
	}
	
}
