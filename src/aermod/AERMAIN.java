package aermod;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.*;

public class AERMAIN implements Runnable{
	
	private final Queue<String> queue = new LinkedList<>();
	private final int max_thread; // 최대 동시 계산 개수
	private final JLabel[][] matters_label;
	private final JButton[] btnList;
	private final AERDTO aerdto;
	
	public AERMAIN(AERDTO aerdto, JLabel[][] matters_label, JButton[] btnList, Integer max_thread) {
		this.aerdto = aerdto;
		this.matters_label = matters_label;
		this.btnList = btnList;
		this.max_thread = max_thread;
	}

	@Override
	public void run() {
		try {
			// 큐에 입력
			List<String> matters = aerdto.getMatters();
			queue.addAll(matters);
			Thread[] threads = new Thread[max_thread]; // 최대 쓰레드 개수
			ThreadInfo t_info = new ThreadInfo(max_thread);
			while(queue.size() != 0) {
				final OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
				double currentCpuLoad = osBean.getSystemCpuLoad();
				if(t_info.current_thread_count != max_thread) {
					if(currentCpuLoad < 0.5) { // CPU 가용률 확인
						String matter = queue.poll();
						int num = matters.indexOf(matter); // 리스트에서 몇번째인지 가져옴(테이블 순서 때문)
						int index_thread = -1; // -1로 해야 할당 받지 못했을때 시작하지 않음
						for (int i = 0; i < max_thread; i++) {
							if (!t_info.index[i]) {
								index_thread = i;
								t_info.index[i] = true;
								System.out.println("use index num : " + i);
								break;
							}
						}
						if (index_thread != -1) {
							assert matter != null;
							AERMOD aermod = new AERMOD(aerdto, matter, matters_label[num][1], matters_label[num][2],
									t_info, index_thread, queue, btnList);
							threads[index_thread] = new Thread(aermod, matter);
							threads[index_thread].start();
							t_info.current_thread_count++;
						}
					}
				}
				System.out.println("CPU usage : " + currentCpuLoad);
				System.out.println("WAIT_" + "current thread count : " + t_info.current_thread_count + " / queue size : " + queue.size());
				for(int i = 0; i < max_thread; i++)
					System.out.println("thread[" + i + "] : " + (t_info.index[i] ? "is used" : "none"));
				System.out.println();
				Thread.sleep(3000);
			}
			System.out.println("Queue is empty");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
