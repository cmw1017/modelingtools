package aermod;

import java.util.*;
import javax.swing.JFrame;


public class AerMain extends JFrame {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	JFrame frame = new JFrame();
	
	public AerMain() {
		frames = new HashMap<String, PanelTemplete>();
		frames.put("aerres", new AERMODResultPanel(frame));
		
		Iterator<String> iter = frames.keySet().iterator();
		while(iter.hasNext()) {
			PanelTemplete frame = frames.get(iter.next());
			frame.setPanel();
			frame.setUnVisible();
			frame.setFrames(frames);
		}
		
		// 나중에 지워져야할 부분
		List<String> matters = new ArrayList<String>();
		matters.add("SO2");
		matters.add("TSP");
		matters.add("CO");
		matters.add("PM10");
		matters.add("NH3");
		Map<String,Map<String,String>> inpparam = new HashMap<String,Map<String,String>>();
		inpparam.put("SO2", new HashMap<String,String>());
		inpparam.get("SO2").put("@@!1", "SO2");
		inpparam.get("SO2").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		inpparam.get("SO2").put("@@!3", "43168");
		inpparam.get("SO2").put("@@!4", "47158");
		inpparam.put("TSP", new HashMap<String,String>());
		inpparam.get("TSP").put("@@!1", "TSP");
		inpparam.get("TSP").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		inpparam.get("TSP").put("@@!3", "43168");
		inpparam.get("TSP").put("@@!4", "47158");
		inpparam.put("CO", new HashMap<String,String>());
		inpparam.get("CO").put("@@!1", "CO");
		inpparam.get("CO").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		inpparam.get("CO").put("@@!3", "43168");
		inpparam.get("CO").put("@@!4", "47158");
		inpparam.put("PM10", new HashMap<String,String>());
		inpparam.get("PM10").put("@@!1", "PM10");
		inpparam.get("PM10").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		inpparam.get("PM10").put("@@!3", "43168");
		inpparam.get("PM10").put("@@!4", "47158");
		inpparam.put("NH3", new HashMap<String,String>());
		inpparam.get("NH3").put("@@!1", "NH3");
		inpparam.get("NH3").put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		inpparam.get("NH3").put("@@!3", "43168");
		inpparam.get("NH3").put("@@!4", "47158");
		// 나중에 지워져야할 부분
		
		AermodDTO aermodDTO = new AermodDTO();
		aermodDTO.setMatters(matters);
		aermodDTO.setInpparam(inpparam);
		frames.get("aerres").exet(aermodDTO);
		
		frames.get("aerres").setVisible();

		frame.setTitle("AERMOD");
		frame.setSize(1000, 700);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x 버튼을 눌렀을때 종료
	}
	
	public static void main(String[] args) throws InterruptedException {
		new AerMain();
	}

}
