package aermod;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.*;

public class AERPRE {
	// 모델링 구동 전 필요한 전처리 파일을 만들어주는 클래스
	private String[] pollist = { "SO2", "CO", "NO2", "Pb", "Benzene", "PM-10", "Zn", "NH3", "CS2", "Cr", "Hg", "Cu",
			"Vinylchloride", "H2S", "Dichloromethane", "TCE", "As", "Ni", "Cd", "Br", "F", "HCN", "HCl", "Phenol",
			"Formaldehyde" };
	private List<String> matters; // 모델에 사용할 오염물질들만 모음
	private ArrayList<String> stack_header; // source 파일의 헤더
	private ArrayList<Map<String, Double>> stack_info; // source 파일의 내용
	private String[] rmo_id = new String[3]; // 가장 가까운 기상대 id(기본, 저층, 고층)
	private Process process;
	private Map<String, Map<String, String>> inpparam;	// inp 생성시 필요한 값들
	private Map<String, Map<String, Double>> criteria;	// 환경기준
	private Map<String, Map<String, Double>> result;	// 모델링 결과 저장
	private Map<String, Map<String, Map<String, Map<String, Double>>>> air_list; // 기존오염도
	
	private String base_path; //메인 폴더 경로

	private AermodDTO aermodDTO;

	public AERPRE(AermodDTO aermodDTO) {
		this.aermodDTO = aermodDTO;
		this.base_path = aermodDTO.getBase_path();
		this.inpparam = aermodDTO.getInpparam();
	}

