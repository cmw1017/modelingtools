package aermod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AERDTO {

	// 모델링 오염물질
	// 순서 SO2 CO NO2 Pb Benzene PM-10 Zn NH3 CS2 Cr Hg Cu Vinylchloride H2S Dichloromethane TCE As Ni Cd Br F HCN HCl
	// Phenol Formaldehyde
	public static final String[] polList = { "SO2", "CO", "NO2", "Pb", "Benzene", "PM-10", "Zn", "NH3", "CS2", "Cr", "Hg",
			"Cu", "Vinylchloride", "H2S", "Dichloromethane", "TCE", "As", "Ni", "Cd", "Br", "F", "HCN", "HCl",
			"Phenol", "Formaldehyde" };
	private Integer thread_num; //쓰레드 개수
	private String base_path; 	//실행파일이 있는 위치 경로
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
	private List<String> matters; // 모델에 사용할 오염물질들만 모음
	private Map<String,Map<String,String>> inpParams;
	private Map<String,Map<String,Double>> criteria;
	private Map<String,Map<String,Double>> result;
	private String selected_file_path = "/"; //업로드 하고 있는 파일 위치
	
	
	public Integer getThread_num() {
		return thread_num;
	}
	public void setThread_num(Integer thread_num) {
		this.thread_num = thread_num;
	}
	public String getBase_path() {
		return base_path;
	}
	public void setBase_path(String base_path) {
		this.base_path = base_path;
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
	public Map<String, Map<String, String>> getInpParams() {
		return inpParams;
	}
	public void setInpParams(Map<String, Map<String, String>> inpParams) {
		this.inpParams = inpParams;
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
	public String getSelected_file_path() {return selected_file_path;}
	public void setSelected_file_path(String selected_file_path) {this.selected_file_path = selected_file_path;}
}
