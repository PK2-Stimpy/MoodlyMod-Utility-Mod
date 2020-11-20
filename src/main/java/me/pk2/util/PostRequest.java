package me.pk2.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequest {
    public static HttpURLConnection genCon(String Url) throws Exception {
        URL url = new URL(Url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        return con;
    }
    public static HttpURLConnection genUnOutCon(String Url) throws Exception {
        URL url = new URL(Url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        return con;
    }
    public static HttpURLConnection sendJSON(HttpURLConnection con, JSONObject jsonObject) throws IOException { return sendString(con, jsonObject.toString()); }
    public static HttpURLConnection sendString(HttpURLConnection con, String data) throws IOException {
        try(OutputStream os = con.getOutputStream()) {
            byte[] in = data.getBytes("utf-8");
            os.write(in, 0, in.length);
        }

        return con;
    }
    public static String read(HttpURLConnection con) throws Exception {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null)
                response.append(responseLine.trim());

            return response.toString();
        }
    }
}