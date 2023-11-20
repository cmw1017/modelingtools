package aermod;

import java.io.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JLabel;

public class AERMOD implements Runnable {

	String matter;
	String base_path;
	JLabel produce;
	JLabel state;
	Process process = null;
	ProcessBuilder processBuilder = null;
	ThreadInfo t_info;
	int index_thread;
	Queue<String> queue;
	AERDTO AERDTO;
	JButton[] btnList;

	// base_path : 기본 파일이 넣어져 있는 경로(에어모드 실행파일, inp base 파일 등..)
	// matter : 모델링 할 물질 명
	// produce : 진행 상태를 알려주는 상자(CLI 경우 NULL)
	// count : 현재 모델링 진행 횟수를 알려주는 상자(CLI 경우 NULL)
	// t_info : 모든 쓰레드의 정보 및 상태를 가지고 있는 클래스
	// index_thread : 쓰레드의 사용 상태를 알려주는 변수
	// queue : 모델링 해야할 물질을 저장
	// btnList : 모델링 이후 보여져야 할 버튼의 리스트(CLI 경우 NULL)
	public AERMOD(AERDTO AERDTO, String matter, JLabel produce, JLabel state, ThreadInfo t_info, int index_thread,
				  Queue<String> queue, JButton[] btnList) {
		this.AERDTO = AERDTO;
		this.base_path = AERDTO.getBase_path();
		this.matter = matter;
		this.produce = produce;
		this.state = state;
		this.t_info = t_info;
		this.index_thread = index_thread;
		this.queue = queue;
		this.btnList = btnList;
	}

	public void ReadyProcess() throws InterruptedException, IOException {

		process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\run\\" + matter).start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\3aermod.exe",
				base_path + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\aermod.bat",
				base_path + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\aermod_" + matter + ".inp",
				base_path + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "move",
				base_path + "\\run\\" + matter + "\\aermod_" + matter + ".inp",
				base_path + "\\run\\" + matter + "\\aermod.inp").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\AERMOD.PFL",
				base_path + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\AERMOD.SFC",
				base_path + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\POINT_" + matter + ".dat",
				base_path + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\receptor_input.dat",
				base_path + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process.destroy();
	}

	public void RunProcess() throws IOException, InterruptedException {

		File aermod = new File(base_path + "\\run\\" + matter + "\\aermod.bat");
		File aermodDir = new File(base_path + "\\run\\" + matter);
		String str;

		String aermodSrc = aermod.getAbsolutePath();

		if (aermod.isFile()) {
			List<String> cmd = new ArrayList<>();
			cmd.add(aermodSrc);
			processBuilder = new ProcessBuilder(cmd);
			processBuilder.directory(aermodDir);
			process = processBuilder.start();
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));

			if(state != null) state.setText("진행중");
			while ((str = stdOut.readLine()) != null) {
				if (str.contains("+Now Processing Data For Day No.")) {
					str = str.substring(str.lastIndexOf("No.") + 5);
					str = str.substring(0, 3);
					if(produce != null) produce.setText(str + "/365");
					else System.out.println("MODELING IN PROGRESS_" + matter + "(" + str + "/365)");
				}
			}
			process.waitFor();
			process.destroy();
			if(state != null) state.setText("완료");
		} else {
			System.out.println("에러 : 모델링 실행파일이 없습니다.(3aermod.exe)");
		}
	}

	public void FinishProcess() throws IOException, InterruptedException {
		File receptorResult = new File(base_path + "\\result\\receptors");
		if(!receptorResult.exists()) {
			process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\result\\receptors").start();
			process.waitFor();
		}
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\" + matter + "\\" + matter + "_an.FIL",
				base_path + "\\result\\receptors\\" + matter + "_an.FIL").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\" + matter + "\\" + matter + "_24.FIL",
				base_path + "\\result\\receptors\\" + matter + "_24.FIL").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\run\\" + matter + "\\" + matter + "_1.FIL",
				base_path + "\\result\\receptors\\" + matter + "_1.FIL").start();
		process.waitFor();
		process.destroy();

		t_info.index[index_thread] = false;
		t_info.current_thread_count--;
		System.out.println("< aermod complete(" + matter + ") >");
	}

	public void PostProcess() {
		AERPOST aerpost = new AERPOST(AERDTO, matter);
		aerpost.RunProcess();
		boolean end = true;
		for (int i = 0; i < t_info.index.length; i++) {
			if (t_info.index[i]) {
				end = false; // 실행중인 쓰래드가 한개라도 있으면 false 로 변환
				break;
			}
		}
		if (end) { // 최종 결과 출력
			System.out.println("Modeling ALL END");
			System.out.println("Start Modeling REPORT");
			StaticFunctions fn = new StaticFunctions();
			fn.result_post(AERDTO);
			if(btnList != null) for (JButton jButton : btnList) jButton.setVisible(true);
		}
	}

	@Override
	public void run() {
		try {
			ReadyProcess();
			Thread.sleep(10);
			RunProcess();
			Thread.sleep(10);
			FinishProcess();
			Thread.sleep(10);
			PostProcess();
			Thread.sleep(10);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
