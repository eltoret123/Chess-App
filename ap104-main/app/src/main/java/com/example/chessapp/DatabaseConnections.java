package com.example.chessapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseConnections {
    public DatabaseConnections() {
    }

    public String makeGETRequest(String urlName) {
        BufferedReader rd;
        StringBuilder sb;
        String line;

        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();

            while((line = rd.readLine()) != null) {
                sb.append(line).append("\n");
            }

            conn.disconnect();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static ArrayList<String> parseJSON(String jsonString) {
        ArrayList<String> query = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(jsonString);
            JSONObject curObject = array.getJSONObject(0);
            query.add(curObject.getString("Username"));
            query.add(curObject.getString("Email"));
            return query;
        } catch (JSONException | NullPointerException var4) {
            return query;
        }
    }

    public static void main(String[] args) {
        System.out.println(UserInformationList("crispy", "qwerty32"));
    }

    public static ArrayList<String> UserInformationList(String UserName, String Password) {
        DatabaseConnections rc = new DatabaseConnections();
        String response = rc.makeGETRequest("https://studev.groept.be/api/a24pt104/LoginUserData/" + UserName + "/" + UserName + "/" + Password);
        ArrayList<String> UserData = parseJSON(response);
        if (UserData.isEmpty()) {
            UserData.add("false");
        } else {
            UserData.add("true");
        }

        return UserData;
    }
}
