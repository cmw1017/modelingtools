package aermod;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import calpuff.RoundedButton;

public class InputPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	private Process process = null;
	String base_path;
	String temp_path[] = new String[3];
	AermodDTO data;
	String sido[] = {"서울특별시","인천광역시","경기도"};
	
	JFrame frame;
	private Color white = new Color(255,255,255);
	JPanel aerinjp = new JPanel();
	ImagePanel title = new ImagePanel("D:\\Modeling\\AERMOD\\aermod\\resource\\Step1.png", 1000, 130);
	JLabel content = new JLabel();
	JLabel company = new JLabel();
	JLabel company_lat = new JLabel();
	JLabel company_lon = new JLabel();
	JTextField company_lat_txt = new JTextField();
	JTextField company_lon_txt = new JTextField();
	JLabel company_sido = new JLabel();
	JLabel company_sigun = new JLabel();
	JLabel company_gu = new JLabel();
	JComboBox<String> company_sido_txt = new JComboBox<String>(sido);
	JComboBox<String> company_sigun_txt = new JComboBox<String>(sido);
	JComboBox<String> company_gu_txt = new JComboBox<String>(sido);
	JLabel ksic = new JLabel();
	JLabel terrain = new JLabel();
	JLabel topy = new JLabel();
	JTextField topy_txt = new JTextField();
	JButton topy_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JLabel boundary = new JLabel();
	JTextField boundary_txt = new JTextField();
	JButton boundary_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JLabel source = new JLabel();
	JTextField source_txt = new JTextField();
	JButton source_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JButton next = new RoundedButton("다음", Color.decode("#BF95BC"), white, 20);
	
	public InputPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {
		
		aerinjp.setLayout(null);
		
		// 프레임
		aerinjp.add(title);
		
		aerinjp.add(company);
		aerinjp.add(company_lat);
		aerinjp.add(company_lat_txt);
		aerinjp.add(company_lon);
		aerinjp.add(company_lon_txt);
		aerinjp.add(company_sido);
		aerinjp.add(company_sido_txt);
		aerinjp.add(company_sigun);
		aerinjp.add(company_sigun_txt);
		aerinjp.add(company_gu);
		aerinjp.add(company_gu_txt);
		
		
		aerinjp.add(ksic);

		aerinjp.add(terrain);
		aerinjp.add(topy);
		aerinjp.add(topy_txt);
		aerinjp.add(topy_load);
		aerinjp.add(boundary);
		aerinjp.add(boundary_txt);
		aerinjp.add(boundary_load);
		
//		aerinjp.add(source);
//		aerinjp.add(source_txt);
//		aerinjp.add(source_load);
		
		aerinjp.add(next);
		
		aerinjp.add(content);
		
		title.setLocation(0, 0); title.setSize(1000, 130);
		
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 130); content.setSize(1000, 670);
		
		
		company.setLocation(50, 150); company.setSize(200, 50);
		company.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		company.setText("사업장 정보 입력");
		company_lat.setLocation(150, 200); company_lat.setSize(50, 50);
		company_lat.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lat.setText("위도");
		company_lat_txt.setLocation(200, 210); company_lat_txt.setSize(100, 30);
		company_lat_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lon.setLocation(350, 200); company_lon.setSize(50, 50);
		company_lon.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lon.setText("경도");
		company_lon_txt.setLocation(400, 210); company_lon_txt.setSize(100, 30);
		company_lon_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lon_txt.setText("");
		
		company_sido.setLocation(150, 275); company_sido.setSize(50, 50);
		company_sido.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_sido.setText("시도");
		company_sido_txt.setLocation(200, 285); company_sido_txt.setSize(150, 30);
		company_sido_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_sigun.setLocation(400, 275); company_sigun.setSize(50, 50);
		company_sigun.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_sigun.setText("시군구");
		company_sigun_txt.setLocation(460, 285); company_sigun_txt.setSize(150, 30);
		company_sigun_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_gu.setLocation(650, 275); company_gu.setSize(50, 50);
		company_gu.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_gu.setText("구");
		company_gu_txt.setLocation(680, 285); company_gu_txt.setSize(150, 30);
		company_gu_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		
		ksic.setLocation(150, 350); ksic.setSize(150, 50);
		ksic.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ksic.setText("산업 분류 선택");
		
		terrain.setLocation(50, 420); terrain.setSize(200, 30);
		terrain.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		terrain.setText("지형자료 입력");
		topy.setLocation(150, 470); topy.setSize(200, 30);
		topy.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		topy.setText("지형도(.dxf)");
		topy_txt.setLocation(350, 470); topy_txt.setSize(350, 30);
		topy_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		topy_txt.setText("");
		topy_load.setLocation(750, 470); topy_load.setSize(150, 30);
		topy_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		topy_load.addActionListener(new loadListener());
		
		boundary.setLocation(150, 570); boundary.setSize(200, 30);
		boundary.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		boundary.setText("부지 경계(.dxf)");
		boundary_txt.setLocation(350, 570); boundary_txt.setSize(350, 30);
		boundary_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		boundary_txt.setText("");
		boundary_load.setLocation(750, 570); boundary_load.setSize(150, 30);
		boundary_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		boundary_load.addActionListener(new loadListener());
		
