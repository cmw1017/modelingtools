package aermod;

import java.awt.*;
import javax.swing.*;

public class ProgressBar extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// create a frame
	JFrame frame;
	
	public ProgressBar(JFrame frame) {
		this.frame = frame;
	}

	public void exet() {

		// create a frame
		frame = new JFrame("지형정보 처리 중...");

		// create a panel
		JPanel panel = new JPanel();

		// create a progressbar
		JProgressBar bar = new JProgressBar();

		// set initial value
		bar.setValue(0);
		bar.setIndeterminate(true);
		bar.setPreferredSize(new Dimension(200,50));

		// add progressbar
		panel.add(bar);

		// add panel
		frame.add(panel);

		// set the size of the frame
		frame.setSize(400, 150);
		frame.setVisible(true);
	}

	@Override
	public void run() {
		exet();
	}
}
