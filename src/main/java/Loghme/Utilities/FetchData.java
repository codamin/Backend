package Loghme.Utilities;

import Loghme.entities.PartyFood;
import Loghme.entities.Restaurant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FetchData {

    public static String request(String urlAddress) {
        String result = null;
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();

            BufferedReader inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = inStream.readLine()) != null) {
                content.append(inputLine);
            }
            result = content.toString();
            inStream.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Restaurant> fetchRestaurants() {
        String data = request("http://138.197.181.131:8080/restaurants");
        try {
            List<Restaurant> newRestaurants = new ObjectMapper().readValue(data, new TypeReference<List<Restaurant>>() {});
            return newRestaurants;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Restaurant> fetchFoodParty() {
        String data = request("http://138.197.181.131:8080/foodparty").replace("menu", "partyMenu");
        try {
            System.out.println("################################################# mapping");
            List<Restaurant> newRestaurants = new ObjectMapper().readValue(data, new TypeReference<List<Restaurant>>() {});
            System.out.println("#################################################" + newRestaurants.size());
            return newRestaurants;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
