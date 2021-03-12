package aermod;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AERPRE {

	String matter;
	Map<String, String> inpparam;
	private List<String> matters;
	private String base_path;
	private ArrayList<String> stack_header;
	private ArrayList<Map<String, Double>> stack_info;
	private String[] pollist = {"SO2", "CO", "NO2", "Pb", "Benzene", "PM-10", "Zn", "NH3", "CS2", "Cr", "Hg", "Cu", "Vinylchloride", "H2S", "Dichloromethane", "TCE", "As", "Ni", "Cd", "Br", "F", "HCN", "HCl", "Phenol", "Formaldehyde"};
	AermodDTO aermodDTO;
	
	public AERPRE(AermodDTO aermodDTO) {
		this.aermodDTO = aermodDTO;
		this.base_path = aermodDTO.getBase_path();
	}

	public void CreateInp() {
		try {
			int ch;
			Reader inStream = new FileReader(base_path + "\\aermod_.inp");
			BufferedWriter outStream = new BufferedWriter(
					new FileWriter(base_path + "\\run\\aermod_" + matter + ".inp"));
			StringBuilder str = new StringBuilder();

			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (str.length() != 0) {
						if (str.toString().contains("@@!1")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!1", inpparam.get("@@!1"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!2")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!2", inpparam.get("@@!2"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!3")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!3", inpparam.get("@@!3"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!4")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!4", inpparam.get("@@!4"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						}
						outStream.write(str.toString(), 0, str.toString().length());
						str = new StringBuilder();
					}
					if (ch == -1) {
						System.out.println("complete create INP file(" + matter + ")");
						break;
					}
					outStream.write(String.valueOf((char) ch), 0, String.valueOf((char) ch).length());
				}

			}
			inStream.close();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ReadSource() {
		try {
			stack_header = new ArrayList<>();
			stack_info = new ArrayList<>();
			int ch;
			int series1 = 0, series2 = 0; // series : 열의 개수(그 이상은 읽지 않음)
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(base_path + "\\111.csv"), "euc-kr");
			StringBuilder str = new StringBuilder();

			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44) {
						series1++;
						if (series2 == 0)
							stack_header.add(str.toString()); // 처음 한줄을 스택 정보 종류를 읽어서 저장
						else if (series2 != 0) {
							String number = str.toString();
							if (number.length() == 0)
								number = "0";
							stack_info.get(series2 - 1).put(stack_header.get(series1 - 1), Double.parseDouble(number));
						}
						// 다음부터는 스택의 정보를 저장, 그에 맞는 이름은 위에서 읽은 스택 정보 종류와 연결시킴
					}
					if (series1 == 35 || (series1 == 34 && ch == 13)) {
						if (series2 == 0)
							stack_header.add(str.toString()); // 처음 한줄을 스택 정보 종류를 읽어서 저장
						else if (series2 != 0) {
							String number = str.toString();
							if (number.length() == 0)
								number = "0";
							stack_info.get(series2 - 1).put(stack_header.get(series1), Double.parseDouble(number));
						}
						series1 = 0;
						series2++;
						stack_info.add(new HashMap<String, Double>());
					}
					if (str.length() != 0) {
						str = new StringBuilder();
					}
					if (ch == -1) {
						break;
					}
				}

			}
			inStream.close();
			stack_info.remove(series2 - 1);
			
			// 출력예시
			for (String temp_str : stack_header) {
				System.out.print(temp_str + " ");
			}
			System.out.println();
			for (Map<String, Double> temp_map : stack_info) {
				for (String temp_str : stack_header) {
					System.out.print(temp_map.get(temp_str) + " ");
				}
				System.out.println();
			}
			System.out.println();
			
			double check = 0; // 오염물질 배출량이 있는지 확인(하나도 없는것은 모델링 돌릴 필요가 없으므로)
			matters = new ArrayList<>();
			for (String pol : pollist) { // 모델링 대상 오염물질을 걸러내는 작업
				for (Map<String, Double> temp_map : stack_info) {
						check += temp_map.get(pol);
				}
				if(check != 0) matters.add(pol);
				check = 0;
			}
			
			System.out.print("모델링 오염물질 : ");
			for (String pol : matters) {
				System.out.print(pol + " ");
			}
			aermodDTO.setMatters(matters);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void CreateSource() {
		try {
			if (stack_header.indexOf(matter) == -1)
				System.out.println("에러");
			BufferedWriter outStream = new BufferedWriter(
					new FileWriter(base_path + "\\run\\POINT_" + matter + ".dat"));
			for (Map<String, Double> temp_map : stack_info) {
				if (temp_map.get(matter) != 0) {
					String source1 = "SO LOCATION  "
							+ Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(0)))))
							+ "    POINT" + String.format("%11.2f", temp_map.get(stack_header.get(1)))
							+ String.format("%11.2f", temp_map.get(stack_header.get(2)))
							+ String.format("%4d",
									Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(3))))))
							+ "\n";
					String source2 = "SO SRCPARAM  "
							+ Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(0))))) + "    "
							+ String.format("%7.4f",
									temp_map.get(stack_header.get(9)) * temp_map.get(matter) / 1000 / 60)
							+ String.format("%8.2f", temp_map.get(stack_header.get(5)))
							+ String.format("%8.2f", temp_map.get(stack_header.get(6)) + 273.15)
							+ String.format("%10.5f", temp_map.get(stack_header.get(7)))
							+ String.format("%5.1f", temp_map.get(stack_header.get(8))) + "\n";
					outStream.write(source1, 0, source1.length());
					outStream.write(source2, 0, source2.length());
				}
			}
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void RunProcess() {

		ReadSource();
//		CreateInp();
	}

}
