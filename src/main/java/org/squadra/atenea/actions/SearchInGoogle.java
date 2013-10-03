package org.squadra.atenea.actions;

import java.net.*;
import java.awt.Desktop;

public class SearchInGoogle extends PreloadAction {

	@Override
	public void execute() {
		try {
			String url = "http://www.google.com.ar/search?&q="
					+ URLEncoder.encode(param, "UTF-8");
			Desktop.getDesktop().browse(new URL(url).toURI());
		} catch (Exception e) {
		}
	}


}
