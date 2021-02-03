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
	JPanel mainjp = new JPanel();
	JLabel content = new JLabel();
	JLabel title = new JLabel();
	Color white = new Color(255,255,255);
	JButton ctgproc = new RoundedButton("CTGPROC", Color.decode("#D99C9C"), white);
	JButton makegeo = new RoundedButton("MAKEGEO", Color.decode("#D99C9C"), white);
	JButton read62 = new RoundedButton("READ62", Color.decode("#D99C9C"), white);
	JButton smerge = new RoundedButton("SMERGE", Color.decode("#D99C9C"), white);
	JButton calpost = new RoundedButton("CALPOST 후처리", Color.decode("#D99C9C"), white);
	
	public MainPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		mainjp.setLayout(null);

		
		mainjp.add(ctgproc);
		mainjp.add(makegeo);
		mainjp.add(read62);
		mainjp.add(smerge);
		mainjp.add(calpost);
		mainjp.add(content);
		mainjp.add(title);

		// 타이틀 및 메뉴 버튼들 시작
		title.setText("칼퍼프 서브 모듈");
		title.setBackground(Color.decode("#596C73"));
		title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0 , 25, 0 , 0));
		title.setOpaque(true);
		title.setLocation(0, 0); title.setSize(1000, 75);
		
		content.setText("Main Content");
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(150, 75); content.setSize(850, 625);
		
		ctgproc.setLocation(0, 75); ctgproc.setSize(150, 50);
		ctgproc.addActionListener(new MoveListener());
		ctgproc.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		makegeo.setLocation(0, 125); makegeo.setSize(150, 50);
		makegeo.addActionListener(new MoveListener());
		makegeo.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		read62.setLocation(0, 175); read62.setSize(150, 50);
		read62.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		smerge.setLocation(0, 225); smerge.setSize(150, 50);
		smerge.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		calpost.setLocation(0, 275); calpost.setSize(150, 50);
		calpost.addActionListener(new MoveListener());
		calpost.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		// 타이틀 및 메뉴 버튼들 끝
		
		
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
