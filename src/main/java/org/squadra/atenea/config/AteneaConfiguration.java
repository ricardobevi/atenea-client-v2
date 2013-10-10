package org.squadra.atenea.config;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.extern.log4j.Log4j;

import org.squadra.atenea.base.TextFileUtils;

/**
 * Clase para almacenar las variables de configuracion del sistema leidas
 * de un archivo de texto con formato key:value por linea.
 * @author Leandro Morrone
 *
 */
@Log4j
public class AteneaConfiguration {

	/** Mapa para almacenar las variables obtenidas del archivo de configuracion
	 * con sus respectivos valores */
	private HashMap<String, String> configVariables = new HashMap<>();
	
	/** Ruta del archivo de historial */
	private String configFilePath;
	
	/**
	 * Constructor.
	 * @param configFilePath
	 */
	public AteneaConfiguration(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	
	/**
	 * Carga el archivo de configuracion.
	 * Debe ejecutarse luego del contructor, y antes de cada operacion.
	 */
	public void loadConfigFile() {
		ArrayList<String> lines = TextFileUtils.readTextFile(configFilePath);
		
		for (String line : lines) {
			
			// Salteo las lineas vacias o con el numeral (comentario)
			if (!line.equals("") && !line.equals(" ") 
					&& line.charAt(0) != '#' && line.charAt(0) != 0xFEFF) {
			
				try {
					String[] str = line.split(":");
					configVariables.put(str[0], str[1]);
				}
				catch (Exception e) {
					log.warn("No se pudo cargar la variable de configuracion.");
					log.warn(" -> " + line);
				}
			}
		}
	}
	
	/**
	 * Devuelve el valor de una variable de configuracion.
	 * @param varName Nombre de la variable.
	 * @return Valor de la variable.
	 */
	public String getVariable(String varName) {
		return configVariables.get(varName);
	}
	
	/**
	 * Modifica el valor de una variable y actualiza el archivo.
	 * @param varName Nombre de la variable.
	 * @param value Valor de la variable.
	 * @return Si la variable fue modificada correctamente.
	 */
	public boolean setVariable(String varName, String value) {
		
		// TODO: escribir archivo
		return true;
	}
	
}
