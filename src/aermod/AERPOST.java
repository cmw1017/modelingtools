package aermod;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AERPOST {

	String matter;
	String[] series = {"1", "24", "an"};
	private final AERDTO aerdto;
	
	public AERPOST(AERDTO aerdto, String matter) {
		this.aerdto = aerdto;
		this.matter = matter;
	}
	
	public double ReadData(String series) {
		String base_path = this.aerdto.getBase_path();
		double maxConc = -1;
		try {
			int ch, series1 = 0, series2 = 0; // series1 : 데이터 종류 구분, series2 : 데이터 개수 구분
			int flag = 0; // 데이터 읽기 시작 플래그
			Reader inStream = new FileReader(base_path + "\\result\\receptors\\" + matter + "_"+ series+".FIL");
			StringBuilder str = new StringBuilder();
			List<Double> conc = new ArrayList<>();
			
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
//							System.out.println(str);
							conc.add(Double.parseDouble(str.toString()));
							series2++;
						}
						str = new StringBuilder();
					}
					if(ch == 13  || ch == 10) series1 = 0;
				}
				if (ch == -1) {
					System.out.println("< FIL read complete >");
					System.out.println("데이터 개수 : " + series2);
					break;
				}
			}
			maxConc = findMax(conc);
//			System.out.println(maxConc);
			inStream.close();
			return maxConc;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return maxConc;
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
	
	public void RunProcess() {
		Map<String,Map<String,Double>> result = aerdto.getResult();
		for(String key : result.keySet()) {
			if(key.equals(matter)) {
				for(String time : series) {
					if(result.get(matter).containsKey((time.equals("24") && matter.equals("CO")) ? "8" : time)) { //CO 에만 8시간이 있으므로 예외처리
						result.get(matter).replace((time.equals("24") && matter.equals("CO")) ? "8" : time, ReadData(time));
					}
				}
			}
		}
	}
}
