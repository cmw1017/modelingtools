package aermod;

import java.util.*;
import javax.swing.JFrame;


public class startupGUI extends JFrame {
    // 프로그램 메인 클래스
    private static final long serialVersionUID = 1L;
    Map<String, PanelTemplete> frames;
    static JFrame frame = new JFrame();

    public startupGUI() {
        // 프레임 기본 설정
        frames = new HashMap<>();
        frames.put("aerin", new InputPanel());
        frames.put("aermet", new MeteoPanel());
        frames.put("aerpol", new PolPanel());
        frames.put("aerres", new AERMODResultPanel());

        // 경로별 설정(테스트 환경에 따라 경로가 달라짐)
//		String base_path = ".\\";  // 패키징 시의 경로
        String base_path = "E:\\cmw\\aermod RELEASE 2.0";

        // 프레임을 하나씩 돌면서 세팅을 진행하고 보이지 않게 숨김(페이지가 많지 않기에 가능함)
        for (String s : frames.keySet()) {
            PanelTemplete frame = frames.get(s);
            frame.setPanel(base_path);
            frame.setUnVisible();
            frame.setFrames(frames);
        }

        AERDTO aerdto = new AERDTO();        // 모델링 프로그램이 구동되는 동안에 필요한 데이터를 가지고 있음
        aerdto.setBase_path(base_path);    // 메인 폴더 경로 등록
        AERPRE aerpre = new AERPRE(aerdto); // 전처리 클래스 선언
        aerpre.setInpParams();                // 기본입력 데이터 설정
        aerpre.readCriteria(null);    // NULL 이면 기본 경로의 파일을 읽어옴
        aerpre.readAirInfo();                // 기존오염도 데이터 읽어오기

        frames.get("aerin").setVisible();
        frames.get("aerin").execute(aerdto);
        frame.setTitle("AERMOD");
        frame.setSize(1000, 800);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x 버튼을 눌렀을때 종료

    }

    public static void main(String[] args) throws InterruptedException {
        new startupGUI();
    }

}
