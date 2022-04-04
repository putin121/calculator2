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

    //строка для хранения списка директорий
    StringBuilder strTree = new StringBuilder();

    //поле для вывода результатов работы программы на экран
    private ListView logTview;
    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logTview = (ListView) findViewById(R.id.logTv);
        list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                android.R.layout.simple_list_item_1,
                list
        );

        //Создаем AsyncTask для работы с рекурсией
        assyncRecursion task = new assyncRecursion();
        //Запускаем AsyncTask
        task.execute();
    }

    //Класс для выполнения тяжелых задач в отдельном потоке и передача в UI-поток результатов работы.
    @SuppressLint("StaticFieldLeak")
    private class assyncRecursion extends AsyncTask<Void, Void, Void> {

        /*Рекурсивный метод формирует текст со списком директорий
        String path - путь к директории
        String indent - строка, иллюстрирующая глубину вложения директории (длина ветки дерева директорий),
        за каждый уровень добавляем --
        */
        private void recursiveCall(String path, String indent) {
            //получаем список файлов и директорий внутри текущей директории
            File[] fileList = new File(path).listFiles();
            if(fileList != null)for (File file : fileList) {
                if (file.isDirectory()) {
                    strTree = new StringBuilder();

                    strTree.append(indent).append(file.getName()).append("\n");
                    list.add(strTree.toString());
                    ArrayAdapter<String> a = (ArrayAdapter<String>) logTview.getAdapter();
                    //рекурсивный вызов для каждой директории из списка
                    recursiveCall(file.getAbsolutePath(), indent + "-- ");
                }
            }
        }

        //Метод выполняется в отдельном потоке, нет доступа к UI
        @Override
        protected Void doInBackground(Void... params) {
            //вызов метода с корневой папкой внешней памяти, глубина 0
            recursiveCall(Environment.getExternalStorageDirectory().toString(), "");
            return null;
        }

        //Метод выполняется после doInBackground, есть доступ к UI
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //отображаем сформированный текст со списком директорий
        }

    }
}