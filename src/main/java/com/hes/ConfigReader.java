package com.hes;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;

class Configs {
    ArrayList locations;
    ArrayList types;

    Configs(ArrayList locations, ArrayList types) {
        this.locations = locations;
        this.types = types;
    }
}
public class ConfigReader {
    public static void main(String[] args) {
        loadConfig();
    }

    public static Configs loadConfig(){
        JSONParser parser = new JSONParser();
        Configs config = null;
        try {
            ClassLoader classLoader = ConfigReader.class.getClassLoader();
            InputStream is = classLoader.getResourceAsStream("locations.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb_locations = new StringBuilder();

            String line;
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb_locations.append(line);
            }
            is = classLoader.getResourceAsStream("types.json");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            StringBuilder sb_types = new StringBuilder();

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb_types.append(line);
            }

            Object sub = parser.parse(sb_locations.toString());
            Object type = parser.parse(sb_types.toString());

            JSONObject jsonObject = (JSONObject) sub;
            ArrayList suburbs = (ArrayList)jsonObject.get("suburbs");
            jsonObject = (JSONObject) type;
            ArrayList types = (ArrayList)jsonObject.get("types");
            config = new Configs(suburbs,types);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
}

