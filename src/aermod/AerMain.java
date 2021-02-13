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
		
		List<String> matters = new ArrayList<String>();
		matters.add("SO2");
		matters.add("TSP");
		matters.add("CO");
		Data data = new Data();
		data.setMatters(matters);
		frames.get("aerres").exet(data);
		
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
