package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadJSON extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                String result="";
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (Exception e){
                TextView first=(TextView)findViewById(R.id.textView2);
                TextView second=(TextView)findViewById(R.id.textView3);
                TextView third=(TextView)findViewById(R.id.textView4);
                TextView fourth=(TextView)findViewById(R.id.textView5);
                TextView fifth=(TextView)findViewById(R.id.textView6);
                first.setText("Could not load!!");
                second.setText("Some suggestions: ");
                third.setText("Please try a proper city name.");
                fourth.setText("Please check your internet connection.");
                fifth.setText("Please try later if nothing works.");
                e.printStackTrace();
                return null;
            }
        }
    }

    public void getWeather(View view)
    {
        TextView first=(TextView)findViewById(R.id.textView2);
        TextView second=(TextView)findViewById(R.id.textView3);
        TextView third=(TextView)findViewById(R.id.textView4);
        TextView fourth=(TextView)findViewById(R.id.textView5);
        TextView fifth=(TextView)findViewById(R.id.textView6);
        EditText city=(EditText)findViewById(R.id.editTextTextPersonName);
        DownloadJSON obj=new DownloadJSON();
        String url="http://api.openweathermap.org/data/2.5/weather?q="+city.getText().toString()+"&APPID=388a333540c8da57f46f3b05c2359edb";
        String weather;
        try{
            weather=obj.execute(url).get();
            System.out.println(weather);
            JSONObject jsonObject=new JSONObject(weather);
            String weatherinfo=jsonObject.getString("weather");
            String temp=jsonObject.getString("main");
            String cityName=jsonObject.getString("name");
            System.out.println(weatherinfo);
            System.out.println(temp);
            System.out.println(cityName);
            JSONArray arr1=new JSONArray(weatherinfo);
            for(int i=0;i<arr1.length();i++)
            {
                JSONObject jsonPart=arr1.getJSONObject(i);
                first.setText(jsonPart.getString("main")+": "+jsonPart.getString("description")+"\r\n");
            }
            JSONObject jsonPart=new JSONObject(temp);
            second.setText("Temperature: "+((int)Double.parseDouble(jsonPart.getString("temp"))-273)+" C");
            System.out.println(jsonPart.toString());
            third.setText("Feels-Like: "+((int)Double.parseDouble(jsonPart.getString("feels_like"))-273)+" C");
            fourth.setText("Temp-min: "+((int)Double.parseDouble(jsonPart.getString("temp_min"))-273)+" C, Temp-max: "+((int)Double.parseDouble(jsonPart.getString("temp_max"))-273)+" C");
            fifth.setText("City: "+cityName);
            InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(city.getWindowToken(),0);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}