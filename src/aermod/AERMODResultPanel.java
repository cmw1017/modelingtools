package aermod;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.*;

public class AERMODResultPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	private Map<String, PanelTemplete> frames;
	String base_path;
	String temp_path;
	AermodDTO aermodDTO;
	
	private String[] header = {"오염물질", "모델 진행도", "모델링 횟수","배출농도","환경기준","통과여부"};
	private List<String> matters;
	
	private JPanel aerresjp = new JPanel();
	private JLabel[] headers = new JLabel[6];
	private Color white = new Color(255,255,255);
	private JLabel content = new JLabel();
	JLabel pol = new JLabel();
	private JButton back = new RoundedButton("메인페이지로", Color.decode("#84B1D9"), white, 20);
	private JButton complete = new RoundedButton("완료", Color.decode("#84B1D9"), white, 20);
	
	
	public AERMODResultPanel() {
	}
	
	public void setPanel(String base_path) {

		aerresjp.setLayout(null);
		
		// 프레임
		this.base_path = base_path;
		ImagePanel title = new ImagePanel(base_path+"\\resource\\Step3.png", 1000, 130);
		aerresjp.add(title);
		aerresjp.add(pol);
		aerresjp.add(back);
		aerresjp.add(complete);
		
		
		title.setLocation(0, 0); title.setSize(1000, 130);
		
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 100); content.setSize(1000, 870);

		pol.setLocation(50, 150); pol.setSize(250, 50);
		pol.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		pol.setText("모델링 진행 정보");
		
		for(int i = 0; i < headers.length; i++) {
			headers[i] = new JLabel();
			headers[i].setHorizontalAlignment(SwingConstants.CENTER);
			headers[i].setOpaque(true);
			headers[i].setBackground(Color.decode("#D0D8DA"));
			headers[i].setFont(new Font("맑은 고딕", Font.BOLD, 15));
			headers[i].setLocation(50 + 150*i, 220);
			headers[i].setSize(150, 50);
			headers[i].setText(header[i]);
			aerresjp.add(headers[i]);
		}
		
		// 이동 버튼 시작
		back.setLocation(625, 870);
		back.setSize(150, 50);
		back.addActionListener(new MoveListener());
		back.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		//back.setVisible(false);
		complete.setLocation(800, 870);
		complete.setSize(100, 50);
		complete.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		complete.addActionListener(new MoveListener());
		//complete.setVisible(false);
		System.out.println("setPanel End");
	}
	
	public void setVisible() {
		AerMain.frame.add(aerresjp);
		aerresjp.setVisible(true);
	}
	
	public void setUnVisible() {
		aerresjp.setVisible(false);
	}
	
	public void setFrames(Map<String, PanelTemplete> frames) {
		this.frames = frames;
	}
	
	// 페이지 이동
	class MoveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}

	@Override
	public void exet(AermodDTO aermodDTO) {
		this.aermodDTO = aermodDTO;
		AerMain.frame.setPreferredSize(new Dimension(1000,1000));
		AerMain.frame.pack();
		AERPRE aerpre = new AERPRE(aermodDTO);
		aerpre.RunProcess();
		
		matters = aermodDTO.getMatters();
		int length = matters.size();
		
		JLabel[][] matters_label = new JLabel[length][3];
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < 3; j++) {
				matters_label[i][j] = new JLabel();
				matters_label[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				matters_label[i][j].setOpaque(true);
				matters_label[i][j].setBackground(Color.decode("#D0D8DA"));
				matters_label[i][j].setFont(new Font("맑은 고딕", Font.BOLD, 15));
				matters_label[i][j].setLocation(50 + 150 * j, 260 + 30 * i);
				matters_label[i][j].setSize(150, 50);
				if(j ==0) matters_label[i][j].setText(matters.get(i));
				else matters_label[i][j].setText("0");
				aerresjp.add(matters_label[i][j]);
			}
		}
		aerresjp.add(content);
//		AERMOD_main aermain = new AERMOD_main(aermodDTO, matters_label);
//		Thread thread = new Thread(aermain, "aermod_main");
//		thread.start();
	}
}
