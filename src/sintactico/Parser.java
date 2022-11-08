//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "g11_gramatica.y"
	package sintactico;
	import java.util.*;
	import lexico.Lexico;
	import java.util.Stack;
//#line 22 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short ELSE=259;
public final static short END_IF=260;
public final static short OUT=261;
public final static short FUN=262;
public final static short RETURN=263;
public final static short BREAK=264;
public final static short DISCARD=265;
public final static short FOR=266;
public final static short CONTINUE=267;
public final static short I8=268;
public final static short F32=269;
public final static short ID=270;
public final static short CADENA=271;
public final static short CTE_I8=272;
public final static short CTE_F32=273;
public final static short MAYOR_IGUAL=274;
public final static short MENOR_IGUAL=275;
public final static short DISTINTO=276;
public final static short ASIGNACION=277;
public final static short AND=278;
public final static short OR=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    1,    2,    2,    5,    5,    5,
    6,    6,    3,    3,    4,    4,   10,   10,   10,   10,
   10,   13,   12,   18,   18,   19,   20,   14,   14,   14,
   14,   17,   17,   17,   24,   24,   21,   25,   25,   23,
   26,   26,   26,   26,   26,   26,   11,   11,   11,   27,
   27,   28,   28,   28,   28,    9,    9,    9,   29,   29,
   29,   30,   30,   22,   22,   16,   16,    8,    8,   31,
    7,    7,    7,   32,   15,
};
final static short yylen[] = {                            2,
    4,    1,    1,    1,    2,    3,    3,    1,    1,    1,
    3,   16,    1,    2,    1,    2,    2,    1,    1,    1,
    1,    6,    7,    1,    3,    3,    2,   13,   11,   12,
   10,    1,    3,    1,    1,    1,    3,    1,    2,    3,
    1,    1,    1,    1,    1,    1,    4,    3,    3,    4,
    1,    3,    3,    2,    2,    3,    3,    1,    3,    3,
    1,    1,    1,    1,    1,    2,    1,    2,    1,    1,
    1,    3,    1,    2,    5,
};
final static short yydefred[] = {                         0,
    0,    8,    9,    0,   10,    0,    0,    2,    0,    0,
   13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   14,   15,    0,   18,   19,   20,   21,    0,   71,    0,
   73,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   27,    7,   16,   17,    0,   11,    0,   70,    0,
    0,   69,   67,    1,    0,   62,   64,   65,   44,   43,
   45,   41,   42,   46,    0,    0,   63,   32,   34,    0,
    0,   61,    0,    0,    0,    0,    0,    0,    0,   49,
   48,   51,    0,   72,   66,    0,   68,    0,    0,    0,
   35,   36,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   55,    0,   47,    0,    0,    0,    0,    0,    0,
   33,   59,   60,   75,    0,    0,    0,    0,   53,   52,
    0,    0,    0,    0,    0,   22,    0,    0,   50,    0,
    0,    0,   23,    0,    0,    0,    0,    0,   26,   25,
    0,    0,    0,    0,    0,    0,    0,   31,    0,    0,
    0,    0,   29,    0,   30,    0,    0,   28,    0,   12,
};
final static short yydgoto[] = {                          7,
   33,    8,    9,   20,   10,   11,   30,   51,   65,   22,
   23,   24,   25,   26,   27,   52,   66,  124,  125,   28,
  117,   67,   68,   94,   69,   70,   81,   82,   71,   72,
   53,   31,
};
final static short yysindex[] = {                      -114,
 -257,    0,    0,  -72,    0, -133,    0,    0, -157, -203,
    0,   32, -157, -157,   39,   48, -163,   70,  -53,  -96,
    0,    0,   58,    0,    0,    0,    0, -141,    0,    3,
    0, -129,    9, -157, -139, -139,    8, -134,  105, -123,
    4,    0,    0,    0,    0,  109,    0, -118,    0, -117,
  -40,    0,    0,    0, -139,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -7,  -26,    0,    0,    0, -140,
   31,    0,  117, -129, -115,  100,    5, -110,   40,    0,
    0,    0, -123,    0,    0,  106,    0, -140, -140, -140,
    0,    0,  -86, -140,   53, -140, -140,  114,  127,  -93,
  -38,    0,  138,    0,  123, -125,   31,   31,   53,   62,
    0,    0,    0,    0,  128,  -12,  132,  145,    0,    0,
  -93,   69, -139,  -67,  -65,    0, -140, -197,    0,  137,
 -133,  -90,    0,   62,   53,  156, -197, -157,    0,    0,
 -139,  157, -142,  -52, -139,  159,  141,    0,   28, -140,
  142,  143,    0,   50,    0,  144,   79,    0,  147,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   82,   83,  210,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   27,    0,    0,
    0,    0,    0,    0,   86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -24,
  -41,    0,    0,    0,    0,    0,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -19,    0,    0,    0,    0,    0,
    0,    0,  158,    0,    0,    0,  -35,  -29,  -13,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -45,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  160,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,   52,   43,   10,   25,    0,    0,  -30,   46,
   -3,    0,    0,  175,    0,  -10,    0,    0,   87,    0,
   99,  -85,  129,    0,    0,  -42,    0,    0,   14,   54,
    0,    0,
};
final static int YYTABLESIZE=298;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   86,   58,  119,   58,   42,   56,  148,   56,    6,   56,
   79,   57,   12,   57,   93,   57,   38,   58,   58,   58,
   58,   39,   90,   56,   56,   56,   56,   40,   43,   57,
   57,   57,   57,   21,  139,   88,   76,   89,   21,   95,
   87,   50,  136,   78,  101,  102,   48,   62,   64,   63,
   13,  142,   62,   64,   63,   35,   36,   14,   21,  109,
   50,   47,   42,   99,   34,   44,   29,   62,   64,   63,
   74,   32,   96,  127,   57,   58,   55,   97,   37,  105,
   44,   44,   88,   50,   89,   74,  153,   38,   62,   62,
  157,   62,   88,   62,   89,   88,  135,   89,  104,   15,
   44,  107,  108,   16,    1,   62,   39,   17,   18,   40,
    2,    3,   19,    5,   15,  122,   45,   15,   16,  154,
  146,   16,   17,   18,   46,   17,   18,   19,    1,   56,
   19,   57,   58,   54,    2,    3,   73,    5,    2,    3,
   49,    5,    2,    3,   74,    5,   75,    1,   83,  112,
  113,   84,   85,    2,    3,    4,    5,   98,  100,  103,
   15,   41,   21,  106,   16,  132,   15,  115,   17,   18,
   16,  110,  114,   19,   17,   18,  116,   44,  120,   19,
  143,  121,  138,  144,  123,  129,  126,  149,   44,   44,
  128,  131,  133,  134,   44,  137,  141,  145,  150,  151,
  155,  156,  158,  159,   15,  160,    3,    4,   16,    6,
    5,  147,   17,   18,   24,   80,   54,   19,   37,  130,
  140,    0,  111,   41,    0,    0,    0,    2,    3,   49,
    5,  118,   58,   58,   58,    0,   58,   58,   56,   56,
   56,    0,   56,   56,   57,   57,   57,    0,   57,   57,
    0,   91,   92,   38,   38,    0,    0,    0,   39,   39,
    0,   59,   60,   61,   40,   40,   59,   60,   61,   18,
    0,    0,    0,   77,    0,   57,   58,   56,    0,   57,
   58,   59,   60,   61,   15,    0,    0,    0,   16,    0,
    0,  152,   17,   18,    0,    0,    0,   19,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   58,   41,   59,   43,  123,   45,
   41,   41,  270,   43,   41,   45,   41,   59,   60,   61,
   62,   41,   65,   59,   60,   61,   62,   41,  125,   59,
   60,   61,   62,    9,  125,   43,   40,   45,   14,   70,
   51,   32,  128,   40,   40,   41,   44,   60,   61,   62,
  123,  137,   60,   61,   62,   13,   14,    6,   34,   90,
   51,   59,   58,   74,   13,   20,  270,   60,   61,   62,
   44,   40,   42,  116,  272,  273,   34,   47,   40,   83,
   35,   36,   43,   74,   45,   59,   59,   40,   42,   43,
   41,   45,   43,   47,   45,   43,  127,   45,   59,  257,
   55,   88,   89,  261,  262,   59,  270,  265,  266,   40,
  268,  269,  270,  271,  257,  106,   59,  257,  261,  150,
  263,  261,  265,  266,  266,  265,  266,  270,  262,  270,
  270,  272,  273,  125,  268,  269,  271,  271,  268,  269,
  270,  271,  268,  269,   40,  271,  270,  262,   40,   96,
   97,  270,  270,  268,  269,  270,  271,   41,   59,  270,
  257,  277,  138,   58,  261,  123,  257,   41,  265,  266,
  261,  258,   59,  270,  265,  266,  270,  132,   41,  270,
  138,   59,  131,  141,  123,   41,   59,  145,  143,  144,
   59,  123,  260,  259,  149,   59,   41,   41,   40,   59,
   59,   59,   59,  125,  257,   59,  125,  125,  261,    0,
  125,  264,  265,  266,  260,   41,   59,  270,   59,  121,
  134,   -1,   94,  277,   -1,   -1,   -1,  268,  269,  270,
  271,  270,  274,  275,  276,   -1,  278,  279,  274,  275,
  276,   -1,  278,  279,  274,  275,  276,   -1,  278,  279,
   -1,  278,  279,  278,  279,   -1,   -1,   -1,  278,  279,
   -1,  274,  275,  276,  278,  279,  274,  275,  276,  266,
   -1,   -1,   -1,  270,   -1,  272,  273,  270,   -1,  272,
  273,  274,  275,  276,  257,   -1,   -1,   -1,  261,   -1,
   -1,  264,  265,  266,   -1,   -1,   -1,  270,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","THEN","ELSE","END_IF","OUT","FUN",
