package aermod;

import java.util.List;
import java.util.Map;

public class AermodDTO {

	private String load_path;
	private String base_path; 	//실행파일이 있는 위치 경로
	private Double latitude; 	//위도
	private Double longitude;	//경도
	private String sido; 		//시도
	private String sigun; 		//시군
	private String gu; 			//구
	private String ec_path;		//환경기준 사용 파일 경로
	private String rmo;			//기상대 정보
	private List<String> matters;
	private Map<String,Map<String,String>> inpparam;
	private Map<String,Map<String,Double>> criteria;
	

	public String getEc_path() {
		return ec_path;
	}
	public void setEc_path(String ec_path) {
		this.ec_path = ec_path;
	}
	public String getRmo() {
		return rmo;
	}
	public void setRmo(String rmo) {
		this.rmo = rmo;
	}
	
	public String getSido() {
		return sido;
	}
	public void setSido(String sido) {
		this.sido = sido;
	}
	public String getSigun() {
		return sigun;
	}
	public void setSigun(String sigun) {
		this.sigun = sigun;
	}
	public String getGu() {
		return gu;
	}
	public void setGu(String gu) {
		this.gu = gu;
	}
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
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
