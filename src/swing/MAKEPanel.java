package swing;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Map;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class MAKEPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	
	// 파일명, 남서 X, Y, GRID X, Y, GRID 해상도, GRID 탐색 반경
	JFrame frame;
	JPanel makejp = new JPanel();
	JLabel content1 = new JLabel();
	JButton load = new JButton("파일 불러오기");
	JButton exe = new JButton("실행");
	JButton back = new JButton("메인 페이지로");
	JLabel load_path = new JLabel();
	
	public MAKEPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
	
		makejp.setLayout(null);
		
		makejp.add(content1);
		makejp.add(load);
		makejp.add(load_path);
		makejp.add(exe);
		makejp.add(back);
		
		// 위치 및 모양 설정
		content1.setText("MAKEGEO Content");
		content1.setHorizontalAlignment(SwingConstants.CENTER);
		content1.setVerticalAlignment(SwingConstants.CENTER);
		content1.setOpaque(true);
		content1.setBackground(Color.WHITE);
		content1.setBounds(25, 25, 725, 225);
		
		load.setBounds(50,275,125,50);
		load.addActionListener(new loadListener());
		load_path.setBounds(200,275,550,50);
		load_path.setHorizontalAlignment(SwingConstants.CENTER);
		load_path.setVerticalAlignment(SwingConstants.CENTER);
		load_path.setOpaque(true);
		load_path.setBackground(Color.WHITE);
		
		back.setBounds(450,490,150,50);
		back.addActionListener(new MoveListener());
		exe.setBounds(625,490,100,50);
		exe.addActionListener(new MoveListener());
	}
	
	public void setVisible() {
		frame.add(makejp);
		makejp.setVisible(true);
	}
	
	public void setUnVisible() {
		makejp.setVisible(false);
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
	        
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("set Files", "set"); // filter 확장자 추가
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
					frames.get("make").setUnVisible();
				} else if(e.getSource() == exe) {
					frames.get("makeres").setVisible();
					frames.get("make").setUnVisible();
				}
			}
		}
}
