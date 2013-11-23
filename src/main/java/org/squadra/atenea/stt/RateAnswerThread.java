package org.squadra.atenea.stt;

import lombok.extern.log4j.Log4j;

/**
 * Hilo de ejecucion que se comunica con el servidor para enviarle la calificacion de una respuesta
 * para obtener una respuesta y la reproduce.
 * @author Leandro Morrone
 */
@Log4j
public class RateAnswerThread implements Runnable {

	/** Indica si la calificacion a enviar es positiva o negativa */
	private boolean positiveRate;
	
	public RateAnswerThread(boolean positiveRate) {
		this.positiveRate = positiveRate;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Calificar positivo: " + positiveRate);
	}

}
