package nativelot;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.enterprise.inject.Model;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/***
 * Native interface, accessing native LOT Airlines API
 */
@Model
public class LotInterface {

    public static final String CURRENT_FINAL_URL= "https://api.lot.com/flights-dev/v2/";
    public static final String X_API_KEY = "X-Api-Key";
    private static String accessToken;



    public String getAccesToken() {
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
            StringEntity se= new StringEntity(jsonBody);
            post.setEntity(se);
            post.getEntity().getContent();
            HttpResponse response = client.execute(post);
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.toString());
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonObject personObject = reader.readObject();
            return personObject.getString("access_token");
        } catch (IOException e) {
            System.err.println("Cant execute the API key request");
            return "";
        }
    }
}
