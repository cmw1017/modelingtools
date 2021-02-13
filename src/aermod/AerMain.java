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
//		List<String> matters = new ArrayList<String>();
//		matters.add("SO2");
//		matters.add("TSP");
//		Thread[] threads = new Thread[5];
//		int index = 0;
//		for(;index < 2; index ++) {
//			String matter = matters.get(index);
//			AERMOD aermod = new AERMOD(matter);
//			threads[index] = new Thread(aermod, matter);
//			threads[index].start();
//			Thread.sleep(100);
//		}
//
//		for(;index < 2; index ++) {
//			threads[index].join();
//		}
//		System.out.println("end");
		new AerMain();
	}

}
