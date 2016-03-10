package com.example.lcoll_000.calanderapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Environment;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.example.lcoll_000.calanderapp.CreateDateFragment.*;


public class MainActivity extends AppCompatActivity {

    public class Event{
        int hour;
        int seconds;
        int month;
        int day;
        int year;
        String Title;

        public Event(int ihour, int iseconds, int imonth, int iday, int iyear, String title) {
            hour = ihour;
            seconds = iseconds;
            month = imonth;
            day = iday;
            year = iyear;
            Title = title;
        }

    }

    public ArrayList<Event> eventList = new ArrayList<>();

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File root = new File(Environment.getExternalStorageDirectory(), "Dates");
        File file = new File(root, "dates.txt");
        if (file == null){
            createFile();
        }else{
            loadDatesFromFile();
        }

        fragmentManager = getFragmentManager();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, CalenderFragment.newInstance(eventList, "null") )
                .addToBackStack(null)
                .commit();
       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, CreateDateFragment.newInstance("null","null") )
                .addToBackStack(null)
                .commit();
    }

    private void loadDatesFromFile(){
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Dates");
            File file = new File(root, "dates.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String inLine = br.readLine();
            while( br.readLine() != null){
                String[] event = inLine.split(".");
                Event readEvent = new Event(Integer.parseInt(event[1]), Integer.parseInt(event[2]), Integer.parseInt(event[3]), Integer.parseInt(event[4]), Integer.parseInt(event[5]), event[0]);
                eventList.add(readEvent);
                inLine = br.readLine();
            }
            //loop for reading dates

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDatesToFile(){
        try {
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Dates");
            File file = new File(root, "/dates.txt");
            FileOutputStream fow = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fow);

            for (int i = 0; i < eventList.size(); i++){
                writer.write(eventList.get(i).Title + "." + eventList.get(i).hour + "." + eventList.get(i).seconds + "." + eventList.get(i).day + "." + eventList.get(i).month + "." + eventList.get(i).year);
                writer.write("/n");
            }
            writer.close();
            //Write loops to store dates

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFile(){

        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Dates");
        root.mkdirs();

        File file = new File(root, "/dates.txt");
    }

    public void createDate(int hour, int seconds, int month, int day, int year, String title){
        Event newEvent = new Event(hour, seconds, month, day, year, title);
        eventList.add(newEvent);
        writeDatesToFile();
    }
}
