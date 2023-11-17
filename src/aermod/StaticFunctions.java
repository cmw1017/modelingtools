package aermod;

import java.io.*;
import java.util.List;
import java.util.Map;

public class StaticFunctions {

	public static Double deg2rad(double deg) {
		return (deg * 3.14159265358979 / 180);
	}

	public static Double rad2deg(double rad) {
		return (rad * 180 / 3.14159265358979);
	}

	public static Double distance(double lat1, double lon1, double lat2, double lon2) {

		double num = lon1 - lon2;
		double num1 = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(num));
		num1 = Math.acos(num1);
		num1 = rad2deg(num1) * 60 * 1.1515;
		return num1 * 1.609344;
	}
	public static int getDecimal(double val) {
		String val_str = String.valueOf(val);
		val_str = val_str.substring(val_str.indexOf("."));
		return val_str.length()-1;
	}

	public static String findCharsetWithFile(String path) throws Exception{
		InputStreamReader inStream = new InputStreamReader(new FileInputStream(path), "CP949");
		int ch;
		while (true) {
			ch = inStream.read();
			if(ch == 30308 || ch == 65533) {// UTF-8로 한글을 읽으면 30308 또는 65533가 출력됨
				System.out.println("Charset : UTF-8");
				return "UTF-8";
			} else if(ch == -1) {
				System.out.println("Charset : EUC-KR");
				return "EUC-KR";
			}
		}
//		System.out.println("Charset : " + ch);
	}


	public void result_post(AERDTO AERDTO) {
		Map<String, Map<String, Double>> criteria = AERDTO.getCriteria();
		Map<String, Map<String, Double>> result = AERDTO.getResult();
		List<String> matters = AERDTO.getMatters();
		String base_path = AERDTO.getBase_path();
		String sido = AERDTO.getSido();
		String sigun = AERDTO.getSigun();
		String gu = AERDTO.getGu();
		Map<String, Map<String, Map<String, Map<String, Double>>>> air_list = AERDTO.getAir_list();
		try {
			OutputStreamWriter outStream = new OutputStreamWriter(
					new FileOutputStream(base_path + "\\result\\report.csv"), "euc-kr");
			String tempStr = "오염물질,시간,환경기준,기존오염도(BC),추가오염도(PC),총오염도(PEC),1단계(PC장<3%EQS),판정,2단계(PEC장<100%EQS),판정,2단계(PC단<EQS단-장),2단계(PEC단<100%EQS),판정,최종판정\n";
			outStream.write(tempStr, 0, tempStr.length());
			for (String matter : matters) {
				double step1_an = -1.0; // 1단계(PC장<3%EQS)
				double step2_1_1 = -1.0; // 2단계(PC단<EQS단-장) 1hr
				double step2_1_2 = -1.0; // 2단계(PEC단<100%EQS) 1hr
				double step2_24_1 = -1.0; // 2단계(PC단<EQS단-장) 24hr
				double step2_24_2 = -1.0; // 2단계(PEC단<100%EQS) 24hr
				double step2_an = -1.0; // 2단계(PEC장<100%EQS)
				int step1_an_judge;  // 1단계(PC장<3%EQS)
				int step2_1_judge;  // 2단계(PC단<EQS단-장) 1hr
				int step2_24_judge;  // 2단계(PC단<EQS단-장) 24hr
				int step2_an_judge;  // 2단계(PEC장<100%EQS)
				int step2_judge;
				
				// 통과여부 계산 파트
				for (String series : criteria.get(matter).keySet()) {
					double criteria_val = criteria.get(matter).get(series);
					double result_val = result.get(matter).get(series);
					double air_val = air_list.get(sido).get(sigun).get(gu).get(matter);
					if (series.equals("1")) {
						if (criteria.get(matter).containsKey("an")) {
							step2_1_1 = result_val / (criteria_val - criteria.get(matter).get("an")) * 100;
						}
						step2_1_2 = (result_val + air_val) / criteria_val * 100;
					} else if(series.equals("8") || series.equals("24")) {
						if (criteria.get(matter).containsKey("an")) {
							step2_24_1 = result_val / (criteria_val - criteria.get(matter).get("an")) * 100;
						}
						step2_24_2 = (result_val + air_val) / criteria_val * 100;
					} else if (series.equals("an")) {
						step1_an = result_val / criteria_val * 100;
						step2_an = (result_val + air_val) / criteria_val * 100;
					}
				}
				step1_an_judge = step1_an == -1 ? -1 : step1_an < 3 ? 1 : 0;
				step2_an_judge = step2_an == -1 ? -1 : step2_an < 100 ? 1 : 0;
				if (step2_1_1 == -1)
					if(step2_1_2 == -1) step2_1_judge = -1;
					else step2_1_judge = step2_1_2 < 100 ? 1 : 0;
				else 
					if(step2_1_2 == -1) step2_1_judge = step2_1_1 < 100 ? 1 : 0;
					else step2_1_judge = (step2_1_1 < 100 || step2_1_2 < 100) ? 1 : 0;
				
				if (step2_24_1 == -1)
					if(step2_24_2 == -1) step2_24_judge = -1;
					else step2_24_judge = step2_24_2 < 100 ? 1 : 0;
				else 
					if(step2_24_2 == -1) step2_24_judge = step2_24_1 < 100 ? 1 : 0;
					else step2_24_judge = (step2_24_1 < 100 || step2_24_2 < 100) ? 1 : 0;
				
				if (step2_1_judge == -1)
					if(step2_24_judge == -1) 
						if(step2_an_judge == -1) step2_judge = -1; 	// 1 : X, 24 : X, an : X
							else step2_judge = step2_an_judge; 		// 1 : X, 24 : X, an : O
					else 
						if(step2_an_judge == -1) step2_judge = step2_24_judge; // 1 : X, 24 : O, an : X
							else step2_judge = (step2_24_judge == 1 && step2_an_judge == 1) ? 1 : 0; // 1 : X, 24 : O, an : O
				else 
					if(step2_24_judge == -1) 
						if(step2_an_judge == -1) step2_judge = step2_1_judge; // 1 : O, 24 : X, an : X
							else step2_judge = (step2_1_judge == 1 && step2_an_judge == 1) ? 1 : 0; // 1 : O, 24 : X, an : O
					else 
						if(step2_an_judge == -1) step2_judge = (step2_1_judge == 1 && step2_24_judge == 1) ? 1 : 0; // 1 : O, 24 : O, an : X
							else  step2_judge = (step2_1_judge == 1 && step2_24_judge == 1 && step2_an_judge == 1) ? 1 : 0; // 1 : O, 24 : O, an : O
				
				// 결과 출력 파트
				for (String series : criteria.get(matter).keySet()) {
					Double criteria_val = criteria.get(matter).get(series);
					Double result_val = result.get(matter).get(series);
					Double air_val = air_list.get(sido).get(sigun).get(gu).get(matter);
					tempStr = matter + ",";
					tempStr += series + ",";
					tempStr += criteria_val + ",";
					tempStr += air_val + ",";
					tempStr += result_val + ",";
					tempStr += (result_val + air_val) + ",";
					if (series.equals("1")) {
						tempStr += ",,,,";
						tempStr += (step2_1_1 == -1.0 ? "," : step2_1_1 + ",");
						tempStr += (step2_1_2 == -1.0 ? ",," : step2_1_2 + "," + (step2_1_judge == 1 ? "통과," : "미통과,"));
					} else if(series.equals("8") || series.equals("24")) {
						tempStr += ",,,,";
						tempStr += (step2_24_1 == -1.0 ? "," : step2_24_1 + ",");
						tempStr += (step2_24_2 == -1.0 ? ",," : step2_24_2 + "," + (step2_24_judge == 1 ? "통과," : "미통과,"));
					} else if (series.equals("an")) {
						tempStr += (step1_an == -1.0 ? ",," : step1_an + "," + (step1_an_judge == 1 ? "통과," : "미통과,"));
						tempStr += (step2_an == -1.0 ? ",," : step2_an + "," + (step2_an_judge == 1 ? "통과," : "미통과,"));
						tempStr += ",,,";
					}
					tempStr += ((step1_an_judge != -1 && step1_an_judge == 1) ? "통과" :
						(step2_judge != -1 && step2_judge == 1) ? "통과" : "미통과");
					tempStr += "\n";
					outStream.write(tempStr, 0, tempStr.length());
				}
			}
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
