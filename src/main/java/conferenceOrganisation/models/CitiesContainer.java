package conferenceOrganisation.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CitiesContainer {
	
	private List<String> cities = new ArrayList<String>();

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	
	public void addCity(String city) {
		this.cities.add(city);
	}

}
