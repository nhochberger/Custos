package modules.weather;

public class CountryCity {

	private String country;
	private String city;

	public CountryCity() {
		super();
	}

	public String country() {
		return this.country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String city() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}
}
