package main;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class CALPOST_summary {

	public void mainP(Scanner sc) throws IOException {

		// 입력 데이터 경로 입력--------------------------------------------------------------------------------------------------------------------
		System.out.print("확장자 명을 포함한 파일 전체 이름 입력하여 주세요 : ");
		sc.nextLine();
		String insrc = sc.nextLine();
		//insrc = ".\\input\\" + insrc;
		insrc = "F:\\input\\" + insrc;
		// ---------------------------------------------------------------------------------------------------------------------------------
		
		// 기본정보 읽기  ------------------------------------------------------------------------------------------------------------------------
		// series1 : 데이터 개수
		// hourNum : 평균 기간(ex. 1, 2, 3, 4, 6, 8, 24, 8760)
		// reNum : 수용점 개수
		// totalhour : 모델링 기간
		// datalen : 시간별 데이터 개수
		// orderNum : 내림차순으로 순위
		Reader inStream = new FileReader(insrc);
		int ch, series1 = 0, hourNum = 0, reNum = 0, totalhour = 0, datalen = 0, orderNum=0;
		String polutid = "";
		StringBuilder str = new StringBuilder();
		Double[] xcordinate;
		Double[] ycordinate;
		String[] date;
		Double[][] conc;
		while (true) {
			ch = inStream.read();
			if (ch != ' ' && ch != 10 && ch != 13) {
				str.append((char) ch);
			} else {
				if (str.length() != 0) {
					series1++;
					if (series1 == 4)
						polutid = str.toString();
					if (series1 == 6)
						hourNum = Integer.parseInt(str.toString());
					if (series1 == 15)
						reNum = Integer.parseInt(str.toString());
					if (str.toString().equals("Type:"))
						break;
					str = new StringBuilder();
				}
			}
		}
		// ---------------------------------------------------------------------------------------------------------------------------------
		
		
		// 데이터 추출  -------------------------------------------------------------------------------------------------------------------------
		series1 = 0;
		System.out.print("데이터의 총 시간을 입력하여 주세요 : ");
		totalhour = sc.nextInt();
		System.out.print("출력될 데이터의 순위를 입력하여 주세요(must >= 1) : ");
		orderNum = sc.nextInt();
		datalen = totalhour / hourNum;
		if(orderNum > datalen || orderNum==0) {
			System.out.println("순위가 데이터 개수 범위를 벗어났거나, 0순위를 입력하였습니다.  ( orderNum = " + orderNum +"/ datalen = " + datalen+" )");
			inStream.close();
			return;
		}
		System.out.println("입력 총 시간 : " + totalhour);
		System.out.println(hourNum + "시간 평균 데이터");
		System.out.println(reNum + "개의 수용점이 존재합니다.");
		xcordinate = new Double[reNum];
		ycordinate = new Double[reNum];
		date = new String[datalen];
		conc = new Double[datalen][reNum];
		int series2 = 0, series3 = 0;
		for (int i = 0; i < datalen; i++) {
			date[i] = "";
		}
		System.out.println("진행중....");
		while (true) {
			ch = inStream.read();
			if (ch != ' ' && ch != 10 && ch != 13 && ch!=-1) {
				str.append((char) ch);
			} else {
				if (str.length() != 0) {
					series1++;
					if (str.toString().equals("x(km):")) {
						series1 = 0;
						series2++;
						str = new StringBuilder();
						continue;
					} else if (str.toString().equals("y(km):")) {
						if(series1 == reNum){
							System.out.println("데이터가 충분하지 않습니다");
						}
						series1 = 0;
						series2++;
						str = new StringBuilder();
						continue;
					} else if (str.toString().equals("YYYY")) {
						if(series1 == reNum){
							System.out.println("데이터가 충분하지 않습니다");
						}
						series1 = 0;
						series2++;
						str = new StringBuilder();
						continue;
					} else if (str.toString().equals("JDY")) {
						series1 = 0;
						str = new StringBuilder();
						continue;
					} else if (str.toString().equals("HHMM")) {
						series1 = 0;
						str = new StringBuilder();
						continue;
					}

					switch (series2) {
					case 1: {
						xcordinate[series1 - 1] = Double.parseDouble(str.toString());
						break;
					}
					case 2: {
						ycordinate[series1 - 1] = Double.parseDouble(str.toString());
						break;
					}
					case 3: {
						if (series1 == 1 || series1 == 2 || series1 == 3) {
							date[series3] += str.toString() + " ";
						} else {
							conc[series3][series1 - 4] = Double.parseDouble(str.toString());
						}
						if (series1-3 == reNum) {
							series3++;
							series1 = 0;
						}
						break;
					}
					}
					str = new StringBuilder();
				}
			}
			if(ch == -1) {
				System.out.println("읽기 종료");
				break;
			}
		}
		if(series3 != datalen) {
			System.out.println("데이터가 충분하지 않습니다.(현재 데이터 시간 : " + series3 +")");
			inStream.close();
			return;
		}
		inStream.close();
		// ---------------------------------------------------------------------------------------------------------------------------------
		
		

		// 데이터 요약  -------------------------------------------------------------------------------------------------------------------------
		// orderArray : 수용점 별 입력 순위 데이터 위치
		int[] orderArray = new int[datalen];
		Double[] gridData = new Double[reNum]; // 그리드 출력시 필요
		Double[] gridAvgData = new Double[reNum];
		String tempstr;
		orderArray = findOrder(conc, datalen, orderNum, reNum);
		//String listsrc = ".\\output\\SUMARRY_"+polutid+"_"+hourNum+"HOUR"+"_"+orderNum+"TH_CONC.DAT";
		String listsrc = "F:\\output\\SUMARRY_"+polutid+"_"+hourNum+"HOUR"+"_"+orderNum+"TH_CONC.DAT";
		BufferedWriter outStream = new BufferedWriter(new FileWriter(listsrc));
		tempstr = "   X       Y      CONC";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		tempstr = "_______ _______ _______";
		outStream.write(tempstr, 0, tempstr.length());
		outStream.newLine();
		for(int i =0 ; i < reNum ; i ++) {
			gridData[i] = conc[orderArray[i]][i];
			tempstr = xcordinate[i] + "	" + ycordinate[i] + "	" + String.format("%.6E", conc[orderArray[i]][i]);
			outStream.write(tempstr, 0, tempstr.length());
			outStream.newLine();
		}
		outStream.close();
		if(hourNum == 1) {
			//String anlistsrc = ".\\output\\SUMARRY_"+polutid+"_"+totalhour+"HOUR"+"_1ST_CONC.DAT";
			String anlistsrc = "F:\\output\\SUMARRY_"+polutid+"_"+totalhour+"HOUR"+"_1ST_CONC.DAT";
			gridAvgData = avgCalc(conc, datalen,reNum);
			BufferedWriter outStream2 = new BufferedWriter(new FileWriter(anlistsrc));
			tempstr = "   X       Y      CONC";
			outStream2.write(tempstr, 0, tempstr.length());
			outStream2.newLine();
			tempstr = "_______ _______ _______";
			outStream2.write(tempstr, 0, tempstr.length());
			outStream2.newLine();
			for(int i =0 ; i < reNum ; i ++) {
				tempstr = xcordinate[i] + "	" + ycordinate[i] + "	" + String.format("%.6E", gridAvgData[i]);
				outStream2.write(tempstr, 0, tempstr.length());
				outStream2.newLine();
			}
			outStream2.close();
		}
		// ---------------------------------------------------------------------------------------------------------------------------------
	
		
		// 그리드 데이터 출력 여부 확인  ---------------------------------------------------------------------------------------------------------------
		int answer;
		System.out.println("GRID 파일을 생성하시겠습니까?(1: Yes / 0 : No)");
		answer = sc.nextInt();
		if(answer == 1) {
			String gridsrc = ".\\output\\GRID_"+polutid+"_"+hourNum+"HOUR"+"_"+orderNum+"TH_CONC.GRD";
			BufferedWriter outStream3 = new BufferedWriter(new FileWriter(gridsrc));
			tempstr = "DSAA";
			outStream3.write(tempstr, 0, tempstr.length());
			outStream3.newLine();
			tempstr = "          " + String.format("%.0f",Math.sqrt(reNum)) + "          "+String.format("%.0f",Math.sqrt(reNum)); 
			outStream3.write(tempstr, 0, tempstr.length());
			outStream3.newLine();
			tempstr = "     " + String.format("%.3f",findMin(xcordinate)) + "     "+String.format("%.3f",findMax(xcordinate)); 
			outStream3.write(tempstr, 0, tempstr.length());
			outStream3.newLine();
			tempstr = "    " + String.format("%.3f",findMin(ycordinate)) + "    "+String.format("%.3f",findMax(ycordinate)); 
			outStream3.write(tempstr, 0, tempstr.length());
			outStream3.newLine();
			tempstr = "  " + String.format("%.4E", findMin(gridData)) + "  "+String.format("%.4E", findMax(gridData)); 
			outStream3.write(tempstr, 0, tempstr.length());
			outStream3.newLine();
			for(int i =0 ; i < Math.sqrt(reNum) ; i++) {
				tempstr= "";
				for(int j=0 ; j< Math.sqrt(reNum) ; j++) {
					tempstr += String.format("%.4E", gridData[i*(int)Math.sqrt(reNum)+j])+"	";
				}
				outStream3.write(tempstr, 0, tempstr.length());
				outStream3.newLine();
			}
			outStream3.close();
			if(hourNum == 1) {
				String angridsrc = ".\\output\\GRID_"+polutid+"_"+totalhour+"HOUR"+"_1ST_CONC.GRD";
				BufferedWriter outStream4 = new BufferedWriter(new FileWriter(angridsrc));
				tempstr = "DSAA";
				outStream4.write(tempstr, 0, tempstr.length());
				outStream4.newLine();
				tempstr = "          " + String.format("%.0f",Math.sqrt(reNum)) + "          "+String.format("%.0f",Math.sqrt(reNum)); 
				outStream4.write(tempstr, 0, tempstr.length());
				outStream4.newLine();
				tempstr = "     " + String.format("%.3f",findMin(xcordinate)) + "     "+String.format("%.3f",findMax(xcordinate)); 
				outStream4.write(tempstr, 0, tempstr.length());
				outStream4.newLine();
				tempstr = "    " + String.format("%.3f",findMin(ycordinate)) + "    "+String.format("%.3f",findMax(ycordinate)); 
				outStream4.write(tempstr, 0, tempstr.length());
				outStream4.newLine();
				tempstr = "  " + String.format("%.4E", findMin(gridAvgData)) + "  "+String.format("%.4E", findMax(gridAvgData)); 
				outStream4.write(tempstr, 0, tempstr.length());
				outStream4.newLine();
				for(int i =0 ; i < Math.sqrt(reNum) ; i++) {
					tempstr= "";
					for(int j=0 ; j< Math.sqrt(reNum) ; j++) {
						tempstr += String.format("%.4E", gridAvgData[i*(int)Math.sqrt(reNum)+j])+"	";
					}
					outStream4.write(tempstr, 0, tempstr.length());
					outStream4.newLine();
				}
				outStream4.close();
			}
			System.out.println("작업을 성공적으로 완료하였습니다.");
		}else {
			System.out.println("작업을 성공적으로 완료하였습니다.");
		}
		// ---------------------------------------------------------------------------------------------------------------------------------
	}

	int[] findOrder(Double[][] conc, int datalen, int orderNum, int reNum) {
		int[] orderArray = new int[reNum];
		Double[] concorder = new Double[orderNum];
		int[] order = new int[orderNum];

		for (int o = 0; o < reNum; o++) {
			for (int i = 0; i < orderNum; i++) {
				concorder[i] = 0.0;
			}
			for (int i = 0; i < datalen; i++) {
				for (int j = 0; j < orderNum; j++) {
					if (concorder[j] < conc[i][o]) {
						for (int k = orderNum - 1; k > j; k--) {
							concorder[k] = concorder[k - 1];
							order[k] = order[k - 1];
						}
						concorder[j] = conc[i][o];
						order[j] = i;
						break;
					}
				}
			}
			orderArray[o] = order[orderNum-1]; 
		}

		return orderArray;
	}

	Double[] avgCalc(Double[][] conc, int datalen, int reNum) {
		Double[] avgconc = new Double[reNum];
		for (int j = 0; j < reNum; j++) {
			avgconc[j] = 0.0;
			for (int i = 0; i < datalen; i++) {
				avgconc[j] += conc[i][j];
			}
			avgconc[j] = avgconc[j]/datalen;
		}
		return avgconc;
	}

	double findMin(Double[] array) {
		double min = array[0];
		for(int i =1 ; i < array.length ; i++) {
			if(min > array[i]) {
				min = array[i];
			}
		}
		return min;
	}
	double findMax(Double[] array) {
		double max = array[0];
		for(int i =1 ; i < array.length ; i++) {
			if(max < array[i]) {
				max = array[i];
			}
		}
		return max;
	}
}
