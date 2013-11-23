package org.squadra.atenea;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.squadra.atenea.ateneacommunication.AteneaWs;
import org.squadra.atenea.config.AteneaConfiguration;
import org.squadra.atenea.gui.Resources;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.gui.SplashGUI;
import org.squadra.atenea.history.History;
import org.squadra.atenea.stt.Microphone;
import org.squadra.atenea.util.StaticMethods;


/**
 * Esta clase carga la configuracion del programa, contiene el estado del sistema y 
 * lanza la interfaz de usuario. Es un singleton para que pueda ser llamada desde 
 * cualquier parte del proyecto.
 * @author Leandro Morrone
 */
public @Data class Atenea {

	/** Constante con el nombre del sistema operativo sobre el que corre la aplicacion */
	public static final String SO_NAME = StaticMethods.getSOName();
	
	/** Variable que contiene el cliente del Web Service */
	@Getter @Setter private AteneaWs client;
	
	/** Variable que almacena el estado actual del sistema */
	private AteneaState state;
	
	/** Nombre del usuario que utiliza el software */
	@Getter @Setter private String user;
	
	/** Historial de conversacion y acciones */
	@Getter @Setter private History history;
	
	/** Variables de configuracion del cliente */
	@Getter @Setter private AteneaConfiguration configuration;
	
	/** Objeto microfono que contiene las funciones de captura de audio */
	@Getter @Setter private Microphone microphone;
	
	
	/** Singleton */
	private static Atenea INSTANCE = null;
	
	/**
	 * Crea una instancia del objeto Atenea, y si ya existe la devuelve
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static Atenea createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Atenea();
		}
		return INSTANCE;
	}
	
	/**
	 * Devuelve la instancia de Atenea
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static Atenea getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Constructor privado: Carga la configuracion, el historial 
	 * e inicializa las variables necesarias.
	 * @author Leandro Morrone
	 */
	private Atenea() {
		
		// Cargo la configuracion
		SplashGUI.getInstance().setProgressBarPercent(50, "Cargando configuración de usuario...");
		this.configuration = new AteneaConfiguration(Resources.Configuration.clientConfig);
		this.configuration.loadConfigFile();
		System.out.println(configuration);
		
		this.user = configuration.getVariable("userName");
		
		// Cargo el historial
		SplashGUI.getInstance().setProgressBarPercent(70, "Cargando historial...");
		this.history = new History(Resources.HistoryElements.jsonPath);
		this.history.loadHistoryFile();
		
		this.state = new AteneaState();
		this.microphone = new Microphone();
	}

	/**
	 * Lanza la interfaz de usuario principal
	 * @author Leandro Morrone
	 */
	public void launchMainGUI() {
		SplashGUI.getInstance().setProgressBarPercent(90, "Creando interfaz de usuario...");
		MainGUI.createInstance();
		//MainGUIPrototype.createInstance();
	}

	/**
	 * Obtiene el estado del sistema en formato de entero
	 * @return estado actual del sistema
	 * @author Leandro Morrone
	 */
	public int getState() {
		return state.getState();
	}
	
	/**
	 * Obtiene el estado del sistema en formato de texto
	 * @return estado actual del sistema
	 * @author Leandro Morrone
	 */
	public String getStateText() {
		return state.toString();
	}

	/**
	 * Modifica el estado actual del sistema
	 * @param state nuevo estado
	 * @author Leandro Morrone
	 */
	public void setState(int state) {
		this.state.setState(state);
		MainGUI.getInstance().changeGUIByState(this.getState());
		//MainGUIPrototype.getInstance().setTxtEstadoDelSistema(this.getStateText());
	}
	
}
