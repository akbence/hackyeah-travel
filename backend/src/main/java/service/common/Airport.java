package service.common;

import java.util.ArrayList;
import java.util.List;

public class Airport {

    private String country;
    private List<City> cities;

    public Airport() {
        cities = new ArrayList<>();
    }

    public Airport(String country, List<City> cities) {
        this.country = country;
        this.cities = cities;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
