package aermod;

import java.awt.*;
import javax.swing.*;

public class RoundedButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Color c = new Color(255, 247, 242); // 배경색 결정
	Color o = new Color(247, 99, 12); // 글자색 결정
	int arcWidth = 0;
	
	public RoundedButton() {
		super();
		decorate();
	}

	public RoundedButton(String text) {
		super(text);
		decorate();
	}
	
	public RoundedButton(String text, Color background, Color content) {
		super(text);
		c= background;
		o = content;
		decorate();
	}
	
	public RoundedButton(String text, Color background, Color content, int arcWidth) {
		super(text);
		c = background;
		o = content;
		this.arcWidth = arcWidth;
		decorate();
	}

	public RoundedButton(Action action) {
		super(action);
		decorate();
	}

	public RoundedButton(Icon icon) {
		super(icon);
		decorate();
	}

	public RoundedButton(String text, Icon icon) {
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
			graphics.setColor(c.darker());
		} else if (getModel().isRollover()) {
			graphics.setColor(c.brighter());
		} else {
			graphics.setColor(c);
		}
		graphics.fillRoundRect(0, 0, width, height, arcWidth, arcWidth);
		FontMetrics fontMetrics = graphics.getFontMetrics();
		Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();
		int textX = (width - stringBounds.width) / 2;
		int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
		graphics.setColor(o);
		graphics.setFont(getFont());
		graphics.drawString(getText(), textX, textY);
		graphics.dispose();
		super.paintComponent(g);
	}
}