"RETURN","BREAK","DISCARD","FOR","CONTINUE","I8","F32","ID","CADENA","CTE_I8",
"CTE_F32","MAYOR_IGUAL","MENOR_IGUAL","DISTINTO","ASIGNACION","AND","OR",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID '{' conjunto_sentencias_programa '}'",
"programa : errores_programa",
"conjunto_sentencias_programa : conjunto_sentencias_declarativas",
"conjunto_sentencias_programa : conjunto_sentencias_ejecutables",
"conjunto_sentencias_programa : conjunto_sentencias_declarativas conjunto_sentencias_ejecutables",
"errores_programa : '{' conjunto_sentencias_declarativas conjunto_sentencias_ejecutables",
"errores_programa : conjunto_sentencias_declarativas conjunto_sentencias_ejecutables '}'",
"tipo : I8",
"tipo : F32",
"tipo : CADENA",
"sentencia_declarativa : tipo lista_variables ';'",
"sentencia_declarativa : FUN ID '(' lista_parametros ')' ':' tipo '{' conjunto_sentencias_declarativas conjunto_sentencias_ejecutables RETURN '(' expresion ')' '}' ';'",
"conjunto_sentencias_declarativas : sentencia_declarativa",
"conjunto_sentencias_declarativas : conjunto_sentencias_declarativas sentencia_declarativa",
"conjunto_sentencias_ejecutables : ejecutable",
"conjunto_sentencias_ejecutables : conjunto_sentencias_ejecutables ejecutable",
"ejecutable : asignacion ';'",
"ejecutable : sentencia_if",
"ejecutable : sentencia_DISCARD",
"ejecutable : sentencia_for",
"ejecutable : sentencia_salida",
"sentencia_DISCARD : DISCARD ID '(' parametro ')' ';'",
"sentencia_if : IF '(' condicion ')' THEN bloque_if END_IF",
"bloque_if : sentencia_ejecutable_if",
"bloque_if : sentencia_ejecutable_if ELSE sentencia_ejecutable_if",
"sentencia_ejecutable_if : '{' conjunto_sentencias_ejecutables '}'",
"etiqueta : ID ':'",
"sentencia_for : etiqueta FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables BREAK ';' ';'",
"sentencia_for : etiqueta FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables ';'",
"sentencia_for : FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables BREAK ';' ';'",
"sentencia_for : FOR '(' asignacion ';' condicion_for ';' constante ')' conjunto_sentencias_ejecutables ';'",
"condicion : condicion_AUX",
"condicion : condicion operador condicion_AUX",
"condicion : errores_condicion",
"operador : AND",
"operador : OR",
"condicion_for : ID comparador expresion",
"errores_condicion : comparador",
"errores_condicion : comparador expresion",
"condicion_AUX : expresion comparador expresion",
"comparador : '<'",
"comparador : '>'",
"comparador : MENOR_IGUAL",
"comparador : MAYOR_IGUAL",
"comparador : DISTINTO",
"comparador : '='",
"asignacion : ID ASIGNACION expresion ';'",
"asignacion : ID ASIGNACION invocacion_funcion",
"asignacion : ID ASIGNACION sentencia_for",
"invocacion_funcion : ID '(' ID ')'",
"invocacion_funcion : errores_invocacion_funcion",
"errores_invocacion_funcion : '(' ID ')'",
"errores_invocacion_funcion : ID '(' ')'",
"errores_invocacion_funcion : '(' ID",
"errores_invocacion_funcion : ID ')'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : constante",
"constante : CTE_I8",
"constante : CTE_F32",
"parametro : tipo ID",
"parametro : errores_parametro",
"lista_parametros : lista_parametros parametro",
"lista_parametros : parametro",
"errores_parametro : ID",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : errores_lista_variables",
"errores_lista_variables : lista_variables ','",
"sentencia_salida : OUT '(' CADENA ')' ';'",
};

//#line 155 "g11_gramatica.y"
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
//#line 396 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 6:
//#line 22 "g11_gramatica.y"
{System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta la '{' del programa");}
break;
case 7:
//#line 23 "g11_gramatica.y"
{System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta la '}' del programa");}
break;
case 52:
//#line 107 "g11_gramatica.y"
{System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el ID que hace referencia al nombre de la funcion");}
break;
case 53:
//#line 108 "g11_gramatica.y"
{System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el ID que hace referencia al nombre de la funcion");}
break;
case 54:
//#line 109 "g11_gramatica.y"
{ System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el ')' de la funcion");}
break;
case 55:
//#line 110 "g11_gramatica.y"
{ System.out.println("SINTACTICO en la Linea N°" + lexico.getLinea() + "- Falta el '(' de la funcion");}
break;
//#line 569 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
