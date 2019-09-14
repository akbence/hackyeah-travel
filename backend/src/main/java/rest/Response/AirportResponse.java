package rest.Response;

import service.common.Airport;

import java.util.List;

public class AirportResponse {
    private List<Airport> airports;

    public AirportResponse(List<Airport> airports) {
        this.airports = airports;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public void setAirports(List<Airport> airports) {
        this.airports = airports;
    }
}
