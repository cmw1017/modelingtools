package aermod;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AERPRE {
	// 모델링 구동 전 필요한 전처리 파일을 만들어주는 클래스
	private ArrayList<String> stack_header; // source 파일의 헤더
	private ArrayList<Map<String, Double>> stack_info; // source 파일의 내용
	private final AERDTO aerdto;

	public AERPRE(AERDTO aerdto) {
		this.aerdto = aerdto;
	}

	// 기본 입력데이터 설정
	public void setInpParams() {
		// inp 파일의 파라미터 설정, 추후에 오염물질별 inp 파일 생성할때 사용되는 데이터
		Map<String,Map<String,String>> inpParams = new HashMap<>();
		for(String matter : AERDTO.polList) {
			inpParams.put(matter, new HashMap<>());
			inpParams.get(matter).put("@@!1", matter);
			if(matter.equals("SO2") || matter.equals("CO") || matter.equals("NO2"))
				inpParams.get(matter).put("@@!2", "931780.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
			else
				inpParams.get(matter).put("@@!2", "1000000.000 (GRAMS/(SEC-M**2)) micrograms/cubic-meter ");
		}
		this.aerdto.setInpParams(inpParams); // 초기 inp 파라미터 등록
	}

	// 사업장 정보 입력
	public void readCompanyInfo(String companyInfoPath) {
		try {
			int ch; // 한 단어씩 읽어옴
			StringBuilder str = new StringBuilder();

			ArrayList<String> company_info_list = new ArrayList<>();

			String charset = StaticFunctions.findCharsetWithFile(companyInfoPath);
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(companyInfoPath), charset);
			while (true) { // 회사 정보 파일 읽기
				ch = inStream.read(); // 한 문자씩 읽기
				if (ch == 124) { // 구분문자 "|"가 나오면 문자열 저장
					String value = str.toString();
					company_info_list.add(value);
					str = new StringBuilder();
				} else if (ch != -1) { // 구분문자가 아니면 문자열 형성
					str.append((char) ch);
				}
				if (ch == -1) {
					String value = str.toString();
					company_info_list.add(value);
					break;
				}
			}
			this.aerdto.setLatitude(Double.parseDouble(company_info_list.get(4)) / 10000); // 위도 값 지정
			this.aerdto.setLongitude(Double.parseDouble(company_info_list.get(13)) / 10000); // 경도 값 지정
			this.aerdto.setSido(company_info_list.get(2));
			this.aerdto.setSigun(company_info_list.get(3));
			this.aerdto.setGu(company_info_list.get(14));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 환경기준 읽는 함수
	public void readCriteria(String EC_path) {
		try {
			System.out.println("Read criteria Data in criteria.csv");
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			// 환경기준
			Map<String, Map<String, Double>> criteria = new HashMap<>(); // 기준 값을 넣는 맵 HashMap<오염물질명, Map<시간, 값>>
			// 모델링 결과 저장
			Map<String, Map<String, Double>> result = new HashMap<>(); // 나중에 결과 값을 넣는 맵을 미리 만들어 놓음, 기준 값을 넣을때 한번 훑기 때문
			int ch; // 한 단어씩 읽어옴
			int series1 = 0, series2 = 0; // series : 열의 개수(그 이상은 읽지 않음)
			InputStreamReader inStream;
			String charset;
			// 파라미터가 없는 경우 디폴트 경로 파일 읽음
			if (EC_path == null) {
				charset = StaticFunctions.findCharsetWithFile(base_path + "\\resource\\criteria.csv");
				inStream = new InputStreamReader(new FileInputStream(base_path + "\\resource\\criteria.csv"), charset);
			} else {
				charset = StaticFunctions.findCharsetWithFile(EC_path);
				inStream = new InputStreamReader(new FileInputStream(EC_path), charset);
			}


			StringBuilder str = new StringBuilder(); // 하나의 단어를 형성하기 위한 틀
			String[] values = new String[4]; // 한 줄의 값을 저장하는 배열
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44 || ch == 10 || ch == 13) { // 쉼표, 개행문자, 복귀를 만났을 경우 데이터를 배열에 집어넣음
						series1++;
						if (series2 != 0 && ch != 10) { //마지막에 개행문자를 만나면 series1이 5가 되므로 배열범위를 벗어나게됨
							String value = str.toString();
							values[series1 - 1] = value;
						}
					}
					if (series1 == 5) { // series1이 4인 13에서는 데이터를 받고, series1이 5인 10에서는 받은 데이터를 배열에서 입력
						// 한 줄에 받는 값을 다 받거나 끝에 오는 문자인 경우 맵에 넣음(기준+결과)
						if (series2 != 0) { // 첫번째는 헤더이기에 넣지 않음
							if (criteria.containsKey(values[0])) {
								criteria.get(values[0]).put(values[1], Double.parseDouble(values[2]));
							} else {
								criteria.put(values[0], new HashMap<>());
								criteria.get(values[0]).put(values[1], Double.parseDouble(values[2]));
								result.put(values[0], new HashMap<>());
							}
							result.get(values[0]).put(values[1], Double.parseDouble(values[2]));

						}
						series1 = 0;
						series2++;
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
			this.aerdto.setCriteria(criteria);
			this.aerdto.setResult(result);
//			for(String key : criteria.keySet()) {
//				System.out.println(key);
//				for(String subKey : criteria.get(key).keySet()) {
//					System.out.println(subKey + " : " + criteria.get(key).get(subKey));
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 기존 오염도 읽는 함수
	public void readAirInfo() {
		try {
			System.out.println("Read criteria Data in airInfo.csv");
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			// 기존오염도
			Map<String, Map<String, Map<String, Map<String, Double>>>> air_list = new LinkedHashMap<>();
			int ch;
			int series1 = 0, series2 = 0; // series : 열의 개수(그 이상은 읽지 않음)
			InputStreamReader inStream;
			String charset = StaticFunctions.findCharsetWithFile(base_path + "\\resource\\AirInfoKorea.csv");
			inStream = new InputStreamReader(new FileInputStream(base_path + "\\resource\\AirInfoKorea.csv"), charset);
			StringBuilder str = new StringBuilder();
			String[] values = new String[5];
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44 || ch == 10 || ch == 13) { // 쉼표, 개행문자, 복귀를 만났을 경우 데이터를 배열에 집어넣음
						series1++;
						if (series2 != 0 && ch != 10) { //마지막에 개행문자를 만나면 series1이 5가 되므로 배열범위를 벗어나게됨
							String value = str.toString();
							values[series1 - 1] = value;
						}
					}
					if (series1 == 6) { // series1이 5인 13에서는 데이터를 받고, series1이 6인 10에서는 받은 데이터를 배열에서 입력
						// 한 줄에 받는 값을 다 받거나 끝에 오는 문자인 경우 맵에 넣음
//						for (String temp : values)
//							System.out.print("value : " + temp);
//						System.out.println();
						if (series2 != 0) { // 첫번째는 헤더이기에 넣지 않음
							if (air_list.containsKey(values[0])) { // 시도 수준에서 중복 값이 있는지 확인
								if (air_list.get(values[0]).containsKey(values[1])) { // 시군 수준에서 중복 값이 있는지 확인
									if (air_list.get(values[0]).get(values[1]).containsKey(values[2])) { // 구 수준에서 중복 값이 있는지 확인
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) { // 오염물질 수준에서 중복 값이 있는지 확인
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									} else {
										air_list.get(values[0]).get(values[1]).put(values[2],
												new HashMap<>());
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) {
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									}
								} else {
									air_list.get(values[0]).put(values[1], new HashMap<>());
									if (air_list.get(values[0]).get(values[1]).containsKey(values[2])) {
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) {
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									} else {
										air_list.get(values[0]).get(values[1]).put(values[2],
												new HashMap<>());
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) {
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									}
								}
							} else {
								air_list.put(values[0], new HashMap<>());
								if (air_list.get(values[0]).containsKey(values[1])) {
									if (air_list.get(values[0]).get(values[1]).containsKey(values[2])) {
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) {
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									} else {
										air_list.get(values[0]).get(values[1]).put(values[2],
												new HashMap<>());
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) {
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									}
								} else {
									air_list.get(values[0]).put(values[1], new HashMap<>());
									if (air_list.get(values[0]).get(values[1]).containsKey(values[2])) {
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) {
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									} else {
										air_list.get(values[0]).get(values[1]).put(values[2],
												new HashMap<>());
										if (air_list.get(values[0]).get(values[1]).get(values[2])
												.containsKey(values[3])) {
											air_list.get(values[0]).get(values[1]).get(values[2]).replace(values[3],
													Double.parseDouble(values[4]));
										} else {
											air_list.get(values[0]).get(values[1]).get(values[2]).put(values[3],
													Double.parseDouble(values[4]));
										}
									}
								}
							}
						}
						series1 = 0;
						series2++;
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
//			OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(base_path + "\\run\\tttt.csv"), "euc-kr");
//			for (String sido : air_list.keySet()) {
//				for (String sigun : air_list.get(sido).keySet()) {
//					for (String gu : air_list.get(sido).get(sigun).keySet()) {
//						for (String pol : air_list.get(sido).get(sigun).get(gu).keySet()) {
//							String tempstr = sido + "," + sigun + "," + gu + "," + pol + ","
//									+ air_list.get(sido).get(sigun).get(gu).get(pol) + "\n";
//							System.out.println(sido + "," + sigun + "," + gu + "," + pol + ","
//									+ air_list.get(sido).get(sigun).get(gu).get(pol));
//							outStream.write(tempstr, 0, tempstr.length());
//						}
//					}
//				}
//			}
//			outStream.close();
			aerdto.setAir_list(air_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 기상대 정보를 읽어서 가장 가까운 기상대 정보를 찾아줌
	public void readRMO() {
		try {
			System.out.println("Read RMO Data in rmo_info.csv");
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			double distance;
			double min_distance = -1;
			int ch;
			int series1 = 0, series2 = 0; // series : 열의 개수(그 이상은 읽지 않음)

			String charset = StaticFunctions.findCharsetWithFile(base_path + "\\resource\\AirInfoKorea.csv");
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(base_path + "\\resource\\rmo_info.csv"), charset);
			StringBuilder str = new StringBuilder();
			String[] values = new String[5];

			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44 || ch == 10 || ch == 13) { // 쉼표, 개행문자, 복귀를 만났을 경우 데이터를 배열에 집어넣음
						series1++;
						if (series2 != 0 && ch != 10) { //마지막에 개행문자를 만나면 series1이 5가 되므로 배열범위를 벗어나게됨
							String value = str.toString();
							values[series1 - 1] = value;
						}
					}
					if (series1 == 6) {
						// series1이 5인 13에서는 데이터를 받고, series1이 6인 10에서는 받은 데이터를 배열에서 입력
						// 한 줄에 받는 값을 다 받거나 끝에 오는 문자인 경우 맵에 넣음
//						for (String temp : values)
//							System.out.print("value : " + temp);
//						System.out.println();
						if (series2 != 0) { // 첫번째는 헤더이기에 넣지 않음
							// 기상대 정보를 저장
							RMO rmo = new RMO();
							rmo.setLatitude(Double.parseDouble(values[0]));
							rmo.setLongitude(Double.parseDouble(values[1]));
							rmo.setElev(Double.parseDouble(values[2]));
							if (values[3].length() == 2) // 자릿수 맞추기 위함
								rmo.setId("0" + values[3]);
							else
								rmo.setId(values[3]);
							rmo.setName(values[4]);

							// 처음에 입력한 위경도 좌표와 거리가 얼마나 되는지 저장
							distance = StaticFunctions.distance(Double.parseDouble(values[0]), Double.parseDouble(values[1]),
									aerdto.getLatitude(), aerdto.getLongitude());
							rmo.setDistance(distance);
//							System.out.print(rmo.getLatitude() + " ");
//							System.out.print(rmo.getLongitude() + " ");
//							System.out.print(rmo.getElev() + " ");
//							System.out.print(rmo.getId() + " ");
//							System.out.print(rmo.getName() + " ");
//							System.out.println(rmo.getDistance() + " ");
							// 최소 거리와 비교하여 가장 가까운 기상대를 찾음
							if (min_distance == -1)
								min_distance = distance;
							else if (min_distance > distance) {
								min_distance = distance;
								aerdto.setRmo(rmo);
							}

						}
						series1 = 0;
						series2++;

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ReadRMO 에서 가져온 가장 가까운 기상대 정보를 temp 폴더에 옮겨주고 내부의 저층, 고층의 ID를 추출
	public void selectRMO() {
		System.out.println("Select nearest RMO");
		String base_path = this.aerdto.getBase_path();
		System.out.println("Base Path : " + base_path);
		ArrayList<String> RMO_info = new ArrayList<>();
		String RMO_ID = this.aerdto.getRmo().getId();
		String RMO_SFC_ID;
		String RMO_PFL_ID;
		try {
			int ch;
			String charset = StaticFunctions.findCharsetWithFile(base_path + "\\resource\\RMO_decrypt\\" + RMO_ID + "_AERMOD.SFC");
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(base_path + "\\resource\\RMO_decrypt\\" + RMO_ID + "_AERMOD.SFC"), charset);
			StringBuilder str = new StringBuilder();
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (str.length() != 0) {
//						System.out.print(str+" ");
						RMO_info.add(str.toString());
						str = new StringBuilder();
					}
					if (ch == 13) {
						break;
					}
				}
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RMO_PFL_ID = String.valueOf(Integer.parseInt(RMO_info.get(3))); // 고층 기상대 정보가 있는 위치의 단어를 추출
		RMO_SFC_ID = RMO_info.get(5); // 저층 기상대 정보가 있는 위치의 단어를 추출
		aerdto.getRmo().setPfl_id(RMO_PFL_ID); // 고층 기상대 정보 입력
		aerdto.getRmo().setSfc_id(RMO_SFC_ID); // 저층 기상대 정보 입력

		System.out.println("Nearest id is : " + RMO_ID + " " + RMO_SFC_ID+ " " + RMO_PFL_ID + " ");


		try {
			// run 폴더에 복사하여 모델링에서 사용
			Process process = new ProcessBuilder("cmd", "/c", "copy",
					base_path + "\\resource\\RMO_decrypt\\" + RMO_ID + "_AERMOD.PFL",
					base_path + "\\run\\AERMOD.PFL").start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy",
					base_path + "\\resource\\RMO_decrypt\\" + RMO_ID + "_AERMOD.SFC",
					base_path + "\\run\\AERMOD.SFC").start();
			process.waitFor();

			// result 폴더에도 복사하여 결과창에서 다운로드 가능하게 함
			process = new ProcessBuilder("cmd", "/c", "copy",
					base_path + "\\resource\\RMO_decrypt\\" + RMO_ID + "_AERMOD.PFL",
					base_path + "\\result\\PFL.dat").start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy",
					base_path + "\\resource\\RMO_decrypt\\" + RMO_ID + "_AERMOD.SFC",
					base_path + "\\result\\SFC.dat").start();
			process.waitFor();
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	// Input 파일을 만들어줌
	public void createInp(String matter) {
		try {
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			RMO tempRMO = this.aerdto.getRmo();
			Map<String, Map<String, String>> inpParams = this.aerdto.getInpParams();
			int ch;
			// 베이스 파일을 읽어옴
			Reader inStream = new FileReader(base_path + "\\resource\\aermod_.inp");
			BufferedWriter outStream = new BufferedWriter(
					new FileWriter(base_path + "\\run\\aermod_" + matter + ".inp"));
			StringBuilder str = new StringBuilder();

			// 지정된 자리에 맞는 파라미터를 채워 넣음
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (str.length() != 0) {
						if (str.toString().contains("@@!1")) { // 오염물질 입력
							String str2 = str.toString();
							str2 = str2.replace("@@!1", inpParams.get(matter).get("@@!1"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!2")) { // 오염물질 입력
							String str2 = str.toString();
							str2 = str2.replace("@@!2", inpParams.get(matter).get("@@!2"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!3")) { // 지표 기상대 아이디 입력
							String str2 = str.toString();
							str2 = str2.replace("@@!3", tempRMO.getSfc_id());
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!4")) { // 고층 기상대 아이디 입력
							String str2 = str.toString();
							str2 = str2.replace("@@!4", tempRMO.getPfl_id());
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

	// 배출원 입력 파일을 읽어옴
	public void readSource() {
		try {
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			stack_header = new ArrayList<>();
			stack_info = new ArrayList<>();
			int ch;
			int series1 = 0, series2 = 0; // series1 : 열의 개수(그 이상은 읽지 않음) series2 : 행번호
			int stack_read_check = 1, data_read_check = 1; // stack_read_check : 굴뚝 전체의 읽기 여부, 오염물질 데이터 읽기 여부 체크
			String charset = StaticFunctions.findCharsetWithFile(base_path + "\\run\\source.csv");
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(base_path + "\\run\\source.csv"), charset);
			StringBuilder str = new StringBuilder();
			ArrayList<Integer> valid_stack_list = new ArrayList<>(); // 배열에 저장된 순서와 가져오는 데이터의 열번호를 맞추는 배열

			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44 || ch == 10 || ch == 13) { // 쉼표, 개행문자, 복귀를 만났을 경우 데이터를 배열에 집어넣음
						series1++;
//						System.out.print(series1 + ": ");
						boolean row_check = series1 == 1 || (series1 >= 4 && series1 <= 12) || (series1 >= 13 && (series1 % 2 == 0)); // 필요한 데이터가 있는 열을 구분
						if (ch != 10 && series2 == 0 && series1 <= 62 && (row_check)) {
							switch (str.toString()) {
								case "SO₂":
									stack_header.add("SO2");
									break;
								case "NO₂":
									stack_header.add("NO2");
									break;
								case "CS₂":
									stack_header.add("CS2");
									break;
								case "H₂S":
									stack_header.add("H2S");
									break;
								default:
									stack_header.add(str.toString()); // 처음 한줄을 스택 정보 종류를 읽어서 저장
									break;
							}
							valid_stack_list.add(series1);
						}
						else if (ch != 10 && series2 != 0 && series1 <= 62) {
							System.out.print(str + "/ ");
							String number = str.toString();
							if (row_check) { // 필요한 데이터가 있는 열만 가져와서 Stack 정보에 넣음
								if (number.length() == 0 || !(stack_read_check == 1 && data_read_check == 1)) // 선택이 0인 경우 체크
									number = "0";
								stack_info.get(series2 - 1).put(stack_header.get(valid_stack_list.indexOf(series1)), Double.parseDouble(number));
							} else if (series1 != 3) { // 선택이 0인 경우 체크
								if (number.equals("0") && series1==2) stack_read_check=0;
								else if (number.equals("0")) data_read_check=0; // series1 != 2 조건이 있었음
								else if (number.equals("1") && series1==2) stack_read_check=1;
								else if (number.equals("1")) data_read_check=1; // series1 != 2 조건이 있었음
							}
						}
						// 다음부터는 스택의 정보를 저장, 그에 맞는 이름은 위에서 읽은 스택 정보 종류와 연결시킴
					}
					if (ch == 10) {
						series1 = 0;
						series2++;
						stack_read_check = 1;
						data_read_check = 1;
						stack_info.add(new HashMap<>());
						System.out.println();
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
			System.out.println("Print Stack Header");
			for (String temp_str : stack_header) {
				System.out.print(temp_str + " ");
			}
			System.out.println();
			System.out.println("Print Stack Info");
			for (Map<String, Double> temp_map : stack_info) {
				for (String temp_str : stack_header) {
					System.out.print(temp_map.get(temp_str) + " ");
				}
				System.out.println();
			}
			System.out.println();

			double check = 0; // 오염물질 배출량이 있는지 확인(하나도 없는것은 모델링 돌릴 필요가 없으므로)
			List<String> matters = new ArrayList<>();
			for (String pol : AERDTO.polList) { // 모델링 대상 오염물질을 걸러내는 작업
				for (Map<String, Double> temp_map : stack_info) {
					check += temp_map.get(pol);
				}
				if (check != 0)
					matters.add(pol);
				check = 0;
			}

			System.out.print("모델링 오염물질 : ");
			for (String pol : matters) {
				System.out.print(pol + " ");
			}
			System.out.println();
			this.aerdto.setMatters(matters);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ReadSource 에서 읽은 배출원 입력 파일을 가지고 배출원 정보 파일을 만듬
	public void createSource(String matter) {
		try {
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			if (!stack_header.contains(matter)) {
				System.out.println("파일 에러(해당 오염물질이 입력되지 않음)");
				return;
			}
			BufferedWriter outStream = new BufferedWriter(
					new FileWriter(base_path + "\\run\\POINT_" + matter + ".dat"));
			for (Map<String, Double> temp_map : stack_info) {
				if (temp_map.get(matter) != 0) {
					String source1 = "SO LOCATION  "
							+ Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(0))))) // 굴뚝 번호

							+ "    POINT" + String.format(" %" + (StaticFunctions.getDecimal(temp_map.get(stack_header.get(1))) + 6) + "." + StaticFunctions.getDecimal(temp_map.get(stack_header.get(1))) + "f",
									temp_map.get(stack_header.get(1))) // X 좌표(TM)

							+ " "  + String.format(" %" + (StaticFunctions.getDecimal(temp_map.get(stack_header.get(2))) + 6) + "." + StaticFunctions.getDecimal(temp_map.get(stack_header.get(2))) + "f",
									temp_map.get(stack_header.get(2))) // Y 좌표(TM)

							+ " "  + String.format(" %" + (StaticFunctions.getDecimal(temp_map.get(stack_header.get(3))) + 5) + "." + StaticFunctions.getDecimal(temp_map.get(stack_header.get(3))) + "f",
									temp_map.get(stack_header.get(3))) // 표고
							+ "\n";
					String source2 = "SO SRCPARAM  "
							+ Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(0))))) + "    " // 굴뚝 번호
							+ new BigDecimal(String.format("%.6g",
									temp_map.get(stack_header.get(9)) * temp_map.get(matter) / 1000 / 60)) // 배출량(g/s)

							+ " "  + String.format(" %" + (Math.max(StaticFunctions.getDecimal(temp_map.get(stack_header.get(4))), StaticFunctions.getDecimal(temp_map.get(stack_header.get(5)))) + 6) // 더 긴 소수점으로 처리
													+ "." + Math.max(StaticFunctions.getDecimal(temp_map.get(stack_header.get(4))), StaticFunctions.getDecimal(temp_map.get(stack_header.get(5)))) + "f",
									temp_map.get(stack_header.get(5)) + temp_map.get(stack_header.get(4))) //굴뚝 높이

							+ " "  + String.format(" %" + (Math.max(StaticFunctions.getDecimal(temp_map.get(stack_header.get(6))), 2) + 6) // 더 긴 소수점으로 처리
													+ "." + Math.max(StaticFunctions.getDecimal(temp_map.get(stack_header.get(6))), 2) + "f",
									temp_map.get(stack_header.get(6)) + 273.15) // 온도(K)

							+ " "  + String.format(" %" + (StaticFunctions.getDecimal(temp_map.get(stack_header.get(7))) + 5) + "." + StaticFunctions.getDecimal(temp_map.get(stack_header.get(7))) + "f",
									temp_map.get(stack_header.get(7))) // 유속

							+ " " + String.format(" %" + (StaticFunctions.getDecimal(temp_map.get(stack_header.get(8))) + 5) + "." + StaticFunctions.getDecimal(temp_map.get(stack_header.get(8))) + "f",
									temp_map.get(stack_header.get(8))) + "\n"; // 내경
					outStream.write(source1, 0, source1.length());
					outStream.write(source2, 0, source2.length());
				}
			}
			outStream.close();
			System.out.println("complete create Point dat file(" + matter + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// aerpre 메인 프로세스(배출원 정보를 읽고 Inp, source 파일을 만들어줌)
	public void runProcess(String source_path) {
		try {
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			Process process;
			process = new ProcessBuilder("cmd", "/c", "copy", source_path, base_path + "\\run\\source.csv")
					.start();
			process.waitFor();
			process.destroy();

			readSource();
			List<String> matters = this.aerdto.getMatters();
			for (String matter : matters) {
				createInp(matter);
				createSource(matter);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 기상대 관련 프로세스
	public void runRMO() {
		readRMO();
		selectRMO();
	}
	
	// 지형관련 프로세스(dxf 변환)
	public void runTerrainWithDXF(String topy_path, String boundary_path){
		try {
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			Process process;
			ProcessBuilder processBuilder;
			process = new ProcessBuilder("cmd", "/c", "copy", topy_path, base_path + "\\temp\\topy.dxf")
					.start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy", boundary_path, base_path + "\\temp\\boundary.dxf")
					.start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\terrain.bat", base_path + "\\temp\\terrain.bat")
					.start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\0aermod.exe", base_path + "\\temp\\0aermod.exe")
					.start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\1aermod.exe", base_path + "\\temp\\1aermod.exe")
					.start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\2aermod.exe", base_path + "\\temp\\2aermod.exe")
					.start();
			process.waitFor();

			File terrain = new File(base_path + "\\temp\\terrain.bat");
			File terrainDir = new File(base_path + "\\temp");
			String terrainPath = terrain.getAbsolutePath();
			if (terrain.isFile()) {
				List<String> cmd = new ArrayList<>();
				cmd.add(terrainPath);
				processBuilder = new ProcessBuilder(cmd);
				processBuilder.directory(terrainDir);
				process = processBuilder.start();
				BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String str;
				while ((str = stdOut.readLine()) != null) {
					System.out.println(str);
				}
				process.waitFor();
			} else {
				System.out.println("에러 : 지형변환 실행파일이 없습니다.(terrain.dat)");
				return;
			}
			process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\temp\\receptor_input.dat", base_path + "\\run\\receptor_input.dat")
					.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 지형관련 프로세스(sav, dat 파일 로딩)
	public void runTerrainWithSAV(String sav_path){
		try {
			String base_path = this.aerdto.getBase_path();
			System.out.println("Base Path : " + base_path);
			Process process;
			ProcessBuilder processBuilder;
			String extension = sav_path.substring(sav_path.lastIndexOf('.') + 1);
			if (extension.equals("sav")) {
				// sav 파일 및 디코딩 프로그램 복사
				process = new ProcessBuilder("cmd", "/c", "copy", sav_path, base_path + "\\run\\receptor_input.sav")
						.start();
				process.waitFor();
				process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\TEM.exe", base_path + "\\run\\TEM.exe")
						.start();
				process.waitFor();
				process = new ProcessBuilder("cmd", "/c", "copy", base_path + "\\exe\\sav_decoding.bat", base_path + "\\run\\sav_decoding.bat")
						.start();
				process.waitFor();

				// 디코딩 프로그램 실행
				System.out.println("============ Decoding Receptor_input Data ============");
				File src_decoder = new File(base_path + "\\run\\sav_decoding.bat");
				File src_decoder_dir = new File(base_path + "\\run\\");
				String src_decoder_src = src_decoder.getAbsolutePath();
				if (src_decoder.isFile()) {
					List<String> cmd = new ArrayList<>();
					cmd.add(src_decoder_src);
					processBuilder = new ProcessBuilder(cmd);
					processBuilder.directory(src_decoder_dir);
					process = processBuilder.start();
					process.waitFor();
				}

				// 디코딩 후 파일이 실제 입력은 아니므로 수정
				System.out.println("============ Read Decoding Receptor_input Data ============");
				Thread.sleep(1000);
				String charset = StaticFunctions.findCharsetWithFile(base_path + "\\run\\receptor_input.dat");
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(base_path + "\\run\\receptor_input.dat"), charset);

				int ch;
				StringBuilder str = new StringBuilder();
				while (true) {
					ch = inStream.read();
					if (ch == '=' || ch == -1)
						break;
					str.append((char) ch);
				}
				String datString = str.toString();
				System.out.println("============ Decoded Receptor_input Data ============");
				System.out.println(datString);

				// 디코딩 및 수정완료 파일 출력
				OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(base_path + "\\run\\receptor_input.dat"), StandardCharsets.UTF_8);
				outStream.write(datString, 0, datString.length());
				outStream.close();
			} else {
				process = new ProcessBuilder("cmd", "/c", "copy", sav_path, base_path + "\\run\\receptor_input.dat")
						.start();
				process.waitFor();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
