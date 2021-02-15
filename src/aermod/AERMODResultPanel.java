package aermod;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.*;

public class AERMODResultPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	private Map<String, PanelTemplete> frames;
	private JFrame frame;
	private JPanel aerresjp = new JPanel();
	private JLabel title = new JLabel();
	private JLabel[] headers = new JLabel[3];
	private Color white = new Color(255,255,255);
	private JLabel content2 = new JLabel();
	private JButton back = new RoundedButton("메인페이지로", Color.decode("#84B1D9"), white, 20);
	private JButton complete = new RoundedButton("완료", Color.decode("#84B1D9"), white, 20);
	private List<String> matters;
	private String[] header = {"오염물질", "모델링 진행시간", "모델링 횟수"};
	
	
	public AERMODResultPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {

		aerresjp.setLayout(null);
		
		// 프레임
		aerresjp.add(title);
		aerresjp.add(back);
		aerresjp.add(complete);
		
		// 타이틀 및 메뉴 버튼들 시작
		title.setText("에어모드 모델링 결과");
		title.setBackground(Color.decode("#596C73"));
		title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
		title.setOpaque(true);
		title.setLocation(0, 0);
		title.setSize(1000, 75);
		
		content2.setVerticalAlignment(SwingConstants.TOP);
		content2.setOpaque(true);
		content2.setBackground(Color.decode("#D0D8DA"));
		content2.setLocation(0, 75);
		content2.setSize(1000, 625);
		content2.setText("");
		// 타이틀 및 메뉴 버튼들 끝
		
		for(int i = 0; i < 3; i++) {
			headers[i] = new JLabel();
			headers[i].setHorizontalAlignment(SwingConstants.CENTER);
			headers[i].setOpaque(true);
			headers[i].setBackground(Color.decode("#D0D8DA"));
			headers[i].setFont(new Font("맑은 고딕", Font.BOLD, 15));
			headers[i].setLocation(50 + 150*i, 125);
			headers[i].setSize(150, 50);
			headers[i].setText(header[i]);
			aerresjp.add(headers[i]);
		}
		
		// 이동 버튼 시작
		back.setLocation(650, 550);
		back.setSize(150, 50);
		back.addActionListener(new MoveListener());
		back.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		back.setVisible(false);
		complete.setLocation(825, 550);
		complete.setSize(100, 50);
		complete.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		complete.addActionListener(new MoveListener());
		complete.setVisible(false);
		System.out.println("setPanel End");
	}
	
	public void setVisible() {
		frame.add(aerresjp);
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
				matters_label[i][j].setLocation(50 + 150 * j, 175 + 50 * i);
				matters_label[i][j].setSize(150, 50);
				if(j ==0) matters_label[i][j].setText(matters.get(i));
				else matters_label[i][j].setText("0");
				aerresjp.add(matters_label[i][j]);
			}
		}
		aerresjp.add(content2);
		String insrc = "D:\\Modeling\\AERMOD\\yeosu";
		AERMOD_main aermain = new AERMOD_main(matters, matters_label, insrc);
		Thread thread = new Thread(aermain, "aermod_main");
		thread.start();
	}
}
