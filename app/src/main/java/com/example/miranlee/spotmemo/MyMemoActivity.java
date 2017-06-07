package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by miran lee on 2017-05-14.
 */

public class MyMemoActivity extends ActionBarActivity {

    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymemo);
        setTitle("내가 남긴 메모");

        listView = (ListView)findViewById(R.id.mlistView);

        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            ArrayList<String> fName = new ArrayList<>();
            File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Documents/SpotMemo/Voice");
            ArrayAdapter<String> filelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fName);
            if (files.listFiles().length > 0){
                for (File file:files.listFiles()){
                    fName.add(file.getName());
                }
            }
            files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Documents/SpotMemo/Text");
            if (files.listFiles().length > 0){
                for (File file:files.listFiles()){
                    fName.add(file.getName());
                }
            }
            files = null;
            listView.setAdapter(filelist);
        }
    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }
}
