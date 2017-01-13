package com.example.pedro.vaichover;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import static com.example.pedro.vaichover.TelaBusca.getCidadesTratadas;

public class ListaCidades  extends FragmentActivity {

    private ViewGroup mListView;

    private static String cidadeSelecionada;

    public static String getcidadeSelecionada() {
        return cidadeSelecionada;
    }

    public static void setcidadeSelecionada(String informacao) {
        ListaCidades.cidadeSelecionada = informacao;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.activity_lista_cidades, null);
        setContentView(R.layout.activity_lista_cidades);

        mListView = (ViewGroup) findViewById(R.id.list_cities);
        String cidadesNaArea = getCidadesTratadas();
        String[] information;
        information = cidadesNaArea.split(",");
        Button[] buttons = new Button[15];
        final String[] str = new String[15];
        for (int i = 0; i < 15; i++) {
            final Button button = new Button(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            button.setLayoutParams(layoutParams);
            button.setId(i);
            button.setText(information[i*4]);
            str[i] = information[i*4]+ "," + information[(i*4)+1]+ "," + information[(i*4)+2]+ "," + information[(i*4)+3]  ;
            button.setTag(DetalhesCidade.class);
            buttons[i] = button;
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    setcidadeSelecionada(str[finalI]);
                    startActivity(new Intent(ListaCidades.this, DetalhesCidade.class));
                }
            });
        }
        for (int i = 0; i < 15; i++) {
            mListView.addView(buttons[i], LinearLayout.LayoutParams.MATCH_PARENT);
        }
    }


    /*
    private class asyncGetJson extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
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
            return mainResponseObject;
        }

        @Override
        protected void onPostExecute(JSONObject message) {
            String CurrentString = message.toString();
            String[] separated = CurrentString.split(",");
            String[] useStrings = new String[60];
            String cidades = "";
            int index = 0;
            for (String x : separated) {
                if (x.contains("name") || x.contains("temp_") || x.contains("description")) {
                    String[] information = new String[2];
                    information = x.split(":");
                    if (information[1].charAt(information[1].length() - 1) == '}') {
                        information[1] = information[1].substring(0, information.length - 1);
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
            //Log.d("test b", (String) textField.getText());
        }
    }

    /*
    private String getResponseText(String stringUrl) throws IOException
    {
        StringBuilder response  = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
            String strLine = null;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }*/


}
