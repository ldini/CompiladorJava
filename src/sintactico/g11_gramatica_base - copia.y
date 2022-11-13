%{
	package Semantico;
	import util.*;
	import Compilador.*;
	import Simbolo.*;
	import java.util.Stack;

%}

%token IF then ELSE end_if out fun RETURN BREAK discard FOR CONTINUE AND OR ID CTE_INT CTE_FLOAT INT FLOAT CADENA MAYOR_IGUAL MENOR_IGUAL DISTINTO ASIGNACION
%start programa

%%
programa: ID '{' conjunto_sentencias_programa '}'
		  | errores_programa
		  ;

conjunto_sentencias_programa: conjunto_sentencias_declarativas
							  | conjunto_sentencias_ejecutables
							  | conjunto_sentencias_declarativas conjunto_sentencias_ejecutables
							  ;	

errores_programa: '{' conjunto_sentencias_declarativas conjunto_sentencias_ejecutables {Notificador.addError(lexico.getLineaActual(), ("SINTACTICO en la Linea N°" + lexico.getLineaActual() + "- Falta la '{' del programa"));}
				  | conjunto_sentencias_declarativas conjunto_sentencias_ejecutables '}' {Notificador.addError(lexico.getLineaActual(), ("SINTACTICO en la Linea N°" + lexico.getLineaActual() + "- Falta la '}' del programa"));}
				  ;
tipo: INT
      | FLOAT
      | CADENA
      ;

sentencia_declarativa: tipo lista_variables ';'
		             | fun ID '(' lista_parametros')' ':' tipo '{' conjunto_sentencias_declarativas conjunto_sentencias_ejecutables RETURN '(' expresion ')' '}' ';'

					 ;

conjunto_sentencias_declarativas: sentencia_declarativa
				  | conjunto_sentencias_declarativas sentencia_declarativa
				  ;

conjunto_sentencias_ejecutables: ejecutable
								 | conjunto_sentencias_ejecutables ejecutable
								 ;

ejecutable: asignacion ';'
	        | sentencia_if
			| sentencia_discard
			| sentencia_for
			| sentencia_salida
	        ;

sentencia_discard: discard ID '(' parametro ')' ';'
				   ;

sentencia_if: IF '(' condicion ')' then bloque_if end_if
	          ;

bloque_if: sentencia_ejecutable_if
		   | sentencia_ejecutable_if ELSE sentencia_ejecutable_if

sentencia_ejecutable_if: '{' conjunto_sentencias_ejecutables '}'

etiqueta: ID ':' ;

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

errores_invocacion_funcion: '(' ID ')' {Notificador.addError(lexico.getLineaActual(), ("SINTACTICO en la Linea N°" + lexico.getLineaActual() + "- Falta el ID que hace referencia al nombre de la Funcion"));}
			    | ID '(' ')' {Notificador.addError(lexico.getLineaActual(), ("SINTACTICO en la Linea N°" + lexico.getLineaActual() + "- Falta el ID que hace referencia al nombre de la Funcion"));}
			    | '(' ID {Notificador.addError(lexico.getLineaActual(), ("SINTACTICO en la Linea N°" + lexico.getLineaActual() + "- Falta el ')' de la Funcion"));}
			    | ID ')' {Notificador.addError(lexico.getLineaActual(), ("SINTACTICO en la Linea N°" + lexico.getLineaActual() + "- Falta el '(' de la Funcion"));}
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

constante: CTE_INT
	   | CTE_FLOAT
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

sentencia_salida: out '(' CADENA ')' ';'
	

%%
public AnalizadorLexico lexico;
private TablaSimbolos tablaSimbolos;
private Stack <Integer> p;

public Parser(AnalizadorLexico lexico, TablaSimbolos tablaSimbolos){
	this.tablaSimbolos = tablaSimbolos;
	this.lexico = lexico;
	p = new Stack<Integer>();
}

public int yylex(){
	int token = lexico.tokenGenerado();
	yylval = new ParserVal(lexico.ultimoLexemaGenerado);
	return token;
}

private void yyerror(String error){
	Notificador.addError(lexico.getLineaActual(), error);
}