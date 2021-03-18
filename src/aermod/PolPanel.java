package aermod;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class PolPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	String base_path;
	String temp_path;
	AermodDTO aermodDTO;
	Map<String,Map<String,Double>> criteria;

	private Color white = new Color(255,255,255);
	JPanel aermetjp = new JPanel();
	JLabel content = new JLabel();
	JLabel pol = new JLabel();
	String[] pollist = {"SO2", "CO", "NO2", "Pb", "Benzene", "PM-10", "Zn", "NH3", "CS2", "Cr", "Hg", "Cu", "Vinylchloride", "H2S", "Dichloromethane", "TCE", "As", "Ni", "Cd", "Br", "F", "HCN", "HCl", "Phenol", "Formaldehyde"};
	String[] hourlist = {"1","8","24","an"};
	ArrayList<JLabel> polname = new ArrayList<>();
	ArrayList<JLabel> poltime = new ArrayList<>();
	ArrayList<JLabel> polval = new ArrayList<>();
	JButton next = new RoundedButton("다음", Color.decode("#BF95BC"), white, 20);
	
	public PolPanel() {
	}
	
	public void setPanel(String base_path) {
		
		aermetjp.setLayout(null);
		
		// 프레임
		this.base_path = base_path;
		ImagePanel title = new ImagePanel(base_path+"\\resource\\Step2.png", 1000, 130);
		aermetjp.add(title);
		
		aermetjp.add(pol);
		aermetjp.add(next);
		

		
		title.setLocation(0, 0); title.setSize(1000, 130);
		
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 100); content.setSize(1000, 870);
		
		pol.setLocation(50, 150); pol.setSize(250, 50);
		pol.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		pol.setText("환경기준 정보");
		
		
		next.setLocation(800, 870); next.setSize(150, 50);
		next.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		next.addActionListener(new MoveListener());
		
	}
	
	public void setVisible() {
		AerMain.frame.add(aermetjp);
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
				frames.get("aerres").exet(aermodDTO);
			} 
			
			
		}
	}

	@Override
	public void exet(AermodDTO aermodDTO) {
		this.aermodDTO = aermodDTO;
		criteria = aermodDTO.getCriteria();
		AerMain.frame.setPreferredSize(new Dimension(1000,1000));
		AerMain.frame.pack();
		int x = 0;
		int y = 0;
		int z = 1;
		for (int i = 1, k = 1; i < pollist.length+1; i++, k++) {
			polname.add(new JLabel());
			polname.get(i-1).setLocation(50 + 320 * x, 200 + 40 * y);
			polname.get(i-1).setSize(150, 50);
			polname.get(i-1).setFont(new Font("맑은 고딕", Font.BOLD, 15));
			polname.get(i-1).setText(pollist[i-1]);
			aermetjp.add(polname.get(i-1));
			for(int j = 1; j < hourlist.length+1; j++) {
				if(criteria.get(pollist[i-1]).containsKey(hourlist[j-1]))
				{
					poltime.add(new JLabel());
					poltime.get(z-1).setLocation(200 + 320 * x, 200 + 40 * y);
					poltime.get(z-1).setSize(150, 50);
					poltime.get(z-1).setFont(new Font("맑은 고딕", Font.BOLD, 15));
					poltime.get(z-1).setText(hourlist[j-1]);
					polval.add(new JLabel());
					polval.get(z-1).setLocation(250 + 320 * x, 200 + 40 * y);
					polval.get(z-1).setSize(150, 50);
					polval.get(z-1).setFont(new Font("맑은 고딕", Font.BOLD, 15));
					polval.get(z-1).setText(String.valueOf(criteria.get(pollist[i-1]).get(hourlist[j-1])));
					aermetjp.add(poltime.get(z-1));
					aermetjp.add(polval.get(z-1));
					y++;
					z++;
				}
			}
			if((x != 2 && k % 8 == 0) || (x == 2 && k % 12 == 0)) {
				x++; y=0; k=1;
			}
		}
		aermetjp.add(content);
	}
}
