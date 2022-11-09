package compilador;
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.IOException;
import java.util.ArrayList;

import lexico.Lexico;
import sintactico.Parser;


public class Main { 

    
    public static void main(String[] args) throws IOException {
    	String path = "C:\\Users\\Leandro\\Desktop\\Compiladores\\Compilador2.0\\src\\test\\test.txt";
    	Lexico lex = new Lexico(path);
    	Parser parser = new Parser(lex);
    	
    	parser.run();

    }
}