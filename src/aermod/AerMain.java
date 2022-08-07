package aermod;

import java.util.*;
import javax.swing.JFrame;


public class AerMain extends JFrame {
	// 프로그램 메인 클래스
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	static JFrame frame = new JFrame();
	
	public AerMain() {
		// 프레임 기본 설정
		frames = new HashMap<String, PanelTemplete>();
		frames.put("aerin", new InputPanel());
		frames.put("aermet", new MeteoPanel());
		frames.put("aerpol", new PolPanel());
		frames.put("aerres", new AERMODResultPanel());

		// 경로별 설정(테스트 환경에 따라 경로가 달라짐)
//		String base_path = ".\\";
//		String base_path = "C:\\Users\\cmw10\\OneDrive\\aermod RELEASE 1.4";
		String base_path = "E:\\cmw\\aermod RELEASE 1.5";

		// 프레임을 하나씩 돌면서 세팅을 진행하고 보이지 않게 숨김(페이지가 많지 않기에 가능함)
		Iterator<String> iter = frames.keySet().iterator();
		while(iter.hasNext()) {
			PanelTemplete frame = frames.get(iter.next());
			frame.setPanel(base_path);
			frame.setUnVisible();
			frame.setFrames(frames);
		}
		
		
		// 기본입력 데이터 설정
		AermodDTO aermodDTO = new AermodDTO(); // 모델링 프로그램이 구동되는 동안에 필요한 데이터를 가지고 있음
		// 모델링 오염물질
		// 순서 SO2 CO NO2 Pb Benzene PM-10 Zn NH3 CS2 Cr Hg Cu Vinylchloride H2S Dichloromethane TCE As Ni Cd Br F HCN HCl Phenol Formaldehyde
		String[] pollist = { "SO2", "CO", "NO2", "Pb", "Benzene", "PM-10", "Zn", "NH3", "CS2", "Cr", "Hg", "Cu",
				"Vinylchloride", "H2S", "Dichloromethane", "TCE", "As", "Ni", "Cd", "Br", "F", "HCN", "HCl", "Phenol",
				"Formaldehyde" };

		// inp 파일의 파라미터 설정, 추후에 오염물질별 inp 파일 생성할때 사용되는 데이터
		Map<String,Map<String,String>> inpparam = new HashMap<String,Map<String,String>>();
		for(String matter : pollist) {
			inpparam.put(matter, new HashMap<String,String>());
			inpparam.get(matter).put("@@!1", matter);
			if(matter.equals("SO2") || matter.equals("CO") || matter.equals("NO2")) // 210520 : ==에서 equal로 변경
				inpparam.get(matter).put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
			else 
				inpparam.get(matter).put("@@!2", "1000000.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		}
		
		
		aermodDTO.setBase_path(base_path); // 메인 폴더 경로 등록
		aermodDTO.setInpparam(inpparam); // 초기 inp 파라미터 등록
		AERPRE aerpre = new AERPRE(aermodDTO); // 전처리 클래스 선언
		aerpre.ReadCriteria(null); // null이면 기본 경로의 파일을 읽어옴
		aerpre.ReadAirInfo(); // 기존오염도 데이터 읽어오기
		
		frames.get("aerin").setVisible();
		frames.get("aerin").exet(aermodDTO);
		frame.setTitle("AERMOD");
		frame.setSize(1000, 800);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x 버튼을 눌렀을때 종료

	}
	
	public static void main(String[] args) throws InterruptedException {
		new AerMain();
	}

}
