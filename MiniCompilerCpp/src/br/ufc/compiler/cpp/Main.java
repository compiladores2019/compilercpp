package br.ufc.compiler.cpp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import br.ufc.compiler.lexicon.LexiconAnalyzer;
import br.ufc.compiler.lexicon.Token;
import br.ufc.compiler.parse.Analyze;
import br.ufc.compiler.parse.Parser;

public class Main {

	public static void main(String[] args) throws IOException {


		//JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView());

		//int getValue = chooser.showOpenDialog(null);

		//if (getValue == JFileChooser.APPROVE_OPTION) {

		//File isSelectedFile = chooser.getSelectedFile();

		LexiconAnalyzer ln = new LexiconAnalyzer();

		//ln.builderSymbolTable(isSelectedFile.getAbsolutePath());
		ln.builderSymbolTable("_files/test.cpp");

		//System.out.println(ln.getSymbolTable());

	//	Analyze.start(); //realiza a análise sintática
	//	LexiconAnalyzer.getSymbolTable().clear(); //limpando a tabela dos tokens
	//	Parser.destroy(); // limpando array e resetando a posição do apontador de índice


		 JFrame window = new JFrame("Symbol Table");
			JPanel panel = new JPanel();
			window.add(panel);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setLocationRelativeTo(null);
			window.pack();
			window.setSize(500, 500);
			DefaultTableModel model = new DefaultTableModel();
			JTable table = new JTable(model);
			table.setSize(500, 500);
			JScrollPane scroll = new JScrollPane(table);
			panel.add(BorderLayout.CENTER, scroll);
			table.setFont(new Font("Serif", Font.PLAIN, 16));
			model.addColumn("KIND");
			model.addColumn("LEXEME");
			model.addColumn("VALUE");
			model.addColumn("DESCRIBE");
			model.addColumn("LINE");
			for (Token t : LexiconAnalyzer.getSymbolTable())
				model.addRow(new Object[] { t.getKind(), t.getLexeme(), t.getValue(), t.getDescribe(), t.getLine() });

			window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			window.setVisible(true);
			scroll.setVisible(true);



		//}

	}

}