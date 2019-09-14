package rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.Input.UserAuthInput;
import rest.Response.AirportResponse;
import rest.Response.LoginResponse;
import service.authentication.AuthService;
import service.authentication.Secured;
import service.common.Airport;
import service.common.CommonService;
import service.test.Test;

import java.util.List;
import java.util.ResourceBundle;


@Path("/user")
@ApplicationScoped
public class RestService {

    @Inject
    private AuthService authService;

    @Inject
    private Test testService;

    @Inject
    CommonService commonService;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserAuthInput userAuthInput) {
        try {
            authService.registerUser(userAuthInput);

        } catch (Exception e) {
            System.err.println("registration exception");
        }
        return Response.status(200).entity("").build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserAuthInput userAuthInput){
        LoginResponse loginResponse;
        try {
            loginResponse=authService.loginUser(userAuthInput);
            return Response.status(200).entity(loginResponse).build();
        } catch (Exception e) {
            System.err.println("Login exception");
        }
        return Response.status(400).entity("").build();
    }

    @GET
    @Path("/test")
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