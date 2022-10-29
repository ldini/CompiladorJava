package compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexico {
	private ArrayList<Character> codigoFuente; // el codigo fuente se guarda en una lista de characteres
	private String buffer;
	private HashMap<Integer,HashMap<Character,Integer>> MatrizTransicion;
	private int index; //index es la posicion donde me encuentro en el codigo fuente
	
	public Lexico(String path) throws FileNotFoundException, IOException {
		codigoFuente = getCodigoFuente(path);
		index = 0;
		HashMap<Character,Integer> transicionEstado_0 = new HashMap<Character,Integer>();
		transicionEstado_0.put('c', 1);
		transicionEstado_0.put(' ', 0);
		HashMap<Character,Integer> transicionEstado_1 = new HashMap<Character,Integer>();
		transicionEstado_1.put('c', 1);
		transicionEstado_1.put(' ', 2);
		MatrizTransicion = new HashMap<Integer,HashMap<Character,Integer>>();
		MatrizTransicion.put(0, transicionEstado_0);
		MatrizTransicion.put(1, transicionEstado_1);

	}
	
	public int nextEstado(int estadoActual,char caracterActual) {
		return MatrizTransicion.get(estadoActual).get(caracterActual);
	}
	
	public int getToken() {
		Character c;
		buffer = "";
		int estadoActual = 0;
		while(estadoActual != 2 && codigoFuente.size() > index) {
			c = codigoFuente.get(index);
			index ++;
			estadoActual = nextEstado(estadoActual,c);
			if(estadoActual != 0 && estadoActual != 2)
				buffer += c;
		}
		index--;
		System.out.println(buffer);
		return 1;
	}
	
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
	
	
}
