package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Illustrate {
	private final JFrame frame;

	public Illustrate() {
		this.frame = new JFrame("iLlustrate");
		this.frame.setMinimumSize(new Dimension(400, 400));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setVisible(boolean value) {
		this.frame.setVisible(value);
	}
}
