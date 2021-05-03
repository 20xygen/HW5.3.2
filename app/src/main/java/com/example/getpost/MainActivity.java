package com.example.getpost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView lastnameF;
    String answerHTTP;
    String lastnameS, firstnameS;
    Boolean isFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastnameF = (TextView) findViewById(R.id.lastnameF);
    }

    public void sendPOST(View view) {
        EditText lastname = (EditText) findViewById(R.id.lastname);
        EditText firstname = (EditText) findViewById(R.id.firstname);
        lastnameS = lastname.getText().toString();
        firstnameS = firstname.getText().toString();
        new MyAsyncTask().execute("");
    }

    public void sendGET(View view){
        EditText lastname = (EditText) findViewById(R.id.lastname);
        EditText firstname = (EditText) findViewById(R.id.firstname);
        lastnameS = lastname.getText().toString();
        firstnameS = firstname.getText().toString();
        new MySecondAsyncTask().execute("");
    }

    class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            isFailed = false;
            // Создаем HttpClient и Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.72.3:8080");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("lastname", lastnameS));
                nameValuePairs.add(new BasicNameValuePair("firstname", firstnameS));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                // Выполняем HTTP Post запрос
                HttpResponse response = httpclient.execute(httppost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    System.out.println("It's ok");
                    System.out.println(entity);
                    answerHTTP = EntityUtils.toString(entity);
                }
                else {
                    System.out.println("something goes wrong");
                }
            } catch (ClientProtocolException e) {
                System.out.println("First exception");
                isFailed = true;
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Second exception");
                isFailed = true;
                e.printStackTrace();
                // TODO Auto-generated catch block
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Post ended", Toast.LENGTH_SHORT);
            toast.show();
            if (isFailed){
                lastnameF.setText("Что-то пошло не так...");
            }
            else {
                lastnameF.setText(answerHTTP);
            }

        }
    }

    class MySecondAsyncTask extends AsyncTask<String, String, String> {
        BufferedReader in = null;

        @Override
        protected String doInBackground(String... params) {
            isFailed = false;
            // Создаем HttpClient и Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://192.168.72.3:8080");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("lastname", lastnameS));
                nameValuePairs.add(new BasicNameValuePair("firstname", firstnameS));
//                httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                // Выполняем HTTP Post запрос
                HttpResponse response = httpclient.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 200) {
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
                    answerHTTP = in.readLine();
                }
                else {
                    System.out.println("something goes wrong");
                }
            } catch (ClientProtocolException e) {
                System.out.println("First exception");
                isFailed = true;
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Second exception");
                isFailed = true;
                e.printStackTrace();
                // TODO Auto-generated catch block
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Get ended", Toast.LENGTH_SHORT);
            toast.show();
            if (isFailed){
                lastnameF.setText("Что-то пошло не так...");
            }
            else {
                lastnameF.setText(answerHTTP);
            }

        }
    }
}