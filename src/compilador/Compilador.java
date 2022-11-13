package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

import lexico.Lexico;
import sintactico.Parser;

public class Compilador {
	private Lexico lexico;
	private Parser parser;
	
	private Lexico lexicoPrueba;
	public Compilador() {
		


	}

	public void ejecutar(String path) throws FileNotFoundException, IOException {
		lexico = new Lexico(path);
		parser = new Parser(lexico);
		parser.run();
		
		System.out.println("/////////////////////////////////////////////////////");
		lexicoPrueba = new Lexico(path);
		for (int i = 0; i < 14; i++) {
			lexicoPrueba.getToken();
			
		}
		System.out.println("/////////////////////////////////////////////////////");
	}

}
