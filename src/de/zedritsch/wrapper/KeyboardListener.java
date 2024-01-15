package de.zedritsch.wrapper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
	public boolean key_pressed = false;
	public char pressed_key;

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		pressed_key = e.getKeyChar();
		key_pressed = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
