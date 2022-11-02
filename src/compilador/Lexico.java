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
	private ArrayList<Function> AccionesSemanticas;
	
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
		cargarAccionesSemanticas();
		TablaDeSimbolos = new HashMap<String,Integer>();
		InicializarTablaDeSimbolos();
		index = 0;
	}
	

//	// SEPARA DEL CODIGO FUENTE UN TOKEN SIN MODIFICAR EL MISMO
//	public int getToken() {
//		Character simbolo = codigoFuente.get(index);
//		int estadoActual = ESTADO_INICIAL;
//		
//		while(estadoActual != ESTADO_FINAL && !EOF()) {
//			
//			estadoActual = nextEstado(estadoActual,simbolo);
//		}
//		
//		
//		buffer = "";
//		
//		
//		
//		
//		if(EOF())
//			System.out.println("----FIN DE ARCHIVO----");
//		else {
//			
//		
//		
//		//ESTE ITERACION ES UTILIZADA LIMPIAR EL IDENTIFICADOR
//		while(estadoActual == ESTADO_INICIAL && !EOF()) {
//			simbolo = codigoFuente.get(index);
//			buffer += simbolo;
//			estadoActual = nextEstado(estadoActual,simbolo);
//			if(estadoActual == ESTADO_INICIAL) {
//				buffer = "";
//			}
//			index ++;
//		} 
//		
//		//SE RECONOCE EL IDENTIFICADOR
//		while(estadoActual != ESTADO_FINAL && !EOF()) {
//			simbolo = codigoFuente.get(index);
//			estadoActual = nextEstado(estadoActual,simbolo);
//			if(estadoActual != ESTADO_FINAL) {
//				if(buffer.length() < 25)
//					buffer += simbolo;
//				index++;
//			}
//		}
//		
//
//		}
//		
//		System.out.println(buffer);
//		
//		if(TablaDeSimbolos.containsKey(buffer)) 
//			return TablaDeSimbolos.get(buffer);
//		return -1;
//	}
	
	// SEPARA DEL CODIGO FUENTE UN TOKEN SIN MODIFICAR EL MISMO
	public int getToken() { 
		Character simbolo;
		buffer = "";
		int estadoActual = ESTADO_INICIAL;
		
		if(EOF())
			System.out.println("----FIN DE ARCHIVO----");
		else {
		
		//ESTE ITERACION ES UTILIZADA LIMPIAR EL IDENTIFICADOR
		/*while(estadoActual == ESTADO_INICIAL && !EOF()) {
			simbolo = codigoFuente.get(index);
			buffer += simbolo;
			estadoActual = nextEstado(estadoActual,simbolo);
			if(estadoActual == ESTADO_INICIAL) {
				buffer = "";
			}
			index ++;
		} */
		
		//SE RECONOCE EL IDENTIFICADOR
		while(estadoActual != ESTADO_FINAL && !EOF()) {
			simbolo = codigoFuente.get(index);
			ejecutarAccionesSemanticas(estadoActual,simbolo);
			estadoActual = nextEstado(estadoActual,simbolo);
			index++;
		}
		

		}
		
		System.out.println(buffer);
		
		if(TablaDeSimbolos.containsKey(buffer)) 
			return TablaDeSimbolos.get(buffer);
		return -1;
	}
	
	private void ejecutarAccionesSemanticas(int estadoActual,char caracterActual) {
		AccionesSemanticas.get(MatrizAccionesSemanticas[estadoActual][toInt(caracterActual)]).apply(1);
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
	
	public boolean EOF() {
		return index == codigoFuente.size();
	}
	
	private static int[][] inicializarMatrizDeTransicion() {	
		int F = ESTADO_FINAL; // 'F' REPRESENTA EL ESTADO FINAL
		int [][] matrizTransicion = {				
		
		//DENTRO DE LA MATRIZ HAY ESTADOS DE ERROR QUE SE MANEJAN CON ACCIONES SEMANTICAS Y VAN A ESTADO FINAL.
				
		//LM-{F}	lm		Digito	Blanco	/n	Tab	F   _   =   <	>	(	)	{	}	,	;	-   + 	/	.	!	 ' 	
			{1,		1,		7,		0,		0,	0,	1,	0,	5,	3,	4,	F,	F,	F,	F,	F,	F,	F,	F,	F,	8,	F,	10},	// ESTADO 0 INICIAL 
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
		
//		//SIMBOLOS INDIVIDUALES
//		TablaDeSimbolos.put("<",60);
//		TablaDeSimbolos.put(">",62);
//		TablaDeSimbolos.put("(",72);
//		TablaDeSimbolos.put(")",73);
//		TablaDeSimbolos.put("*",74);
//		TablaDeSimbolos.put("+",75);
//		TablaDeSimbolos.put(",",76);
//		TablaDeSimbolos.put("-",77);
//		TablaDeSimbolos.put("/",79);
//		TablaDeSimbolos.put(";",91);
//		TablaDeSimbolos.put("=",93);
//		TablaDeSimbolos.put("{",123);
//		TablaDeSimbolos.put("}",125);
//
//		//COMPARADORES Y ASIGNACION
//		TablaDeSimbolos.put(":=", 256);
//		TablaDeSimbolos.put(">=", 257);
//		TablaDeSimbolos.put("<=", 258);
//		TablaDeSimbolos.put("=!", 259);
//		TablaDeSimbolos.put(">=", 260);
		
		//PALABRAS RESERVADAS
		TablaDeSimbolos.put("if", 261);
		TablaDeSimbolos.put("then", 262);
		TablaDeSimbolos.put("else", 263);
		TablaDeSimbolos.put("end-if", 264);
		TablaDeSimbolos.put("out", 265);
		TablaDeSimbolos.put("fun", 266);
		TablaDeSimbolos.put("return", 267);
		TablaDeSimbolos.put("break", 268);

	}
	
	//ACCIONES SEMANTICAS ATOMICAS/SiMPLES
	
	
	//AS1: LEE EL CARACTER
	private Integer AS_1(Integer i) {
		buffer = "";
		return 0;
	}
	
	//AS2: Inicializa el string 
	private Integer AS_2(Integer i) {
		buffer += codigoFuente.get(index);
		return 0;
	}
	
	//AS3: Concatena los símbolos OK
	private Integer AS_3(Integer i) {
		index--;
		return 0;
	}
	
	//AS4: Devuelve el último carácter leído a la entrada
	private  Integer AS_4(Integer i) {
		index++;
		return 0;
	}
	//AS5: Buscar en la TS
	private static void AS_6() {
		
	}
	
	//AS6: Devuelve el token 
	private static void AS_7() {
		
	}
	
	//AS7: Aumenta el contador de la cantidad de saltos de línea
	private static void AS_8() {
		
	}
	
	//AS8: Parsea string a punto flotante y verifica si cumple el rango, en caso que cumpla llama a AS5.
	private static void AS_9() {
		
	}
	
	//AS9: Parsea string a entero corto y verifica si cumple el rango, en caso que cumpla llama a AS5.
	private static void AS_10() {
		
	}
	//AS10: truncar la cadena de caracteres en caso de superar la cantidad permitida. 
	private static void AS_5() {
		
	}
	
	//AS10: Toma el simbolo, lo ignora y pasa al siguiente estado para leer el proximo caracter.
	private static void AS_11() {
		
	}
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_12() {
		
	}
	
	//ACCIONES SEMANTICAS COMPUESTAS
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_13() {
		
	}
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_14() {
		
	}
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_15() {
		
	}
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_16() {
		
	}
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_17() {
		
	}
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_18() {
		
	}	
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_19() {
		
	}
	
	//AS11: Reconoce un salto de linea, y verifica que el carácter anterior haya sido una “ / “
	private static void AS_20() {
		
	}
	//MATRIZ DE ACCIONES SEMANTICAS
	
	private static int[][] inicializarMatrizDeAccionesSemanticas() {	

		int [][] matrizTransicion = {
				//LM	lm		DIGITO	BLANCO	/n	TAB	F	_	= 	<	>	(	)	{	}	,	;	-	 + 	/	.	!	 ' 	
				{2,		2,		13,		0,		7,	0,	2,	0,	2,	2,	2,	1,	1,	1,	1,	1,	1,	1,	1,	1,	13,	0,	14},	
				{3,		3,		3,		17,		17,	17,	3,	3,	17,	17,	17,	17,	17,	17,	17,	17,	17,	17,	17,	17,	17,	17,	17},	
				{4,		4,		4,		4,		4,	4,	4,	4,	15,	16,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4},	
				{4,		4,		4,		4,		4,	4,	4,	4,	15,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4},	
				{4,		4,		4,		4,		4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	4,	15,	15},	
				{0,		0,		0,		0,		7,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},	
				{18,	18,		3,		18,		18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	18,	3,	18,	18},	
				{19,	19,		3,		19,		19,	19,	3,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19},	
				{19,	19,		3,		19,		19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	3,	3,	19,	19,	19,	19},	
				{19,	19,		3,		19,		19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19,	19},	
				{3,		3,		3,		3,		0,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	10,	3,	3,	5},	
				{0,		0,		0,		10,		20,	10,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	10,	0,	0,	0},	
				{3,		3,		3,		3,		0,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	3,	0,	3,	3,	0}	
		
		};
		
		return matrizTransicion;
	}
	
	private void cargarAccionesSemanticas() {

			Function<Integer,Integer> aux;
			
			aux = this::AS_1;
			AccionesSemanticas.add(aux);

			aux = this::AS_2;
			AccionesSemanticas.add(aux);
			
			aux = this::AS_3;
			AccionesSemanticas.add(aux);

			aux = this::AS_4;
			AccionesSemanticas.add(aux);

	}
	
}
