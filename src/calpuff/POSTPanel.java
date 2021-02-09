package calpuff;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Map;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class POSTPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	
	// 파일명, 남서 X, Y, GRID X, Y, GRID 해상도, GRID 탐색 반경
	private JFrame frame;
	private JPanel postjp =new JPanel();
	private JLabel title = new JLabel();
	private JLabel content = new JLabel();
	private JLabel hour = new JLabel();
	private JLabel rank = new JLabel();
	private JLabel grid = new JLabel();
	private JTextField hourT = new JTextField();
	private JTextField rankT = new JTextField();
	private JRadioButton gridR = new JRadioButton();
	private Color white = new Color(255,255,255);
	private JButton ctgproc = new RoundedButton("CTGPROC", Color.decode("#D99C9C"), white);
	private JButton makegeo = new RoundedButton("MAKEGEO", Color.decode("#D99C9C"), white);
	private JButton read62 = new RoundedButton("READ62", Color.decode("#D99C9C"), white);
	private JButton smerge = new RoundedButton("SMERGE", Color.decode("#D99C9C"), white);
	private JButton calpost = new RoundedButton("CALPOST 후처리", Color.decode("#DDC3C1"), white);
	private JButton exe = new RoundedButton("실행", Color.decode("#84B1D9"), white, 15);
	private JButton back = new RoundedButton("메인 페이지로", Color.decode("#84B1D9"), white, 20);
	private JButton load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	private JLabel load_path = new JLabel();
	
	public POSTPanel(JFrame frame) {
		this.frame = frame;
		
		
	}
	
	public void setPanel() {
		
		postjp.setLayout(null);
		
		// 프레임
		postjp.add(ctgproc);
		postjp.add(makegeo);
		postjp.add(read62);
		postjp.add(smerge);
		postjp.add(calpost);
		postjp.add(title);
				
		postjp.add(load);
		postjp.add(load_path);
		postjp.add(exe);
		postjp.add(back);
				
		postjp.add(load);
		postjp.add(load_path);
		
		postjp.add(hour);
		postjp.add(hourT);
		postjp.add(rank);
		postjp.add(rankT);
		postjp.add(grid);
		postjp.add(gridR);
		
		postjp.add(content);
		
		// 타이틀 및 메뉴 버튼들 시작
		title.setText("칼퍼프 서브 모듈 - CALPOST 후처리");
		title.setBackground(Color.decode("#596C73"));
		title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
		title.setOpaque(true);
		title.setLocation(0, 0);
		title.setSize(1000, 75);
		content.setBackground(new Color(255, 255, 255, 122));
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(150, 75);
		content.setSize(850, 625);
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
		
		// content 시작
		load_path.setText("D:\\Modeling\\CALPUFF 개발프로그램\\sample\\TSERIES_SOX_1HR_CONC.DAT");
		
		load.setLocation(200, 350); load.setSize(125, 50);
		load.addActionListener(new loadListener());
		load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		load_path.setLocation(350, 350); load_path.setSize(550, 50);
		load_path.setHorizontalAlignment(SwingConstants.CENTER);
		load_path.setVerticalAlignment(SwingConstants.CENTER);
		load_path.setOpaque(true);
		load_path.setBackground(Color.WHITE);
		

		hour.setText("총 입력 시간");
		hour.setFont(new Font("맑은 고딕", Font.BOLD, 10));
		hour.setLocation(250, 460); hour.setSize(100, 50);
		hourT.setLocation(325, 470); hourT.setSize(75, 30);
		
		rank.setText("출력 데이터 순위");
		rank.setFont(new Font("맑은 고딕", Font.BOLD, 10));
		rank.setLocation(450, 460); rank.setSize(100, 50);
		rankT.setLocation(550, 470); rankT.setSize(75, 30);
		
		grid.setText("GRID 생성 여부");
		grid.setFont(new Font("맑은 고딕", Font.BOLD, 10));
		grid.setLocation(650, 460); grid.setSize(100, 50);
		gridR.setLocation(750, 470); gridR.setSize(30, 30);
		
		
		// 이동 버튼 시작
		back.setLocation(650, 550);
		back.setSize(150, 50);
		back.addActionListener(new MoveListener());
		back.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		exe.setLocation(825, 550);
		exe.setSize(100, 50);
		exe.addActionListener(new MoveListener());
		exe.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		// 이동 버튼 끝
	}
	
	public void setVisible() {
		frame.add(postjp);
		postjp.setVisible(true);
	}
	
	public void setUnVisible() {
		postjp.setVisible(false);
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
			String folderPath = "";
	        
	        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // 디렉토리 설정
	        chooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
	        chooser.setAcceptAllFileFilterUsed(true);   // Fileter 모든 파일 적용 
	        chooser.setDialogTitle("타이틀"); // 창의 제목
	        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드
	        
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("dat", "dat"); // filter 확장자 추가
	        chooser.setFileFilter(filter); // 파일 필터를 추가
	        
	        int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈
	        
	        if(returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭 
	            folderPath = chooser.getSelectedFile().toString();
	            load_path.setText(folderPath);
	        }else if(returnVal == JFileChooser.CANCEL_OPTION){ // 취소를 클릭
	            System.out.println("cancel"); 
	            folderPath = "";
	        }
		}
		
	}
	
	// 페이지 이동
	class MoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ctgproc) {
				frames.get("post").setUnVisible();
				frames.get("ctg").setVisible();
			} else if (e.getSource() == makegeo) {
				frames.get("post").setUnVisible();
				frames.get("make").setVisible();
			} else if (e.getSource() == calpost) {
				frames.get("post").setUnVisible();
				frames.get("post").setVisible();
			} else if (e.getSource() == back) {
				frames.get("post").setUnVisible();
				frames.get("main").setVisible();
			} else if(e.getSource() == exe) {
				frames.get("post").setUnVisible();
				frames.get("postres").setVisible();
				
				Data d = new Data();
				d.setLoad_path(load_path.getText());
				d.setGridR(gridR.isSelected());
				d.setRankT(Integer.parseInt(rankT.getText()));
				d.setHourT(Integer.parseInt(hourT.getText()));
				frames.get("postres").exet(d);
				
			}
		}
	}

	@Override
	public void exet(Data data) {
		// TODO Auto-generated method stub
		
	}
}
