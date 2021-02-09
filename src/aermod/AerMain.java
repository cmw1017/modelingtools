package aermod;

public class AerMain {

	public static void main(String[] args) {
		String insrc = "D:\\Modeling\\AERMOD\\여수(예)\\SO2_1 - 복사본.FIL";
		int series = 0;
		POSTProcess post = new POSTProcess(insrc, series);
		post.exet();
	}

}
