package aermod;

import java.util.*;
import javax.swing.JFrame;


public class AerMain extends JFrame {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	JFrame frame = new JFrame();
	
	public AerMain() {
		frames = new HashMap<String, PanelTemplete>();
		frames.put("aerin", new InputPanel(frame));
		frames.put("aermet", new MeteoPanel(frame));
		frames.put("aerpol", new PolPanel(frame));
		frames.put("aerres", new AERMODResultPanel(frame));
		
		Iterator<String> iter = frames.keySet().iterator();
		while(iter.hasNext()) {
			PanelTemplete frame = frames.get(iter.next());
			frame.setPanel();
			frame.setUnVisible();
			frame.setFrames(frames);
		}
		
		// 나중에 지워져야할 부분
		// SO2 CO NO2 Pb Benzene PM-10 Zn NH3 CS2 Cr Hg Cu Vinylchloride H2S Dichloromethane TCE As Ni Cd Br F HCN HCl Phenol Formaldehyde
//		List<String> matters = new ArrayList<String>();
//		matters.add("SO2");
//		matters.add("TSP");
//		matters.add("CO");
//		matters.add("PM-10");
//		matters.add("NH3");
//		Map<String,Map<String,String>> inpparam = new HashMap<String,Map<String,String>>();
//		inpparam.put("SO2", new HashMap<String,String>());
//		inpparam.get("SO2").put("@@!1", "SO2");
//		inpparam.get("SO2").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
//		inpparam.get("SO2").put("@@!3", "43168");
//		inpparam.get("SO2").put("@@!4", "47158");
//		inpparam.put("TSP", new HashMap<String,String>());
//		inpparam.get("TSP").put("@@!1", "TSP");
//		inpparam.get("TSP").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
//		inpparam.get("TSP").put("@@!3", "43168");
//		inpparam.get("TSP").put("@@!4", "47158");
//		inpparam.put("CO", new HashMap<String,String>());
//		inpparam.get("CO").put("@@!1", "CO");
//		inpparam.get("CO").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
//		inpparam.get("CO").put("@@!3", "43168");
//		inpparam.get("CO").put("@@!4", "47158");
//		inpparam.put("PM-10", new HashMap<String,String>());
//		inpparam.get("PM-10").put("@@!1", "PM10");
//		inpparam.get("PM-10").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
//		inpparam.get("PM-10").put("@@!3", "43168");
//		inpparam.get("PM-10").put("@@!4", "47158");
//		inpparam.put("NH3", new HashMap<String,String>());
//		inpparam.get("NH3").put("@@!1", "NH3");
//		inpparam.get("NH3").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
//		inpparam.get("NH3").put("@@!3", "43168");
//		inpparam.get("NH3").put("@@!4", "47158");
//		Map<String,Map<String,Double>> criteria = new HashMap<String,Map<String,Double>>();
//		criteria.put("SO2", new HashMap<String, Double>());
//		criteria.get("SO2").put("1", 100.00);
//		criteria.get("SO2").put("24", 20.00);
//		criteria.get("SO2").put("an", 5.00);
//		criteria.put("TSP", new HashMap<String, Double>());
//		criteria.get("TSP").put("1", 200.00);
//		criteria.get("TSP").put("24", 20.00);
//		criteria.get("TSP").put("an", 5.00);
//		criteria.put("CO", new HashMap<String, Double>());
//		criteria.get("CO").put("1", 200.00);
//		criteria.get("CO").put("24", 10.00);
//		criteria.get("CO").put("an", 5.00);
//		criteria.put("PM-10", new HashMap<String, Double>());
//		criteria.get("PM-10").put("1", 200.00);
//		criteria.get("PM-10").put("24", 20.00);
//		criteria.get("PM-10").put("an", 5.00);
//		criteria.put("NH3", new HashMap<String, Double>());
//		criteria.get("NH3").put("1", 200.00);
//		criteria.get("NH3").put("24", 20.00);
//		criteria.get("NH3").put("an", 1.00);
//		

//		aermodDTO.setMatters(matters);
//		aermodDTO.setInpparam(inpparam);
//		aermodDTO.setCriteria(criteria);
//		frames.get("arein").exet(aermodDTO);
		// 나중에 지워져야할 부분
		
		String base_path = "D:\\Modeling\\AERMOD\\aermod";
		AermodDTO aermodDTO = new AermodDTO();
		aermodDTO.setBase_path(base_path);
		frames.get("aermet").setVisible();
		frames.get("aermet").exet(aermodDTO);
//		frames.get("aerin").setVisible();
//		frames.get("aerin").exet(aermodDTO);
		
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
