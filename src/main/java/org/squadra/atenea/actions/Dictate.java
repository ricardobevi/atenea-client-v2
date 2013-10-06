package org.squadra.atenea.actions;

import java.awt.Robot;
import java.awt.event.*;

public class Dictate extends PreloadAction {

	@Override
	public void execute() {
		typeString(param);
	}

	public void typeString(String s) {
		try {
			Robot robik = new Robot();
			byte[] bytes = s.getBytes();
			for (byte b : bytes) {
				int code = b;
				// Si la letra es mayuscula, apreto Shift
				if (code > 64 && code < 91)
					robik.keyPress(KeyEvent.VK_SHIFT);
				if (code > 96 && code < 123) 
					code -= 32;		
				
				robik.delay(40);
				robik.keyPress(code);
				robik.keyRelease(code);
				// Si la letra es mayuscula, suelto Shift
				if (code > 64 && code < 91)
					robik.keyRelease(KeyEvent.VK_SHIFT);
			}
		} catch (Exception e) {}
	}
}
