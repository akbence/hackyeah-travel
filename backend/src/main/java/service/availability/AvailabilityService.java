package service.availability;

import nativelot.LotInterface;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

public class AvailabilityService {

    @Inject
    LotInterface lotInterface;

    public List<FlightData> baseFunc(List<String> origin, List<String> destination, List<LocalDate> departureDate, LocalDate returnDate, CabinClass cabinClass, String market, TripType tripType,
                                     Integer adults, Integer teenagers, Integer children, Integer infants, boolean fromCache, List<FareType> fareType){
        try {
            lotInterface.getFlightsDatas( origin,  destination,  departureDate,  returnDate,  cabinClass,  market,  tripType,
                     adults,  teenagers,  children,  infants, fromCache,  fareType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
