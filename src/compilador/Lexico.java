package compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;

public class Lexico {
	private ArrayList<Character> codigoFuente; // SE REPRESENTA EL CODIGO FUENTE COMO UN ARRAY DE CHARS.
	private String buffer; // SE UTILIZA PARA IR FORMANDO EL IDENTIFICADOR.
	private static int [][] MatrizTransicion;
	private static int [][] MatrizAccionesSemanticas;
	private static HashMap<String,Integer> TablaDeSimbolos;
	private int index; // POSICION ACTUAL EN EL CODIGO FUENTE.
	private ArrayList<Function<Integer, Integer>> AccionesSemanticas;
	private int linea;
	private int id_token;
	private int token;
	private static int ID = 256;
	private static int CTE = 257;

	
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
	public static int ASTERISCO = 23;		// '	
	public static int OTRO = 24;				//CUALQUIER OTRO CARACTER NO CONTEMPLADO EN ESTA ENTREGA
	public static int EOF = 25;					//FIN DEL ARCHIVO
	
	public static int ENTRADAS = 26;			//TOTAL DE ENTRADAS POSIBLES.
	public static int ESTADOS = 13;				//TOTAL DE ESTADOS DISPONIBLES.
	
	//CONSTRUCTOR LEXICO
	public Lexico(String path) throws FileNotFoundException, IOException {
		codigoFuente = getCodigoFuente(path);
		
		MatrizTransicion = inicializarMatrizDeTransicion();
		
		MatrizAccionesSemanticas = inicializarMatrizDeAccionesSemanticas();
		AccionesSemanticas = new ArrayList<Function<Integer, Integer>>();
		cargarAccionesSemanticas();
		
		TablaDeSimbolos = new HashMap<String,Integer>();
		InicializarTablaDeSimbolos();
		
		index = 0;
		linea = 1;
		id_token = 268;
		token = -1;
	}
	

	// SEPARA DEL CODIGO FUENTE UN TOKEN SIN MODIFICAR EL MISMO
	public int getToken() { 
		Character simbolo;
		buffer = ""; 
		int estadoActual = ESTADO_INICIAL;
		
		if(!EOF()) {
			while(estadoActual != ESTADO_FINAL && !EOF()) {
				simbolo = codigoFuente.get(index);
				ejecutarAccionesSemanticas(estadoActual,simbolo);
				estadoActual = nextEstado(estadoActual,simbolo);
			}
		}
		else
			System.out.println("----FIN DE ARCHIVO----");		
		
		System.out.println(token + ": " + buffer);
		return token;
	}
	
	// A PARTIR DE EL ESTADO ACTUAL Y UN CARACTER DETERMINA ACCIONES SEMANTICAS A EJECUTAR.
	private void ejecutarAccionesSemanticas(int estadoActual,char caracterActual) {
		AccionesSemanticas.get(MatrizAccionesSemanticas[estadoActual][toInt(caracterActual)]).apply(1);
	}
	
