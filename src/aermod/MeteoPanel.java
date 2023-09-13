package aermod;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class MeteoPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	private Process process = null;
	String base_path;
	String temp_path;
	AermodDTO aermodDTO;

	private Color white = new Color(255, 255, 255);
	JPanel aermetjp = new JPanel();
	JLabel content = new JLabel();
	JLabel station = new JLabel();
	JLabel station_info = new JLabel();
	JLabel bc = new JLabel();
	JLabel bc_info = new JLabel();
	JLabel ec = new JLabel();
	JLabel ec_info = new JLabel();
	JButton ec_download = new RoundedButton("다운로드", Color.decode("#BF95BC"), white, 20);
	JTextField ec_select_info = new JTextField();
	JButton ec_select_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JRadioButton ec_default = new JRadioButton();
	JRadioButton ec_select = new JRadioButton();
	ButtonGroup ec_group = new ButtonGroup();

	JButton next = new RoundedButton("다음", Color.decode("#BF95BC"), white, 20);

	public MeteoPanel() {
	}

	public void setPanel(String base_path) {

		aermetjp.setLayout(null);

		// 프레임
		this.base_path = base_path;
		ImagePanel title = new ImagePanel(base_path+"\\resource\\Step2.png", 1000, 130);
		aermetjp.add(title);
		
		aermetjp.add(station);
		aermetjp.add(station_info);
		aermetjp.add(bc);
		aermetjp.add(bc_info);
		aermetjp.add(ec);
		aermetjp.add(ec_info);
		aermetjp.add(ec_download);
		aermetjp.add(ec_select_info);
		aermetjp.add(ec_select_load);
		aermetjp.add(ec_default);
		aermetjp.add(ec_select);
		ec_group.add(ec_default);
		ec_group.add(ec_select);

		aermetjp.add(next);

		aermetjp.add(content);

		title.setLocation(0, 0);
		title.setSize(1000, 130);

		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 130);
		content.setSize(1000, 670);

		station.setLocation(50, 200);
		station.setSize(200, 50);
		station.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		station.setText("기상대 정보 : ");
		station_info.setLocation(200, 200);
		station_info.setSize(300, 50);
		station_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		station_info.setText("");

		bc.setLocation(50, 275);
		bc.setSize(200, 50);
		bc.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		bc.setText("기존 오염도 정보 : ");
		bc_info.setLocation(250, 275);
		bc_info.setSize(300, 50);
		bc_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		bc_info.setText("2019 ~ 2021년 기존오염도");

		ec.setLocation(50, 350);
		ec.setSize(200, 50);
		ec.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		ec.setText("환경기준 정보 : ");
		ec_info.setLocation(250, 350);
		ec_info.setSize(300, 50);
		ec_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ec_info.setText("환경정책기본법 대기환경기준");
		ec_download.setLocation(600, 365);
		ec_download.setSize(150, 30);
		ec_download.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ec_download.addActionListener(new DownloadListener());

		ec_select_info.setLocation(250, 415);
		ec_select_info.setSize(300, 30);
		ec_select_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ec_select_info.setText("");
		ec_select_load.setLocation(600, 415);
		ec_select_load.setSize(150, 30);
		ec_select_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ec_select_load.addActionListener(new loadListener());

		ec_default.setLocation(225, 365);
		ec_default.setSize(25, 25);
		ec_default.setBackground(Color.decode("#D0D8DA"));
		ec_default.setSelected(true);
		ec_select.setLocation(225, 415);
		ec_select.setSize(25, 25);
		ec_select.setBackground(Color.decode("#D0D8DA"));

		next.setLocation(800, 570);
		next.setSize(150, 50);
		next.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		next.addActionListener(new MoveListener());

	}

	public void setVisible() {
		AerMain.frame.add(aermetjp);
		aermetjp.setVisible(true);
	}

	public void setUnVisible() {
		aermetjp.setVisible(false);
	}

	public void setFrames(Map<String, PanelTemplete> frames) {
		this.frames = frames;
	}

	// 파일 다운로드창
	class DownloadListener implements ActionListener {

		JFileChooser chooser;

		DownloadListener() {
			chooser = new JFileChooser();
		}

		public void actionPerformed(ActionEvent e) {
			String folderPath = "";
	        
	        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // 디렉토리 설정
	        chooser.setCurrentDirectory(new File(aermodDTO.getSelected_file_path())); // 현재 사용 디렉토리를 지정
	        chooser.setAcceptAllFileFilterUsed(true);   // Fileter 모든 파일 적용 
	        chooser.setDialogTitle("파일 선택"); // 창의 제목
	        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv"); // filter 확장자 추가
	        chooser.setFileFilter(filter); // 파일 필터를 추가
	        
	        int returnVal = chooser.showSaveDialog(null); // 열기용 창 오픈
	        
	        if(returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭 
	            folderPath = chooser.getSelectedFile().toString();
	            if(folderPath.lastIndexOf(".") != -1)
					folderPath = folderPath.substring(0, folderPath.lastIndexOf("."));
				try {
					process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\resource\\criteria.csv", folderPath+".csv").start();
					process.waitFor();
					process.destroy();
					JOptionPane.showMessageDialog(null, "다운로드가 완료되었습니다.","다운로드 완료",JOptionPane.PLAIN_MESSAGE);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				aermodDTO.setSelected_file_path(folderPath);
	        }else if(returnVal == JFileChooser.CANCEL_OPTION){ // 취소를 클릭
	            System.out.println("cancel"); 
	            folderPath = "";
	        }
		}

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
		        chooser.setCurrentDirectory(new File(aermodDTO.getSelected_file_path())); // 현재 사용 디렉토리를 지정
		        chooser.setAcceptAllFileFilterUsed(true);   // Fileter 모든 파일 적용 
		        chooser.setDialogTitle("파일 선택"); // 창의 제목
		        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드
		        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv"); // filter 확장자 추가
		        chooser.setFileFilter(filter); // 파일 필터를 추가
		        
		        int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈
		        
		        if(returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭 
		            folderPath = chooser.getSelectedFile().toString();
		            ec_select_info.setText(folderPath);
		            temp_path = folderPath;
					aermodDTO.setSelected_file_path(folderPath);
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
				if(ec_select.isSelected() == true) {
					System.out.println("use discrete ec_criteria");
					aermodDTO.setEc_path(temp_path);
					AERPRE aerpre = new AERPRE(aermodDTO);
					aerpre.ReadCriteria(temp_path);
				} else {
					aermodDTO.setEc_path(null);
				}
				frames.get("aermet").setUnVisible();
				frames.get("aerpol").setVisible();
				frames.get("aerpol").exet(aermodDTO);
				System.out.println("Move PolPanel");
				System.out.println("Ec path : " + aermodDTO.getEc_path());
			}

		}
	}

	@Override
	public void exet(AermodDTO aermodDTO) {
		this.aermodDTO = aermodDTO;
		base_path = aermodDTO.getBase_path();
		AERPRE aerpre = new AERPRE(aermodDTO);
		aerpre.RunRMO();
		RMO rmo = aermodDTO.getRmo();
		station_info.setText(rmo.getName() + " 기상대");
	}
}
