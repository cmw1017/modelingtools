package aermod;

import java.awt.*;

import javax.swing.*;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image img;
	private int sizeX;
	private int sizeY;

	public ImagePanel(String img, int sizeX, int sizeY) {
		this(new ImageIcon(img).getImage());
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public ImagePanel(Image img) {
		this.img = img;
		this.sizeX = img.getWidth(null);
		this.sizeY = img.getHeight(null);
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, sizeX, sizeY, null);
	}
}
