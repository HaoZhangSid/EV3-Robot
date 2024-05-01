package app;

import lejos.hardware.Button;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ev3.exercises.SharedControl;

public class httptest implements Runnable {

    private volatile boolean running = true;
    private SharedControl sharedControl;

    public httptest(SharedControl sharedControl) {
        this.sharedControl = sharedControl;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Execute httptest
                getDataFromAPI();
                // Wait for 2 seconds
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    private void getDataFromAPI() {
        HttpURLConnection conn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            URL url = new URL("http://192.168.32.224:8080/rest/lego/getlego");
            conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String jsonData = sb.toString();

            // Parse JSON data
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray legoSettingArray = jsonObject.getAsJsonArray("legoSetting");
            JsonObject legoSettingObject = legoSettingArray.get(0).getAsJsonObject(); // Assuming there is only one object in the array

            // Extract data and store in variables
            int action = legoSettingObject.get("action").getAsInt();
            int speed = legoSettingObject.get("speed").getAsInt();
            double proportional = legoSettingObject.get("proportional").getAsDouble();
            double integral = legoSettingObject.get("integral").getAsDouble();
            double derivative = legoSettingObject.get("derivative").getAsDouble();

            // Set the extracted data in the shared control instance
            sharedControl.setAction(action);
            sharedControl.setSpeed(speed);
            sharedControl.setProportional(proportional);
            sharedControl.setIntegral(integral);
            sharedControl.setDerivative(derivative);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Some problem!");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}