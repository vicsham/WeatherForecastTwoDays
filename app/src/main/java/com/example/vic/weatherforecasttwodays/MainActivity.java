package com.example.vic.weatherforecasttwodays;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
   // private ListView lvTomorrow,lvAfterTomorrow;
    public static String LOG_TAG = "JSON_result";

    String cityUrl="Vitoria-Gasteiz";
    String landUrl="es";
    String apiId="e25c9e1eb33eefc821749053b8257ae8";


    TextView textCity;
    private TextView textTomorrow,textTempTomorrow,textDescriptionTomorrow,textIdTomorrow;
    private TextView textAfterTomorrow,textTempAfterTomorrow,textDescriptionAfterTomorrow,textIdAfterTomorrow;
    private ImageView imageTomorrow,imageAfterTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetDatosWeather().execute();
    }

    private class GetDatosWeather extends AsyncTask<Object, Object, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(Object... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //url de ciudad elegido:
            String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+cityUrl+",%20"+landUrl+"&mode=json&appid="+apiId+"&units=metric&lang=es&cnt=2";

            //datos recibidos:
            String jsonStr = sh.makeServiceCall(url);
            // выводим целиком полученную json-строку
            // Log.d(LOG_TAG, jsonStr);

            Log.e(TAG, "Response from url: " + jsonStr);

            return jsonStr;

        }



        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String jsonStr=result;
            String  nameCity="";
            String tempDayTomorrow="";
            String descriptionTomorrow="";
            String weatherIdTomorrow="";
            String pressureTomorrow="";
            String cod="";

            if (jsonStr != null){
                try {
                    JSONObject jsonDataGeneral = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray list = jsonDataGeneral.getJSONArray("list");

                    JSONObject listTomorrow=list.getJSONObject(0);
                    JSONObject listAfterTomorrow=list.getJSONObject(1);

                    JSONObject tempTomorrow=listTomorrow.getJSONObject("temp");
                     tempDayTomorrow=tempTomorrow.getString("day");

                    JSONArray weatherTomorrow=listTomorrow.getJSONArray("weather");

                    JSONObject weatherOfTomorrow=weatherTomorrow.getJSONObject(0);

                   descriptionTomorrow = weatherOfTomorrow.getString("description");
                    weatherIdTomorrow = weatherOfTomorrow.getString("id");

                    JSONObject city = jsonDataGeneral.getJSONObject("city");
                    nameCity=city.getString("name");

                    cod=jsonDataGeneral.getString("cod");

                    pressureTomorrow=listTomorrow.getString("pressure");

                }catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            textCity=(TextView) findViewById(R.id.textCity);

            textTomorrow=(TextView)findViewById(R.id.textTomorrow) ;
            textAfterTomorrow=(TextView)findViewById(R.id.textAfterTomorrow) ;
            textTempTomorrow=(TextView)findViewById(R.id.textTempTomorrow);
            textTempAfterTomorrow =(TextView)findViewById(R.id.textTempAfterTomorrow);
            textDescriptionTomorrow =(TextView)findViewById(R.id.textDescriptionTomorrow);
            textDescriptionAfterTomorrow=(TextView)findViewById(R.id.textDescriptionAfterTomorrow);
            textIdTomorrow =(TextView)findViewById(R.id.textIdTomorrow);
            textIdAfterTomorrow =(TextView)findViewById(R.id.textIdAfterTomorrow);
            imageTomorrow=(ImageView)findViewById(R.id.imageTomorrow);
            imageAfterTomorrow=(ImageView)findViewById(R.id.imageAfterTomorrow);

            textCity.setText(nameCity);
            textTempTomorrow.setText(tempDayTomorrow);
            textDescriptionTomorrow.setText(descriptionTomorrow);
            textIdTomorrow.setText(weatherIdTomorrow);

        }

    }

}
