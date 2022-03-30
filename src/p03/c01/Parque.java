package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{

	private static final int AFOROMAX = 50;
	private static final int AFOROMIN = 0;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;


	public Parque() {
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
	}


	@Override
	public synchronized void entrarAlParque(String puerta) throws InterruptedException{

		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}

		comprobarAntesDeEntrar();


		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");


		checkInvariante();

		notifyAll();

	}

	
	@Override
	public synchronized void  salirDelParque(String puerta) throws InterruptedException{

		// Si no hay salidas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}

		comprobarAntesDeSalir();

		// Decrementamos el contador total y el individual
		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		checkInvariante();

		notifyAll();
		
	}


	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);

		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}

	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
		Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresPuerta += iterPuertas.nextElement();
		}
		return sumaContadoresPuerta;
	}

	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales <= AFOROMAX :"PRE: El numero de personas dentro del parque es el maximo del aforo permitido";
		assert contadorPersonasTotales >= AFOROMIN :"PRE: El numero de personas dentro del parque es el minimo, no hay personas";

	}

	protected synchronized void comprobarAntesDeEntrar() throws InterruptedException{	
		while(contadorPersonasTotales >= AFOROMAX) {
			wait();
		}
	}

	protected synchronized void comprobarAntesDeSalir() throws InterruptedException{		
		while(contadorPersonasTotales <= AFOROMIN) {
			wait();
		}
		
	}


}
