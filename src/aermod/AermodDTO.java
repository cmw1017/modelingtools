package aermod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AermodDTO {

	private String base_path; 	//실행파일이 있는 위치 경로
	private String load_path;
	private Double latitude; 	//위도
	private Double longitude;	//경도
	private String sido; 		//시도
	private String sigun; 		//시군
	private String gu; 			//구
	private ArrayList<String> sido_list; 				//시도 리스트
	private Map<String, ArrayList<String>> sigun_list; 	//시군 리스트
	private Map<String, ArrayList<String>> gu_list; // 구 리스트
	private Map<String, Map<String, Map<String, Map<String, Double>>>> air_list; // 기존오염도
	private String ec_path;		//환경기준 사용 파일 경로
	private RMO rmo;			//기상대 정보
	private List<String> matters;
	private Map<String,Map<String,String>> inpparam;
	private Map<String,Map<String,Double>> criteria;
	private Map<String,Map<String,Double>> result;
	
	
	public String getBase_path() {
		return base_path;
	}
	public void setBase_path(String base_path) {
		this.base_path = base_path;
	}
	public String getLoad_path() {
		return load_path;
	}
	public void setLoad_path(String load_path) {
		this.load_path = load_path;
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
	public ArrayList<String> getSido_list() {
		return sido_list;
	}
	public void setSido_list(ArrayList<String> sido_list) {
		this.sido_list = sido_list;
	}
	public Map<String, ArrayList<String>> getSigun_list() {
		return sigun_list;
	}
	public void setSigun_list(Map<String, ArrayList<String>> sigun_list) {
		this.sigun_list = sigun_list;
	}
	public Map<String, ArrayList<String>> getGu_list() {
		return gu_list;
	}
	public void setGu_list(Map<String, ArrayList<String>> gu_list) {
		this.gu_list = gu_list;
	}
	public Map<String, Map<String, Map<String, Map<String, Double>>>> getAir_list() {
		return air_list;
	}
	public void setAir_list(Map<String, Map<String, Map<String, Map<String, Double>>>> air_list) {
		this.air_list = air_list;
	}
	public String getEc_path() {
		return ec_path;
	}
	public void setEc_path(String ec_path) {
		this.ec_path = ec_path;
	}
	public RMO getRmo() {
		return rmo;
	}
	public void setRmo(RMO rmo) {
		this.rmo = rmo;
	}
	public List<String> getMatters() {
		return matters;
	}
	public void setMatters(List<String> matters) {
		this.matters = matters;
	}
	public Map<String, Map<String, String>> getInpparam() {
		return inpparam;
	}
	public void setInpparam(Map<String, Map<String, String>> inpparam) {
		this.inpparam = inpparam;
	}
	public Map<String, Map<String, Double>> getCriteria() {
		return criteria;
	}
	public void setCriteria(Map<String, Map<String, Double>> criteria) {
		this.criteria = criteria;
	}
	public Map<String, Map<String, Double>> getResult() {
		return result;
	}
	public void setResult(Map<String, Map<String, Double>> result) {
		this.result = result;
	}
	
}
