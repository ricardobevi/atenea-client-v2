package org.squadra.atenea.tts;

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
		if(message.getType() == Message.ORDER) {
			processOrder(message.getOrder());
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
			
			Runnable myRunnable = new Runnable(){

				public void run(){
					new Executer().execute(new String[]{orden});
				}
			};
			
			Thread thread = new Thread(myRunnable);
			thread.start();
			
			// Muestro por pantalla el mensaje de salida
			MainGUI.getInstance().setTxtOutput("Entendido");
			Atenea.getInstance().setState(AteneaState.PLAYING);

			try {
				PlayTextMessage.play("Entendido");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Atenea.getInstance().setState(AteneaState.WAITING);
			
		}
		else if (ListOfAction.getInstance().getCommand(orden) != null)
		{
			// Muestro por pantalla el mensaje de salida
			MainGUI.getInstance().setTxtOutput("Entendido");

			Atenea.getInstance().setState(AteneaState.PLAYING);
			try {
				PlayTextMessage.play("Entendido");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Atenea.getInstance().setState(AteneaState.WAITING);
			
			MainGUI.getInstance().minimizeButtonMouseClicked();

			Command cmd = new Command(Atenea.SO_NAME, 
					ListOfAction.getInstance().getCommand(orden), 
					"./salida.txt");
			cmd.run();
		}
		else 
		{
			MainGUI.getInstance().setTxtOutput("No sé como hacer eso. Por favor, ¿Podrías enseñarmelo?");

			//Accion desconocia. Pido enseñarla
			Atenea.getInstance().setState(AteneaState.PLAYING);
			try {
				PlayTextMessage.play("No sé como hacer eso. Por favor, ¿Podrías enseñarmelo?");
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Atenea.getInstance().setState(AteneaState.WAITING);

			MainGUI.getInstance().actionsButtonMouseClicked();
		}
	}

}
