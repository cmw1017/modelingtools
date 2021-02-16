package aermod;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class AERPRE {
	
	String matter;
	Map<String,String> inpparam;
	String base_path;
	
	public AERPRE(String matter, Map<String,String> inpparam, String base_path) {
		this.matter = matter;
		this.inpparam = inpparam;
		this.base_path = base_path;
	}
	
	public void CreateInp() {
		try {
			int ch;
			Reader inStream = new FileReader(base_path + "\\aermod_.inp");
			BufferedWriter outStream = new BufferedWriter(new FileWriter(base_path + "\\run\\aermod_" + matter + ".inp"));
			StringBuilder str = new StringBuilder();
			
			while (true) {
				ch = inStream.read();
				if (ch != ' ' && ch != 10 && ch != 13 && ch != -1) { //  단어를 구분하는 문자가 아닌 경우
					str.append((char) ch);
				} else {
					if (str.length() != 0) {
						if (str.toString().contains("@@!1")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!1", inpparam.get("@@!1"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!2")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!2", inpparam.get("@@!2"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!3")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!3", inpparam.get("@@!3"));
							outStream.write(str2, 0, str2.length());
							str = new StringBuilder();
							continue;
						} else if (str.toString().contains("@@!4")) {
							String str2 = str.toString();
							str2 = str2.replace("@@!4", inpparam.get("@@!4"));
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
					outStream.write(String.valueOf((char)ch), 0, String.valueOf((char)ch).length());
				}
				
			}
			inStream.close();
			outStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
