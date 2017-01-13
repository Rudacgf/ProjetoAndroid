package com.example.pedro.vaichover;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.widget.TextView;

public class DetalhesCidade extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_detalhes_cidade );
        String str = ListaCidades.getcidadeSelecionada();
        String[] infoCidade = str.split(",");
        TextView cityName = (TextView) findViewById(R.id.cityName);
        cityName.setText("Cidade: " + infoCidade[0]);
        TextView cityTempMin = (TextView) findViewById(R.id.cityTempMin);
        cityTempMin.setText("Temp. Mínima: " + infoCidade[1] + "ºC");
        TextView cityTempMax = (TextView) findViewById(R.id.cityTempMax);
        cityTempMax.setText("Temp. Máxima: " + infoCidade[2] + "ºC");
        TextView cityWDesc = (TextView) findViewById(R.id.cityWDesc);
        cityWDesc.setText("Descrição do Tempo: " + infoCidade[3]);

    }

}
