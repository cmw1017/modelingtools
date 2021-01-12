package main;

import java.io.IOException;
import java.util.Scanner;

public class MainProcess {

	public static void main(String arg[]) throws IOException, InterruptedException {
		Scanner select = new Scanner(System.in);
		System.out.println();
		System.out.println("-------------------- 설         명  ---------------------");
		System.out.println("이 프로그램은 CALPUFF의 전처리 과정 및 결과 통합 과정을 수행하는 프로그램입니다");
		System.out.println("아래의 옵션을 선택하여 원하시는 작업을 수행하여 주세요");
		System.out.println("-------------------- 선 택 옵 션  ---------------------");
		System.out.println("1. CTGPROC");
		System.out.println("2. MAKEGEO");
		System.out.println("3. CALPOST 요약");
		System.out.println("4. 종료");
		System.out.print("메뉴에 해당하는 숫자를 입력하여 주세요 :  ");
		//String selectNum = select.nextLine();
		int selectNum = select.nextInt();
		switch (selectNum) {
		case 1: {
			System.out.println("CTGPROC 작업 시작");
			System.out.println();
			CTGPROC ctg = new CTGPROC();
			if(ctg.mainP(select) == -1) return;
			System.out.println("MAKEGEO 작업을 수행하시겠습니까?(1: Yes / 0 : No)");
			System.out.print("원하시는 숫자를 입력하여 주세요 : ");
			int selectNum2 = select.nextInt();
			if(selectNum2 == 1) {
				System.out.println("MAKEGEO 작업 시작");
				System.out.println();
				MAKEGEO mak = new MAKEGEO();
				mak.mainP(1,"LANDUSE_CLASSIFY.DAT",select);
			}
			break;
		}
		case 2: {
			System.out.println("MAKEGEO 작업 시작");
			System.out.println();
			MAKEGEO mak = new MAKEGEO();
			mak.mainP(0,"",select);
			break;
		}
		case 3: {
			System.out.println("CALPOST 요약 작업 시작");
			System.out.println();
			CALPOST_summary cal = new CALPOST_summary();
			cal.mainP(select);
			break;
		}
		case 4: {
			select.close();
			return;
		}
		}
		select.close();
	}
}
