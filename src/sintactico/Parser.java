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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    2,    2,    3,    3,    6,    4,
    4,    7,   10,   10,    8,    8,   11,   11,   13,   13,
   13,   13,    9,    9,    5,    5,    5,    5,   14,   14,
   18,   18,   18,   15,   15,   19,   20,   20,   20,   20,
   20,   20,   16,   16,   16,   16,   16,   23,   21,   21,
   17,   12,   12,   12,   24,   24,   24,   25,   25,   25,
   25,   22,   22,   22,
};
final static short yylen[] = {                            2,
    5,    1,    1,    2,    1,    2,    1,    2,    3,    1,
    1,    3,    1,    3,   15,   14,    3,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    4,    4,
    4,    6,    5,    9,    7,    3,    1,    1,    1,    1,
    1,    1,   15,   13,   17,   14,    1,    2,    3,    3,
    5,    3,    3,    1,    3,    3,    1,    1,    1,    2,
    2,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   47,    0,    0,    0,    0,   23,   24,
    0,    0,    0,    0,    5,    7,   10,   11,    0,   25,
   26,   27,   28,    0,    0,    0,    0,    0,    0,    0,
   48,    0,    0,    6,    8,   13,    0,    0,   58,   64,
   62,   63,    0,    0,    0,   59,    0,   57,    0,    0,
    0,    0,    0,    0,    0,    0,    1,   12,    0,    0,
   60,   61,   38,   37,   39,   40,   41,   42,    0,    0,
    0,    0,    0,    0,    0,   20,   21,   22,    0,    0,
    0,    0,    0,    0,    0,    0,   29,   30,   14,    0,
    0,    0,    0,    0,   55,   56,   51,    0,   19,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   17,    0,    0,    0,    0,   31,    0,    0,    0,
    0,   35,    0,    0,    0,    0,    0,    0,   33,    0,
    9,    0,    0,    0,   49,   50,    0,    0,   32,    0,
   34,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   44,    0,   16,    0,   46,
    0,    0,   15,   43,    0,    0,    0,   45,
};
final static short yydgoto[] = {                          2,
   12,   13,   14,   15,   16,  109,   17,   18,   19,   37,
   81,   44,   82,   20,   21,   22,   23,   56,   45,   71,
  114,   46,   24,   47,   48,
};
final static short yysindex[] = {                      -219,
  -65,    0, -167,    0,   32,   44, -184,  -39,    0,    0,
  -55,  -32, -167, -174,    0,    0,    0,    0, -173,    0,
    0,    0,    0, -166,   -6, -151,   92, -143, -133,  -10,
    0,   77, -174,    0,    0,    0,    8,   98,    0,    0,
    0,    0, -154,  -28,   99,    0,   12,    0,  100,  -19,
 -126, -132, -127,  104,    4,   88,    0,    0, -122, -120,
    0,    0,    0,    0,    0,    0,    0,    0,   -6,   -6,
   -6, -106,   -6,   -6,   94,    0,    0,    0,   96, -115,
  115,  113,  101, -110,  119, -164,    0,    0,    0, -116,
   12,   12,   26,   39,    0,    0,    0, -188,    0,  105,
 -194, -104,  106, -194,  125,  126, -100, -167, -134,   46,
 -188,    0,  -18,  111, -104,  130,    0,  114,  116,   47,
   39,    0, -167,   51, -207,  133,  118,  120,    0, -104,
    0,  -82,  -83, -167,    0,    0, -149,  138,    0,  123,
    0,  143,  -79,  144, -149,  145,   -6,  146,   39,  148,
 -149,   69,   -6,  -77,   39,  149,   66,   70,  134,  -53,
   39,  135,   67,  136,  137,    0,  -67,    0,  139,    0,
  140,  142,    0,    0,  -69,  147,  150,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -117, -114,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -112,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    3,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  161,    0,    0,    0,    0,    0,    0,    0,    0,
  -36,  -31,  162,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -68,    0,  191,  193,   27,  -85,    0,    0,  -13,    0,
  -48,  -14,  107,    0,    0,    0,    0,    0,    0,   97,
  -62,  -16,   37,   58,   57,
};
final static int YYTABLESIZE=267;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   29,   54,   31,   54,   52,  166,   52,    2,   52,   53,
    3,   53,    4,   53,   69,   55,   70,   54,   54,   54,
   54,   79,   52,   52,   52,   52,   62,   53,   53,   53,
   53,   66,   68,   67,   43,  132,   80,  106,   43,  120,
   35,   66,   68,   67,   58,   58,   69,   58,   70,   58,
    1,   59,  127,   73,  133,  116,   93,    3,   74,   35,
  135,   58,   87,  154,  136,  143,   58,  140,   69,  160,
   70,   25,   80,    9,   10,  167,   76,   77,   78,    9,
   10,    4,    5,   26,  110,   27,    6,   80,    4,    5,
   80,    8,   32,    6,    7,   11,   36,  124,    8,   38,
    9,   10,   11,    9,   10,  105,   76,   77,   78,  157,
  163,   69,   69,   70,   70,   61,   40,   41,   42,   49,
  144,   40,   41,   42,  121,  122,   91,   92,  150,   95,
   96,   50,  152,   51,  156,   57,   52,   60,  158,   72,
   75,   83,   85,   86,   84,    2,   88,   89,    3,   90,
    4,   94,   97,   98,   99,  100,  101,  103,  104,  102,
  107,  108,  111,  113,  115,  117,  118,  119,  123,  126,
  128,  131,  129,  134,  130,  137,  138,  141,  139,  142,
  145,  146,  147,  148,  149,  153,  159,  151,  155,  161,
  162,  169,  164,  168,  170,  171,  172,  173,  174,  175,
  176,   18,   36,   33,   31,   34,    0,  112,  178,  125,
  165,  177,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   30,    0,    0,    0,    0,    0,    0,    0,    0,
   28,    0,   54,   54,   54,    0,    0,   52,   52,   52,
    0,    0,   53,   53,   53,   63,   64,   65,    9,   10,
    0,   76,   77,   78,   53,   63,   64,   65,    0,   54,
   40,   41,   42,   39,   40,   41,   42,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   58,   45,   41,   59,   43,  125,   45,   41,
  125,   43,  125,   45,   43,   30,   45,   59,   60,   61,
   62,   41,   59,   60,   61,   62,   43,   59,   60,   61,
   62,   60,   61,   62,   45,  121,   50,   86,   45,  108,
   14,   60,   61,   62,   42,   43,   43,   45,   45,   47,
  270,   44,  115,   42,  123,  104,   71,  123,   47,   33,
  268,   59,   59,  149,  272,  134,   59,  130,   43,  155,
   45,   40,   86,  268,  269,  161,  271,  272,  273,  268,
  269,  256,  257,   40,   98,  270,  261,  101,  256,  257,
  104,  266,  125,  261,  262,  270,  270,  111,  266,  266,
  268,  269,  270,  268,  269,  270,  271,  272,  273,   41,
   41,   43,   43,   45,   45,  270,  271,  272,  273,  271,
  137,  271,  272,  273,  259,  260,   69,   70,  145,   73,
   74,   40,  147,  277,  151,   59,  270,   40,  153,   41,
   41,  268,  270,   40,  277,  263,   59,  270,  263,  270,
  263,  258,   59,   58,  270,   41,   44,  268,   40,   59,
  277,  123,   58,  268,   59,   41,   41,  268,  123,   59,
   41,  125,   59,  123,   59,   43,   59,  260,   59,  263,
   43,   59,   40,  263,   41,   40,  264,   43,   41,   41,
  125,  125,   59,   59,   59,   59,  264,   59,   59,   58,
  270,   41,   41,   13,   58,   13,   -1,  101,   59,  113,
  264,  175,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  277,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  270,   -1,  274,  275,  276,   -1,   -1,  274,  275,  276,
   -1,   -1,  274,  275,  276,  274,  275,  276,  268,  269,
   -1,  271,  272,  273,  265,  274,  275,  276,   -1,  270,
  271,  272,  273,  270,  271,  272,  273,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=277;
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
"CTE_F32","MAYOR_IGUAL","MENOR_IGUAL","DISTINTO","ASIGNACION",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID '{' conjunto_de_sentencias '}' ';'",
"conjunto_de_sentencias : conjunto_de_sentencias_declarativas",
"conjunto_de_sentencias : conjunto_de_sentencias_ejecutables",
"conjunto_de_sentencias : conjunto_de_sentencias_declarativas conjunto_de_sentencias_ejecutables",
"conjunto_de_sentencias_declarativas : sentencia_declarativa",
"conjunto_de_sentencias_declarativas : conjunto_de_sentencias_declarativas sentencia_declarativa",
"conjunto_de_sentencias_ejecutables : sentencia_ejecutable",
"conjunto_de_sentencias_ejecutables : conjunto_de_sentencias_ejecutables sentencia_ejecutable",
"bloque_de_sentencias_ejecutables : '{' conjunto_de_sentencias '}'",
"sentencia_declarativa : declaracion_de_variable",
"sentencia_declarativa : declaracion_de_funcion",
"declaracion_de_variable : tipo lista_de_variables ';'",
"lista_de_variables : ID",
"lista_de_variables : lista_de_variables ',' ID",
"declaracion_de_funcion : FUN ID '(' lista_de_parametros ')' ':' tipo '{' conjunto_de_sentencias RETURN '(' expresion ')' '}' ';'",
"declaracion_de_funcion : FUN ID '(' ')' ':' tipo '{' conjunto_de_sentencias RETURN '(' expresion ')' '}' ';'",
"lista_de_parametros : parametro ',' parametro",
"lista_de_parametros : parametro",
"parametro : tipo ID",
"parametro : CTE",
"parametro : CTE_I8",
"parametro : CTE_F32",
"tipo : I8",
"tipo : F32",
"sentencia_ejecutable : asignacion",
"sentencia_ejecutable : sentencia_if",
"sentencia_ejecutable : sentencia_for",
"sentencia_ejecutable : sentencia_de_salida",
"asignacion : ID ASIGNACION expresion ';'",
"asignacion : ID ASIGNACION invocacion_funcion ';'",
"invocacion_funcion : ID '(' ID ')'",
"invocacion_funcion : DISCARD ID '(' lista_de_parametros ')' ';'",
"invocacion_funcion : ID '(' lista_de_parametros ')' ';'",
"sentencia_if : IF '(' condicion ')' THEN bloque_de_sentencias_ejecutables ELSE bloque_de_sentencias_ejecutables END_IF",
"sentencia_if : IF '(' condicion ')' THEN bloque_de_sentencias_ejecutables END_IF",
"condicion : expresion comparacion expresion",
"comparacion : MENOR_IGUAL",
"comparacion : MAYOR_IGUAL",
"comparacion : DISTINTO",
"comparacion : '<'",
"comparacion : '>'",
"comparacion : '='",
"sentencia_for : FOR '(' ID ASIGNACION I8 ';' condicion_for ';' '+' constante ')' bloque_de_sentencias_ejecutables BREAK ';' ';'",
"sentencia_for : FOR '(' ID ASIGNACION I8 ';' condicion_for ';' '+' constante ')' bloque_de_sentencias_ejecutables ';'",
"sentencia_for : etiqueta FOR '(' ID ASIGNACION I8 ';' condicion_for ';' '+' constante ')' bloque_de_sentencias_ejecutables BREAK ':' etiqueta ';'",
"sentencia_for : FOR ID ASIGNACION I8 ';' condicion_for ';' '+' constante ')' bloque_de_sentencias_ejecutables BREAK ';' ';'",
"sentencia_for : error",
"etiqueta : ID ':'",
"condicion_for : I8 comparacion I8",
"condicion_for : I8 comparacion CTE_I8",
"sentencia_de_salida : OUT '(' CTE ')' ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : constante",
"factor : '-' ID",
"factor : '-' constante",
"constante : CTE_I8",
"constante : CTE_F32",
"constante : CTE",
};

//#line 152 "g11_gramatica.y"
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
//#line 393 "Parser.java"
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
case 12:
//#line 42 "g11_gramatica.y"
{ System.out.print("Declaracion de variable"); }
break;
case 15:
//#line 51 "g11_gramatica.y"
{ System.out.print("Declaracion de funcion"); }
break;
case 16:
//#line 52 "g11_gramatica.y"
{ System.out.print("Declaracion de funcion"); }
break;
case 26:
//#line 74 "g11_gramatica.y"
{System.out.print("  <- SENTENCIA IF");}
break;
case 27:
//#line 75 "g11_gramatica.y"
{System.out.print("  <- SENTENCIA FOR");}
break;
case 28:
//#line 76 "g11_gramatica.y"
{System.out.print("  <- SENTENCIA SALIDA");}
break;
case 33:
//#line 87 "g11_gramatica.y"
{System.out.println("ERROR LINEA: " + lexico.getLinea() + " -> Invocacion debe ser precedida por discard");}
break;
case 46:
//#line 112 "g11_gramatica.y"
{System.out.println("ERROR LINEA: " + lexico.getLinea() + " ->FALTA PARENTESIS");}
break;
//#line 574 "Parser.java"
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
