package de.zedritsch.wrapper;

import javax.swing.*;
import java.awt.*;

public class Window {

	private final JFrame window;
	public boolean visible = false;
	public boolean resizable = true;
	public int[][] content;
	public String llabel = "";
	public String rlabel = "";
	public String icon;
	private KeyboardListener listener = new KeyboardListener();
	private JTextField scanner = new JTextField();

	public Window(String title) {
		window = new JFrame(title);
	}

	public void show() {
		if (content == null) {
			content = new int[1][1];
			content[0][0] = 0;
		}
		if (!visible) {
			if (icon != null) {
				window.setIconImage(Toolkit.getDefaultToolkit().getImage(icon));
			}
			GridLayout layout = new GridLayout(content[0].length, content.length);
			window.setLayout(layout);
			window.setMinimumSize(new Dimension(516, 582));
			window.setSize(516, 582);
			window.setResizable(resizable);
		}
		int[][] background = new int[content[0].length][content.length];
		for (int x = 0; x < content.length; x++) {
			for (int y = 0; y < content[0].length; y++) {
			background[y][x] = content[x][y];
			}
		}
		for (int[] columns : background) {
			for (int imageName : columns) {
				Image image = new ImageIcon("assets/images/" + imageName + ".png").getImage();
				JLabel label = new JLabel(new ImageIcon(image));
				window.add(label);
			}
		}
		if (!visible) {
			window.setVisible(true);
			visible = true;
			window.addKeyListener(listener);
		}
	}

	public void show(int[][] content, String llabel, String rlabel) {
		this.content = content;
		this.llabel = llabel;
		this.rlabel = rlabel;
		show();
	}

	public void update(int[][] content, String llabel, String rlabel) {
		this.content = content;
		this.llabel = llabel;
		this.rlabel = rlabel;
		window.getContentPane().removeAll();
		show();
		window.validate();
		window.repaint();
	}

	public void popup(String title, String text) {
		popup(title, text, 0);
	}

	public int popup(String title, String text, int type) {
		if (type == 1) {
			Object[] options = { "Ja", "Nein" };
			return JOptionPane.showOptionDialog(window, text, title,
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
			null, options,
			options[1]);
		} else {
			JOptionPane.showMessageDialog(window, text, title,
			JOptionPane.INFORMATION_MESSAGE);
		}
		scanner.requestFocusInWindow();
		return 0;
	}

	public char listen() throws InterruptedException {
		char key = listener.pressed_key;
		while (key == 0) {
			Thread.sleep(50);
			key = listener.pressed_key;
		}
		listener.pressed_key = '\0';
		return key;
	}

}