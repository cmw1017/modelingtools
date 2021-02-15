package aermod;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JLabel;

public class AERMOD_main implements Runnable{
	
	private Queue<String> queue = new LinkedList<>();
	private final int max_thread = 2;
	
	private List<String> matters;
	private JLabel[][] matters_label;
	private String insrc;
	private Process process = null;
	
	public AERMOD_main(List<String> matters, JLabel[][] matters_label, String insrc) {
		this.matters = matters;
		this.matters_label = matters_label;
		this.insrc = insrc;
	}

	@Override
	public void run() {
		try {
			
			process = new ProcessBuilder("cmd", "/c", "mkdir", insrc + "\\run").start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "mkdir", insrc + "\\res").start();
			process.waitFor();
			process.destroy();
			// 큐에 입력
			for(String matter : matters) {
				queue.add(matter);
			}
			Thread[] threads = new Thread[max_thread]; // 최대 쓰레드 개수
			ThreadInfo t_info = new ThreadInfo(max_thread);
			while(queue.size() != 0) {
				if(t_info.current_thread_count != max_thread) {
					String matter = queue.poll();
					int num = matters.indexOf(matter); // 리스트에서 몇번째인지 가져옴(테이블 순서 때문)
					int index_thread = -1; // -1로 해야 할당 받지 못했을때 시작하지 않음
					for(int i = 0; i < max_thread; i++) {
						if(t_info.index[i] == false) {
							index_thread = i;
							t_info.index[i] = true;
							System.out.println("use index num : " + i);
							break;
						}
					}
					if (index_thread != -1) {
						AERMOD aermod = new AERMOD(matter, matters_label[num][1], matters_label[num][2], t_info, index_thread);
						threads[index_thread] = new Thread(aermod, matter);
						threads[index_thread].start();
						t_info.current_thread_count++;
					}
				}
				Thread.sleep(1000);
				System.out.println("wait" + "_" + t_info.current_thread_count + "_" + queue.size() + "_" + t_info.index[0] + "_" + t_info.index[1]);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
