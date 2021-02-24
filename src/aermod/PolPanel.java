package aermod;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

import aermod.MeteoPanel.MoveListener;
import calpuff.RoundedButton;

public class PolPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	public static final PanelTemplete CTGResultPanel = null;
	Map<String, PanelTemplete> frames;
	
	JFrame frame;
	private Color white = new Color(255,255,255);
	JPanel aermetjp = new JPanel();
	JLabel title = new JLabel();
	JLabel content = new JLabel();
	JLabel pol = new JLabel();
	String[] pollist = {"SO2", "CO", "NO2", "Pb", "Benzene", "PM-10", "Zn", "NH3", "CS2", "Cr", "Hg", "Cu", "Vinylchloride", "H2S", "Dichloromethane", "TCE", "As", "Ni", "Cd", "Br", "F", "HCN", "HCl", "Phenol", "Formaldehyde"};
	ArrayList<JLabel> polname = new ArrayList<>();
	ArrayList<JLabel> polval = new ArrayList<>();
	JButton next = new RoundedButton("다음", Color.decode("#BF95BC"), white, 20);
	
	public PolPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		aermetjp.setLayout(null);
		
		// 프레임
		aermetjp.add(title);
		
		aermetjp.add(pol);
		aermetjp.add(next);
		

		
		title.setLocation(0, 0); title.setSize(1000, 100);
		
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 100); content.setSize(1000, 600);
		
		pol.setLocation(100, 150); pol.setSize(250, 50);
		pol.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		pol.setText("모델링 진행 오염물질 정보");
		
		
		next.setLocation(800, 570); next.setSize(150, 50);
		next.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		next.addActionListener(new MoveListener());
		
	}
	
	public void setVisible() {
		frame.add(aermetjp);
		aermetjp.setVisible(true);
	}
	
	public void setUnVisible() {
		aermetjp.setVisible(false);
	}
	
	public void setFrames(Map<String, PanelTemplete> frames) {
		this.frames = frames;
	}
	// 파일 탐색창
	class loadListener implements ActionListener {
		
		JFileChooser chooser;
		
		loadListener() {
			chooser = new JFileChooser();
		}
		
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	// 페이지 이동
class MoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == next) {
				frames.get("aerpol").setUnVisible();
				frames.get("aerres").setVisible();
			} 
			
			
		}
	}

	@Override
	public void exet(AermodDTO data) {

		int x = 0;
		int y = 0;
		for (int i = 0; i < pollist.length; i++, x++) {
			if ((i % 7) == 6) {
				y++; x=0;
			}
			polname.add(new JLabel());
			polname.get(i).setLocation(100 + 200 * y, 200 + 50 * x);
			polname.get(i).setSize(150, 50);
			polname.get(i).setFont(new Font("맑은 고딕", Font.BOLD, 15));
			polname.get(i).setText(pollist[i]);
			polval.add(new JLabel());
			polval.get(i).setLocation(250 + 200 * y, 200 + 50 * x);
			polval.get(i).setSize(150, 50);
			polval.get(i).setFont(new Font("맑은 고딕", Font.BOLD, 15));
			polval.get(i).setText("00");
			aermetjp.add(polname.get(i));
			aermetjp.add(polval.get(i));
		}
		aermetjp.add(content);
	}
}
