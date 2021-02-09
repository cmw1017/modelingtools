package calpuff;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class CTGPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	public static final PanelTemplete CTGResultPanel = null;
	Map<String, PanelTemplete> frames;
	
	// 파일명, 남서 X, Y, GRID X, Y, GRID 해상도, GRID 탐색 반경
	JFrame frame;
	JPanel ctgjp = new JPanel();
	JLabel title = new JLabel();
	Color white = new Color(255,255,255);
	JButton ctgproc = new RoundedButton("CTGPROC", Color.decode("#DDC3C1"), white);
	JButton makegeo = new RoundedButton("MAKEGEO", Color.decode("#D99C9C"), white);
	JButton read62 = new RoundedButton("READ62", Color.decode("#D99C9C"), white);
	JButton smerge = new RoundedButton("SMERGE", Color.decode("#D99C9C"), white);
	JButton calpost = new RoundedButton("CALPOST 후처리", Color.decode("#D99C9C"), white);
	JPanel position = new JPanel();
	JPanel count = new JPanel();
	JPanel grid = new JPanel();
	JLabel content = new JLabel();
	JLabel xposition = new JLabel();
	JLabel yposition = new JLabel();
	JLabel xcount = new JLabel();
	JLabel ycount = new JLabel();
	JLabel gridresolution = new JLabel();
	JLabel gridradius = new JLabel();
	JTextField xpositionT = new JTextField();
	JTextField ypositionT = new JTextField();
	JTextField xcountT = new JTextField();
	JTextField ycountT = new JTextField();
	JTextField gridresolutionT = new JTextField();
	JTextField gridradiusT = new JTextField();
	JButton exe = new RoundedButton("실행", Color.decode("#84B1D9"), white, 15);
	JButton back = new RoundedButton("메인 페이지로", Color.decode("#84B1D9"), white, 20);
	JButton load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JLabel load_path = new JLabel();
	
	public CTGPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		ctgjp.setLayout(null);
		
		// 프레임
		ctgjp.add(ctgproc);
		ctgjp.add(makegeo);
		ctgjp.add(read62);
		ctgjp.add(smerge);
		ctgjp.add(calpost);
		ctgjp.add(title);
		
		ctgjp.add(load);
		ctgjp.add(load_path);
		
		ctgjp.add(position);
		position.add(xposition);
		position.add(xpositionT);
		position.add(yposition);
		position.add(ypositionT);

		ctgjp.add(count);
		count.add(xcount);
		count.add(xcountT);
		count.add(ycount);
		count.add(ycountT);
		
		ctgjp.add(grid);
		grid.add(gridresolution);
		grid.add(gridresolutionT);
		grid.add(gridradius);
		grid.add(gridradiusT);
		
		ctgjp.add(exe);
		ctgjp.add(back);
		
		ctgjp.add(content);
		
		// 타이틀 및 메뉴 버튼들 시작
		title.setText("칼퍼프 서브 모듈 - CTGPROC");
		title.setBackground(Color.decode("#596C73"));
		title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0 , 25, 0 , 0));
		title.setOpaque(true);
		title.setLocation(0, 0); title.setSize(1000, 75);
		
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
		
		// content 시작
		gridradiusT.setText("500");
		gridresolutionT.setText("1000");
		load_path.setText("D:\\Modeling\\CALPUFF 개발프로그램\\sample\\2013_25km(50m)_matched.txt");
		xcountT.setText("40");
		xpositionT.setText("378252");
		ycountT.setText("40");
		ypositionT.setText("3854833");
		
		load.setLocation(200, 350); load.setSize(125, 50);
		load.addActionListener(new loadListener());
		load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		load_path.setBounds(200,275,550,50);
		load_path.setLocation(350, 350); load_path.setSize(550, 50);
		load_path.setHorizontalAlignment(SwingConstants.CENTER);
		load_path.setVerticalAlignment(SwingConstants.CENTER);
		load_path.setOpaque(true);
		load_path.setBackground(Color.WHITE);
		
		position.setLocation(200, 430); position.setSize(175, 100);
		position.setBorder(new TitledBorder(new LineBorder(Color.BLACK,3),"남서쪽 방향 포인트"));
		position.setLayout(new GridLayout(2,2,5,10));
		position.setBackground(Color.decode("#D0D8DA"));
		xposition.setText("X 좌표(m)");
		xposition.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		yposition.setText("Y 좌표(m)");
		yposition.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		count.setLocation(410, 430); count.setSize(175, 100);
		count.setBorder(new TitledBorder(new LineBorder(Color.BLACK,3),"GRID 개수"));
		count.setLayout(new GridLayout(2,2,5,10));
		count.setBackground(Color.decode("#D0D8DA"));
		xcount.setText("X 축(개)");
		xcount.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		ycount.setText("Y 축(개)");
		ycount.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		grid.setLocation(620, 430); grid.setSize(275, 100);
		grid.setBorder(new TitledBorder(new LineBorder(Color.BLACK,3),"GRID 설정"));
		grid.setLayout(new GridLayout(2,2,5,10));
		grid.setBackground(Color.decode("#D0D8DA"));
		gridresolution.setText("GRID 해상도(m)");
		gridresolution.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		gridradius.setText("GRID 계산 반경(m)");
		gridradius.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		// content 끝
		
		//이동 버튼 시작
		back.setLocation(650, 550); back.setSize(150, 50);
		back.addActionListener(new MoveListener());
		back.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		exe.setLocation(825, 550); exe.setSize(100, 50);
		exe.addActionListener(new MoveListener());
		exe.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		//이동 버튼 끝
		
	}
	
	public void setVisible() {
		frame.add(ctgjp);
		ctgjp.setVisible(true);
	}
	
	public void setUnVisible() {
		ctgjp.setVisible(false);
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
	        
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt"); // filter 확장자 추가
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
			
			if (e.getSource() == makegeo) {
				frames.get("ctg").setUnVisible();
				frames.get("make").setVisible();
			} else if (e.getSource() == calpost) {
				frames.get("ctg").setUnVisible();
				frames.get("post").setVisible();
			} else if (e.getSource() == back) {
				frames.get("ctg").setUnVisible();
				frames.get("main").setVisible();
			} else if(e.getSource() == exe) {
				frames.get("ctg").setUnVisible();
				frames.get("ctgres").setVisible();
				Data d = new Data();
				d.setGridradiusT(Integer.parseInt(gridradiusT.getText()));
				d.setGridresolutionT(Integer.parseInt(gridresolutionT.getText()));
				d.setLoad_path(load_path.getText());
				d.setXcountT(Integer.parseInt(xcountT.getText()));
				d.setXpositionT(Integer.parseInt(xpositionT.getText()));
				d.setYcountT(Integer.parseInt(ycountT.getText()));
				d.setYpositionT(Integer.parseInt(ypositionT.getText()));
				frames.get("ctgres").exet(d);
			}
			
			
		}
	}

	@Override
	public void exet(Data data) {
		// TODO Auto-generated method stub
		
	}
}
