package aermod;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import calpuff.RoundedButton;

public class InputPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	private Process process = null;
	ProcessBuilder processBuilder = null;
	String base_path;
	String temp_path[] = new String[4];
	AermodDTO aermodDTO;
	String sido[] = { "서울특별시", "인천광역시", "경기도" };

	private Color white = new Color(255, 255, 255);
	JPanel aerinjp = new JPanel();
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
	JLabel terrain_dxf_info = new JLabel();
	JRadioButton terrain_dxf = new JRadioButton();
	JLabel terrain_dat_info = new JLabel();
	JRadioButton terrain_dat = new JRadioButton();
	ButtonGroup terrain_group = new ButtonGroup();
	JLabel topy = new JLabel();
	JTextField topy_txt = new JTextField();
	JButton topy_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JLabel boundary = new JLabel();
	JTextField boundary_txt = new JTextField();
	JButton boundary_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JLabel dat = new JLabel();
	JTextField dat_txt = new JTextField();
	JButton dat_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JLabel source = new JLabel();
	JTextField source_txt = new JTextField();
	JButton source_load = new RoundedButton("파일 불러오기", Color.decode("#BF95BC"), white, 20);
	JButton next = new RoundedButton("다음", Color.decode("#BF95BC"), white, 20);

	public void setPanel(String base_path) {

		aerinjp.setLayout(null);

		// 프레임
		this.base_path = base_path;
		ImagePanel title = new ImagePanel(base_path + "\\resource\\Step1.png", 1000, 130);
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
		aerinjp.add(terrain_dxf_info);
		aerinjp.add(terrain_dat_info);
		aerinjp.add(terrain_dxf);
		aerinjp.add(terrain_dat);
		terrain_group.add(terrain_dxf);
		terrain_group.add(terrain_dat);
		aerinjp.add(topy);
		aerinjp.add(topy_txt);
		aerinjp.add(topy_load);
		aerinjp.add(boundary);
		aerinjp.add(boundary_txt);
		aerinjp.add(boundary_load);
		aerinjp.add(dat);
		aerinjp.add(dat_txt);
		aerinjp.add(dat_load);


		aerinjp.add(source);
		aerinjp.add(source_txt);
		aerinjp.add(source_load);

		aerinjp.add(next);

		aerinjp.add(content);

		title.setLocation(0, 0);
		title.setSize(1000, 130);

		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 130);
		content.setSize(1000, 670);

		company.setLocation(50, 150);
		company.setSize(200, 50);
		company.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		company.setText("사업장 정보 입력");
		company_lat.setLocation(150, 200);
		company_lat.setSize(50, 50);
		company_lat.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lat.setText("위도");
		company_lat_txt.setLocation(200, 210);
		company_lat_txt.setSize(100, 30);
		company_lat_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lon.setLocation(350, 200);
		company_lon.setSize(50, 50);
		company_lon.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lon.setText("경도");
		company_lon_txt.setLocation(400, 210);
		company_lon_txt.setSize(100, 30);
		company_lon_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_lon_txt.setText("");

		company_sido.setLocation(150, 250);
		company_sido.setSize(50, 50);
		company_sido.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_sido.setText("시도");
		company_sido_txt.setLocation(200, 260);
		company_sido_txt.setSize(150, 30);
		company_sido_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_sigun.setLocation(400, 250);
		company_sigun.setSize(50, 50);
		company_sigun.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_sigun.setText("시군구");
		company_sigun_txt.setLocation(460, 260);
		company_sigun_txt.setSize(150, 30);
		company_sigun_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_gu.setLocation(650, 250);
		company_gu.setSize(50, 50);
		company_gu.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		company_gu.setText("구");
		company_gu_txt.setLocation(680, 260);
		company_gu_txt.setSize(150, 30);
		company_gu_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));

		ksic.setLocation(150, 300);
		ksic.setSize(150, 50);
		ksic.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		ksic.setText("산업 분류 선택");

		terrain.setLocation(50, 380);
		terrain.setSize(200, 30);
		terrain.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		terrain.setText("지형자료 입력");

		terrain_dxf_info.setLocation(250, 380);
		terrain_dxf_info.setSize(200, 30);
		terrain_dxf_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		terrain_dxf_info.setText(".dxf 파일 업로드 ");
		terrain_dxf.setLocation(380, 385);
		terrain_dxf.setSize(25, 25);
		terrain_dxf.setBackground(Color.decode("#D0D8DA"));
		terrain_dxf.addActionListener(new SelectListener());
		terrain_dxf.setSelected(true);
		terrain_dat_info.setLocation(450, 380);
		terrain_dat_info.setSize(200, 30);
		terrain_dat_info.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		terrain_dat_info.setText(".dat 파일 업로드 ");
		terrain_dat.setLocation(580, 380);
		terrain_dat.setSize(25, 25);
		terrain_dat.setBackground(Color.decode("#D0D8DA"));
		terrain_dat.addActionListener(new SelectListener());

		topy.setLocation(150, 440);
		topy.setSize(200, 30);
		topy.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		topy.setText("지형도(.dxf)");
		topy_txt.setLocation(350, 440);
		topy_txt.setSize(350, 30);
		topy_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		topy_txt.setText("");
		topy_load.setLocation(750, 440);
		topy_load.setSize(150, 30);
		topy_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		topy_load.addActionListener(new loadListener());
		boundary.setLocation(150, 495);
		boundary.setSize(200, 30);
		boundary.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		boundary.setText("부지 경계(.dxf)");
		boundary_txt.setLocation(350, 495);
		boundary_txt.setSize(350, 30);
		boundary_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		boundary_txt.setText("");
		boundary_load.setLocation(750, 495);
		boundary_load.setSize(150, 30);
		boundary_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		boundary_load.addActionListener(new loadListener());
		dat.setLocation(150, 440);
		dat.setSize(200, 30);
		dat.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		dat.setText("지형데이터(.dat)");
		dat.setVisible(false);
		dat_txt.setLocation(350, 440);
		dat_txt.setSize(350, 30);
		dat_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		dat_txt.setText("");
		dat_txt.setVisible(false);
		dat_load.setLocation(750, 440);
		dat_load.setSize(150, 30);
		dat_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		dat_load.addActionListener(new loadListener());
		dat_load.setVisible(false);

		source.setLocation(50, 555);
		source.setSize(300, 30);
		source.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		source.setText("배출원 정보 입력(.csv)");
		source_txt.setLocation(350, 585);
		source_txt.setSize(350, 30);
		source_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		source_txt.setText("");
		source_load.setLocation(750, 585);
		source_load.setSize(150, 30);
		source_load.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		source_load.addActionListener(new loadListener());

		next.setLocation(800, 670);
		next.setSize(150, 50);
		next.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		next.addActionListener(new MoveListener());
		
		company_lat_txt.setText("35.555");
		company_lon_txt.setText("127.555");
		topy_txt.setText("D:\\Modeling\\topy.dxf");
		temp_path[0] = "D:\\Modeling\\topy.dxf";
		boundary_txt.setText("D:\\Modeling\\boundary.dxf");
		temp_path[1] = "D:\\Modeling\\boundary.dxf";
		source_txt.setText("D:\\Modeling\\111.csv");
		temp_path[3] = "D:\\Modeling\\111.csv";
	}

	public void setVisible() {
		AerMain.frame.add(aerinjp);
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
			chooser.setAcceptAllFileFilterUsed(true); // Fileter 모든 파일 적용
			chooser.setDialogTitle("파일 선택"); // 창의 제목
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드
			FileNameExtensionFilter filter;
			if (e.getSource() == source_load) {
				filter = new FileNameExtensionFilter("csv", "csv"); // filter 확장자 추가
			} else if(e.getSource() == dat_load){
				filter = new FileNameExtensionFilter("dat", "dat"); // filter 확장자 추가
			} else {
				filter = new FileNameExtensionFilter("dxf", "dxf"); // filter 확장자 추가
			}
			chooser.setFileFilter(filter); // 파일 필터를 추가

			int returnVal = chooser.showOpenDialog(null); // 열기용 창 오픈

			if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
				folderPath = chooser.getSelectedFile().toString();
				if (e.getSource() == topy_load) {
					topy_txt.setText(folderPath);
					temp_path[0] = folderPath;
				} else if (e.getSource() == boundary_load) {
					boundary_txt.setText(folderPath);
					temp_path[1] = folderPath;
				} else if (e.getSource() == dat_load) {
					dat_txt.setText(folderPath);
					temp_path[2] = folderPath;
				} else if (e.getSource() == source_load) {
					source_txt.setText(folderPath);
					temp_path[3] = folderPath;
				}
			} else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
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
				if(terrain_dxf.isSelected() == true) {
					if(temp_path[0] == null || temp_path[1] == null || temp_path[3] == null)
					{
						System.out.println("입력자료가 충분하지 않습니다.");
						JOptionPane.showMessageDialog(null, "입력자료가 충분하지 않습니다.", "입력자료 오류", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					if(temp_path[2] == null || temp_path[3] == null)
					{
						System.out.println("입력자료가 충분하지 않습니다.");
						JOptionPane.showMessageDialog(null, "입력자료가 충분하지 않습니다.", "입력자료 오류", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				try {
					aermodDTO.setLatitude(Double.valueOf(company_lat_txt.getText()));
					aermodDTO.setLongitude(Double.valueOf(company_lon_txt.getText()));
				} catch (Exception e1) {
					System.out.println("위경도 데이터를 입력해주세요");
					JOptionPane.showMessageDialog(null, "위경도 데이터를 입력해주세요", "입력데이터 오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(terrain_dxf.isSelected() == true) 
					JOptionPane.showMessageDialog(null, "지형 변환시에는 시간이 오래 소모될수 있습니다(최대 1시간 소요).","주의",JOptionPane.PLAIN_MESSAGE);
				aermodDTO.setSido(company_sido_txt.getSelectedItem().toString());
				aermodDTO.setSigun(company_sigun_txt.getSelectedItem().toString());
				aermodDTO.setGu(company_gu_txt.getSelectedItem().toString());
				aermodDTO.setSource_path(temp_path[3]);
				frames.get("aerin").setUnVisible();
				frames.get("aermet").setVisible();
				frames.get("aermet").exet(aermodDTO);
				System.out.println("Move MeteoPanel");
				System.out.println("Company sido : " + aermodDTO.getSido());
				System.out.println("Company sigun : " + aermodDTO.getSigun());
				System.out.println("Company gu : " + aermodDTO.getGu());
				System.out.println("Company lat : " + aermodDTO.getLatitude());
				System.out.println("Company lon : " + aermodDTO.getLongitude());
				System.out.println("Source Path : " + aermodDTO.getSource_path());
				
				try {
					
					if(terrain_dxf.isSelected() == true) {
						process = new ProcessBuilder("cmd", "/c", "copy", temp_path[0], base_path + "\\temp\\topy.dxf")
								.start();
						process.waitFor();
						process = new ProcessBuilder("cmd", "/c", "copy", temp_path[1], base_path + "\\temp\\boundary.dxf")
								.start();
						process.waitFor();
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\terrain.bat", base_path + "\\temp\\terrain.bat")
								.start();
						process.waitFor();
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\0aermod.exe", base_path + "\\temp\\0aermod.exe")
								.start();
						process.waitFor();
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\1aermod.exe", base_path + "\\temp\\1aermod.exe")
								.start();
						process.waitFor();
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\2aermod.exe", base_path + "\\temp\\2aermod.exe")
								.start();
						process.waitFor();
						
						File terrain = new File(base_path + "\\temp\\terrain.bat");
						File terraindir = new File(base_path + "\\temp");
						String terrainsrc = terrain.getAbsolutePath();
						if (terrain.isFile()) {
							List<String> cmd = new ArrayList<String>();
							cmd.add(terrainsrc);
							processBuilder = new ProcessBuilder(cmd);
							processBuilder.directory(terraindir);
							process = processBuilder.start();
							process.waitFor();
						} else {
							System.out.println("에러 : 지형변환 실행파일이 없습니다.(terrain.dat)");
							return;
						}
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\temp\\receptor_input.dat", base_path + "\\run\\receptor_input.dat")
								.start();
						process.waitFor();
						
						
					} else {
						process = new ProcessBuilder("cmd", "/c", "copy", temp_path[2], base_path + "\\run\\receptor_input.dat")
								.start();
						process.waitFor();
					}
					process = new ProcessBuilder("cmd", "/c", "copy", temp_path[3], base_path + "\\run\\source.csv")
							.start();
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
	
	class SelectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(terrain_dxf.isSelected() == true) {
				topy.setVisible(true);
				topy_txt.setVisible(true);
				topy_load.setVisible(true);
				boundary.setVisible(true);
				boundary_txt.setVisible(true);
				boundary_load.setVisible(true);
				dat.setVisible(false);
				dat_txt.setVisible(false);
				dat_load.setVisible(false);
			}
			else {
				topy.setVisible(false);
				topy_txt.setVisible(false);
				topy_load.setVisible(false);
				boundary.setVisible(false);
				boundary_txt.setVisible(false);
				boundary_load.setVisible(false);
				dat.setVisible(true);
				dat_txt.setVisible(true);
				dat_load.setVisible(true);
			}
			
		}
	}
	
	
	@Override
	public void exet(AermodDTO aermodDTO) { 
		System.out.println("Set Input Panel");
		this.aermodDTO = aermodDTO;
		try {
			process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\run").start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\result").start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\temp").start();
			process.waitFor();
			process.destroy();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
