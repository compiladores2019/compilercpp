package br.ufc.compiler.util;

// Java program to find infix for 
// a given postfix. 

import java.util.*;

public class Infix {

	static boolean isOperand(char x) {

		return ((x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z') || (x >= 48 && x <= 57));
	}

	// Get Infix for a given postfix
	// expression
	public static String getInfix(String exp) {
		Stack<String> s = new Stack<String>();
		int j = 1;

		for (int i = 0; i < exp.length(); i++) {
			// Push operands
			if (isOperand(exp.charAt(i))) {
				s.push(exp.charAt(i) + "");
			}

			// We assume that input is
			// a valid postfix and expect
			// an operator.
			else {
				String op1 = s.peek();
				s.pop();
				String op2 = s.peek();
				s.pop();
				System.out.println("| " + exp.charAt(i) + " | " + op2 + " | " + op1 + "| T" + j);
				s.push("T" + (j++));
				// s.push(op2 + exp.charAt(i) + op1);
			}
		}

		// There must be a single element
		// in stack now which is the required
		// infix.

		System.out.println("| = |" + "T" + (j-1));
		System.out.println("----------------------------------------------");
		return s.peek();
	}

	// Driver code
	public static void main(String args[]) {
		String exp = "ab*c+";
		String exp2 = "abc*+";

		System.out.println(getInfix(exp));
	}
}
