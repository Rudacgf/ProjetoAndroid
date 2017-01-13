package com.example.pedro.vaichover;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.pedro.vaichover.TelaBusca.setCidadesTratadas;

/**
 * Created by pedro on 13/01/2017.
 */
class asyncGetJson extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        StringBuilder response = new StringBuilder();
        String stringUrl = params[0];
        JSONObject mainResponseObject = null;
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpconn = null;
        try {
            httpconn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                String strLine;
                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mainResponseObject = new JSONObject(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String CurrentString = mainResponseObject.toString();
        String[] separated = CurrentString.split(",");
        String[] useStrings = new String[60];
        String cidades = "";
        int index = 0;
        for (String x : separated) {
            if (x.contains("name") || x.contains("temp_") || x.contains("description")) {
                String[] information = new String[2];
                information = x.split(":");
                if (information[1].charAt(information[1].length() - 1) == '}') {
                    information[1] = information[1].substring(0, information[1].length() - 1);
                }
                if (information[1].matches(".*\\d+\\d+.*")) {
                    Float kelvin = Float.parseFloat(information[1]);
                    kelvin = kelvin - 273.15F;
                    information[1] = kelvin.toString();
                }
                useStrings[index] = information[1];
                cidades = cidades + useStrings[index] + ",";
                index++;
            }
        }
        setCidadesTratadas(cidades);
        return cidades;
        //return mainResponseObject;
    }

    @Override
    protected void onPostExecute(String message) {
        setCidadesTratadas(message);
    }
}


