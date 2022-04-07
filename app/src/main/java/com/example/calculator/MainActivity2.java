package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    StringBuilder strTree = new StringBuilder();
    private ListView logTview;
    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        logTview = (ListView) findViewById(R.id.logTv);
        list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                android.R.layout.simple_list_item_1,
                list
        );
        assyncRecursion task = new assyncRecursion();
        task.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private class assyncRecursion extends AsyncTask<Void, Void, Void> {
        private void recursiveCall(String path, String indent) {
            File[] fileList = new File(path).listFiles();
            if(fileList != null)for (File file : fileList) {
                if (file.isDirectory()) {
                    strTree = new StringBuilder();

                    strTree.append(indent).append(file.getName()).append("\n");
                    list.add(strTree.toString());

                    recursiveCall(file.getAbsolutePath(), indent + "-- ");
                }
            }
        }

        //Метод выполняется в отдельном потоке, нет доступа к UI
        @Override
        protected Void doInBackground(Void... params) {
            recursiveCall(Environment.getExternalStorageDirectory().toString(), "");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,list);
            logTview.setAdapter(adapter);
            super.onPostExecute(result);
        }

    }
}