	// 환경기준 읽는 함수
	public void ReadCriteria(String EC_path) {
		try {
			System.out.println("Read criteria Data in criteria.csv");
			criteria = new HashMap<String, Map<String, Double>>(); // 기준 값을 넣는 맵 HashMap<오염물질명, Map<시간, 값>>
			result = new HashMap<String, Map<String, Double>>(); // 나중에 결과 값을 넣는 맵을 미리 만들어 놓음, 기준 값을 넣을때 한번 훑기 때문
			int ch; // 한 단어씩 읽어옴
			int series1 = 0, series2 = 0; // series : 열의 개수(그 이상은 읽지 않음)
			InputStreamReader inStream;
			// 파라미터가 없는 경우 디폴트 경로 파일 읽음
			if (EC_path == null)
				inStream = new InputStreamReader(new FileInputStream(base_path + "\\resource\\criteria.csv"), "euc-kr");
			else
				inStream = new InputStreamReader(new FileInputStream(EC_path), "euc-kr");


			StringBuilder str = new StringBuilder(); // 하나의 단어를 형성하기 위한 틀
			String[] values = new String[4]; // 한 줄의 값을 저장하는 배열
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44) { // 쉼표를 만났을 경우 데이터를 배열에 집어넣음
						series1++;
						if (series2 != 0) {
							String value = str.toString();
							values[series1 - 1] = value;
						}
					}
					if (series1 == 4 || (series1 == 3 && ch == 13)) {
						// 한 줄에 받는 값을 다 받거나 끝에 오는 문자인 경우 맵에 넣음(기준+결과)
						if (series2 != 0) { // 첫번째는 헤더이기에 넣지 않음
							String value = str.toString();
							values[series1] = value;
							if (criteria.containsKey(values[0])) {
								criteria.get(values[0]).put(values[1], Double.parseDouble(values[2]));
								result.get(values[0]).put(values[1], Double.parseDouble(values[2]));
							} else {
								criteria.put(values[0], new HashMap<String, Double>());
								criteria.get(values[0]).put(values[1], Double.parseDouble(values[2]));
								result.put(values[0], new HashMap<String, Double>());
								result.get(values[0]).put(values[1], Double.parseDouble(values[2]));
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
			aermodDTO.setCriteria(criteria);
			aermodDTO.setResult(result);
//			for(String key : criteria.keySet()) {
//				System.out.println(key);
//				for(String kkey : criteria.get(key).keySet()) {
//					System.out.println(kkey + " : " + criteria.get(key).get(kkey));
//				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 기존 오염도 읽는 함수
	public void ReadAirInfo() {
		try {
			System.out.println("Read criteria Data in airinfo.csv");
			air_list = new LinkedHashMap<String, Map<String, Map<String, Map<String, Double>>>>();
			int ch;
			int series1 = 0, series2 = 0; // series : 열의 개수(그 이상은 읽지 않음)
			InputStreamReader inStream;
			inStream = new InputStreamReader(new FileInputStream(base_path + "\\resource\\AirInfoKorea.csv"), "euc-kr");
			StringBuilder str = new StringBuilder();
			String[] values = new String[5];
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44) { // 쉼표를 만났을 경우 데이터를 배열에 집어넣음
						series1++;
						if (series2 != 0) {
							String value = str.toString();
							values[series1 - 1] = value;
						}
					}
					if (series1 == 5 || (series1 == 4 && ch == 13)) {
						// 한 줄에 받는 값을 다 받거나 끝에 오는 문자인 경우 맵에 넣음
						if (series2 != 0) { // 첫번째는 헤더이기에 넣지 않음
							String value = str.toString();
							values[series1] = value;
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
												new HashMap<String, Double>());
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
									air_list.get(values[0]).put(values[1], new HashMap<String, Map<String, Double>>());
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
												new HashMap<String, Double>());
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
								air_list.put(values[0], new HashMap<String, Map<String, Map<String, Double>>>());
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
												new HashMap<String, Double>());
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
									air_list.get(values[0]).put(values[1], new HashMap<String, Map<String, Double>>());
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
												new HashMap<String, Double>());
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
			aermodDTO.setAir_list(air_list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 기상대 정보를 읽어서 가장 가까운 기상대 정보를 찾아줌
	public void ReadRMO() {
		try {
			System.out.println("Read RMO Data in rmo_info.csv");
			double distance = 0;
			double min_distance = -1;
			int ch;
			int series1 = 0, series2 = 0; // series : 열의 개수(그 이상은 읽지 않음)
			InputStreamReader inStream = new InputStreamReader(
					new FileInputStream(base_path + "\\resource\\rmo_info.csv"), "euc-kr");
			StringBuilder str = new StringBuilder();
			String[] values = new String[5];

			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44) { // 쉼표를 만났을 경우 데이터를 배열에 집어넣음
						series1++;
						if (series2 != 0) {
							String value = str.toString();
							values[series1 - 1] = value;
						}
					}
					if (series1 == 5 || (series1 == 4 && ch == 13)) {
						// 한 줄에 받는 값을 다 받거나 끝에 오는 문자인 경우 맵에 넣음
						if (series2 != 0) { // 첫번째는 헤더이기에 넣지 않음
							String value = str.toString();
							values[series1] = value;
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
							distance = distance(Double.parseDouble(values[0]), Double.parseDouble(values[1]),
									aermodDTO.getLatitude(), aermodDTO.getLongitude());
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
								rmo_id[0] = values[3]; // 기본 기상대 등록
								min_distance = distance;
								aermodDTO.setRmo(rmo);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Double deg2rad(double deg) {
		return (deg * 3.14159265358979 / 180);
	}

	public Double rad2deg(double rad) {
		return (rad * 180 / 3.14159265358979);
	}

	public Double distance(double lat1, double lon1, double lat2, double lon2) {

		double num = lon1 - lon2;
		double num1 = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(num));
		num1 = Math.acos(num1);
		num1 = rad2deg(num1) * 60 * 1.1515;
		return num1 * 1.609344;
	}

	// ReadRMO에서 가져온 가장 가까운 기상대 정보를 temp 폴더에 옮겨주고 내부의 저층, 고층의 ID를 추출
	public void SelectRMO() {
		System.out.println("Select nearst RMO");
		ArrayList<String> RMO_info = new ArrayList<>();
		try {
			int ch;
			InputStreamReader inStream = new InputStreamReader(
					new FileInputStream(base_path + "\\resource\\RMO_decrypt\\" + rmo_id[0] + "_AERMOD.SFC"), "euc-kr");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		rmo_id[1] = String.valueOf(Integer.parseInt(RMO_info.get(3))); // 기상대 정보가 있는 위치의 단어를 추출
		rmo_id[2] = RMO_info.get(5); // 기상대 정보가 있는 위치의 단어를 추출
		RMO temprmo = aermodDTO.getRmo(); // 데이터 수정을 위해 가져옴
		temprmo.setSf_id(rmo_id[2]); // 고층 기상대 정보 입력
		temprmo.setUa_id(rmo_id[1]); // 저층 기상대 정보 입력
		aermodDTO.setRmo(temprmo);

		System.out.println("Nearest id is : " + rmo_id[0] + " " + rmo_id[1] + " " + rmo_id[2] + " ");

		// run 폴더에 이동하여 모델링에서 사용
		try {
			process = new ProcessBuilder("cmd", "/c", "copy",
					base_path + "\\resource\\RMO_decrypt\\" + rmo_id[0] + "_AERMOD.PFL",
					base_path + "\\run\\AERMOD.PFL").start();
			process.waitFor();
			process = new ProcessBuilder("cmd", "/c", "copy",
					base_path + "\\resource\\RMO_decrypt\\" + rmo_id[0] + "_AERMOD.SFC",
					base_path + "\\run\\AERMOD.SFC").start();
			process.waitFor();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
	}

	// Input 파일을 만들어줌
	public void CreateInp(String matter) {
		try {
			RMO temprmo = aermodDTO.getRmo();
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
							str2 = str2.replace("@@!1", inpparam.get(matter).get("@@!1"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!2")) { // 오염물질 입력
							String str2 = str.toString();
							str2 = str2.replace("@@!2", inpparam.get(matter).get("@@!2"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!3")) { // 지표 기상대 아이디 입력
							String str2 = str.toString();
							str2 = str2.replace("@@!3", temprmo.getSf_id());
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!4")) { // 고층 기상대 아이디 입력
							String str2 = str.toString();
							str2 = str2.replace("@@!4", temprmo.getUa_id());
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
	public void ReadSource() {
		try {
			stack_header = new ArrayList<>();
			stack_info = new ArrayList<>();
			int ch;
			int series1 = 0, series2 = 0; // series1 : 열의 개수(그 이상은 읽지 않음) series2 : 행번호
			int stack_read_check = 1, data_read_check = 1; // stack_read_check : 굴뚝 전체의 읽기 여부, 오염물질 데이터 읽기 여부 체크
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(base_path + "\\run\\source.csv"), "euc-kr");
			StringBuilder str = new StringBuilder();
			ArrayList<Integer> valid_stack_list = new ArrayList<>(); // 배열에 저장된 순서와 가져오는 데이터의 열번호를 맞추는 배열

			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1 && ch != 44) { // 단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (ch == 44) { // 쉼표를 만났을 경우
						series1++;
//						System.out.print(series1 + ": ");
						boolean row_check = series1 == 1 || (series1 >= 4 && series1 <= 12) || (series1 >= 13 && (series1 % 2 == 0)); // 필요한 데이터가 있는 열을 구분
						if (series2 == 0 && series1 <= 62 && (row_check)) {
//							System.out.print(str.toString() + "/ ");
							if (str.toString().equals("SO₂"))
								stack_header.add("SO2");
							else if (str.toString().equals("NO₂"))
								stack_header.add("NO2");
							else if (str.toString().equals("CS₂"))
								stack_header.add("CS2");
							else if (str.toString().equals("H₂S"))
								stack_header.add("H2S");
							else
								stack_header.add(str.toString()); // 처음 한줄을 스택 정보 종류를 읽어서 저장
							valid_stack_list.add(series1);
						}
						else if (series2 != 0 && series1 <= 62) {
							System.out.print(str.toString() + "/ ");
							String number = str.toString();
							if (row_check) { // 필요한 데이터가 있는 열만 가져와서 Stack 정보에 넣음
								if (number.length() == 0 || !(stack_read_check == 1 && data_read_check == 1)) // 선택이 0인 경우 체크
									number = "0";
								stack_info.get(series2 - 1).put(stack_header.get(valid_stack_list.indexOf(series1)), Double.parseDouble(number));
							}
							else if (series1 != 3) { // 선택이 0인 경우 체크
								if (number.equals("0") && series1==2) stack_read_check=0;
								else if (number.equals("0") && series1!=2) data_read_check=0;
								else if (number.equals("1") && series1==2) stack_read_check=1;
								else if (number.equals("1") && series1!=2) data_read_check=1;
							}
						}
						// 다음부터는 스택의 정보를 저장, 그에 맞는 이름은 위에서 읽은 스택 정보 종류와 연결시킴
					}
					if (series1 == 76 || ch == 13) {
						series1 = 0;
						series2++;
						stack_read_check = 1;
						data_read_check = 1;
						stack_info.add(new HashMap<String, Double>());
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
				if (check != 0)
					matters.add(pol);
				check = 0;
			}

			System.out.print("모델링 오염물질 : ");
			for (String pol : matters) {
				System.out.print(pol + " ");
			}
			System.out.println();
			aermodDTO.setMatters(matters);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ReadSource에서 읽은 배출원 입력 파일을 가지고 배출원 정보 파일을 만듬
	public void CreateSource(String matter) {
		try {
			if (stack_header.indexOf(matter) == -1) {
				System.out.println("파일 에러(해당 오염물질이 입력되지 않음)");
				return;
			}
			BufferedWriter outStream = new BufferedWriter(
					new FileWriter(base_path + "\\run\\POINT_" + matter + ".dat"));
			for (Map<String, Double> temp_map : stack_info) {
				if (temp_map.get(matter) != 0) {
					String source1 = "SO LOCATION  "
							+ Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(0)))))
							+ "    POINT" + String.format(" %13.4f", temp_map.get(stack_header.get(1)))
							+ String.format(" %13.4f", temp_map.get(stack_header.get(2)))
							+ String.format(" %5d",
									Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(3))))))
							+ "\n";
					String source2 = "SO SRCPARAM  "
							+ Integer.parseInt(String.valueOf(Math.round(temp_map.get(stack_header.get(0))))) + "    "
							+ new BigDecimal(String.format("%.6g",
									temp_map.get(stack_header.get(9)) * temp_map.get(matter) / 1000 / 60)).toString()
							+ String.format(" %9.3f", temp_map.get(stack_header.get(5)) + temp_map.get(stack_header.get(4)))
							+ String.format(" %11.3f", temp_map.get(stack_header.get(6)) + 273.15)
							+ String.format(" %8.3f", temp_map.get(stack_header.get(7)))
							+ String.format(" %8.3f", temp_map.get(stack_header.get(8))) + "\n";
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

	public void RunProcess() {
		ReadSource();
		for (String matter : matters) {
			CreateInp(matter);
			CreateSource(matter);
		}
	}

	public void RunRMO() {
		ReadRMO();
		SelectRMO();
	}

}
