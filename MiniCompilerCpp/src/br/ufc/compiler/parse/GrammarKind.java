package br.ufc.compiler.parse;

import br.ufc.compiler.lexicon.Token.Kind;

/**/
public class GrammarKind {

	
	/*Reconhece declaroçoes do tipo: int v,xd=f,a=9;   */
	static void kind() {

		Parser.InitParser();

		if (Parser.currentToken().getKind().equals(Kind.INT)) {
			Parser.nextToken();

			if (Parser.currentToken().getKind().equals(Kind.ID)) {
				nextID();
				/*
				 * Se executar sem exibir mensagem ou erro, então é pq está
				 * certo!
				 */
			}

			else
				System.out.println("Building.....");

			// int id;
			// int id,id,id;
			// int id = exp;
		}

	}

	private static void nextID() {
		Parser.nextToken();

		if (Parser.currentToken().getLexeme().equals(";"))
			return;

		System.out.println(Parser.currentToken().getLexeme());

		if (Parser.currentToken().getLexeme().equals(",") || Parser.currentToken().getLexeme().equals("=")) {

			nextID();
		}

		else if (Parser.currentToken().getKind().equals(Kind.ID)
				|| Parser.currentToken().getLexeme().equals(Kind.INT)) {
			nextID();
		}

		else
			System.out.println("Erro sitatico!s");

	}

}
