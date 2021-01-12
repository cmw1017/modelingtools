package swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.*;

public class MainPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	JFrame frame;
	ImagePanel mainjp;
	JLabel content = new JLabel();
	JButton ctgproc = new JButton("CTGPROC");
	JButton makegeo = new JButton("MAKEGEO");
	JButton read62 = new JButton("READ62");
	JButton smerge = new JButton("SMERGE");
	JButton calpost = new JButton("CALPOST 후처리");
	
	public MainPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		mainjp = new ImagePanel(new ImageIcon("D:\\eclipse\\workspace\\CALpre\\src\\resource\\back.jpg").getImage());
		
		mainjp.setLayout(null);
		

		mainjp.add(content);
		mainjp.add(ctgproc);
		mainjp.add(makegeo);
		mainjp.add(read62);
		mainjp.add(smerge);
		mainjp.add(calpost);


		content.setText("Main Content");
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.WHITE);
		content.setBounds(25,25,725,225);
		ctgproc.setBounds(50, 275, 150, 100);
		ctgproc.addActionListener(new MoveListener());
		makegeo.setBounds(225, 275, 150, 100);
		makegeo.addActionListener(new MoveListener());
		read62.setBounds(400, 275, 150, 100);
		smerge.setBounds(575, 275, 150, 100);
		calpost.setBounds(50, 400, 150, 100);
		calpost.addActionListener(new MoveListener());
		
		
		
		//frame.add(mainjp);
	}
	
	public void setVisible() {
		frame.add(mainjp);
		frame.pack();
		mainjp.setVisible(true);
	}
	
	public void setUnVisible() {
		mainjp.setVisible(false);
	}

	public void setFrames(Map<String, PanelTemplete> frames) {
		this.frames = frames;
	}
	
	class MoveListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ctgproc) {
				frames.get("main").setUnVisible();
				frames.get("ctg").setVisible();
			} else if (e.getSource() == makegeo) {
				frames.get("main").setUnVisible();
				frames.get("make").setVisible();
			} else if (e.getSource() == calpost) {
				frames.get("main").setUnVisible();
				frames.get("post").setVisible();
			}
		}
	}
}
