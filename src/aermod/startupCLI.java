package aermod;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static aermod.StaticFunctions.*;

public class startupCLI {
    public static void main(String[] args) throws InterruptedException {
        try {
            long startTime = System.currentTimeMillis();
            // 경로별 설정(테스트 환경에 따라 경로가 달라짐)
//		    String base_path = ".\\";  // 패키징 시의 경로
            String base_path = "E:\\cmw\\aermod RELEASE 2.1";

            AERDTO aerdto = new AERDTO();        // 모델링 프로그램이 구동되는 동안에 필요한 데이터를 가지고 있음
            aerdto.setBase_path(base_path);    // 메인 폴더 경로 등록
            AERPRE aerpre = new AERPRE(aerdto); // 전처리 클래스 선언
            aerpre.setInpParams();                // 기본입력 데이터 설정
            aerpre.readCriteria(null);    // NULL 이면 기본 경로의 파일을 읽어옴
            aerpre.readAirInfo();                // 기존오염도 데이터 읽어오기

            // 기존 실행 및 결과 파일 제거
            clear_project(base_path);

            // 신규 파일 생성
            System.out.println("Create New File");
            Process process;
            process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\run").start();
            process.waitFor();
            process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\result").start();
            process.waitFor();
            process = new ProcessBuilder("cmd", "/c", "mkdir", base_path + "\\temp").start();
            process.waitFor();
            process.destroy();

            // Config 파일 로딩
            JSONParser parser = new JSONParser();
            String config_path = base_path + "\\config.json";
            if(args.length != 0)
                config_path = args[0];
            if(file_not_exists(config_path)) {
                System.out.println("Config 파일이 없습니다.");
                // 기존 실행 및 결과 파일 제거
                clear_project(base_path);
                return;
            }
            String charset = StaticFunctions.findCharsetWithFile(config_path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(config_path), charset));
//            FileReader reader = new FileReader(config_path);
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            reader.close();
            System.out.println(jsonObject);

            // 사업장 정보 로딩
            String company_info_path = (String) jsonObject.get("company_info_path");
            if(file_not_exists(company_info_path)) {
                System.out.println("company_info 파일이 없습니다.");
                // 기존 실행 및 결과 파일 제거
                clear_project(base_path);
                return;
            }
            aerpre.readCompanyInfo(company_info_path);
            if(aerdto.getSigun().equals(""))
                aerdto.setSigun("'-");
            if(aerdto.getGu().equals(""))
                aerdto.setGu("'-");

            // 지형자료 처리
            String sav_path = (String) jsonObject.get("sav_path");
            if(sav_path.equals("")) { // dxf 입력
                String topy_path = (String) jsonObject.get("topy_path");
                String boundary_path = (String) jsonObject.get("boundary_path");
                if(file_not_exists(topy_path) || file_not_exists(boundary_path)) {
                    System.out.println("topy 또는 boundary 파일이 없습니다.");
                    // 기존 실행 및 결과 파일 제거
                    clear_project(base_path);
                    return;
                }
                aerpre.runTerrainWithDXF(topy_path, boundary_path);
            } else {
                if(file_not_exists(sav_path)) {
                    System.out.println("지형도 변환파일이 없습니다.");
                    // 기존 실행 및 결과 파일 제거
                    clear_project(base_path);
                    return;
                }
                aerpre.runTerrainWithSAV(sav_path);
            }

            if(file_not_exists(base_path + "\\run\\receptor_input.dat")) {
                System.out.println("지형도 데이터파일이 없습니다.");
                return;
            }

            // 지형자료 데이터 범위 조정
            String receptor_range = (String) jsonObject.get("receptor_range");
            if(!receptor_range.trim().equals("")) {
                int receptor_range_int = Integer.parseInt(receptor_range);
                aerpre.runTerrainCutData(base_path + "\\run\\receptor_input.dat", receptor_range_int);
            }

            // 기상대 로딩
            aerpre.runRMO();

            // 배출원정보 자료 처리
            String source_path = (String) jsonObject.get("source_path");
            if(file_not_exists(source_path)) {
                System.out.println("배출원정보 파일이 없습니다.");
                // 기존 실행 및 결과 파일 제거
                clear_project(base_path);
                return;
            }
            aerpre.runProcess(source_path);

            // 환경기준 로딩
            String ec_select_path = (String) jsonObject.get("ec_select_path");
            if(!ec_select_path.equals("")) {
                if(file_not_exists(ec_select_path)) {
                    System.out.println("별도환경기준 파일이 없습니다.");
                    // 기존 실행 및 결과 파일 제거
                    clear_project(base_path);
                    return;
                } else {
                    aerdto.setEc_path(ec_select_path);
                    aerpre.readCriteria(ec_select_path);
                }
            }

            // 쓰레드 개수 설정
            int multi_thread_num = Integer.parseInt((String) jsonObject.get("multi_thread_num"));
            aerdto.setThread_num(multi_thread_num);

            // 실행
            double cpu_limit = Double.parseDouble((String) jsonObject.get("cpu_limit"));
            AERMAIN aermain = new AERMAIN(aerdto, null, null, aerdto.getThread_num(), cpu_limit);
            aermain.run();

            // 결과파일 저장
            String result_path = (String) jsonObject.get("result_path");
            process = new ProcessBuilder("cmd", "/c", "xcopy", base_path + "\\result\\",
                    result_path + "\\", "/E").start();
            System.out.println("process wait");
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
            String str;
            while ((str = stdOut.readLine()) != null) {
                System.out.println(str);
            }
            process.waitFor(5L, TimeUnit.SECONDS);
            System.out.println("process destroy");
            process.destroy();

            // 경과시간 표기
            long endTime = System.currentTimeMillis();
            long timeDiff = (endTime - startTime) / 1000;
            int timeDiffInt = Optional.of(timeDiff).orElse(0L).intValue();
            System.out.println("경과시간 : " + parseSecond(timeDiffInt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
