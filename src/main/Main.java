package main;
import java.io.BufferedReader; 
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.IOException;
import java.util.ArrayList;


public class Main { 
	
	
	
	public static ArrayList<Character> getCodigoFuente(String path) throws FileNotFoundException, IOException { 
		ArrayList<Character> fuente = new ArrayList<Character>();
		int simbolo;
        FileReader f = new FileReader(path); 
        BufferedReader b = new BufferedReader(f); 
        while((simbolo = b.read())!= -1) {
        	fuente.add((char)simbolo);
        } 
        b.close(); 
        return fuente;
	} 
	
	public static ArrayList<String> getPalabras(ArrayList<Character> CodigoFuente) {
		ArrayList<String> palabras = new ArrayList<String>();
		String palabra = "";
		for(char simbolo:CodigoFuente) {
			if((simbolo >= 'a' && simbolo <= 'z') || (simbolo >= 'A' && simbolo <= 'Z')) {
				palabra += simbolo;
			}else {
				if(palabra != "")
					palabras.add(palabra);		
				palabra = "";
			}
		}
		return palabras;
	}
    
    public static void main(String[] args) throws IOException {
    	
    	System.out.println(getPalabras(getCodigoFuente("C:\\Users\\Leandro\\Desktop\\Compiladores\\Compilador2.0\\src\\test\\test.txt")).toString());
    }
}