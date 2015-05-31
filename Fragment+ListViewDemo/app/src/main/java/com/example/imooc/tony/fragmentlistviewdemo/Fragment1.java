package com.example.imooc.tony.fragmentlistviewdemo;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Created by Tony on 2015/5/30.
 */
public class Fragment1 extends ListFragment {
    private String Tag = Fragment1.class.getName();
    private ListView listView;
    private SimpleAdapter simpleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,container,false);
        listView = (ListView) view.findViewById(R.id.fragment1);
        Log.i(Tag," ------------onCreateView");
        return  view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String [] list = {"class1","class2","class3","class4","class5","class6","class7"};


    }

    private void getData(String[] list) {
    }
}
