package aermod;

import java.io.*;
import java.util.*;

import javax.swing.JLabel;

public class AERMOD implements Runnable{
	
	String matter;
	String[] seires = {"1", "24", "an"};
	String src = "D:\\Modeling\\AERMOD\\yeosu";
	JLabel produce;
	JLabel count;
	Process process = null;
	ProcessBuilder processBuilder = null;
	ThreadInfo t_info;
	int index_thread;
	
	public AERMOD(String matter, JLabel  produce, JLabel count, ThreadInfo t_info, int index_thread) {
		this.matter = matter;
		this.produce = produce;
		this.count = count;
		this.t_info = t_info;
		this.index_thread = index_thread;
	}
	
	public void ReadyProcess(String matter) throws InterruptedException, IOException{
		
		process = new ProcessBuilder("cmd", "/c", "mkdir", src + "\\run\\" + matter).start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\3aermod.exe", src + "\\run\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\aermod.bat", src + "\\run\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\aermod_" + matter + ".inp", src + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "move", src + "\\run\\" +matter + "\\aermod_" + matter + ".inp", src + "\\run\\" +matter + "\\aermod.inp").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\AERMOD.PFL", src + "\\run\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\AERMOD.SFC", src + "\\run\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\POINT_" + matter + ".dat", src + "\\run\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\receptor_input.dat", src + "\\run\\" + matter + "\\").start();
		process.waitFor();
		process.destroy();
	}
	public void RunProcess(String matter) throws IOException, InterruptedException {


		File aermod = new File(src + "\\run\\" + matter + "\\aermod.bat");
		File aermoddir = new File(src + "\\run\\" + matter);
		String str = null;

		String aermodsrc = aermod.getAbsolutePath();

		if (aermod.isFile()) {
			List<String> cmd = new ArrayList<String>();
			cmd.add(aermodsrc);
			processBuilder = new ProcessBuilder(cmd);
			processBuilder.directory(aermoddir);
			process = processBuilder.start();
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			while((str = stdOut.readLine()) != null) {
				if (str.contains("+Now Processing Data For Day No.")) {
					str = str.substring(str.lastIndexOf("No.") + 5, str.length());
					str = str.substring(0, 3);
					produce.setText(str + "/365");
				}
			}
			process.waitFor();
			process.destroy();
			count.setText(String.valueOf(Integer.parseInt(count.getText()) + 1));
		} else {
			System.out.println("에러 : 모델링 실행파일이 없습니다.(3aermod.exe)");
			return;
		}
	}
	
	public void FinishProcess(String matter) throws IOException, InterruptedException {
		process = new ProcessBuilder("cmd", "/c", "mkdir", src + "\\res\\" + matter).start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\run\\" +matter + "\\" + matter + "_an.FIL", src + "\\res\\" +matter + "\\" + matter + "_an.FIL").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\run\\" +matter + "\\" + matter + "_24.FIL", src + "\\res\\" +matter + "\\" + matter + "_24.FIL").start();
		process.waitFor();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\run\\" +matter + "\\" + matter + "_1.FIL", src + "\\res\\" +matter + "\\" + matter + "_1.FIL").start();
		process.waitFor();
		process.destroy();
		
		t_info.index[index_thread] = false;
		t_info.current_thread_count--;
		System.out.println("aermod complete(" + matter + ")");
	}
	

	@Override
	public void run() {
			try {
				ReadyProcess(matter);
				Thread.sleep(10);
				RunProcess(matter);
				Thread.sleep(10);
				FinishProcess(matter);
				Thread.sleep(10);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
	}
}
