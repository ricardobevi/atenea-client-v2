package org.squadra.atenea.tts;

import java.util.ArrayList;
import java.util.Date;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.actions.Command;
import org.squadra.atenea.actions.Executer;
import org.squadra.atenea.ateneacommunication.Message;
import org.squadra.atenea.base.ResourcesActions;
import org.squadra.atenea.base.actions.Click;
import org.squadra.atenea.base.actions.ListOfAction;
import org.squadra.atenea.base.actions.PreloadAction;
import org.squadra.atenea.gui.ActionsGUI;
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
		System.out.println("procesamos");
		// Si es una orden la ejecuto
		if (message.getType() == Message.ORDER) 
		{
			System.out.println("procesamos la orden");
			processOrder(message);
		}
		else if (message.getType() == Message.PRELOAD_ACTION || message.getType() == Message.PRELOAD_ACTION_WITH_PARAM )
		{
			processPreloadOrder(message);
		}
		else if(message.getType() == Message.LEARN_ACTION)
		{
			System.out.println("hay q aprender la orden");
			showSpeakAndLearn(outputText);
			MainGUI.getInstance().actionsButtonMouseClicked();
			ActionsGUI.getInstance().setActionText( message.getOrder() );
		}
		else 
		{
			System.out.println(message.getType());
			showAndSpeak(outputText);
		}


		// Agrego un item al historial

		if(message.getType() == Message.ERROR) {
			Atenea.getInstance().getHistory().addItem(new HistoryItem(
					"Atenea", 
					HistoryItem.OUTPUT_ERROR,
					outputText, new Date()));
		}
		else if (message.getType() == Message.ORDER || message.getType() == Message.PRELOAD_ACTION) {
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

	private static void processPreloadOrder(Message msg)
	{
		PreloadAction action = ListOfAction.getInstance().getPreLoadAction(msg.getOrder());
		PreloadAction actionWithParam = ListOfAction.getInstance().getPreLoadActionWithParam(msg.getOrder());
		
		if (action != null)
		{
			System.out.println("Es una accion precargada");
			// Muestro por pantalla el mensaje de salida
			MainGUI.getInstance().minimizeButtonMouseClicked();
			action.execute();

			showAndSpeak(msg.getText());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {		}
			MainGUI.getInstance().maximizeButtonMouseClicked();
		}
		// Es una
		else if (actionWithParam != null){
			
			System.out.println("Es una accion precargada con parametros");
			// Muestro por pantalla el mensaje de salida
			MainGUI.getInstance().minimizeButtonMouseClicked();
			actionWithParam.execute();
			
			showAndSpeak(msg.getText());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {		}
			MainGUI.getInstance().maximizeButtonMouseClicked();
			
		}
		// Es un comando
		else
		{
			System.out.println("Es un comando");
			// Muestro por pantalla el mensaje de salida
			showAndSpeak(msg.getText());

			Command cmd = new Command(Atenea.SO_NAME, 
					ListOfAction.getInstance().getCommand(msg.getOrder()), 
					ResourcesActions.Actions.output_command_file);
			cmd.run();
		}
	}

	/** Busca la orden o comando en el archivo JSON y la ejecuta */
	private static void processOrder(final Message msg) {

		// si es una macro
		System.out.println("Es una macro");
		// Muestro por pantalla el mensaje de salida
		showAndSpeak(msg.getText());

		MainGUI.getInstance().minimizeButtonMouseClicked();
		//Deserealizo lo q llego en el msg
		ArrayList<String> clicks = msg.getIcons();
		ArrayList<Click> orden = new ArrayList<Click>();
		for (String click : clicks) {
			orden.add(Click.deserialize(click));
		}

		new Executer().executeListOfClicks(orden);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {			}
		MainGUI.getInstance().maximizeButtonMouseClicked();


	}


	public static void showAndSpeak (final String text) {
		// Muestro por pantalla el mensaje de salida
		MainGUI.getInstance().setTxtOutput(text);

		Runnable playThread = new Runnable() {

			public void run() {
				
				if (Boolean.parseBoolean(
						Atenea.getInstance().getConfiguration().getVariable("playResponses"))) {
					
					Atenea.getInstance().setState(AteneaState.PLAYING);
					try {
						PlayTextMessage.play(text);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Atenea.getInstance().setState(AteneaState.WAITING);
			}
		};

		new Thread(playThread).start();

	}

	public static void showSpeakAndLearn (final String text) {
		// Muestro por pantalla el mensaje de salida
		MainGUI.getInstance().setTxtOutput(text);

		Runnable playThread = new Runnable() {

			public void run() {

				if (Boolean.parseBoolean(
						Atenea.getInstance().getConfiguration().getVariable("playResponses"))) {
					
					Atenea.getInstance().setState(AteneaState.PLAYING);
					try {
						PlayTextMessage.play(text);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		new Thread(playThread).start();

	}


}
