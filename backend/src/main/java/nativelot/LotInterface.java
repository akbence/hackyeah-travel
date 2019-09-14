package nativelot;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import service.common.Airport;
import service.common.City;

import javax.enterprise.inject.Model;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
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
        for (Header allHeader : get.getAllHeaders()) {
            System.out.println(allHeader.getName() + " "  +allHeader.getValue());
        }

        try {
            List<Airport> ret=new ArrayList<>();
            HttpResponse response = client.execute(get);
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.toString());
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonArray countries = reader.readArray();
            for (JsonValue jsonValue : countries) {
                JsonObject countryDatas= (JsonObject) jsonValue;
                Airport airport = new Airport();
                airport.setCountry(countryDatas.get("country").toString());
                JsonArray citiesDatas= (JsonArray) countryDatas.get("cities");
                for (JsonValue citiesData : citiesDatas) {
                    City city = new City();
                    city.setCity(((JsonObject)citiesData).get("city").toString());
                    city.setIata(((JsonObject)citiesData).get("iata").toString());
                    airport.getCities().add(city);
                }
                System.out.println(airport.getCountry());
                airport.getCities().forEach(t -> System.out.println(t.getCity()+ " "+ t.getIata()) );
                ret.add(airport);
            }
            return ret;
        } catch (IOException e) {
            System.err.println("Cant execute the airport request");
            throw e;
        }
    }
}
