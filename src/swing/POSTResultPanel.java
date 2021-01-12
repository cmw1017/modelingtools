package swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class POSTResultPanel extends JFrame implements PanelTemplete {
	private static final long serialVersionUID = 1L;
	Map<String, PanelTemplete> frames;
	JFrame frame;
	JPanel postresjp = new JPanel();
	JLabel content = new JLabel();
	JButton complete = new JButton("완료");
	
	public POSTResultPanel(JFrame frame) {
		this.frame = frame;
	}
	
	public void setPanel() {

		postresjp.setLayout(null);
		postresjp.add(content);
		
		postresjp.add(complete);
		
		
		content.setText("CALPOST Result Content");
		content.setHorizontalAlignment(SwingConstants.CENTER);
		content.setVerticalAlignment(SwingConstants.CENTER);
		content.setOpaque(true);
		content.setBackground(Color.WHITE);
		content.setBounds(25,25,725,450);
		
		complete.setBounds(625,490,150,50);
		complete.addActionListener(new MoveListener());
	}
	
	public void setVisible() {
		frame.add(postresjp);
		postresjp.setVisible(true);
	}
	
	public void setUnVisible() {
		postresjp.setVisible(false);
	}
	
	public void setFrames(Map<String, PanelTemplete> frames) {
		this.frames = frames;
	}
	
	// 페이지 이동
		class MoveListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == complete) {
					frames.get("main").setVisible();
					frames.get("postres").setUnVisible();
				}
			}
		}
}
