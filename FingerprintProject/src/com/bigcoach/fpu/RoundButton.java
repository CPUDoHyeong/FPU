package com.bigcoach.fpu;

import java.awt.*;

import javax.swing.*;

public class RoundButton extends JButton {
	
	private int strokeSize = 3;
	private Dimension arcs = new Dimension(20, 20);
	
	public RoundButton() {
        super();
        decorate();
    }
 
    public RoundButton(String text) {
        super(text);
        decorate();
    }
 
    public RoundButton(Action action) {
        super(action);
        decorate();
    }
 
    public RoundButton(Icon icon) {
        super(icon);
        decorate();
    }
 
    public RoundButton(String text, Icon icon) {
        super(text, icon);
        decorate();
    }	
    
    protected void decorate() {
        setBorderPainted(false);
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
     
        Graphics2D graphics = (Graphics2D) g;
     
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     
        
        if (getModel().isArmed()) {
        	// 마우스가 눌러진 상태
            // graphics.setColor(getBackground().darker());
        	graphics.setColor(new Color(200, 200, 200));
        	graphics.drawRoundRect(0, 0, width - strokeSize, height - strokeSize, arcs.width, arcs.height);
        } else {
        	// 기본 상태
            // graphics.setColor(getBackground());
        	graphics.setColor(Color.white);
        	graphics.drawRoundRect(0, 0, width - strokeSize, height - strokeSize, arcs.width, arcs.height);
        }
        
        graphics.setColor(Color.red);
        
     
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();
     
        int textX = (width - stringBounds.width) / 2;
        int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
     
        // graphics.setColor(getForeground());
        graphics.setColor(Color.white);
        graphics.setFont(getFont());
        graphics.drawString(getText(), textX, textY);
        graphics.dispose();
     
        super.paintComponent(g);
    }
}
