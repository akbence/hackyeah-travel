package rest;

import rest.Response.AirportResponse;
import service.common.Airport;
import service.common.CommonService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/common")
public class CommonRest {
    @Inject
    CommonService commonService;

    @GET
    @Path("/airports")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(){
        try{
            List<Airport> ap=commonService.getAirports();
            return Response.status(200).entity(new AirportResponse(ap)).build();
        }catch (Exception e ){
            Response.status(400).entity("Not successfull operation").build();
        }
        return null;
    }
}
