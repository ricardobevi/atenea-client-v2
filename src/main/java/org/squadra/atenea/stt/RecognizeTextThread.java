package org.squadra.atenea.stt;

import java.util.Date;

import lombok.extern.log4j.Log4j;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.ateneacommunication.Message;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.history.HistoryItem;
import org.squadra.atenea.tts.MessageProcessor;

/**
 * Hilo de ejecucion que se comunica con el servidor y le envia el texto recibido de la interfaz
 * para obtener una respuesta y la reproduce.
 * @author Leandro Morrone
 */
@Log4j
public class RecognizeTextThread implements Runnable {

	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea = Atenea.getInstance();

	@Override
	public void run() {
		
		//String textMessage = MainGUIPrototype.getInstance().getTxtEntradaTexto();
		String textMessage = MainGUI.getInstance().getTxtInput();
		
		// Agrego un item al historial
		Atenea.getInstance().getHistory().addItem(new HistoryItem(
						Atenea.getInstance().getUser(), 
						HistoryItem.INPUT_TEXT_MESSAGE,
						textMessage, new Date()));
		
		Message outputMessage = new Message();
		
		try {
			Message inputMessage = new Message(textMessage);
			inputMessage.addMetadata("userName", atenea.getUser());
			
			// ESTA LINEA ENVIA EL MENSAJE AL SERVIDOR Y RECIBE LA RESPUESTA
			outputMessage = atenea.getClient().dialog(inputMessage);

		} catch (Exception e) {
			outputMessage = new Message("No logro conectarme al servidor.", Message.ERROR);
			log.error("Error de conexion con el servidor");
		}
		
		MessageProcessor.processMessage(outputMessage);
	}

}
