package aermod;

public class AerMain {

	public static void main(String[] args) {
		String insrc = "D:\\Modeling\\AERMOD\\여수(예)\\SO2_24.FIL";
		AERPOST post = new AERPOST(insrc);
		post.exet();
	}

}
