package swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.*;

public class MainPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	JFrame frame;
	ImagePanel mainjp;
	JLabel content = new JLabel();
	Color navy = new Color(66,59,105);
	Color white = new Color(255,255,255);
	JButton ctgproc = new RoundedButton("CTGPROC", navy, white);
	JButton makegeo = new RoundedButton("MAKEGEO", navy, white);
	JButton read62 = new RoundedButton("READ62", navy, white);
	JButton smerge = new RoundedButton("SMERGE", navy, white);
	JButton calpost = new RoundedButton("CALPOST 후처리", navy, white);
	
	public MainPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		mainjp = new ImagePanel(new ImageIcon("E:\\jspservelt-work\\calpreprocessor-master\\src\\resource\\back.jpg").getImage());
		
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

	@Override
	public void exet(Data data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paintNow() {
		// TODO Auto-generated method stub
		
	}
}
