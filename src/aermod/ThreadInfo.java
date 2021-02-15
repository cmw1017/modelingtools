package aermod;

public class ThreadInfo {
	Boolean[] index; // 가용 쓰레드 인덱스
	Integer current_thread_count = 0; // 현재 가용 쓰레드 개수
	
	public ThreadInfo(int max_thread_count) {
		index = new Boolean[max_thread_count];
		for(int i =0; i < max_thread_count; i++) {
			index[i] = false;
		}
	}
}
