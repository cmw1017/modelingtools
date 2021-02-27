package aermod;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;

public class MeteoPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	
	JFrame frame;
	private Color white = new Color(255,255,255);
	JPanel aermetjp = new JPanel();
	JLabel title = new JLabel();
	JLabel content = new JLabel();
	JLabel station = new JLabel();
	JLabel station_info = new JLabel();
	JLabel bc = new JLabel();
	JLabel bc_info = new JLabel();
	JLabel ec = new JLabel();
	JLabel ec_info = new JLabel();
	JButton ec_load = new RoundedButton("다운로드", Color.decode("#BF95BC"), white, 20);
	JButton next = new RoundedButton("다음", Color.decode("#BF95BC"), white, 20);
	
	public MeteoPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		aermetjp.setLayout(null);
		
		// 프레임
		aermetjp.add(title);
		
		aermetjp.add(station);
		aermetjp.add(station_info);
		aermetjp.add(bc);
		aermetjp.add(bc_info);
		aermetjp.add(ec);
		aermetjp.add(ec_info);
		aermetjp.add(ec_load);
		
		aermetjp.add(next);
		
		aermetjp.add(content);
		
		title.setLocation(0, 0); title.setSize(1000, 100);
		
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 100); content.setSize(1000, 600);
		
		station.setLocation(100, 150); station.setSize(150, 50);
		station.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		station.setText("기상대 정보 : ");
		station_info.setLocation(300, 150); station_info.setSize(300, 50);
		station_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		station_info.setText("OO 기상대");
		bc.setLocation(100, 250); bc.setSize(150, 50);
		bc.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		bc.setText("기존 오염도 정보 : ");
		bc_info.setLocation(300, 250); bc_info.setSize(300, 50);
		bc_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		bc_info.setText("OO ~ OO년 기존오염도");
		
		ec.setLocation(100, 350); ec.setSize(200, 50);
		ec.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ec.setText("환경기준 정보");
		ec_info.setLocation(300, 350); ec_info.setSize(300, 50);
		ec_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ec_info.setText("대기환경기준");
		ec_load.setLocation(600, 350); ec_load.setSize(150, 50);
		ec_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ec_load.addActionListener(new loadListener());
		
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
				frames.get("aermet").setUnVisible();
				frames.get("aerpol").setVisible();
//				frames.get("aerpol").exet(data);
			} 
			
			
		}
	}

	@Override
	public void exet(AermodDTO data) {
		// TODO Auto-generated method stub
		
	}
}
