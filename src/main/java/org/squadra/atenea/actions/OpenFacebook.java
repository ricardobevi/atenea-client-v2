package org.squadra.atenea.actions;

import java.net.*;
import java.awt.Desktop;

public class OpenFacebook extends PreloadAction {

	@Override
	public void execute() {
		try {
			Desktop.getDesktop().browse(new URL("http://www.facebook.com").toURI());
		} catch (Exception e) {
		}
	}


}
