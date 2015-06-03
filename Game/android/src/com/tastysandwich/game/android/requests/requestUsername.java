package com.tastysandwich.game.android.requests;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Filip on 30.5.2015.
 */
public class requestUsername extends AsyncTask<String, String, Boolean> {

    private static final String cUrl = "http://www.sollmin.tk/api/username.php?do=check";
    private HttpURLConnection conn;
    private URL url;
    private BufferedReader br;
    private String sJSON;
    private Boolean b;


    @Override
    protected Boolean doInBackground(String... params) {
        String str="";
        if (params[1]==null) {
            str = cUrl + "&username="+params[0];
        }
        if (params[1]!=null) {
            str = cUrl + "&username=" + params[0] + "&lun="+params[1];
        }
        try {
            url = new URL(str);
            conn = (HttpURLConnection) url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String row = null;

            while ((row = br.readLine()) != null) {
                sb.append(row + "/n");
            }

            sJSON = sb.toString();
        } catch(Exception e) {
          e.printStackTrace();
        } finally {
            conn.disconnect();
            if (br!=null) {
                try {
                    br.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            JSONObject json = new JSONObject(sJSON);
            if (json.getString("Result").equals("Success")) {
                System.out.println("true");
                b=true;
            }
            if (json.getString("Result").equals("Failed")) {
                System.out.println("false");
                b=false;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return b;
    }
}
