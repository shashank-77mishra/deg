package com.example.deg;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.deg.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText cityEd;
    Button btnFetch;
    TextView resultTv;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEd = findViewById(R.id.city_et);
        resultTv = findViewById(R.id.result_tv);
        btnFetch = findViewById(R.id.btn_ftc);

        btnFetch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ftc){

             cityName = cityEd.getText().toString();
            try {
                getData( );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }
    public void getData( ) throws MalformedURLException {
        Uri uri = Uri.parse("https//datausa.io/api/data?drilldowns=State&measures=Population&year=latest")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);

    }
    class DOTask extends AsyncTask<URL,Void,String>{


        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data =null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);

            } catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public void parseJson(String data) throws JSONException {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);

            }catch (JSONException e){
                e.printStackTrace();
            }


            JSONArray cityArray = jsonObject.getJSONArray("data");


            for (int i=0; i<cityArray.length(); i++){
                JSONObject cityo = cityArray.getJSONObject(i);
                String cityn = cityo.get("state").toString();
                if (cityn.equals(cityName)) {
                    String population = cityo.get("population").toString();
                    resultTv.setText(population);
                    break;
                }
                else {
                    resultTv.setText("Not found");

                }


            }

        }
    }


}