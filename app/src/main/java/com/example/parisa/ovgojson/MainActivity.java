package com.example.parisa.ovgojson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class JsonDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try{
                URL url =  new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{

                JSONObject jsonObject = new JSONObject(s);
                //you can change the name here to get string of specific part
                String sensorInfo = jsonObject.getString("gs$cell");
                Log.i("sensor info", sensorInfo);
                JSONArray jsonArray = new JSONArray(sensorInfo);

                //goes through the json objects and logs the parts

                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    Log.i("inputValue", jsonPart.getString("inputValue"));
                    Log.i("column", jsonPart.getString("column"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }



            Log.i("JSON", s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonDownloader task = new JsonDownloader();
        task.execute("https://spreadsheets.google.com/feeds/cells/1gGyJS2phIcmiTEEUdUhOsyqekEudBue_NtNkjKsTQrQ/1/public/full?alt=json");




    }
}
