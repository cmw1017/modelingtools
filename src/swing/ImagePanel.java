package swing;

import java.awt.*;

import javax.swing.*;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image img;
	
	public ImagePanel(Image img) {
		this.img = img;
		setSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
		setLayout(null);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img,  0,  0,  800, 600, null);
	}
}
