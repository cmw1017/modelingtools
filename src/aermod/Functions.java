package aermod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

public class Functions {

	public void result_post(AermodDTO aermodDTO) {
		Map<String,Map<String,Double>> criteria = aermodDTO.getCriteria();
		Map<String,Map<String,Double>> result = aermodDTO.getResult();
		List<String> matters = aermodDTO.getMatters();
		String base_path = aermodDTO.getBase_path();
		String sido = aermodDTO.getSido();
		String sigun = aermodDTO.getSigun();
		String gu = aermodDTO.getGu();
		Map<String, Map<String, Map<String, Map<String, Double>>>> air_list =aermodDTO.getAir_list();
		try {
			OutputStreamWriter outStream = new OutputStreamWriter(
					new FileOutputStream(base_path + "\\result\\report.csv"), "euc-kr");
			String tempstr = "오염물질,시간,환경기준,기존오염도(BC),추가오염도(PC),총오염도(PEC),1단계(PC장<3%EQS),2단계(PEC장<100%EQS),2단계(PC단<EQS단-장),2단계(PEC단<100%EQS)\n";
			outStream.write(tempstr, 0, tempstr.length());
			for (String matter : matters) {
				for (String series : criteria.get(matter).keySet()) {
					Double criteria_val = criteria.get(matter).get(series);
					Double result_val = result.get(matter).get(series);
					Double air_val = air_list.get(sido).get(sigun).get(gu).get(matter);
					
					@SuppressWarnings("unused")
					Double step1_1 = -1.0;
					@SuppressWarnings("unused")
					Double step2_1 = -1.0;
					@SuppressWarnings("unused")
					Double step2_2 = -1.0;
					@SuppressWarnings("unused")
					Double step2_3 = -1.0;
					if(series.equals("1") || series.equals("8") || series.equals("24")) {
						if(criteria.get(matter).containsKey("an")) {
							step2_2 = result_val / (criteria_val - criteria.get(matter).get("an")) * 100;
						}
						step2_3 = (result_val + air_val) / criteria_val * 100;
					} else if(series.equals("an")) {
						step1_1 = result_val / criteria_val * 100;
						step2_1 = (result_val + air_val) / criteria_val * 100;
					}
					tempstr = matter + ",";
					tempstr += series + ",";
					tempstr += criteria_val + ",";
					tempstr += air_val + ",";
					tempstr += result_val + ",";
					tempstr += (result_val + air_val) + ",";
					tempstr += (step1_1 == -1.0 ? "," : step1_1 + ",");
					tempstr += (step2_1 == -1.0 ? "," : step2_1 + ",");
					tempstr += (step2_2 == -1.0 ? "," : step2_2 + ",");
					tempstr += (step2_3 == -1.0 ? "," : step2_3 + ",");
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
