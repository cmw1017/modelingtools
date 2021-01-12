package main;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class CTGPROC {

	public int mainP(Scanner sc) throws IOException {
		System.out.println("--------------------------------- 주의사항 -----------------------------------------");
		System.out.println("데이터의 컬럼 순서는 X, Y, LUdata순으로 이루어져야 하고 중복된 데이터가 없어야 합니다.           ");
		System.out.println("데이터는 X,Y모두 오름차순이어야 하며 X가 먼저 증가하는 순서로 이루어져야 합니다.              ");
		System.out.println("반드시 txt파일이어야 합니다.                                         ");
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("--------------------------------- INPUT DATA ---------------------------------------");
		System.out.println();
		System.out.print("확장자 명을 포함한 파일 전체 이름 입력하여 주세요 : ");
		sc.nextLine();
		String insrc = sc.nextLine();
		if(!insrc.substring(insrc.indexOf('.')+1).equals("txt")) {
			System.out.println("확장자를 확인하여 주세요(" + insrc.substring(insrc.indexOf('.')+1) + ").");
			return -1;
		}
		insrc = ".\\input\\" + insrc;
		String listsrc = ".\\output\\ProcessList.lst";
		String datasrc = ".\\output\\LANDUSE_CLASSIFY.DAT";

		// 입력 데이터 ArrayList에 저장
		// -----------------------------------------------------------------------------------------------------------------------------
		Reader inStream = new FileReader(insrc);
		int ch, flag = 0, lu = 0, count = 0;
		double xpos = 0, ypos = 0, tempY = 0, tempX = 0;
		// TreeMap에 PosY를 Key로하고 <PosX를 Key로하고 LU데이터를 Value로 하는 >다른 TreeMap을 Value로 연결
		TreeMap<Double, TreeMap<Double, Integer>> dataList = new TreeMap<Double, TreeMap<Double, Integer>>();
		TreeMap<Double, Integer> tempMap = new TreeMap<Double, Integer>();
		StringBuilder str = new StringBuilder();
		while (true) {
			ch = inStream.read();
			if (ch == ',' || ch == 13) { // 한문자식 읽다가 구분자를 만나면 저장함
				flag++; // 데이터의 컬럼에 따라 분류
				switch (flag) {
				case 1: {
					xpos = Double.parseDouble(str.toString());
					str = new StringBuilder();
					if (tempX == xpos) { //중복 값이 있는경우 에러 발생
						System.out.println("Error : Not Sorted or Exist Overlapped data");
						return -1;
					}
					break;
				}
				case 2: {
					ypos = Double.parseDouble(str.toString());
					if (tempY != ypos) {
						tempMap = new TreeMap<Double, Integer>();
						tempX = 0;
					}
					str = new StringBuilder();
					break;
				}
				case 3: {
					if(tempY > ypos || tempX > xpos) { //정렬 되어있지 않은경우 발생
						System.out.println("Error : Not Sorted or Exist Overlapped data");
						return -1;
					}
					tempX = xpos;
					tempY = ypos;
					try {
						lu = Integer.parseInt(str.toString());
					}
					catch (NumberFormatException e) {
						double temLU = Double.parseDouble(str.toString());
						lu = (int)temLU;
					}
					str = new StringBuilder();
					tempMap.put(xpos, lu);
					dataList.put(ypos, tempMap);
					count++;
					flag = 0;
					break;
				}
				}
				continue;
			}
			if (ch == -1)
				break;
			str.append((char) ch);
		}
		System.out.println("파일 읽기 완료");
		System.out.println("입력 데이터 수 : " + count);
		System.out.println();
		System.out.println();
		// 입력 데이터 ArrayList에 저장
		// -----------------------------------------------------------------------------------------------------------------------------

		// 데이터 출력을 위한 남서쪽 포인트 입력 및 탐색범위 등 기본 설정 정보 입력
		// -----------------------------------------------------------------------------------------------------
		double findX, findY;
		int radius, cellNumX, cellNumY, cellSize;
		System.out.print("남서쪽 지점의 X(m) 좌표를 입력하여 주세요 : ");
		findX = sc.nextDouble();
		System.out.print("남서쪽 지점의 Y(m) 좌표를 입력하여 주세요 : ");
		findY = sc.nextDouble();
		System.out.print("GRID X축의 셀의 개수를 입력하여 주세요 : ");
		cellNumX = sc.nextInt();
		System.out.print("GRID Y축의 셀의 개수를 입력하여 주세요 : ");
		cellNumY = sc.nextInt();
		if(cellNumX < 0  || cellNumY < 0) {
			System.out.println("Error : CellNum must be positive number");
			sc.close();
			inStream.close();
			return -1;
		}
		System.out.print("GRID 셀의 해상도를 입력하여 주세요(단위:m) : ");
		cellSize = sc.nextInt();
		if(cellSize < 0 ) {
			System.out.println("Error : Resolution must be positive number");
			sc.close();
			inStream.close();
			return -1;
		}
		System.out.print("GRID 데이터 탐색 반경을 입력하여 주세요(단위:m)(해상도의 절반을 권장합니다) : ");
		radius = sc.nextInt();
		if(radius < 0 ) {
			System.out.println("Error : A Square Side Length must be positive number");
			inStream.close();
			return -1;
		}
		inStream.close();
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println();
		// 데이터 출력을 위한 남서쪽 포인트 입력 및 탐색범위 등 기본 설정 정보 입력
		// -----------------------------------------------------------------------------------------------------

		// list파일에 데이터 입력
		// ---------------------------------------------------------------------------------------------------------------------------------
		BufferedWriter outStream = new BufferedWriter(new FileWriter(listsrc));
		String tempstr = "--- input info -----------------------------------------------------------------";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "SW Position X(m) : " + findX;
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "SW Position Y(m) : " + findY;
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "Searching A Square Side Length(m) : " + radius;
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "Cell Number X: " + cellNumX;
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "Cell Number Y: " + cellNumY;
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "Resolution(m) : " + cellSize;
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "--------------------------------------------------------------------------------";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "--- Frequency result -----------------------------------------------------------";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "Position Y	Position X	Value(Frequency)";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		// list파일에 데이터 입력
		// ---------------------------------------------------------------------------------------------------------------------------------

		// 메인 프로세스
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// outputList : Y->X->LU로 연결
		TreeMap<Double, TreeMap<Double, Integer>> outputList = new TreeMap<Double, TreeMap<Double, Integer>>();
		TreeMap<Double, Integer> tempMap2 = new TreeMap<Double, Integer>();
		HashMap<Integer, Integer> frequencyLU = new HashMap<Integer, Integer>();
		List<Double> listY = new ArrayList<Double>();
		double outdata[][][] = new double[cellNumX][cellNumY][38]; // 분률로 쪼개기 위해 선언
		int cellcategory[] = { 11, 12, 13, 14, 15, 16, 17, 21, 22, 23, 24, 31, 32, 33, 41, 42, 43, 51, 52, 53, 54, 55,
				61, 62, 71, 72, 73, 74, 75, 76, 77, 81, 82, 83, 84, 85, 91, 92 }; // CTGPROC에서 분류되는 코드
		int X = 0, Y = 0; // 배열에 순서에 맞게 입력하기 위해 선언
		Set<Double> yks = dataList.keySet();
		for (double i = findY; i < findY + cellSize * cellNumY; i += cellSize) { // Y좌표에 셀사이즈와 셀 개수를 곱한 값을 최대 탐색 영역으로 지정
			Y++;
			X = 0; // 배열에 순서에 맞게 입력하기 위해 선언
			// 전체 X에서 해당범위만 추출
			listY = new ArrayList<Double>();
			for (Double posY : yks) {
				if (posY > i - radius && posY <= i + radius) { // 입력된 반경으로 탐색
					listY.add(posY);
				}
			}
			tempMap2 = new TreeMap<Double, Integer>();
			for (double j = findX; j < findX + cellSize * cellNumX; j += cellSize) {
				X++; // 배열에 순서에 맞게 입력하기 위해 선언
				// 빈도 체크 Map
				frequencyLU = new HashMap<Integer, Integer>();
				// X 범위에 해당하는 Y범위의 LU 저장
				for (Double posY : listY) {
					Set<Double> xks = dataList.get(posY).keySet();
					for (Double posX : xks) {
						if (posX > j - radius && posX <= j + radius) {
							if (frequencyLU.get(dataList.get(posY).get(posX)) == null) {
								frequencyLU.put(dataList.get(posY).get(posX), 1);
							} else {
								int tempCount = frequencyLU.get(dataList.get(posY).get(posX)) + 1;
								frequencyLU.replace(dataList.get(posY).get(posX), tempCount);
							}
						}
					}
				}
				Set<Integer> luks = frequencyLU.keySet();
				int firstCount = 0, firstLU = -999, secondCount = 0, secondLU = -999, totHit = 0;
				tempstr = i + ",	" + j + ",	";
				outStream.write(tempstr, 0, tempstr.length());
				// 가장 빈도수가 높은 값을 탐색
				for (Integer mapLU : luks) {
					tempstr = mapLU + "(" + frequencyLU.get(mapLU) + ")" + ",	";
					totHit += frequencyLU.get(mapLU);
					outStream.write(tempstr, 0, tempstr.length());
					if (firstCount < frequencyLU.get(mapLU)) {// 같을경우도 고려해야할듯
						firstCount = frequencyLU.get(mapLU);
						firstLU = mapLU;
					}
				}
				// 두번째로 빈도수가 높은 값을 탐색
				for (Integer mapLU : luks) {
					if (secondCount < frequencyLU.get(mapLU) && firstCount >= frequencyLU.get(mapLU)) {// 같을경우도 고려해야할듯
						if (mapLU != firstLU) {
							secondCount = frequencyLU.get(mapLU);
							secondLU = mapLU;
						}
					}
				}
				// 모든 값을 0으로 초기화
				for (int k = 0; k < 38; k++) {
					outdata[X - 1][Y - 1][k] = 0.000;
				}
				int dataType = 0; // 두번째로 빈도가 높은 토지이용도 분률을 넣을때 첫번째 토지이용도 분률의 타입을 저장
				// 첫번째 토지이용도 분율 분류
				for (int k = 0; k < 38; k++) {
					if (firstLU == cellcategory[k]) {
						if (frequencyLU.get(firstLU) >= 0.7 * totHit) {
							outdata[X - 1][Y - 1][k] = 1.000;
						} else if (frequencyLU.get(firstLU) < 0.7 * totHit
								&& frequencyLU.get(firstLU) >= 0.6 * totHit) {
							outdata[X - 1][Y - 1][k] = 0.667;
							dataType = 2;
						} else {
							outdata[X - 1][Y - 1][k] = 0.500;
							dataType = 3;
						}
					}
				}
				// 두번째 토지이용도 분율 분류
				for (int k = 0; k < 38; k++) {
					if (secondLU == cellcategory[k]) {
						switch (dataType) {
						case 2: {
							outdata[X - 1][Y - 1][k] = 0.333;
							break;
						}
						case 3: {
							outdata[X - 1][Y - 1][k] = 0.500;
							break;
						}
						}
					}

				}
				tempstr = "fin";
				outStream.write(tempstr, 0, tempstr.length());
				outStream.newLine();
				tempMap2.put(j, firstLU);
			}
			outputList.put(i, tempMap2);
		}
		// 메인 프로세스
		// ----------------------------------------------------------------------------------------------------------------------------------------

		// list파일에 데이터 입력
		// ---------------------------------------------------------------------------------------------------------------------------------
		tempstr = "--------------------------------------------------------------------------------";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "--- Extract result -------------------------------------------------------------";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "No.X	No.Y	Position Y	Position X	LU";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		Set<Double> outputYks = outputList.keySet();
		X = 0; // modify
		Y = 0; // modify
		for (Double posY : outputYks) {
			Y++;
			X = 0;
			Set<Double> outputXks = outputList.get(posY).keySet();
			for (Double posX : outputXks) {
				X++;
				tempstr = X + ",	" + Y + ",	" + posY + ",	" + posX + ",	" + outputList.get(posY).get(posX);
				outStream.write(tempstr, 0, tempstr.length());
				outStream.newLine();
			}
		}
		tempstr = "--------------------------------------------------------------------------------";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		outStream.close();
		// list파일에 데이터 입력
		// ---------------------------------------------------------------------------------------------------------------------------------

		// output파일에 데이터 입력
		// ---------------------------------------------------------------------------------------------------------------------------------
		outStream = new BufferedWriter(new FileWriter(datasrc));
		tempstr = "LU.DAT          2.1             Coordinate parameters and LandUse categories                    ";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "   2";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "Produced by CMW Version: 1.0.0  Level: 150211                                     ";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "Internal Coordinate Transformations  ---  COORDLIB   Version: 1.99   Level: 070921    ";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "FRACTION";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "UTM     ";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "  52N   ";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "WGS-84  08-21-2019";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = cellNumX +"       "+cellNumY +"       "+ String.format("%.3f", findX/1000) + "    " + String.format("%.3f", findY/1000) +"       "+ String.format("%.3f", (double)cellSize/1000) +"       "+ String.format("%.3f", (double)cellSize/1000)+ "    38";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "KM";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "		11    12    13    14    15    16    17    21    22    23    24    31    32    33    41    42    43    51    52    53    54    55    61    62    71    72    73    74    75    76    77    81    82    83    84    85    91    92";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		X = 0;
		Y = 0;
		StringBuilder datastr = new StringBuilder();
		for (Double posY : outputYks) {
			Y++;
			X = 0;
			Set<Double> outputXks = outputList.get(posY).keySet();
			for (@SuppressWarnings("unused") Double posX : outputXks) {
				X++;
				datastr.append(X + "	" + Y + "	");
				for (int i = 0; i < 38; i++) {
					datastr.append(String.format("%.3f", outdata[X - 1][Y - 1][i]) + "	");
				}
				datastr.append(".");
				outStream.write(datastr.toString(), 0, datastr.toString().length());
				outStream.newLine();
				datastr = new StringBuilder();
			}
		}
		outStream.close();
		System.out.println("작업을 성공적으로 완료하였습니다.");
		// ---------------------------------------------------------------------------------------------------------------------------------
		return 0;
	}
}
