package main;
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.IOException;
import java.util.ArrayList;

import compilador.Lexico;


public class Main { 
	
	
	

	
//	public static ArrayList<String> getPalabras(ArrayList<Character> CodigoFuente) {
//		ArrayList<String> palabras = new ArrayList<String>();
//		String palabra = "";
//		for(char simbolo:CodigoFuente) {
//			if((simbolo >= 'a' && simbolo <= 'z') || (simbolo >= 'A' && simbolo <= 'Z')) {
//				palabra += simbolo;
//			}else {
//				if(palabra != "")
//					palabras.add(palabra);		
//				palabra = "";
//			}
//		}
//		return palabras;
//	}
    
    public static void main(String[] args) throws IOException {
    	String path = "C:\\Users\\Leandro\\Desktop\\Compiladores\\Compilador2.0\\src\\test\\test.txt";
    	Lexico lex = new Lexico(path);
    	
    	lex.getToken();
    	lex.getToken();
    	lex.getToken();
    	lex.getToken();
    	lex.getToken();
    	lex.getToken();
    	lex.getToken();
    }
}