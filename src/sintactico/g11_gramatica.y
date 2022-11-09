%{
	package sintactico;
	import java.util.*;
	import lexico.Lexico;
	import java.util.Stack;
%}

%token IF THEN ELSE END_IF OUT FUN RETURN BREAK DISCARD FOR CONTINUE I8 F32 ID CADENA CTE_I8 CTE_F32 MAYOR_IGUAL MENOR_IGUAL DISTINTO ASIGNACION AND OR  

%start programa

%%
programa: ID '{' conjunto_sentencias_programa '}'
		  | errores_programa
		  ;

conjunto_sentencias_programa: conjunto_sentencias_declarativas
							  | conjunto_sentencias_ejecutables
							  | conjunto_sentencias_declarativas conjunto_sentencias_ejecutables
							  ;	

errores_programa: '{' conjunto_sentencias_declarativas conjunto_sentencias_ejecutables {System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta la '{' del programa");}
				  | conjunto_sentencias_declarativas conjunto_sentencias_ejecutables '}' {System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta la '}' del programa");}
				  | error { System.out.println("ERROR"); }
				  ;
tipo: I8
      | F32
      ;



sentencia_declarativa: tipo lista_variables ';' { System.out.println("declaracion"); }
		             | FUN ID '(' lista_parametros')' ':' tipo '{' conjunto_sentencias_declarativas conjunto_sentencias_ejecutables RETURN '(' expresion ')' '}' ';'
					 ;

conjunto_sentencias_declarativas: sentencia_declarativa
				  | conjunto_sentencias_declarativas sentencia_declarativa
				  ;

conjunto_sentencias_ejecutables: ejecutable
								 | conjunto_sentencias_ejecutables ejecutable
								 ;

ejecutable: asignacion ';'
	        | sentencia_if
			| sentencia_DISCARD
			| sentencia_for
			| sentencia_salida
	        ;

sentencia_DISCARD: DISCARD ID '(' parametro ')' ';'
				;

sentencia_if: IF '(' condicion ')' THEN bloque_if END_IF
	          ;

bloque_if: sentencia_ejecutable_if
		   | sentencia_ejecutable_if ELSE sentencia_ejecutable_if
		   ;

sentencia_ejecutable_if: '{' conjunto_sentencias_ejecutables '}'
						;

etiqueta: ID ':'
	;

sentencia_for: etiqueta FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables BREAK';' ';'
			   | etiqueta FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables ';'
			   | FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables BREAK';' ';'
			   | FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables ';'
	           ;

condicion: condicion_AUX
		   | condicion operador condicion_AUX
		   | errores_condicion
		   ;

operador: AND
		  | OR
		  ;

condicion_for: ID comparador expresion
			  ;

errores_condicion: comparador
		           | comparador expresion
		           ;

condicion_AUX: expresion comparador expresion
	       ;  

comparador: '<'
			| '>'
			| MENOR_IGUAL
			| MAYOR_IGUAL
			| DISTINTO
			| '='
			;

asignacion: ID ASIGNACION expresion ';'
	        | ID ASIGNACION invocacion_funcion
			| ID ASIGNACION sentencia_for
	        ;	
			
invocacion_funcion: ID '(' ID ')'
		    | errores_invocacion_funcion
	        ;

errores_invocacion_funcion: '(' ID ')' {System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el ID que hace referencia al nombre de la funcion");}
			    | ID '(' ')' {System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el ID que hace referencia al nombre de la funcion");}
			    | '(' ID { System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el ')' de la funcion");}
			    | ID ')' { System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el '(' de la funcion");}
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
	;

constante: CTE_I8
	   | CTE_F32
	   ;

parametro: tipo ID
	   | errores_parametro
	   ; 

lista_parametros: lista_parametros parametro
	   | parametro
	   ;

errores_parametro: ID 
		   ;

lista_variables: ID
		 | lista_variables ',' ID
		 | errores_lista_variables
		 ; 

errores_lista_variables: lista_variables ',' 
		 ;

sentencia_salida: OUT '(' CADENA ')' ';'
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

private void yyerror(String error){
	
}