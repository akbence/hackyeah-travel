package rest;

import service.availability.AvailabilityService;
import service.availability.CabinClass;
import service.availability.FareType;
import service.availability.TripType;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Path("/availability")
public class AvailabilityRest {


    @Inject
    AvailabilityService availabilityService;

    @POST
    @Path("/test")
    public Response getFlightsData(){
        availabilityService.baseFunc(Arrays.asList("BUD"), Arrays.asList("LON"),  Arrays.asList(LocalDate.of(2019,10,10)),  null, CabinClass.E,  "PL", TripType.O,
                1,  0,  0, 0, false, Arrays.asList(FareType.ALL));
        return Response.status(200).build();
    }

}
