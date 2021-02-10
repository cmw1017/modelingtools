package aermod;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class AERPOST {

	String insrc;
	
	public AERPOST(String insrc) {
		this.insrc = insrc;
	}
	
	public double exet() {
		double maxconc = -1;
		try {
			int ch, series1 = 0, series2 = 0; // serise1 : 데이터 종류 구분, series2 : 데이터 개수 구분
			int flag = 0; // 데이터 읽기 시작 플래그
			Reader inStream = new FileReader(insrc);
			StringBuilder str = new StringBuilder();
			List<Double> conc = new ArrayList<Double>();
			
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1) { //  단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (str.length() != 0) {
						if (str.toString().equals("________") && ch == 13 && flag == 0) {
							flag = 1;
							str = new StringBuilder();
							continue;
						}
						if(flag == 1) series1++;
						if(series1 == 3) {
							System.out.println(str);
							conc.add(Double.parseDouble(str.toString()));
							series2++;
						}
						str = new StringBuilder();
					}
					if(ch == 13  || ch == 10) series1 = 0;
				}
				if (ch == -1) {
					System.out.println("읽기 종료");
					System.out.println("데이터 개수 : " + series2);
					break;
				}
			}
			maxconc = findMax(conc);
			System.out.println(maxconc);
			inStream.close();
			return maxconc;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return maxconc;
	}
	
	double findMax(List<Double> array) {
		double max = array.get(0);
		for (Double conc : array) {
			if (max < conc) {
				max = conc;
			}
		}
		return max;
	}
}
