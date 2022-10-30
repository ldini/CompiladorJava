package compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexico {
	private ArrayList<Character> codigoFuente; // SE REPRESENTA EL CODIGO FUENTE COMO UN ARRAY DE CHARS.
	private String buffer; // SE UTILIZA PARA IR FORMANDO EL IDENTIFICADOR.
	private static int [][] MatrizTransicion;
	private int index; // POSICION ACTUAL EN EL CODIGO FUENTE.
	
	// TOTAL DE ESTADOS.
	public static int ESTADO_INICIAL = 0;      //LETRAS MAYUSCULA	
	public static int ESTADO_FINAL = 13;      //LETRAS MAYUSCULA	
	
	// TODAS LAS POSIBLES ENTRADAS QUE PUEDEN SER LEIDAS DESDE EL CODIGO FUENTE
	public static int LETRA_MAYUSCULA = 0;      //LETRAS MAYUSCULA
	public static int LETRA_MINUSCULA = 1;		//LETRAS MINUSCULA
	public static int DIGITO = 2;				//NUMEROS
	public static int ESPACIO_EN_BLANCO = 3;	//ESPACIO EN BLANCO
	public static int SALTO_DE_LINEA = 4;   	//SALTO DE LINEA O /n
	public static int TAB = 5;					//TECLA DE TABULACION
	public static int F_FLOTANTE = 6;			//F PARA LOS NUMEROS FLOTANTES
	public static int GUION_BAJO = 7;			// _ 
	public static int IGUAL = 8;				// =
	public static int MENOR = 9;				// <
	public static int MAYOR = 10;				// >
	public static int PARENTESIS_OPEN = 11; 	// (
	public static int PARENTESIS_CLOSE = 12;	// )
	public static int LLAVE_OPEN = 13;			// {
	public static int LLAVE_CLOSE = 14;			// }
	public static int COMA = 15;				// ,
	public static int PUNTO_Y_COMA = 16;		// ;
	public static int RESTA = 17;				// -
	public static int SUMA = 18;				// +
	public static int DIVISION = 19;			// /
	public static int PUNTO = 20;				// .
	public static int EXCLAMACION = 21;			// !
	public static int COMILLA_SIMPLE = 22;		// '
	public static int OTRO = 23;				//CUALQUIER OTRO CARACTER NO CONTEMPLADO EN ESTA ENTREGA
	public static int EOF = 24;					//FIN DEL ARCHIVO
	
	public static int ENTRADAS = 25;			//TOTAL DE ENTRADAS POSIBLES.
	public static int ESTADOS = 13;				//TOTAL DE ESTADOS DISPONIBLES.
	
	//CONSTRUCTOR LEXICO
	public Lexico(String path) throws FileNotFoundException, IOException {
		codigoFuente = getCodigoFuente(path);
		MatrizTransicion = inicializarMatrizDeTransicion();
		index = 0;
	}
	
	private boolean EOF() {
		return index == codigoFuente.size()-1;
	}
	
	// SEPARA DEL CODIGO FUENTE UN TOKEN SIN MODIFICAR EL MISMO
	public int getToken() {
		Character simbolo;
		buffer = "";
		int estadoActual = ESTADO_INICIAL;
		while(estadoActual != ESTADO_FINAL && !EOF()) {
			simbolo = codigoFuente.get(index);
			index ++;
			estadoActual = nextEstado(estadoActual,simbolo);
			if(estadoActual != ESTADO_INICIAL && estadoActual != ESTADO_FINAL)
				buffer += simbolo;
		}
		index--;
		System.out.println(buffer);
		return 1;
	}
	
	// A PARTIR DE EL ESTADO ACTUAL Y UN CARACTER DETERMINA CUAL ES EL SIGUIENTE ESTADO
	public int nextEstado(int estadoActual,char caracterActual) {
		return MatrizTransicion[estadoActual][toInt(caracterActual)];
	}
	
	// ESTA CLASE A PARTIR DE UN PATH ME DEVUELVE UN ARRAY DE CARACTERES QUE TOMO COMO CODIGO FUENTE
	private static ArrayList<Character> getCodigoFuente(String path) throws FileNotFoundException, IOException { 
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
	
	private static int[][] inicializarMatrizDeTransicion() {	
		int F = ESTADO_FINAL; // 'F' REPRESENTA EL ESTADO FINAL
		int [][] matrizTransicion = {				
		
		//DENTRO DE LA MATRIZ HAY ESTADOS DE ERROR QUE SE MANEJAN CON ACCIONES SEMANTICAS Y VAN A ESTADO FINAL.
				
		//LM-{F}	lm		Digito	Blanco	/n	Tab	F   _   =   <	>	(	)	{	}	,	;	-   + 	/	.	!	 ' 	
			{1,		1,		7,		0,		0,	0,	1,	F,	5,	3,	4,	F,	F,	F,	F,	F,	F,	F,	F,	F,	8,	F,	10},	// ESTADO 0 INICIAL 
			{1,		1,		1,		F,		F,	F,	1,	1,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F},		// ESTADO 1
			{F,		F,		F,		F,		F,	F,	F,	F,	F,	5,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F},		// ESTADO 2
			{F,		F,		F,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F},		// ESTADO 3
			{F,		F,		F,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F},		// ESTADO 4
			{5,		5,		5,		5,		0,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	F},		// ESTADO 5
			{F,		F,		6,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	7,	F,	F},		// ESTADO 6
			{F,		F,		7,		F,		F,	F,	8,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F},		// ESTADO 7
			{F,		F,		8,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	9,	9,	F,	F,	F,	F},		// ESTADO 8
			{10,	10,		10,		10,		F,	F,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	11,	10,	10,	F},		// ESTADO 9
			{F,		F,		F,		11,		12,	11,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	11,	F,	F,	F},		// ESTADO 10
			{10,	10,		10,		10,		F,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	F,	10,	10,	F}		// ESTADO 11
		
		};
		
		return matrizTransicion;		
	}
	
	private static int toInt(char c){
		if(c >= 'A' && c <= 'Z' && c != 'F')
			return LETRA_MAYUSCULA;
		if(c >= 'a' && c <= 'z')
			return LETRA_MINUSCULA;
		if(c >= '0' && c <= '9')
			return DIGITO;
		
		switch(c){	
			case ' ':
				return ESPACIO_EN_BLANCO;
			case '\n':
				return SALTO_DE_LINEA;
			case '	':
				return TAB;
			case 'F':
				return F_FLOTANTE;
			case '_':
				return GUION_BAJO;
			case '=':
				return IGUAL;	
			case '<':
				return MENOR;
			case '>':
				return MAYOR;
			case '(':
				return PARENTESIS_OPEN;
			case ')':
				return PARENTESIS_CLOSE;
			case '{':
				return LLAVE_OPEN;
			case '}':
				return LLAVE_CLOSE;
			case ',':
				return COMA;
			case ';':
				return PUNTO_Y_COMA;
			case '-':				
				return RESTA;
			case '+':				
				return SUMA;
			case '/':				
				return DIVISION;
			case '.':
				return PUNTO;
			case '!':
				return EXCLAMACION;
			default:
				if ((char)c == 39)
					return COMILLA_SIMPLE;
				else
					return OTRO;

		}
	}
}
