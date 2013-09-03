package org.squadra.atenea.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.gui.MainGUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class ListOfAction {

	private static HashMap<String, List<Click>> clicks ;
	private static HashMap<String, String> commands = new HashMap<String, String>();
	private static ListOfAction INSTANCE = null;


	public void addAction(String actionName, List<Click> listOfClicks)
	{
		clicks.put(actionName, listOfClicks);
	}

	public List<Click> getAction(String actionName) {
		return clicks.get(actionName);
	}
	
	public String getCommand(String command) {
		return commands.get(command);
	}

	private ListOfAction() {
		File dir = new File("images");
		if (!dir.exists())
		{
			dir.mkdir();
		}

		File archivo = new File("images" + File.separatorChar + "actions.json");
		if (!archivo.exists())
		{
			try {
				archivo.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileReader fr = null;
		BufferedReader br = null;
		try {
			Gson gson = new GsonBuilder().create();
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			clicks = gson.fromJson(br,  new TypeToken<HashMap<String, List<Click>>>(){}.getType());
			if (clicks == null)
				clicks = new HashMap<String, List<Click>>();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File archivoCommands = new File("commands.txt");
		if (!archivoCommands.exists())
		{
			try {
				archivoCommands.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		BufferedReader br2 = null;
		try{
			br2 = new BufferedReader(new FileReader("commands.txt"));
			String line;
			while ((line = br2.readLine()) != null) {
				String a[] = line.split(",");
				commands.put(a[0], a[1]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally
		{
			try {
				br2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static ListOfAction createInstance()	
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ListOfAction();
		}
		return INSTANCE;
	}

	public static ListOfAction getInstance()
	{
		return createInstance();		
	}

	public void writeToFile()
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter writer = new FileWriter("images" + File.separatorChar + "actions.json");
			writer.write(gson.toJson(clicks));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