	// A PARTIR DE EL ESTADO ACTUAL Y UN CARACTER DETERMINA CUAL ES EL SIGUIENTE ESTADO.
	private int nextEstado(int estadoActual,char caracterActual) {
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
	
	private boolean EOF() {
		return index == codigoFuente.size();
	}
	
	private static int[][] inicializarMatrizDeTransicion() {	
		int F = ESTADO_FINAL; // 'F' REPRESENTA EL ESTADO FINAL
		int [][] matrizTransicion = {				
		
		//DENTRO DE LA MATRIZ HAY ESTADOS DE ERROR QUE SE MANEJAN CON ACCIONES SEMANTICAS Y VAN A ESTADO FINAL.
				
		//LM-{F}	lm		Digito	Blanco	/n	Tab	F   _   =   <	>	(	)	{	}	,	;	-   + 	/	.	!	 ' 	:  *
			{1,		1,		6,		0,		0,	0,	1,	0,	4,	2,	3,	F,	F,	F,	F,	F,	F,	F,	F,	F,	7,	F,	10, 3 ,3},	// ESTADO 0  INICIAL OK FALTA :
			{1,		1,		1,		F,		F,	F,	1,	1,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,  3 ,3},		// ESTADO 1  OK  
			{F,		F,		F,		F,		F,	F,	F,	F,	F,	5,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,  3 ,3},		// ESTADO 2  OK
			{F,		F,		F,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,  3 ,3},		// ESTADO 3  OK
			{F,		F,		F,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,  3 ,3},		// ESTADO 4  OK
			{5,		5,		5,		5,		0,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,	5,  3 ,3},		// ESTADO 5  OK
			{F,		F,		6,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	7,	F,	F,  3 ,3},		// ESTADO 6  OK
			{F,		F,		7,		F,		F,	F,	8,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,  3 ,3},		// ESTADO 7  OK
			{F,		F,		8,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	9,	9,	F,	F,	F,	F,  3 ,3},		// ESTADO 8  OK
			{F,		F,		9,		F,		F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,	F,  3 ,3},		// ESTADO 9  OK
			{10,	10,		10,		10,		F,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	11,	10,	10,	F,  3 ,3},		// ESTADO 10 OK
			{10,	10,		10,		10,		F,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	11,	10,	10,	F,  3 ,3},		// ESTADO 11 OK
			{10,	10,		10,		10,		12,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	10,	11,	10,	10,	F,  3 ,3}		// ESTADO 12 OK
		};
		
		return matrizTransicion;		
	}
	
	// RETORNA LA CONSTANTE CONRRESPONDIENTE
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
			case '\r':
				return SALTO_DE_LINEA;
			case '\t':
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
	
	private static void InicializarTablaDeSimbolos(){
		
		//PALABRAS RESERVADAS
		TablaDeSimbolos.put("if", 258);
		TablaDeSimbolos.put("then", 259);
		TablaDeSimbolos.put("else", 260);
		TablaDeSimbolos.put("end-if", 261);
		TablaDeSimbolos.put("out", 262);
		TablaDeSimbolos.put("fun", 263);
		TablaDeSimbolos.put("return", 264);
		TablaDeSimbolos.put("break", 265);
		TablaDeSimbolos.put("i8", 266);  // TEMA PARTICULAR 1
		TablaDeSimbolos.put("f32", 267); // TEMA PARTICULAR 7

	}
	
	//MATRIZ DE ACCIONES SEMANTICAS
	
	private static int[][] inicializarMatrizDeAccionesSemanticas() {	
		
		int R = -1;
		//CADA NUMERO UBICADO EN LA CELDA DE LA MATRIZ CORRESPONDE A SU ACCION SEMANTICA CORRESPONDIENTE.
		int [][] matriz = {
				//LM	lm		DIGITO	BLANCO	/n	TAB	F	_	= 	<	>	(	)	{	}	,	;	-	 + 	/	.	!	'  :   *
				{10,	10,		R,		2,		13,	2,	10,	2,	R,	R,	R,	9,	9,	9,	9,	9,	9,	9,	9,	9,	R,	9,	R, 9 , 9},	//Estado 0  OK
				{11,	11,		11,		12,		12,	12,	11,	11,	12,	12, 12,	12,	12,	12,	12,	12,	12,	12,	12,	12,	12,	12,	12, 12 , 12},	//Estado 1  R
				{9,		9,		9,		9,		9,	9,	9,	9,	4,	4,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9, 9 , 9},	//Estado 2  OK
				{9,		9,		9,		9,		9,	9,	9,	9,	4,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9, 3 , 3},	//Estado 3  OK
				{9,		9,		9,		9,		9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	4,	9, 3 , 3}, //Estado 4  OK FALTA :	
				{0,		0,		0,		0,		8,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0, 3 , 3},	//Estado 5  OK
				{9,		9,		4,		9,		9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	4,	9,	9, 3 , 3}, //Estado 6  OK	
				{9,		9,		4,		9,		9,	9,	4,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9, 3 , 3}, //Estado 7  OK
				{9,		9,		4,		9,		9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	4,	4,	9,	9,	9,	9, 3 , 3}, //Estado 8  OK
				{9,		9,		4,		9,		9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9,	9, 3 , 3}, //Estado 9  OK	
				{4,		4,		4,		4,		-1,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	0,	4,	4,	6, 3 , 3},	//Estado 10 OK
				{4,		4,		4,		4,		0,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	6, 3 , 3},	//Estado 11 OK
				{4,		4,		4,		4,		0,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	0,	4,	4,	6, 3 , 3}	//Estado 12 OK
		
		};
		
		return matriz;
	}
	
	//ACCIONES SEMANTICAS
	
	//AS1: INICIALIZA EL BUFFER EN VACIO
	private Integer AS_0(Integer i) {
		buffer = "";
		return 0;	
	}
	
	//AS2: CONCATENA EL CARACTER ACTUAL AL STRING BUFFER
	private Integer AS_1(Integer i) {
		buffer += codigoFuente.get(index);
		return 0;
	}
	
	//AS0: AVANZAA A LA SIGUIENTE POSICION
	private  Integer AS_2(Integer i) {
		index++;
		return 0;
	}
	
	//AS9: RETROCEDE A LA POSICION ANTERIOR
	private Integer AS_3(Integer i) {
		index--;
		return 0;
	}
	
	//AS8: AUMENTA EN UNO LA LINEA ACTUAL Y LLAMA A AS0
	private Integer AS_4(Integer i) {
		linea++;
		return 0;
	}
	
	//AS5: ESTA ACCION SEMANTICA ES AGREGADA PARA COLOLARLE A LOS SIMBOLOS SU RESPECTIVO NUMERO EN LA TABLA ASCII COMO TOKEN.
	//ES UTILIZADA DEL ESTADO 0->F CUANDO SE DETECTA "+","-","*","/","(",")","{","}",",",";","*" 
	private Integer AS_5(Integer i) {
		if(!TablaDeSimbolos.containsKey(buffer))
			TablaDeSimbolos.put(buffer, (int)codigoFuente.get(index));
		return 0;
	}
	
	//AS6: AGREGA UN IDENTIFICADOR A LA TABLA DE SIMBOLOS Y LE ASIGNA UN TOKEN
	private Integer AS_6(Integer i) {
		if(!TablaDeSimbolos.containsKey(buffer)) {		
			id_token++;
			TablaDeSimbolos.put(buffer, id_token);
		}
		return 0;
	}
	
	//AS10: DEVUELVE EL TOKEN IDENTIFICADOR O PALABRA RESERVADA EN CASO QUE LO SEA
	private Integer AS_7(Integer i) {
		if(TablaDeSimbolos.get(buffer) >= 258 && TablaDeSimbolos.get(buffer) <= 267)
			token = TablaDeSimbolos.get(buffer);
		else 
			token = ID;
		return 0;
	}
	
	
	
	//AS11: ASIGNA UN NUEVO TOKEN A LA COSTANTE SI ES QUE NO EXISTE EN LA TABLA DE SIMBOLOS PARA LOS NUMEROS ENTEROS Y FLOTANTES
	private Integer AS_8(Integer i) {
		token = CTE;
		return 0;
	}
	

	
	//AS3: DESDE EL ESTADO 2,3 y 4, CON LOS CARACTERES QUE NO CORRESPONDIDOS SE QUE CON EL STRING ACTUAL Y VUELVE AL ESTADO ANTERIOR
	private Integer AS_9(Integer i) {
		AS_0(1);
		AS_1(1);
		AS_5(1);
		AS_7(1);
		AS_2(1);
		return 0;

	}
	
	private Integer AS_10(Integer i) {
		AS_0(1);
		AS_1(1);
		AS_2(1);
		return 0;

	}
	
	private Integer AS_11(Integer i) {
		AS_1(1);
		AS_2(1);
		return 0;

	}
	
	private Integer AS_12(Integer i) {
		AS_6(1);
		AS_7(1);
		return 0;
	}
	
	private Integer AS_13(Integer i) {
		AS_4(1);
		AS_2(1);
		return 0;
	}



	


	

	
	
	
	private void cargarAccionesSemanticas() {

			Function<Integer,Integer> aux;
			
			aux = this::AS_0;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_1;
			AccionesSemanticas.add(aux);

			aux = this::AS_2;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_3;
			AccionesSemanticas.add(aux);

			aux = this::AS_4;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_5;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_6;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_7;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_8;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_9;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_10;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_11;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_12;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_13;
			AccionesSemanticas.add(aux);
			


	}
	
}
