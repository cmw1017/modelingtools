package aermod;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class PolPanel extends JFrame implements PanelTemplete {
    private static final long serialVersionUID = 1L;
    private Map<String, PanelTemplete> frames;
    String base_path;
    private AERDTO aerdto;
    private final String[] hourList = {"1", "8", "24", "an"};
	private final Color white = new Color(255, 255, 255);
    private final JPanel basePanel = new JPanel();
    private final JLabel content = new JLabel();
    private final JLabel pol = new JLabel();
    private final JLabel thread = new JLabel();
    private final JTextField thread_txt = new JTextField();
    private final ArrayList<JLabel> polName = new ArrayList<>();
    private final ArrayList<JLabel> polTime = new ArrayList<>();
    private final ArrayList<JLabel> polVal = new ArrayList<>();
    private final JButton next = new RoundedButton("다음", Color.decode("#BF95BC"), white, 20);

    public PolPanel() { }

    public void setPanel(String base_path) {

        basePanel.setLayout(null);

        // 프레임
        this.base_path = base_path;
        ImagePanel title = new ImagePanel(base_path + "\\resource\\Step2.png", 1000, 130);
        basePanel.add(title);

        basePanel.add(pol);
        basePanel.add(next);
        basePanel.add(thread);
        basePanel.add(thread_txt);


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
        pol.setText("환경기준 정보");

        thread.setLocation(200, 870);
        thread.setSize(200, 50);
        thread.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        thread.setText("동시 모델링 횟수(1~10) : ");
        thread_txt.setLocation(400, 870);
        thread_txt.setSize(100, 50);
        thread_txt.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        thread_txt.setText("3");
        next.setLocation(800, 870);
        next.setSize(150, 50);
        next.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        next.addActionListener(new MoveListener());

    }

    public void setVisible() {
        startupGUI.frame.add(basePanel);
        basePanel.setVisible(true);
    }

    public void setUnVisible() {
        basePanel.setVisible(false);
    }

    public void setFrames(Map<String, PanelTemplete> frames) {
        this.frames = frames;
    }

    // 페이지 이동
    class MoveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == next) {
                if (Integer.parseInt(thread_txt.getText()) == 0) {
                    JOptionPane.showMessageDialog(null, "동시모델링 횟수는 1 이상이어야 합니다.", "동시모델링 횟수 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                aerdto.setThread_num(Integer.parseInt(thread_txt.getText()));
                frames.get("aerpol").setUnVisible();
                frames.get("aerres").setVisible();
                frames.get("aerres").execute(aerdto);
            }
        }
    }

    @Override
    public void execute(AERDTO aerdto) {
        this.aerdto = aerdto;
		Map<String, Map<String, Double>> criteria = aerdto.getCriteria();
        startupGUI.frame.setPreferredSize(new Dimension(1000, 1000));
        startupGUI.frame.pack();
        int x = 0;
        int y = 0;
        int z = 1;
        for (int i = 1, k = 1; i < AERDTO.polList.length + 1; i++, k++) {
            polName.add(new JLabel());
            polName.get(i - 1).setLocation(50 + 320 * x, 200 + 40 * y);
            polName.get(i - 1).setSize(150, 50);
            polName.get(i - 1).setFont(new Font("맑은 고딕", Font.BOLD, 15));
            polName.get(i - 1).setText(AERDTO.polList[i - 1]);
            basePanel.add(polName.get(i - 1));
            for (int j = 1; j < hourList.length + 1; j++) {
                if (criteria.get(AERDTO.polList[i - 1]).containsKey(hourList[j - 1])) {
                    polTime.add(new JLabel());
                    polTime.get(z - 1).setLocation(200 + 320 * x, 200 + 40 * y);
                    polTime.get(z - 1).setSize(150, 50);
                    polTime.get(z - 1).setFont(new Font("맑은 고딕", Font.BOLD, 15));
                    polTime.get(z - 1).setText(hourList[j - 1]);
                    polVal.add(new JLabel());
                    polVal.get(z - 1).setLocation(250 + 320 * x, 200 + 40 * y);
                    polVal.get(z - 1).setSize(150, 50);
                    polVal.get(z - 1).setFont(new Font("맑은 고딕", Font.BOLD, 15));
                    polVal.get(z - 1).setText(String.valueOf(criteria.get(AERDTO.polList[i - 1]).get(hourList[j - 1])));
                    basePanel.add(polTime.get(z - 1));
                    basePanel.add(polVal.get(z - 1));
                    y++;
                    z++;
                }
            }
            if ((x != 2 && k % 8 == 0) || (x == 2 && k % 12 == 0)) {
                x++;
                y = 0;
                k = 1;
            }
        }
        basePanel.add(content);
    }
}
