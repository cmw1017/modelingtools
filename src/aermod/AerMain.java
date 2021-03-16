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
//		String base_path = "C:\\Users\\cmw10\\OneDrive\\aermod";
		String base_path = "C:\\Users\\DELL\\OneDrive\\aermod";
		
		Iterator<String> iter = frames.keySet().iterator();
		while(iter.hasNext()) {
			PanelTemplete frame = frames.get(iter.next());
			frame.setPanel(base_path);
			frame.setUnVisible();
			frame.setFrames(frames);
		}
		
		// 나중에 지워져야할 부분
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
		
		// 나중에 지워져야할 부분
//		Map<String,Map<String,Double>> criteria = new HashMap<String,Map<String,Double>>();
//		criteria.put("SO2", new HashMap<String, Double>());
//		criteria.get("SO2").put("1", 100.00);
//		criteria.get("SO2").put("24", 20.00);
//		criteria.get("SO2").put("an", 5.00);
//		criteria.put("NO2", new HashMap<String, Double>());
//		criteria.get("NO2").put("1", 200.00);
//		criteria.get("NO2").put("24", 10.00);
//		criteria.get("NO2").put("an", 5.00);
//		criteria.put("PM-10", new HashMap<String, Double>());
//		criteria.get("PM-10").put("1", 200.00);
//		criteria.get("PM-10").put("24", 20.00);
//		criteria.get("PM-10").put("an", 5.00);
//		criteria.put("Zn", new HashMap<String, Double>());
//		criteria.get("Zn").put("1", 200.00);
//		criteria.get("Zn").put("24", 20.00);
//		criteria.get("Zn").put("an", 1.00);
//		criteria.put("NH3", new HashMap<String, Double>());
//		criteria.get("NH3").put("1", 200.00);
//		criteria.get("NH3").put("24", 20.00);
//		criteria.get("NH3").put("an", 1.00);
//		criteria.put("Dichloromethane", new HashMap<String, Double>());
//		criteria.get("Dichloromethane").put("1", 200.00);
//		criteria.get("Dichloromethane").put("24", 20.00);
//		criteria.get("Dichloromethane").put("an", 1.00);
//		criteria.put("Br", new HashMap<String, Double>());
//		criteria.get("Br").put("1", 200.00);
//		criteria.get("Br").put("24", 20.00);
//		criteria.get("Br").put("an", 1.00);
//		criteria.put("Formaldehyde", new HashMap<String, Double>());
//		criteria.get("Formaldehyde").put("1", 200.00);
//		criteria.get("Formaldehyde").put("24", 20.00);
//		criteria.get("Formaldehyde").put("an", 1.00);
		// 나중에 지워져야할 부분
		
		
		AermodDTO aermodDTO = new AermodDTO();
		aermodDTO.setBase_path(base_path);
		aermodDTO.setInpparam(inpparam);

		
		// 나중에 지워져야할 부분
//		aermodDTO.setCriteria(criteria);
		aermodDTO.setLatitude(35.4213);
		aermodDTO.setLongitude(127.3965);
		// 나중에 지워져야할 부분
		
		frames.get("aerin").setVisible();
		frames.get("aerin").exet(aermodDTO);
		
		AERPRE aerpre = new AERPRE(aermodDTO);
		aerpre.ReadCriteria();
		frame.setTitle("AERMOD");
		frame.setSize(1000, 800);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x 버튼을 눌렀을때 종료
		
		
//		frames.get("aerres").setVisible();
//		frames.get("aerres").exet(aermodDTO);
	}
	
	public static void main(String[] args) throws InterruptedException {
		new AerMain();
	}

}
