package com.tastysandwich.game.android.requests;

import android.os.AsyncTask;

import com.tastysandwich.game.UserScore;

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
public class postHiScore extends AsyncTask<UserScore,String,Boolean> {

    private static final String sPostHiscore = "http://www.sollmin.tk/api/index.php?do=post";
    private URL cUrl;
    private HttpURLConnection conn;
    private BufferedReader is;
    private String sJSON;
    private Boolean b;


    @Override
    protected Boolean doInBackground(UserScore... params) {
        try {
            cUrl = new URL(sPostHiscore+"&score="+String.valueOf(params[0].getScore())+"&username="+params[0].getUser());
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
            if (jsonObject.getString("Result").equals("Success")) {
                b = true;
            } else {
                b = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return b;
    }
}
