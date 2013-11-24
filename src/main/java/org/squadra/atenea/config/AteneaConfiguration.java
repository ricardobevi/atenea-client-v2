package org.squadra.atenea.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import lombok.ToString;
import lombok.extern.log4j.Log4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Clase para almacenar las variables de configuracion del sistema leidas
 * de un archivo JSON.
 * @author Leandro Morrone
 *
 */
@Log4j
@ToString
public class AteneaConfiguration {

	/** Mapa para almacenar las variables obtenidas del archivo de configuracion
	 * con sus respectivos valores */
	private HashMap<String, String> configVariables = new HashMap<String, String>();
	
	/** Ruta del archivo de configuracion */
	private String configFilePath;
	
	/**
	 * Constructor.
	 * @param configFilePath
	 */
	public AteneaConfiguration(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	
	/**
	 * Carga el archivo JSON de configuracion.
	 * Debe ejecutarse luego del contructor, y antes de cada operacion.
	 */
	public void loadConfigFile() {
		
		Gson gson = new GsonBuilder().create();
		
		try {
	        // Cargo el archivo JSON en el objeto History
	        FileReader fr = new FileReader(configFilePath);
			BufferedReader br = new BufferedReader(fr);
			
			configVariables = gson.fromJson(br, new TypeToken<HashMap<String, String>>(){}.getType());
			
			br.close();
	 
		} catch (IOException e) {
			log.error("Error al cargar archivo de configuracion.");
			e.printStackTrace();
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
	 * Setea el valor de una variable de configuracion existente.
	 * @param varName Nombre de la variable.
	 * @param value Nuevo valor de la variable.
	 */
	public void updateVariable(String varName, String value) {
		if (configVariables.containsKey(varName)) {
			configVariables.put(varName, value);
		}
	}
	
	/**
	 * Actualiza el archivo JSON de configuracion.
	 * @return Si la variable fue modificada correctamente.
	 */
	public void updateConfigFile() {
		
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			// Convierto el objeto History a formato Json
			String json = gson.toJson(configVariables);
			System.out.println(json);
	
			// Escribo el archivo JSON
			FileWriter writer = new FileWriter(configFilePath);
			writer.write(json);
			writer.close();
	 
		} catch (IOException e) {
			log.error("Error al escribir archivo de configuracion.");
			e.printStackTrace();
		} 
	}
	
}
