/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;
import org.json.JSONArray;
import java.io.*;
/**
 *
 * @author manul
 */


public class DBManager {
    public static JSONArray readJSONFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return new JSONArray(sb.toString());
        } catch (Exception e) {
            return new JSONArray(); // Return empty array if file not found
        }
    }

    public static void writeJSONFile(String filePath, JSONArray data) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(data.toString(4)); // Pretty print JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
