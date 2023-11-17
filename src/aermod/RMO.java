package aermod;

public class RMO {
	
	private String name;		//기상대 이름
	private String id;			//기상대 파일 id
	private String sfc_id;		//표층 기상대 id
	private String pfl_id;		//고층 기상대 id
	private Double latitude; 	//위도
	private Double longitude;	//경도
	private Double elev;		//고도
	private Double distance;	//고도

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSfc_id() {
		return sfc_id;
	}

	public void setSfc_id(String sfc_id) {
		this.sfc_id = sfc_id;
	}

	public String getPfl_id() {
		return pfl_id;
	}

	public void setPfl_id(String pfl_id) {
		this.pfl_id = pfl_id;
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

	public Double getElev() {
		return elev;
	}

	public void setElev(Double elev) {
		this.elev = elev;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
