package com.example.pedro.vaichover;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.inlocomedia.android.InLocoMedia;
import com.inlocomedia.android.InLocoMediaOptions;
import com.inlocomedia.android.ads.AdError;
import com.inlocomedia.android.ads.AdRequest;
import com.inlocomedia.android.ads.AdType;
import com.inlocomedia.android.ads.AdView;
import com.inlocomedia.android.ads.interstitial.InterstitialAd;
import com.inlocomedia.android.ads.interstitial.InterstitialAdListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.delay;
import static android.R.attr.startDelay;


public class TelaBusca extends FragmentActivity implements OnMapReadyCallback {

    private static LatLng markerLatLng = null;

    protected int getLayoutId() {
        return R.layout.activity_tela_busca;
    }

    private GoogleMap mMap;

    //private LatLng markerLatLng;

    public static LatLng getMarkerLatLng() {
        return markerLatLng;
    }

    public static void setMarkerLatLng(LatLng latLng) {
        markerLatLng = latLng;
    }

    private static String cidadesTratadas;

    public static String getCidadesTratadas() {
        return cidadesTratadas;
    }

    public static void setCidadesTratadas(String informacao) {
        TelaBusca.cidadesTratadas = informacao;
    }

    String urlJSON = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        InLocoMediaOptions options = InLocoMediaOptions.getInstance(this);
        options.setAdsKey("698cd863655f82878faf9142be932008349c0c8178e368dfaac5bf2e5d7cf9b4");
        options.setLogEnabled(true);
        options.setDevelopmentDevices("EE764459E508574A9D5F6A4D878E79A");
        InLocoMedia.init(this, options);
        setUpMap();
    }

    @Override
    public void onDestroy() {
        setMarkerLatLng(null);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callAd();/*
        InterstitialAd interstitialAd = new InterstitialAd(this);
        AdRequest adRequest = new AdRequest();
        adRequest.setAdUnitId("698cd863655f82878faf9142be932008349c0c8178e368dfaac5bf2e5d7cf9b4");
        interstitialAd.loadAd(adRequest);
        interstitialAd.show();*/
        setUpMap();
    }

    private void callAd(){
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setInterstitialAdListener(new InterstitialAdListener() {

            @Override
            public void onAdReady(final InterstitialAd ad) {
                //Called when the view has received an advertisement and is ready to be shown
                //You can call the interstitialAd present method here, or save it for any other moment you wish to present it.
                ad.show();
            }

            @Override
            public void onAdError(InterstitialAd ad, AdError error) {
                //Called when the ad request has failed.
            }

            @Override
            public void onAdOpened(InterstitialAd ad) {
                //Called right before the interstitialAd content is presented on the screen
            }

            @Override
            public void onAdClosed(InterstitialAd ad) {
                //Called right before the interstitialAd content is dismissed on the screen
            }
        });

        AdRequest adRequest = new AdRequest();
        adRequest.setAdUnitId("698cd863655f82878faf9142be932008349c0c8178e368dfaac5bf2e5d7cf9b4");
        interstitialAd.loadAd(adRequest);
    }

    private void setUpMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;

        final Button btnBusca = (Button) findViewById(R.id.button_busca);
        btnBusca.setClickable(false);
        btnBusca.setText("Clique no mapa");
        btnBusca.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String str = getCidadesTratadas();
                if (markerLatLng != null && str != null) {
                        startActivity(new Intent(TelaBusca.this, ListaCidades.class));
                }else{
                    btnBusca.setText("Clique novamente");
                }
            }
        });

        mMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                btnBusca.setText("Buscando..");
                btnBusca.setClickable(false);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                setMarkerLatLng(new LatLng( latLng.latitude,latLng.longitude ));
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);

                String urlJSON = "http://api.openweathermap.org/data/2.5/find?lat="+ markerLatLng.latitude +"&lon="+ markerLatLng.longitude +"&cnt=15&APPID=67d182bcf6db8ed76c97c70e04d41625";
                new asyncGetJson().execute(urlJSON);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Button btnBusca = (Button) findViewById(R.id.button_busca);
                        btnBusca.setText("Vai Chover ?");
                        btnBusca.setClickable(true);
                    }}, 5500);
            }
        });


    }


/*
    private class asyncGetJson extends AsyncTask<String, Void, String> {

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
    }*/

}
