package nativelot;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import service.availability.CabinClass;
import service.availability.FareType;
import service.availability.FlightData;
import service.availability.TripType;
import service.common.Airport;
import service.common.City;

import javax.enterprise.inject.Model;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Native interface, accessing native LOT Airlines API
 */
@Model
public class LotInterface {

    public static final String CURRENT_FINAL_URL= "https://api.lot.com/flights-dev/v2/";
    public static final String X_API_KEY = "X-Api-Key";
    private static String accessToken;


    /**
     * Necessary access token for everywhere authenticated endpoint
     * @return
     */
    private String getAccesToken() throws IOException {
        //TODO: needs to cache the token, not create everytime
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(CURRENT_FINAL_URL + "auth/token/get");
        ResourceBundle rb = ResourceBundle.getBundle("apikey");
        post.setHeader(X_API_KEY, rb.getString(X_API_KEY));
        post.setHeader("Content-Type", MediaType.APPLICATION_JSON);

        String jsonBody = Json.createObjectBuilder()
                .add("secret_key", rb.getString("secret_key"))
                .build()
                .toString();
        try {
            post.setEntity(new StringEntity(jsonBody));
            HttpResponse response = client.execute(post);
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.toString());
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonObject personObject = reader.readObject();
            return personObject.getString("access_token");
        } catch (IOException e) {
            System.err.println("Cant execute the API key request");
            throw e;
        }
    }


    public List<Airport> getAirports() throws IOException {
        String token = getAccesToken();
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(CURRENT_FINAL_URL + "/common/airports/get");
        get.setHeader("Content-Type", MediaType.APPLICATION_JSON);
        ResourceBundle rb = ResourceBundle.getBundle("apikey");
        get.setHeader(X_API_KEY, rb.getString(X_API_KEY));
        get.setHeader("Authorization", "Bearer "+ token);

        try {
            List<Airport> ret=new ArrayList<>();
            HttpResponse response = client.execute(get);
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.toString());
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonArray countries = reader.readArray();
            for (JsonValue jsonValue : countries) {
                JsonObject countryDatas= (JsonObject) jsonValue;
                Airport airport = new Airport();
                airport.setCountry(countryDatas.get("country").toString().replace("\"",""));
                JsonArray citiesDatas= (JsonArray) countryDatas.get("cities");
                for (JsonValue citiesData : citiesDatas) {
                    City city = new City();
                    city.setCity(((JsonObject)citiesData).get("city").toString().replace("\"",""));
                    city.setIata(((JsonObject)citiesData).get("iata").toString().replace("\"",""));
                    airport.getCities().add(city);
                }
                ret.add(airport);
            }
            return ret;
        } catch (IOException e) {
            System.err.println("Cant execute the airport request");
            throw e;
        }
    }


    public List<FlightData> getFlightsDatas(List<String> origin, List<String> destination, List<LocalDate> departureDate, LocalDate returnDate, CabinClass cabinClass, String market, TripType tripType,
                                Integer adults, Integer teenagers, Integer children, Integer infants, boolean fromCache, List<FareType> fareType) throws Exception {
        checkMandatoryFields(origin, destination, departureDate, cabinClass, market, tripType, adults);

        String token = getAccesToken();
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(CURRENT_FINAL_URL + "booking/availability");
        ResourceBundle rb = ResourceBundle.getBundle("apikey");
        post.setHeader(X_API_KEY, rb.getString(X_API_KEY));
        post.setHeader("Content-Type", MediaType.APPLICATION_JSON);
        post.setHeader("Authorization", "Bearer "+ token);


        JsonObjectBuilder jsonOptionsBuilder = Json.createObjectBuilder();

        JsonObjectBuilder jsonBodyParamsBuilder = Json.createObjectBuilder()
                .add("cabinClass", cabinClass.toString())
                .add("market", market)
                .add("tripType", tripType.toString())
                .add("adt", adults);

        JsonArrayBuilder originArrayBuilder = Json.createArrayBuilder();
        origin.forEach(originArrayBuilder::add);
        jsonBodyParamsBuilder.add("origin",originArrayBuilder);

        JsonArrayBuilder destinationArrayBuilder = Json.createArrayBuilder();
        destination.forEach(destinationArrayBuilder::add);
        jsonBodyParamsBuilder.add("destination",destinationArrayBuilder);

        JsonArrayBuilder depDateArrayBuilder = Json.createArrayBuilder();
        departureDate.forEach(t->depDateArrayBuilder.add(t.format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
        jsonBodyParamsBuilder.add("departureDate",depDateArrayBuilder);

        checkNonMandatoryFieldsAndAddToRequest(returnDate, teenagers, children, infants, fromCache, fareType, jsonBodyParamsBuilder,jsonOptionsBuilder);



        JsonObjectBuilder jsonBodyBuilder = Json.createObjectBuilder()
                .add("params",jsonBodyParamsBuilder)
                .add("options",jsonOptionsBuilder);


        String finalBody=jsonBodyBuilder.build().toString();

        System.out.println(finalBody);

        try {
            List<FlightData> ret=new ArrayList<>();
            post.setEntity(new StringEntity(finalBody));
            HttpResponse response = client.execute(post);
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.toString());
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonObject responseBody = reader.readObject();
            JsonArray data = responseBody.getJsonArray("data");
            System.out.println(json);
            System.out.println(data);
            for (JsonValue jsonValue : data) {
                JsonArray innerData = (JsonArray) jsonValue;
                System.out.println(innerData);
                /*JsonObject countryDatas= (JsonObject) jsonValue;
                Airport airport = new Airport();
                airport.setCountry(countryDatas.get("country").toString().replace("\"",""));
                JsonArray citiesDatas= (JsonArray) countryDatas.get("cities");
                for (JsonValue citiesData : citiesDatas) {
                    City city = new City();
                    city.setCity(((JsonObject)citiesData).get("city").toString().replace("\"",""));
                    city.setIata(((JsonObject)citiesData).get("iata").toString().replace("\"",""));
                    airport.getCities().add(city);
                }
                System.out.println(airport.getCountry());
                ret.add(airport);*/

            }
            return null;
        } catch (IOException e) {
            System.err.println("Cant execute the airport request");
            throw e;
        }

    }

    private void checkNonMandatoryFieldsAndAddToRequest(LocalDate returnDate, Integer teenagers, Integer children, Integer infants, boolean fromCache, List<FareType> fareType, JsonObjectBuilder jsonParamsBodyBuilder,JsonObjectBuilder jsonOptionsBodyBuilder) {
        if(returnDate!=null){
            jsonParamsBodyBuilder.add("returnDate",returnDate.format(DateTimeFormatter.ofPattern("ddMMYYYY")));
        }
        if(teenagers != null){
            jsonParamsBodyBuilder.add("c14",children);
        }
        if(children != null){
            jsonParamsBodyBuilder.add("chd",children);
        }
        if(infants  != null){
            jsonParamsBodyBuilder.add("inf",infants);
        }
        jsonOptionsBodyBuilder.add("fromCache",fromCache);
        if(fareType !=null){
            JsonArrayBuilder fareTypeArrayBuilder = Json.createArrayBuilder();
            fareType.forEach(t->fareTypeArrayBuilder.add(t.toString()));
            jsonOptionsBodyBuilder.add("fareType",fareTypeArrayBuilder);
        }
    }

    private void checkMandatoryFields(List<String> origin, List<String> destination, List<LocalDate> departureDate, CabinClass cabinClass, String market, TripType tripType, Integer adults) throws Exception {
        if(origin==null || destination==null || departureDate==null || cabinClass == null || market == null || tripType == null || adults == null  ){
            throw new Exception("Mandatory fields need to be filled");
        }
    }
}
