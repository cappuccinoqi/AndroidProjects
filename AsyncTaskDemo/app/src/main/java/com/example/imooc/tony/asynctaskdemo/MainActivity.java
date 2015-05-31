package com.example.imooc.tony.asynctaskdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private ListView mListView;
    private static String URL= "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.lv_main);
        new MyAsyncTask().execute(URL);
    }


    private List<Bean> getJsonData(String url) {
        List<Bean> beanList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            Log.d("aaa",jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return beanList;
    }
    private String readStream(InputStream is){
        InputStreamReader isReader;
        String result = "";
        try {
            String line ="";
            isReader = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isReader);
            while((line = br.readLine())!=null){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    class MyAsyncTask extends AsyncTask<String ,Void,List<Bean>>{

        @Override
        protected List<Bean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }
    }
}
