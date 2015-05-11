package com.tastysandwich.game.android.requests;

import android.os.AsyncTask;

import com.tastysandwich.game.UserScore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Filip on 3.5.2015.
 */
public class requestHiScore extends AsyncTask<String, String, UserScore[]> {

    private static final String sGetHiscores = "http://www.sollmin.tk/api/index.php?do=get";
    private URL cUrl;
    private HttpURLConnection conn;
    private BufferedReader is;
    private String sJSON;
    private UserScore u[];

    @Override
    protected UserScore[] doInBackground(String... params) {
        try {
            cUrl = new URL(sGetHiscores);
            conn = (HttpURLConnection) cUrl.openConnection();
            is = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String row = null;

            while ((row = is.readLine()) != null) {
                sb.append(row + "/n");
            }
            sJSON = sb.toString();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (is!=null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONObject jsonObject = new JSONObject(sJSON);
            if (jsonObject.has("error")) {
                u = new UserScore[1];
                u[0] = new UserScore("Error no results", 0);
            } else {
                JSONArray jsonArray = jsonObject.names();
                u = new UserScore[jsonArray.length()];
                for (int i=0; i< jsonArray.length(); i++) {
                    System.out.println(jsonArray.getString(i)+ jsonObject.getString(jsonArray.getString(i)));
                    u[i] = new UserScore(jsonArray.getString(i), Integer.valueOf(jsonObject.getString(jsonArray.getString(i))));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }
}