//		source.setLocation(100, 510); source.setSize(200, 30);
//		source.setFont(new Font("맑은 고딕", Font.BOLD, 15));
//		source.setText("배출원 정보 입력(.csv)");
//		source_txt.setLocation(300, 510); source_txt.setSize(350, 30);
//		source_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
//		source_txt.setText("");
//		source_load.setLocation(700, 510); source_load.setSize(150, 30);
//		source_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
//		source_load.addActionListener(new loadListener());
		
		next.setLocation(800, 670); next.setSize(150, 50);
		next.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		next.addActionListener(new MoveListener());

		
	}
	
	public void setVisible() {
		frame.add(aerinjp);
		aerinjp.setVisible(true);
	}
	
	public void setUnVisible() {
		aerinjp.setVisible(false);
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
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("dxf", "dxf"); // filter 확장자 추가
	        if (e.getSource() == source_load) {
				filter = new FileNameExtensionFilter("csv", "csv"); // filter 확장자 추가
			}
	        chooser.setFileFilter(filter); // 파일 필터를 추가
	        
	        int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈
	        
	        if(returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭 
	            folderPath = chooser.getSelectedFile().toString();
	            if (e.getSource() == topy_load) {
	            	topy_txt.setText(folderPath);
	            	temp_path[0] = folderPath;
				} else if (e.getSource() == boundary_load) {
	            	boundary_txt.setText(folderPath);
	            	temp_path[1] = folderPath;
				} else if (e.getSource() == source_load) {
					source_txt.setText(folderPath);
	            	temp_path[2] = folderPath;
				}
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
			
			if (e.getSource() == next) {
				for(String temp : temp_path) {
					if(temp == null) {
						System.out.println("입력자료가 충분하지 않습니다.");
						JOptionPane.showMessageDialog(null, "입력자료가 충분하지 않습니다.","입력자료 오류",JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				data.setLatitude(Double.valueOf(company_lat_txt.getText()));
				data.setLongitude(Double.valueOf(company_lon_txt.getText()));
				frames.get("aerin").setUnVisible();
				frames.get("aermet").setVisible();
				frames.get("aermet").exet(data);
				try {
//					JFrame popupFrame = new JFrame();
//					ProgressBar pb = new ProgressBar(popupFrame);
//					Thread thread = new Thread(pb, "pb");
//					thread.run();
					process = new ProcessBuilder("cmd", "/c", "copy", temp_path[0], base_path + "\\temp\\topy.dxf").start();
					process.waitFor();
					process = new ProcessBuilder("cmd", "/c", "copy", temp_path[1], base_path + "\\temp\\boundary.dxf").start();
					process.waitFor();
					process = new ProcessBuilder("cmd", "/c", "copy", temp_path[2], base_path + "\\temp\\source.csv").start();
					process.waitFor();
					process.destroy();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} 
			
			
		}
	}

	@Override
	public void exet(AermodDTO data) { //전 페이지에서 넘어오면서 실행되어
		this.data = data;
		base_path = data.getBase_path();
		try {
		process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\run").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\result").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\temp").start();
		process.waitFor();
		process.destroy();
		} catch(InterruptedException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
