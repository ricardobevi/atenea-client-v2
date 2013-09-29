package org.squadra.atenea.tts;

import java.net.InetAddress;
import java.util.Date;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.actions.Command;
import org.squadra.atenea.actions.Executer;
import org.squadra.atenea.actions.ListOfAction;
import org.squadra.atenea.ateneacommunication.Message;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.history.HistoryItem;

/**
 * Clase que se encarga de procesar el mensaje retornado por el servidor. Actua segun
 * el tipo de mensaje devuelto, por ejemplo, reproduce una respuesta, ejecuta una
 * accion, etc.
 * @author Leandro Morrone
 */
public class MessageProcessor {

	/**
	 * Este metodo procesa el mensaje de salida segun su tipo.
	 * Si es una orden la ejecuta y reproduce el mensaje de voz.
	 * @param message Mensaje de salida devuelto por el servidor
	 */
	public static void processMessage(Message message) {

		String outputText = message.getText();

		// Si es una orden la ejecuto
		if (message.getType() == Message.ORDER) {
			processOrder(message.getOrder());
		}
		else {
			showAndSpeak(outputText);
		}


		// Agrego un item al historial

		if(message.getType() == Message.ERROR) {
			Atenea.getInstance().getHistory().addItem(new HistoryItem(
					"Atenea", 
					HistoryItem.OUTPUT_ERROR,
					outputText, new Date()));
		}
		else if (message.getType() == Message.ORDER) {
			Atenea.getInstance().getHistory().addItem(new HistoryItem(
					"Atenea", 
					HistoryItem.OUTPUT_ACTION,
					outputText, new Date()));
		}
		else {
			Atenea.getInstance().getHistory().addItem(new HistoryItem(
					"Atenea", 
					HistoryItem.OUTPUT_MESSAGE,
					outputText, new Date()));
		}


	}

	/** Busca la orden o comando en el archivo JSON y la ejecuta */
	private static void processOrder(final String orden) {
		System.out.println(orden);
		if (ListOfAction.getInstance().getAction(orden) != null)
		{
			MainGUI.getInstance().minimizeButtonMouseClicked();	
			
			Runnable executerThread = new Runnable() {

				public void run(){
					new Executer().execute(new String[]{orden});
				}
			};
			
			new Thread(executerThread).start();
			
			// Muestro por pantalla el mensaje de salida
			showAndSpeak("Entendido");
			
		}
		else if (ListOfAction.getInstance().getCommand(orden) != null)
		{
			// Muestro por pantalla el mensaje de salida
			showAndSpeak("Entendido");
			
			MainGUI.getInstance().minimizeButtonMouseClicked();

			Command cmd = new Command(Atenea.SO_NAME, 
					ListOfAction.getInstance().getCommand(orden), 
					"./salida.txt");
			cmd.run();
		}
		else 
		{
			//Accion desconocia. Pido enseñarla
			showAndSpeak("No sé como hacer eso. Por favor, ¿Podrías enseñarmelo?");
			MainGUI.getInstance().actionsButtonMouseClicked();
		}
	}
	
	
	public static void showAndSpeak (final String text) {
		// Muestro por pantalla el mensaje de salida
		MainGUI.getInstance().setTxtOutput(text);

		Runnable playThread = new Runnable() {

			public void run(){
				Atenea.getInstance().setState(AteneaState.PLAYING);
				try {
					PlayTextMessage.play(text);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Atenea.getInstance().setState(AteneaState.WAITING);
			}
		};
		
		new Thread(playThread).start();
			
	}

}
