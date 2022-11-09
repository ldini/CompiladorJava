package compilador;

import java.io.IOException;





public class Main { 

    
    public static void main(String[] args) throws IOException {
    	
    	String path = "C:\\Users\\Leandro\\Desktop\\Compiladores\\Compilador2.0\\src\\test\\test.txt";
    	
    	Compilador compilador = new Compilador();
    	compilador.ejecutar(path);

    }
}