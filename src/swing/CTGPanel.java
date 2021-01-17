package swing;

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
	JButton exe = new JButton("실행");
	JButton back = new JButton("메인 페이지로");
	JButton load = new JButton("파일 불러오기");
	JLabel load_path = new JLabel();
	
	public CTGPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		ctgjp.setLayout(null);
		
		// 콘텐츠 그룹 설정 ctgjp > position, grid > ..
		
		ctgjp.add(content);
		
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
		
		gridradiusT.setText("500");
		gridresolutionT.setText("1000");
		load_path.setText("E:\\atest\\2019_40km(30m)_matched_ys.txt");
		xcountT.setText("40");
		xpositionT.setText("378252");
		ycountT.setText("40");
		ypositionT.setText("3854833");
		
		// 위치 및 모양 설정
		content.setText("CTGPROC Content");
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
		
		position.setBounds(50,360,175,100);
		position.setBorder(new TitledBorder(new LineBorder(Color.BLACK,3),"남서쪽 방향 포인트"));
		position.setLayout(new GridLayout(2,2,5,10));
		xposition.setText("X 좌표(m)");
		xposition.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		yposition.setText("Y 좌표(m)");
		yposition.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		count.setBounds(250,360,175,100);
		count.setBorder(new TitledBorder(new LineBorder(Color.BLACK,3),"GRID 개수"));
		count.setLayout(new GridLayout(2,2,5,10));
		xcount.setText("X 축(개)");
		xcount.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		ycount.setText("Y 축(개)");
		ycount.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		grid.setBounds(450,360,275,100);
		grid.setBorder(new TitledBorder(new LineBorder(Color.BLACK,3),"GRID 설정"));
		grid.setLayout(new GridLayout(2,2,5,10));
		gridresolution.setText("GRID 해상도(m)");
		gridresolution.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		gridradius.setText("GRID 계산 반경(m)");
		gridradius.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		back.setBounds(450,490,150,50);
		back.addActionListener(new MoveListener());
		exe.setBounds(625,490,100,50);
		exe.addActionListener(new MoveListener());
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
			if (e.getSource() == back) {
				System.out.println("1");
				frames.get("main").setVisible();
				frames.get("ctg").setUnVisible();
			} else if(e.getSource() == exe) {
				System.out.println("2");
				frames.get("ctgres").setVisible();
				frames.get("ctg").setUnVisible();
				frames.get("ctgres").paintNow();
				
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

	@Override
	public void paintNow() {
		// TODO Auto-generated method stub
		
	}
	
}
