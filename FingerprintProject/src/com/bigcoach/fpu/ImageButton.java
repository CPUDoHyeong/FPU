package com.bigcoach.fpu;

import javax.swing.*;

public class ImageButton extends JButton {
	public ImageButton() {
		super();
	}
	
	public ImageButton(ImageIcon baseIcon, ImageIcon pressedIcon) {
		super();
		decorate(baseIcon, pressedIcon);
	}
	
	protected void decorate(ImageIcon baseIcon, ImageIcon pressedIcon) {
		this.setIcon(baseIcon);
		this.setFocusPainted(false);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setPressedIcon(pressedIcon);
	}
}
