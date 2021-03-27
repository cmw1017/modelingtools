package aermod;

import java.util.*;
import javax.swing.JFrame;


public class AerMain extends JFrame {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	static JFrame frame = new JFrame();
	
	public AerMain() {
		frames = new HashMap<String, PanelTemplete>();
		frames.put("aerin", new InputPanel());
		frames.put("aermet", new MeteoPanel());
		frames.put("aerpol", new PolPanel());
		frames.put("aerres", new AERMODResultPanel());
//		String base_path = ".\\";
		String base_path = "C:\\Users\\cmw10\\OneDrive\\aermod RELEASE 1.1";
//		String base_path = "C:\\Users\\DELL\\OneDrive\\aermod RELEASE 1.1";
		Iterator<String> iter = frames.keySet().iterator();
		while(iter.hasNext()) {
			PanelTemplete frame = frames.get(iter.next());
			frame.setPanel(base_path);
			frame.setUnVisible();
			frame.setFrames(frames);
		}
		
		
		// 기본입력 데이터 설정
		AermodDTO aermodDTO = new AermodDTO();
		// SO2 CO NO2 Pb Benzene PM-10 Zn NH3 CS2 Cr Hg Cu Vinylchloride H2S Dichloromethane TCE As Ni Cd Br F HCN HCl Phenol Formaldehyde
		String[] pollist = { "SO2", "CO", "NO2", "Pb", "Benzene", "PM-10", "Zn", "NH3", "CS2", "Cr", "Hg", "Cu",
				"Vinylchloride", "H2S", "Dichloromethane", "TCE", "As", "Ni", "Cd", "Br", "F", "HCN", "HCl", "Phenol",
				"Formaldehyde" };
		Map<String,Map<String,String>> inpparam = new HashMap<String,Map<String,String>>();
		for(String matter : pollist) {
			inpparam.put(matter, new HashMap<String,String>());
			inpparam.get(matter).put("@@!1", matter);
			if(matter == "SO2" || matter == "CO" || matter == "NO2")
				inpparam.get(matter).put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
			else 
				inpparam.get(matter).put("@@!2", "1000000.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		}
		
		
		aermodDTO.setBase_path(base_path);
		aermodDTO.setInpparam(inpparam);
		AERPRE aerpre = new AERPRE(aermodDTO);
		aerpre.ReadCriteria(null);
		aerpre.ReadAirInfo();
		
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
