package br.ufc.compiler.cpp;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import br.ufc.compiler.lexicon.LexiconAnalyzer;
import br.ufc.compiler.lexicon.Token;

public class Main {

	public static void main(String[] args) throws IOException {

		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView());

		int getValue = chooser.showOpenDialog(null);

		if (getValue == JFileChooser.APPROVE_OPTION) {

			File isSelectedFile = chooser.getSelectedFile();

			LexiconAnalyzer ln = new LexiconAnalyzer();

			ln.builderSymbolTable(isSelectedFile.getAbsolutePath());

			for (Token t : ln.getSymbolTable())
				System.out.print(t);
		}
	}

}
