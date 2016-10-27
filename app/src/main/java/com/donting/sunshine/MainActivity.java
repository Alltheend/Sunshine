package com.donting.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appsee.Appsee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    private ListView listWeather;
    private ArrayList<String> weatherDataStr = new ArrayList<>();
    private WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadWeatherData();

//        Intent intent = new Intent(this, MyAccessibilityService.class);
//        startService(intent);

        Appsee.start("2b05c3c4ce1c4f819039d8b8eab766c1");

    }

    private void initView() {
        listWeather = (ListView) findViewById(R.id.list_weather);
        weatherAdapter = new WeatherAdapter(this, weatherDataStr);
        listWeather.setAdapter(weatherAdapter);
    }

    private class WeatherAdapter extends BaseAdapter {

        private ArrayList<String> forecastData;
        private Context context;
        private LayoutInflater layoutInflater;

        public WeatherAdapter(Context context, ArrayList<String> forecastData) {
            this.context = context;
            this.forecastData = forecastData;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return forecastData.size();
        }

        @Override
        public Object getItem(int i) {
            return forecastData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.list_forecast_item, null);
            }
            TextView txtWeather = (TextView) view.findViewById(R.id.txt_weather_info);
            String weatherData = this.forecastData.get(i);
            txtWeather.setText(weatherData);

            return view;
        }
    }

    private void loadWeatherData() {
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String forecastJsonStr = null;
                String url = "http://api.openweathermap.org/data/2.5/weather?q=Beijing&appid=73f11aea2b0aca392084c6351c905b5a";
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();

                    if (inputStream == null) {
                        forecastJsonStr = null;
                    }
                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer buffer = new StringBuffer();
                    String line;
                    while ((line = bufferReader.readLine()) != null) {
                        buffer.append(line);
                    }

                    if (buffer.length() == 0) {
                        forecastJsonStr = null;
                    }

                    forecastJsonStr = buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return forecastJsonStr;
            }

            @Override
            protected void onPostExecute(String forecastJsonStr) {
                if (forecastJsonStr != null) {
                    String forecastData = getWeatherInfo(forecastJsonStr);
                    if (!TextUtils.isEmpty(forecastData)) {
                        refreshAdapter(forecastData);
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void refreshAdapter(String weatherData) {
        weatherDataStr.add(weatherData);
        weatherAdapter.notifyDataSetChanged();
    }

    private String getWeatherInfo(String forecastJsonStr) {
        try {
            JSONObject jsonForecastData = new JSONObject(forecastJsonStr);
            if (!jsonForecastData.isNull("weather")) {
                JSONArray jsonArray = jsonForecastData.getJSONArray("weather");
                int len = jsonArray.length();
                StringBuffer forecastData = new StringBuffer();
                for (int i=0; i<len; i++) {
                    JSONObject jsonWeather = jsonArray.getJSONObject(i);
                    if (!jsonWeather.isNull("main")) {
                        String main = jsonWeather.getString("main");
                        forecastData.append(main);
                        forecastData.append("\n");
                    }
                    if (!jsonWeather.isNull("description")) {
                        String description = jsonWeather.getString("description");
                        forecastData.append(description);
                        forecastData.append("\n");
                    }
                }

                return forecastData.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
