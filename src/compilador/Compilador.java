package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

import lexico.Lexico;
import sintactico.Parser;

public class Compilador {
	private Lexico lexico;
	private Parser parser;
	
	public Compilador() {

	}

	public void ejecutar(String path) throws FileNotFoundException, IOException {
		lexico = new Lexico(path);
		parser = new Parser(lexico);
		parser.run();
	}

}
