package colntrev.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    ArrayList<SleepEntry> items; // This list will get the previous list view contents and reload them
    ArrayAdapter<String> adapter;

    // Thy: for database
    private SleepRecordDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        items = new ArrayList<>();




        // add items from database in "<frequency> <activity>" format
        datasource = new SleepRecordDataSource(this);
        datasource.open();
        datasource.getAllSleepEntries(items);


        populateLog();

    }


    private void populateLog(){

        ArrayList<String> list = new ArrayList<>();
        for(SleepEntry se : items){
            String entry = se.getActivity();
            if(!("".equals(entry)) && !list.contains(entry)){
                list.add(entry);
            }
        }
        adapter = new ArrayAdapter<String>(this, R.layout.items, list);
        ListView lst = (ListView) findViewById(R.id.logList);
        lst.setAdapter(adapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();
                EditText et = (EditText) findViewById(R.id.newItemAdd);
                et.setText(item);
            }
        });
    }

    public void addNewItem(View view) {
        EditText newItem = (EditText) findViewById(R.id.newItemAdd);
        String text = newItem.getText().toString();
        if(adapter.getPosition(text) == -1) {
            adapter.add(text); // adding new item to list view
        }


        // Thy: update database entry
        SharedPreferences preferences = getSharedPreferences(SetupActivity.PREFS_NAME, 0);
        long rowID = preferences.getLong(SetupActivity.PREF_KEY_ROWID, -1);
        if (rowID > -1) {
            int num = datasource.updateSleepEntry(rowID, text);
            Log.d("addNewItem", "updated for row "+rowID+" num="+num);
        }else{
            Log.d("addNewItem", "bad rowid");
        }
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        datasource.close();
        super.onDestroy();
    }
}
