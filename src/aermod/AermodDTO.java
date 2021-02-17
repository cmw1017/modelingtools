package aermod;

import java.util.List;
import java.util.Map;

public class AermodDTO {

	private String load_path;
	private String base_path;
	private List<String> matters;
	private Map<String,Map<String,String>> inpparam;
	private Map<String,Map<String,Double>> criteria;
	
	
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
	public String getBase_path() {
		return base_path;
	}
	public void setBase_path(String base_path) {
		this.base_path = base_path;
	}
	public Map<String,Map<String,Double>> getCriteria() {
		return criteria;
	}
	public void setCriteria(Map<String,Map<String,Double>> criteria) {
		this.criteria = criteria;
	}

}
