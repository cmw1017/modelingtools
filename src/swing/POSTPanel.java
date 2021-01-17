package swing;

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
	JFrame frame;
	JPanel postjp =new JPanel();
	JLabel content = new JLabel();
	JLabel hour = new JLabel();
	JLabel rank = new JLabel();
	JLabel grid = new JLabel();
	JTextField hourT = new JTextField();
	JTextField rankT = new JTextField();
	JRadioButton gridR = new JRadioButton();
	JButton exe = new JButton("실행");
	JButton back = new JButton("메인 페이지로");
	JButton load = new JButton("파일 불러오기");
	JLabel load_path = new JLabel();
	
	public POSTPanel(JFrame frame) {
		this.frame = frame;
		
		
	}
	
	public void setPanel() {
		
		postjp.setLayout(null);
		
		// 콘텐츠 그룹 설정 ctgjp > position, grid > ..
		
		postjp.add(content);
		
		postjp.add(load);
		postjp.add(load_path);
		
		postjp.add(hour);
		postjp.add(hourT);
		postjp.add(rank);
		postjp.add(rankT);
		postjp.add(grid);
		postjp.add(gridR);
		
		
		postjp.add(exe);
		postjp.add(back);
		
		// 위치 및 모양 설정
		content.setText("CALPOST Content");
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.WHITE);
		content.setBounds(25,25,725,225);
		
		load.setBounds(50,275,125,50);
		load.addActionListener(new loadListener());
		load_path.setBounds(200,275,550,50);
		load_path.setHorizontalAlignment(SwingConstants.CENTER);
		load_path.setVerticalAlignment(SwingConstants.CENTER);
		load_path.setOpaque(true);
		load_path.setBackground(Color.WHITE);
		

		hour.setText("총 입력 시간");
		hour.setBounds(50,360,100,50);
		hourT.setBounds(125,370,75,30);
		
		rank.setText("출력 데이터 순위");
		rank.setBounds(250,360,100,50);
		rankT.setBounds(350,370,75,30);
		
		grid.setText("GRID 생성 여부");
		grid.setBounds(450,360,100,50);
		gridR.setBounds(550,370,30,30);
		
		
		back.setBounds(450,490,150,50);
		back.addActionListener(new MoveListener());
		exe.setBounds(625,490,100,50);
		exe.addActionListener(new MoveListener());
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
			if (e.getSource() == back) {
				frames.get("main").setVisible();
				frames.get("post").setUnVisible();
			} else if(e.getSource() == exe) {
				frames.get("postres").setVisible();
				frames.get("post").setUnVisible();
				frames.get("postres").paintNow();
				
//				System.out.println(hourT.getText());
//				System.out.println(rankT.getText());
//				System.out.println(gridR.isSelected());
				
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

	@Override
	public void paintNow() {
		// TODO Auto-generated method stub
		
	}
	
	
}
