%{
	package sintactico;
	import java.util.*;
	import lexico.Lexico;
	import java.util.Stack;
%}

%token IF THEN ELSE END_IF OUT FUN RETURN BREAK DISCARD FOR CONTINUE I8 F32 ID CTE CTE_I8 CTE_F32 MAYOR_IGUAL MENOR_IGUAL DISTINTO ASIGNACION 

%start programa

%%
programa: ID '{' conjunto_de_sentencias '}' ';'
		; 


conjunto_de_sentencias: conjunto_de_sentencias_declarativas
		| conjunto_de_sentencias_ejecutables
		| conjunto_de_sentencias_declarativas conjunto_de_sentencias_ejecutables
		;	


conjunto_de_sentencias_declarativas: sentencia_declarativa
		| conjunto_de_sentencias_declarativas sentencia_declarativa
		;


conjunto_de_sentencias_ejecutables: sentencia_ejecutable
		| conjunto_de_sentencias_ejecutables sentencia_ejecutable
		;


bloque_de_sentencias_ejecutables: '{' conjunto_de_sentencias '}'
		; 


sentencia_declarativa: declaracion_de_variable
		| declaracion_de_funcion
		;


declaracion_de_variable: tipo lista_de_variables';' { System.out.print("Declaracion de variable"); }
		;


lista_de_variables: ID
		| lista_de_variables ',' ID
		; 


declaracion_de_funcion: FUN ID '(' lista_de_parametros')' ':' tipo '{' conjunto_de_sentencias RETURN '(' expresion ')' '}' ';' { System.out.print("Declaracion de funcion"); }
		| FUN ID '('')' ':' tipo '{' conjunto_de_sentencias RETURN '(' expresion ')' '}' ';' { System.out.print("Declaracion de funcion"); }
		;


lista_de_parametros: parametro ',' parametro
	 	| parametro
	    ;


parametro: tipo ID
		| CTE
		| CTE_I8
		| CTE_F32
	   	; 


tipo: 	I8
      	| F32
      	;


sentencia_ejecutable: asignacion
		| sentencia_if {System.out.print("  <- SENTENCIA IF");}
		| sentencia_for {System.out.print("  <- SENTENCIA FOR");}
		| sentencia_de_salida {System.out.print("  <- SENTENCIA SALIDA");}
		;


asignacion: ID ASIGNACION expresion ';'
		| ID ASIGNACION invocacion_funcion ';'
		;	


invocacion_funcion: ID '(' ID ')'
		| DISCARD ID '('lista_de_parametros')' ';'
		| ID '('lista_de_parametros')'';'  {System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Invocacion debe ser precedida por discard");}
		;


sentencia_if: IF '(' condicion ')' THEN bloque_de_sentencias_ejecutables ELSE bloque_de_sentencias_ejecutables END_IF
		| IF '(' condicion ')' THEN bloque_de_sentencias_ejecutables END_IF
	    ;


condicion: expresion comparacion expresion
		;


comparacion: MENOR_IGUAL
		| MAYOR_IGUAL
		| DISTINTO
		| '<'
		| '>'
		| '='
		;


sentencia_for: FOR '('ID ASIGNACION I8 ';' condicion_for ';' '+'constante ')' bloque_de_sentencias_ejecutables BREAK';' ';'
		| FOR '('ID ASIGNACION I8 ';' condicion_for ';' '+'constante ')' bloque_de_sentencias_ejecutables ';'
 		| etiqueta FOR '('ID ASIGNACION I8 ';' condicion_for ';' '+'constante ')' bloque_de_sentencias_ejecutables BREAK':'etiqueta';'  
        | FOR ID ASIGNACION I8 ';' condicion_for ';' '+'constante ')' bloque_de_sentencias_ejecutables BREAK';' ';' {System.out.println("ERROR LINEA: " + lexico.getLinea() + " ->FALTA PARENTESIS");}
		| error ; 
		;

etiqueta: ID ':'
		;


condicion_for: I8 comparacion I8
		| I8 comparacion CTE_I8
		;


sentencia_de_salida: OUT '(' CTE ')' ';'
		;

expresion: expresion '+' termino
		| expresion '-' termino
		| termino
		;

termino: termino '*' factor
		| termino '/' factor
		| factor
		;

factor: ID
		| constante
		| '-' ID
		| '-' constante 
		;

constante: CTE_I8
		| CTE_F32
		| CTE
		;



%%
public Lexico lexico;
private Stack <Integer> p;
private HashMap<String, Integer> tablaSimbolos;

public Parser(Lexico lexico){
	this.tablaSimbolos = lexico.getTablaDeSimbolos();
	this.lexico = lexico;
	p = new Stack<Integer>();
}

public int yylex(){
	int token = lexico.getToken();
	yylval = new ParserVal(lexico.getLexema(token));
	return token;
}

// private ParserVal negativo(ParserVal numero){
// 	row = tablaSimbolos.get(numero.ival)
// 	if (row.tipo === i8) 
// 		if(row.val < rangoint)
// 		  if(tablaSimbolos.find(-row.val))
// 			return ParserVal(tablaSimbolos.find(-row.val))
// 			else 
			
// }

// private int negativo(int numero){
	
// }

private void yyerror(String error){
	
}