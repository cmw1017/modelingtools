package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MAKEResultPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	private Map<String, PanelTemplete> frames;
	private JFrame frame;
	private JPanel makeresjp = new JPanel();
	private JLabel title = new JLabel();
	private Color white = new Color(255,255,255);
	private JButton ctgproc = new RoundedButton("CTGPROC", Color.decode("#D99C9C"), white);
	private JButton makegeo = new RoundedButton("MAKEGEO", Color.decode("#DDC3C1"), white);
	private JButton read62 = new RoundedButton("READ62", Color.decode("#D99C9C"), white);
	private JButton smerge = new RoundedButton("SMERGE", Color.decode("#D99C9C"), white);
	private JButton calpost = new RoundedButton("CALPOST 후처리", Color.decode("#D99C9C"), white);
	private JLabel content = new JLabel();
	private JLabel content2 = new JLabel();
	private JButton back = new RoundedButton("메인 페이지로", Color.decode("#84B1D9"), white, 20);
	private JButton complete = new RoundedButton("완료", Color.decode("#84B1D9"), white, 20);
	private MAKEProcess process;
	
	public MAKEResultPanel() {
		// TODO Auto-generated constructor stub
	}
	
	public MAKEResultPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {

		makeresjp.setLayout(null);
		
		// 프레임
		makeresjp.add(ctgproc);
		makeresjp.add(makegeo);
		makeresjp.add(read62);
		makeresjp.add(smerge);
		makeresjp.add(calpost);
		makeresjp.add(title);
		
		makeresjp.add(complete);
		
		makeresjp.add(content2);
		makeresjp.add(content);
		
		
		// 타이틀 및 메뉴 버튼들 시작
		title.setText("칼퍼프 서브 모듈 - MAKEGEO 결과");
		title.setBackground(Color.decode("#596C73"));
		title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
		title.setOpaque(true);
		title.setLocation(0, 0);
		title.setSize(1000, 75);

		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(150, 75);
		content.setSize(850, 625);

		content2.setVerticalAlignment(SwingConstants.TOP);
		content2.setOpaque(true);
		content2.setBackground(Color.decode("#FFFFFF"));
		content2.setLocation(200, 125);
		content2.setSize(725, 400);
		content2.setText("");

		ctgproc.setLocation(0, 75);
		ctgproc.setSize(150, 50);
		ctgproc.addActionListener(new MoveListener());
		ctgproc.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		makegeo.setLocation(0, 125);
		makegeo.setSize(150, 50);
		makegeo.addActionListener(new MoveListener());
		makegeo.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		read62.setLocation(0, 175);
		read62.setSize(150, 50);
		read62.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		smerge.setLocation(0, 225);
		smerge.setSize(150, 50);
		smerge.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		calpost.setLocation(0, 275);
		calpost.setSize(150, 50);
		calpost.addActionListener(new MoveListener());
		calpost.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		// 타이틀 및 메뉴 버튼들 끝
		
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
		frame.add(makeresjp);
		makeresjp.setVisible(true);
	}
	
	public void setUnVisible() {
		makeresjp.setVisible(false);
	}
	
	public void setFrames(Map<String, PanelTemplete> frames) {
		this.frames = frames;
	}
	
	// 페이지 이동
	class MoveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ctgproc) {
				frames.get("makeres").setUnVisible();
				frames.get("ctg").setVisible();
			} else if (e.getSource() == makegeo) {
				frames.get("makeres").setUnVisible();
				frames.get("make").setVisible();
			} else if (e.getSource() == calpost) {
				frames.get("makeres").setUnVisible();
				frames.get("post").setVisible();
			} else if (e.getSource() == complete) {
				frames.get("makeres").setUnVisible();
				frames.get("main").setVisible();
			}
		}
	}

	@Override
	public void exet(Data data) {
		process = new MAKEProcess(data.getLoad_path(), content2, complete);
		Thread thread = new Thread(process,"process");
		thread.start();

	}
}
