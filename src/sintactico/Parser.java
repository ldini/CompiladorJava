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
public final static short CTE=271;
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
    0,    0,    1,    1,    1,    2,    2,    2,    5,    5,
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
    8,    0,    9,   10,    0,    0,    0,    2,    0,    0,
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
    0, -257,    0,    0,  -72, -190,    0,    0, -157, -203,
    0,   48, -157, -157,   58,   67, -160,   74,  -53,  -96,
    0,    0,   66,    0,    0,    0,    0, -149,    0,    3,
    0, -133,   20, -157, -139, -139,    8, -138,  106, -141,
    4,    0,    0,    0,    0,  107,    0, -121,    0, -120,
  -40,    0,    0,    0, -139,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -7,  -26,    0,    0,    0, -197,
   49,    0,  110, -133, -125,   94,    5, -113,   40,    0,
    0,    0, -141,    0,    0,  100,    0, -197, -197, -197,
    0,    0,  -99, -197,   50, -197, -197,  101,  121, -106,
  -38,    0,  127,    0,  113, -166,   49,   49,   50,   54,
    0,    0,    0,    0,  114,  -12,  120,  141,    0,    0,
 -106,   62, -139,  -74,  -68,    0, -197, -134,    0,  128,
 -190,  -90,    0,   54,   50,  151, -134, -157,    0,    0,
 -139,  152, -142,  -52, -139,  154,  137,    0,   28, -197,
  138,  139,    0,   89,    0,  140,   75,    0,  142,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   77,   78,  204,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   27,    0,    0,
    0,    0,    0,    0,   81,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -24,
  -41,    0,    0,    0,    0,    0,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -19,    0,    0,    0,    0,    0,
    0,    0,  148,    0,    0,    0,  -35,  -29,  -13,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -50,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  149,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,   52,   43,   10,   25,    0,    0,  -30,   46,
   -3,    0,    0,  170,    0,  -10,    0,    0,   82,    0,
   96,  -85,  125,    0,    0,  -42,    0,    0,   55,   44,
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
   74,    2,   56,  127,   57,   58,   55,    3,    4,  105,
   44,   44,   88,   50,   89,   74,  153,   32,   62,   62,
   96,   62,   88,   62,   89,   97,  135,   37,  104,   15,
   44,    3,    4,   16,    2,   62,   38,   17,   18,   39,
    3,    4,   19,   40,   15,  122,   46,   15,   16,  154,
  146,   16,   17,   18,   45,   17,   18,   19,   75,  157,
   19,   88,   73,   89,    3,    4,   49,   57,   58,  112,
  113,    1,  107,  108,   54,   74,   83,    2,   84,   85,
   98,   41,  100,    3,    4,    5,  103,  106,  110,  114,
   15,  115,   21,  116,   16,  132,   15,  120,   17,   18,
   16,  121,  126,   19,   17,   18,  123,   44,  128,   19,
  143,  129,  138,  144,  131,  133,  137,  149,   44,   44,
  134,  141,  145,  150,   44,  151,  155,  156,  158,  159,
  160,    3,    4,    6,   15,    5,   54,   37,   16,   24,
   80,  147,   17,   18,    0,  140,  130,   19,  111,    0,
    0,    0,    0,   41,    0,    0,    0,    3,    4,   49,
    0,  118,   58,   58,   58,    0,   58,   58,   56,   56,
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
   44,  262,  270,  116,  272,  273,   34,  268,  269,   83,
   35,   36,   43,   74,   45,   59,   59,   40,   42,   43,
   42,   45,   43,   47,   45,   47,  127,   40,   59,  257,
   55,  268,  269,  261,  262,   59,   40,  265,  266,  270,
  268,  269,  270,   40,  257,  106,  266,  257,  261,  150,
  263,  261,  265,  266,   59,  265,  266,  270,  270,   41,
  270,   43,  271,   45,  268,  269,  270,  272,  273,   96,
   97,  256,   88,   89,  125,   40,   40,  262,  270,  270,
   41,  277,   59,  268,  269,  270,  270,   58,  258,   59,
  257,   41,  138,  270,  261,  123,  257,   41,  265,  266,
  261,   59,   59,  270,  265,  266,  123,  132,   59,  270,
  138,   41,  131,  141,  123,  260,   59,  145,  143,  144,
  259,   41,   41,   40,  149,   59,   59,   59,   59,  125,
   59,  125,  125,    0,  257,  125,   59,   59,  261,  260,
   41,  264,  265,  266,   -1,  134,  121,  270,   94,   -1,
   -1,   -1,   -1,  277,   -1,   -1,   -1,  268,  269,  270,
   -1,  270,  274,  275,  276,   -1,  278,  279,  274,  275,
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
"RETURN","BREAK","DISCARD","FOR","CONTINUE","I8","F32","ID","CTE","CTE_I8",
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
"errores_programa : error",
"tipo : I8",
"tipo : F32",
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
"sentencia_salida : OUT '(' CTE ')' ';'",
};

//#line 157 "g11_gramatica.y"
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
{System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Falta la '{' del programa");}
break;
case 7:
//#line 23 "g11_gramatica.y"
{System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Falta la '}' del programa");}
break;
case 8:
//#line 24 "g11_gramatica.y"
{ System.out.println("ERROR"); }
break;
case 11:
//#line 32 "g11_gramatica.y"
{ System.out.print("declaracion"); }
break;
case 52:
//#line 109 "g11_gramatica.y"
{System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Falta el ID que hace referencia al nombre de la funcion");}
break;
case 53:
//#line 110 "g11_gramatica.y"
{System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Falta el ID que hace referencia al nombre de la funcion");}
break;
case 54:
//#line 111 "g11_gramatica.y"
{ System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Falta el ')' de la funcion");}
break;
case 55:
//#line 112 "g11_gramatica.y"
{ System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Falta el '(' de la funcion");}
break;
//#line 577 "Parser.java"
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
