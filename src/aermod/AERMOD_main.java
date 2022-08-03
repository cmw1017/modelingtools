package aermod;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.*;

public class AERMOD_main implements Runnable{
	
	private Queue<String> queue = new LinkedList<>();
	private int max_thread = 3; // 최대 동시 계산 개수
	
	private List<String> matters;
	private JLabel[][] matters_label;
	AermodDTO aermodDTO;
	JButton result_bt;
	JButton complete_bt;
	
	public AERMOD_main(AermodDTO aermodDTO, JLabel[][] matters_label, JButton result_bt, JButton complete_bt, Integer max_thread) {
		this.aermodDTO = aermodDTO;
		this.matters = aermodDTO.getMatters();
		this.matters_label = matters_label;
		this.result_bt = result_bt;
		this.complete_bt = complete_bt;
		this.max_thread = max_thread;
	}

	@Override
	public void run() {
		
		try {
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
						AERMOD aermod = new AERMOD(aermodDTO, matter, matters_label[num][1], matters_label[num][2],
								t_info, index_thread, queue, result_bt, complete_bt);
						threads[index_thread] = new Thread(aermod, matter);
						threads[index_thread].start();
						t_info.current_thread_count++;
					}
				}
				Thread.sleep(3000);
				System.out.println("WAIT_" + "current thread count : " + t_info.current_thread_count + " / queue size : " + queue.size());
				for(int i = 0; i < max_thread; i++)
					System.out.println("thread[" + i + "] : " + (t_info.index[i] ? "is used" : "none"));
				System.out.println();
			}
			System.out.println("Queue is empty");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
