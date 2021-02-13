package aermod;

import java.io.*;
import java.util.*;

public class AERMOD implements Runnable{
	
	String matter;
	String[] seires = {"1", "24", "an"};
	String src = "D:\\Modeling\\AERMOD\\yeosu";

	Process process = null;
	ProcessBuilder processBuilder = null;
	
	public AERMOD(String matter) {
		this.matter = matter;
	}
	
	public void ReadyProcess(String matter) throws InterruptedException, IOException{
		process = new ProcessBuilder("cmd", "/c", "mkdir", src + "\\" + matter).start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\3aermod.exe", src + "\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\aermod.bat", src + "\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\aermod_" + matter + ".inp", src + "\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "move", src + "\\" +matter + "\\aermod_" + matter + ".inp", src + "\\" +matter + "\\aermod.inp").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\AERMOD.PFL", src + "\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\AERMOD.SFC", src + "\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\POINT_" + matter + ".dat", src + "\\" + matter + "\\").start();
		process = new ProcessBuilder("cmd", "/c", "copy", src + "\\receptor_input.dat", src + "\\" + matter + "\\").start();
		process.waitFor();
		process.destroy();
	}
	public void RunProcess(String matter) throws IOException, InterruptedException {


		File aermod = new File(src + "\\" + matter + "\\aermod.bat");
		File aermoddir = new File(src + "\\" + matter);
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
					System.out.println(str + "/" + matter);
				}
			}
			
			process.waitFor();
			process.destroy();
			
			System.out.println("aermod complete(" + matter + ")");
		} else {
			System.out.println("에러 : 모델링 실행파일이 없습니다.(3aermod.exe)");
			return;
		}
	}
	
	

	@Override
	public void run() {
			try {
				ReadyProcess(matter);
				RunProcess(matter);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
	}
}
