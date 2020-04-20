package Loghme.Utilities;

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

    public static String request(String urlAddress) throws IOException {
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
        inStream.close();
        connection.disconnect();
        return content.toString();
    }

    public static List<Restaurant> fetchRestaurants() {
        String data;
        try {
            data = RequestApi.request("http://138.197.181.131:8080/restaurants");
            ObjectMapper mapper = new ObjectMapper();
            List<Restaurant> newRestaurants = mapper.readValue(data,
                    new TypeReference<List<Restaurant>>() {
                    });
            return newRestaurants;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fetch restaurant shitted out");
            return null;
        }
    }
}
