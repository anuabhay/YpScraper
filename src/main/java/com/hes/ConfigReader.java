package com.hes;
import java.io.FileReader;
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

            Object sub = parser.parse(new FileReader(
                    "/work/anu/scrape-web/ypscrape/src/main/resources/locations.json"));

            Object type = parser.parse(new FileReader(
                    "/work/anu/scrape-web/ypscrape/src/main/resources/types.json"));

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

