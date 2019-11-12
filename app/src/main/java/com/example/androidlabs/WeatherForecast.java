package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ProgressBar progress = findViewById(R.id.bar);
        progress.setVisibility(View.VISIBLE);


        TextView temp = findViewById(R.id.currentTemp);
        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute();


    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>  {
        String windUV;
        String minTemp;
        String maxTemp;
        String currentTemp;
        String iconName;
        Bitmap pic;


        @Override
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String queryUV = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("temperature"))
                            {
                                currentTemp = xpp.getAttributeValue(null, "value"); //What is the String associated with message?
                                publishProgress(25);

                                minTemp = xpp.getAttributeValue(null, "min");
                                publishProgress(50);

                                maxTemp = xpp.getAttributeValue(null, "max");
                                publishProgress(75);

                            } else if (tagName.equals("weather")){
                                iconName = xpp.getAttributeValue (null, "icon");

                                Bitmap image = null;
                                String urlString = "http://openweathermap.org/img/w/" + iconName + ".png";

                                if (fileExistance(iconName + ".png" ) ){
                                    URL imageURL = new URL(urlString);
                                    HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        image = BitmapFactory.decodeStream(connection.getInputStream());
                                        FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);

                                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

                                        outputStream.flush();
                                        outputStream.close();
                                        pic = image;
                                    }

                                } else {
                                    FileInputStream fis = null;
                                    try {fis = openFileInput(iconName + ".png");   }
                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                                    Bitmap bm = BitmapFactory.decodeStream(fis);
                                    pic = bm;
                                    Log.i("image", "the image is found locally");
                                }
                                publishProgress(100);
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}
            //What is returned here will be passed as a parameter to onPostExecute:

            try {
                URL UVURL = new URL(queryUV);
                HttpURLConnection urlConnection = (HttpURLConnection) UVURL.openConnection();
                InputStream inStream = urlConnection.getInputStream();


                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 5);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);
                double value = jObject.getDouble("value");
                windUV = Double.toString(value);


            } catch (MalformedURLException e) {
                ret = "Malformed URL exception";
            } catch (IOException e) {
                ret = "IO Exception: WIFI not connected";
            } catch (JSONException e) {
                ret = "JSON exception";
            }

            return ret;
        }


        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }




        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar progress = findViewById(R.id.bar);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(values[0]);
            //Update GUI stuff only:

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView max = findViewById(R.id.maxTemp);
            max.setText("Maximum Temperature is " + this.maxTemp + " Celsius");

            TextView min = findViewById(R.id.minTemp);
            min.setText("Minimum Temperature is " + this.minTemp + " Celsius");

            TextView current = findViewById(R.id.currentTemp);
            current.setText("Current Temperature is " + this.currentTemp + " Celsius");

            ImageView image = findViewById(R.id.weatherImageView);
            image.setImageBitmap(pic);

            TextView uvLevel = (TextView)findViewById(R.id.uvRating);
            uvLevel.setText("UV Rating" + windUV);
            ProgressBar progress = findViewById(R.id.bar);
            progress.setVisibility(View.INVISIBLE);

        }
    }
}
