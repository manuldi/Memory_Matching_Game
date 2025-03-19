/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.io.BufferedReader; // Used to read data from the API response.
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection; //Java HTTP API to used to make an HTTP request to fetch data from the API.
import java.net.URL;
import java.util.Scanner; // used to get user inputs
import org.json.JSONArray; //JSON API to used to parse the JSON response from the API.
import org.json.JSONObject; //JSON API to used to parse the JSON response from the API.

/**
 *
 * @author manul
 */
public class BananaAPI {
    
    private static final String API_URL = "https://marcconrad.com/uob/banana/api.php";

    public static JSONObject getPuzzle() {
        try {
            // Connect to the API
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //Opens an HTTP connection to the API
            conn.setRequestMethod("GET"); //Sets request method to "GET" since we are retrieving data.
            conn.setRequestProperty("Accept", "application/json"); // Set header to accept JSON
            
            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); //Reads the API’s response
            StringBuilder response = new StringBuilder(); //Stores the response
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close(); //Closes the reader after reading all data.
            
            // Print response to check if it's valid JSON
//            System.out.println("API Response: " + response.toString());
            
            // Convert response to JSON
            return new JSONObject(response.toString()); //The API returns JSON data, and we need to convert it into a Java object for easy access.
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Unable to fetch puzzle from API.");
        }
        return null;
    }
}
