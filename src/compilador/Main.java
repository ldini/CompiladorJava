package compilador;

import java.io.IOException;





public class Main { 

    
    public static void main(String[] args) throws IOException {
    	
    	String path = "C:\\Users\\Leandro\\Desktop\\Compiladores\\Compilador2.0\\src\\test\\";
    	
    	String test_0 = "test_0.txt"; 	//Constantes con el primer y último valor dentro del rango
    	String test_1 = "test_1.txt"; 	//Constantes con el primer y último valor fuera del rango,
    	String test_2 = "test_2.txt";	//Para nros. Flotantes, además, mantisa con y sin parte decimal,con y sin exponente, con exponente positivo y negativo,
    	String test_3 = "test_3.txt";	//Identificadores con menos y más de 25 caracteres
    	String test_4 = "test_4.txt";	//Identificadores con letras, dígitos y “_”,
    	String test_5 = "test_5.txt";	//Intento de incluir en el nombre de un identificador un carácter que no sea letra, dígito o “_“
    	String test_6 = "test_6.txt";	//Palabras reservadas en minúscula y mayúscula
    	String test_7 = "test_7.txt";  	//Comentarios bien y mal escritos
    	String test_8 = "test_8.txt";	//Cadenas multilinea bien y mal escritos
    	
    	
    	Compilador compilador = new Compilador();
    	compilador.ejecutar(path + test_4);

    }
}