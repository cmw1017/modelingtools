package swing;

import java.awt.Color;
import java.awt.Font;
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
	JLabel title = new JLabel();
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
		
		mainjp = new ImagePanel(new ImageIcon("D:\\eclipse\\workspace\\CALpre\\src\\resource\\back.jpg").getImage());
		
		mainjp.setLayout(null);


		mainjp.add(ctgproc);
		mainjp.add(makegeo);
		mainjp.add(read62);
		mainjp.add(smerge);
		mainjp.add(calpost);
		mainjp.add(content);
		mainjp.add(title);

		title.setText("칼퍼프 서브 모듈");
		title.setBackground(new Color(87,37,125));
		title.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0 , 10, 0 , 0));
		title.setOpaque(true);
		title.setLocation(0, 0); title.setSize(800, 75);
		
		content.setBackground(new Color(255,255,255,122));
		content.setText("Main Content");
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(new Color(255,255,255,122));
		content.setLocation(150, 75); content.setSize(650, 400);
		ctgproc.setLocation(0, 75); ctgproc.setSize(150, 50);
		ctgproc.addActionListener(new MoveListener());
		makegeo.setLocation(0, 125); makegeo.setSize(150, 50);
		makegeo.addActionListener(new MoveListener());
		read62.setLocation(0, 175); read62.setSize(150, 50);
		smerge.setLocation(0, 225); smerge.setSize(150, 50);
		calpost.setLocation(0, 275); calpost.setSize(150, 50);
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
