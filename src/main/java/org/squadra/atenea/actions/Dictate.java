package org.squadra.atenea.actions;

import java.awt.Robot;
import java.awt.event.*;

public class Dictate extends PreloadAction {

	@Override
	public void execute() {
		// MainGUI.getInstance().mainButtonMouseClicked();
		typeString(param);
	}

	public void typeString(String s) {
		try {
			Robot robik = new Robot();
			byte[] bytes = s.getBytes();
			for (byte b : bytes) {
				int code = b;
				// keycode only handles [A-Z] (which is ASCII decimal [65-90])
				if (code > 64 && code < 91) 
					robik.keyPress(KeyEvent.VK_SHIFT);
				if (code > 96 && code < 123) 
					code -= 32;
				
				System.out.print(code);
				robik.delay(40);
				robik.keyPress(code);
				robik.keyRelease(code);
				if (code+32 > 64 && code+32 < 91)
					robik.keyRelease(KeyEvent.VK_SHIFT);
			}
		} catch (Exception e) {}
	}
}
