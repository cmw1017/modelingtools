package aermod;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class AERMODResultPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Map<String, PanelTemplete> frames;
	String base_path;
	String temp_path;
	AermodDTO aermodDTO;
	Process process = null;

	private String[] header = { "오염물질", "모델 진행도", "진행 상태" };
	private List<String> matters;

	private JPanel aerresjp = new JPanel();
	private JLabel[] headers = new JLabel[6];
	private Color white = new Color(255, 255, 255);
	private JLabel content = new JLabel();
	private JLabel pol = new JLabel();
	private JButton emissionResult = new RoundedButton("분석결과 저장", Color.decode("#84B1D9"), white, 20);
	private JButton meteoResult = new RoundedButton("기상정보 저장", Color.decode("#84B1D9"), white, 20);
	private JButton pointResult = new RoundedButton("수용점농도 저장", Color.decode("#84B1D9"), white, 20);
	private JButton complete = new RoundedButton("완료", Color.decode("#84B1D9"), white, 20);
	private JButton[] btnList = new JButton[] {emissionResult, meteoResult, pointResult, complete};

	public AERMODResultPanel() {
	}

	public void setPanel(String base_path) {

		aerresjp.setLayout(null);

		// 프레임
		this.base_path = base_path;
		ImagePanel title = new ImagePanel(base_path + "\\resource\\Step3.png", 1000, 130);
		aerresjp.add(title);
		aerresjp.add(pol);
		aerresjp.add(emissionResult);
		aerresjp.add(meteoResult);
		aerresjp.add(pointResult);
		aerresjp.add(complete);

		title.setLocation(0, 0);
		title.setSize(1000, 130);

		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.decode("#D0D8DA"));
		content.setLocation(0, 100);
		content.setSize(1000, 870);

		pol.setLocation(50, 150);
		pol.setSize(250, 50);
		pol.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		pol.setText("모델링 진행 정보");

		for (int i = 0; i < headers.length; i++) {
			headers[i] = new JLabel();
			headers[i].setHorizontalAlignment(SwingConstants.CENTER);
			headers[i].setOpaque(true);
			headers[i].setBackground(Color.decode("#D0D8DA"));
			headers[i].setFont(new Font("맑은 고딕", Font.BOLD, 15));
			headers[i].setLocation(50 + 150 * i, 220);
			headers[i].setSize(150, 50);
			headers[i].setText(header[i % 3]);
			aerresjp.add(headers[i]);
		}
		
		// 이동 버튼 시작
		emissionResult.setLocation(300, 150);
		emissionResult.setSize(125, 50);
		emissionResult.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		emissionResult.addActionListener(new DownloadListener());
		emissionResult.setVisible(false);
		meteoResult.setLocation(450, 150);
		meteoResult.setSize(125, 50);
		meteoResult.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		meteoResult.addActionListener(new DownloadListener());
		meteoResult.setVisible(false);
		pointResult.setLocation(600, 150);
		pointResult.setSize(125, 50);
		pointResult.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		pointResult.addActionListener(new DownloadListener());
		pointResult.setVisible(false);
		complete.setLocation(750, 150);
		complete.setSize(125, 50);
		complete.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		complete.addActionListener(new MoveListener());
		complete.setVisible(false);
	}

	public void setVisible() {
		AerMain.frame.add(aerresjp);
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
			if (e.getSource() == complete) {
				try {
					process = new ProcessBuilder("cmd", "/c", "rmdir", "/s", "/q", base_path + "\\run").start();
					process.waitFor();
					process = new ProcessBuilder("cmd", "/c", "rmdir", "/s", "/q", base_path + "\\temp").start();
					process.waitFor();
					process = new ProcessBuilder("cmd", "/c", "rmdir", "/s", "/q", base_path + "\\result").start();
					process.waitFor();
					process.destroy();
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
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
			chooser.setAcceptAllFileFilterUsed(true); // Filter 모든 파일 적용
			chooser.setDialogTitle("결과 다운로드"); // 창의 제목
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			if(e.getSource() == emissionResult) {
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // 파일 선택 모드
				FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv"); // filter 확장자 추가
				chooser.setFileFilter(filter); // 파일 필터를 추가
			} else {
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 폴더 선택 모드
			}

			int returnVal = chooser.showSaveDialog(null); // 열기용 창 오픈

			if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
				folderPath = chooser.getSelectedFile().toString();
				try {
					if(e.getSource() == emissionResult) { // 분석결과 저장
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\result\\report.csv",
								folderPath + ".csv").start();
						process.waitFor();
						process.destroy();
					} else if (e.getSource() == meteoResult) { // 기상정보 저장
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\result\\PFL.dat",
								folderPath + "\\PFL.dat").start();
						process.waitFor();
						process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\result\\SFC.dat",
								folderPath + "\\SFC.dat").start();
						process.waitFor();
						process.destroy();
					} else { // 수용점농도 저장
						process = new ProcessBuilder("cmd", "/c", "xcopy", base_path + "\\result\\recepters\\",
								folderPath + "\\", "/E").start();
						System.out.println("process wait");
						BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
						String str = null;
						while ((str = stdOut.readLine()) != null) {
							System.out.println(str);
						}
						process.waitFor(5L, TimeUnit.SECONDS);
						System.out.println("process destroy");
						process.destroy();
					}
					JOptionPane.showMessageDialog(null, "다운로드가 완료되었습니다.", "다운로드 완료", JOptionPane.PLAIN_MESSAGE);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				folderPath = folderPath.substring(0, folderPath.lastIndexOf("\\"));
				System.out.println("Save Directory Path : " + folderPath);
				aermodDTO.setSelected_file_path(folderPath);
			} else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
				System.out.println("cancel");
				folderPath = "\\";
			}
		}

	}

	@Override
	public void exet(AermodDTO aermodDTO) {
		this.aermodDTO = aermodDTO;
		AerMain.frame.setPreferredSize(new Dimension(1000, 1000));
		AerMain.frame.pack();
		AERPRE aerpre = new AERPRE(aermodDTO);
		aerpre.RunProcess();

		matters = aermodDTO.getMatters();
		int length = matters.size();

		JLabel[][] matters_label = new JLabel[length][header.length];
		for (int i = 0, k = 0, y = 0; i < length; i++, y++) {
			for (int j = 0; j < 3; j++) {
				matters_label[i][j] = new JLabel();
				matters_label[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				matters_label[i][j].setOpaque(true);
				matters_label[i][j].setBackground(Color.decode("#D0D8DA"));
				matters_label[i][j].setFont(new Font("맑은 고딕", Font.BOLD, 15));
				matters_label[i][j].setLocation(50 + 450 * k + 150 * j, 260 + 50 * y);
				matters_label[i][j].setSize(150, 50);
				if (j == 0)
					matters_label[i][j].setText(matters.get(i));
				else
					matters_label[i][j].setText("대기");
				aerresjp.add(matters_label[i][j]);
			}
			if (i == 10) {
				k++;
				y = -1;
			}
		}
		aerresjp.add(content);
		AERMOD_main aermain = new AERMOD_main(aermodDTO, matters_label, btnList, aermodDTO.getThread_num());
		Thread thread = new Thread(aermain, "aermod_main");
		thread.start();
	}
}
