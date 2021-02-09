package aermod;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class POSTProcess {

	String insrc;
	int series1;
	
	public POSTProcess(String insrc, int series1) {
		this.insrc = insrc;
		this.series1 = series1;
	}
	
	public void exet() {
		System.out.print("AERPOST 진행....");
		try {
			
			int ch, series2 = 0, series3 = 0; // serise1 : 시계열 구분, serise2 : 데이터 종류 구분, series3 : 데이터 개수 구분
			int flag = 0; // 데이터 읽기 시작 플래그
			int type[] = {11, 11, 10};
			Reader inStream = new FileReader(insrc);
			StringBuilder str = new StringBuilder();
			Double[] conc = new Double[type[series1]];
			
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1) { //  단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (str.length() != 0) {
						//System.out.println(str);
						if (str.toString().equals("________") && ch == 13 && flag == 0) {
							flag = 1;
//							System.out.println("flag on");
							str = new StringBuilder();
							continue;
						}
						if(flag == 1) series2++;
						if(ch == 13) series2 = 0;
						if(series2 == 3) {
//							System.out.println(str);
							conc[series3] = Double.parseDouble(str.toString());
						}
						str = new StringBuilder();
					}
				}
				
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
