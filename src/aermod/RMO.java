package aermod;

public class RMO {
	
	private String name;		//기상대 이름
	private String id;			//기상대 id
	private String sf_id;			//기상대 id
	private String ua_id;			//기상대 id
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

	public String getSf_id() {
		return sf_id;
	}

	public void setSf_id(String sf_id) {
		this.sf_id = sf_id;
	}

	public String getUa_id() {
		return ua_id;
	}

	public void setUa_id(String ua_id) {
		this.ua_id = ua_id;
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
