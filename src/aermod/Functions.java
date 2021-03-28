package aermod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

public class Functions {

	public void result_post(AermodDTO aermodDTO) {
		Map<String, Map<String, Double>> criteria = aermodDTO.getCriteria();
		Map<String, Map<String, Double>> result = aermodDTO.getResult();
		List<String> matters = aermodDTO.getMatters();
		String base_path = aermodDTO.getBase_path();
		String sido = aermodDTO.getSido();
		String sigun = aermodDTO.getSigun();
		String gu = aermodDTO.getGu();
		Map<String, Map<String, Map<String, Map<String, Double>>>> air_list = aermodDTO.getAir_list();
		try {
			OutputStreamWriter outStream = new OutputStreamWriter(
					new FileOutputStream(base_path + "\\result\\report.csv"), "euc-kr");
			String tempstr = "오염물질,시간,환경기준,기존오염도(BC),추가오염도(PC),총오염도(PEC),1단계(PC장<3%EQS),판정,2단계(PEC장<100%EQS),판정,2단계(PC단<EQS단-장),2단계(PEC단<100%EQS),판정,최종판정\n";
			outStream.write(tempstr, 0, tempstr.length());
			for (String matter : matters) {
				Double step1_an = -1.0; // 1단계(PC장<3%EQS)
				Double step2_1_1 = -1.0; // 2단계(PC단<EQS단-장) 1hr
				Double step2_1_2 = -1.0; // 2단계(PEC단<100%EQS) 1hr
				Double step2_24_1 = -1.0; // 2단계(PC단<EQS단-장) 24hr
				Double step2_24_2 = -1.0; // 2단계(PEC단<100%EQS) 24hr
				Double step2_an = -1.0; // 2단계(PEC장<100%EQS)
				int step1_an_judge = -1;  // 1단계(PC장<3%EQS)        
				int step2_1_judge = -1;  // 2단계(PC단<EQS단-장) 1hr   
				int step2_24_judge = -1;  // 2단계(PC단<EQS단-장) 24hr  
				int step2_an_judge = -1;  // 2단계(PEC장<100%EQS)     
				int step2_judge = -1;
				
				// 통과여부 계산 파트
				for (String series : criteria.get(matter).keySet()) {
					Double criteria_val = criteria.get(matter).get(series);
					Double result_val = result.get(matter).get(series);
					Double air_val = air_list.get(sido).get(sigun).get(gu).get(matter);
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
					tempstr = matter + ",";
					tempstr += series + ",";
					tempstr += criteria_val + ",";
					tempstr += air_val + ",";
					tempstr += result_val + ",";
					tempstr += (result_val + air_val) + ",";
					if (series.equals("1")) {
						tempstr += ",,,,";
						tempstr += (step2_1_1 == -1.0 ? "," : step2_1_1 + ",");
						tempstr += (step2_1_2 == -1.0 ? ",," : step2_1_2 + "," + (step2_1_judge == 1 ? "통과," : "미통과,"));
					} else if(series.equals("8") || series.equals("24")) {
						tempstr += ",,,,";
						tempstr += (step2_24_1 == -1.0 ? "," : step2_24_1 + ",");
						tempstr += (step2_24_2 == -1.0 ? ",," : step2_24_2 + "," + (step2_24_judge == 1 ? "통과," : "미통과,"));
					} else if (series.equals("an")) {
						tempstr += (step1_an == -1.0 ? ",," : step1_an + "," + (step1_an_judge == 1 ? "통과," : "미통과,"));
						tempstr += (step2_an == -1.0 ? ",," : step2_an + "," + (step2_an_judge == 1 ? "통과," : "미통과,"));
						tempstr += ",,,";
					}
					tempstr += ((step1_an_judge != -1 && step1_an_judge == 1) ? "통과" : 
						(step2_judge != -1 && step2_judge == 1) ? "통과" : "미통과");
					tempstr += "\n";
					outStream.write(tempstr, 0, tempstr.length());
				}
			}
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
