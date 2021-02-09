package calpuff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;


public class CalMain extends JFrame {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	JFrame frame = new JFrame();
	
	public CalMain() {
		
		frames = new HashMap<String, PanelTemplete>();
		frames.put("main", new MainPanel(frame));
		frames.put("ctg", new CTGPanel(frame)); 
		frames.put("ctgres", new CTGResultPanel(frame));
		frames.put("make", new MAKEPanel(frame));
		frames.put("makeres", new MAKEResultPanel(frame));
		frames.put("post", new POSTPanel(frame));
		frames.put("postres", new POSTResultPanel(frame));
		
		Iterator<String> iter = frames.keySet().iterator();
		while(iter.hasNext()) {
			PanelTemplete frame = frames.get(iter.next());
			frame.setPanel();
			frame.setUnVisible();
			frame.setFrames(frames);
		}
		
		frames.get("main").setVisible();

		frame.setTitle("CALPUFF");
		frame.setSize(1000, 700);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x 버튼을 눌렀을때 종료
		
	}

	public static void main(String[] args){ 
		new CalMain(); 
	}
}
