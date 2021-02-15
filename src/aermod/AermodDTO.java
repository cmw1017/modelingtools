package aermod;

import java.util.List;
import java.util.Map;

public class AermodDTO {

	private String load_path;
	private List<String> matters;
	private Map<String,Map<String,String>> inpparam;
	
	
	public String getLoad_path() {
		return load_path;
	}
	public void setLoad_path(String load_path) {
		this.load_path = load_path;
	}
	public List<String> getMatters() {
		return matters;
	}
	public void setMatters(List<String> matters) {
		this.matters = matters;
	}
	public Map<String,Map<String,String>> getInpparam() {
		return inpparam;
	}
	public void setInpparam(Map<String,Map<String,String>> inpparam) {
		this.inpparam = inpparam;
	}

}
