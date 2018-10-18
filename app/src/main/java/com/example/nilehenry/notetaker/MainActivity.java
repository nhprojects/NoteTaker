package com.example.nilehenry.notetaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ListView noteListView;
    static int indexChosen;
    static ArrayAdapter<String> noteArrayAdapter;
    static ArrayList<String> titleArrayList;
    static ArrayList<String> contentArrayList;
    static HashSet<Integer> selectedEntries;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                titleArrayList.clear();
                contentArrayList.clear();
                noteArrayAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences= sharedPreferences = this.getSharedPreferences("com.example.nilehenry.notetaker", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.titleArrayList)).apply();
                    sharedPreferences.edit().putString("content",ObjectSerializer.serialize(MainActivity.contentArrayList)).apply();
                } catch (IOException e) {
                }
                Toast.makeText(MainActivity.this,"Changes Saved!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteItem:
                ArrayList<Integer> removeList=new ArrayList<Integer>(selectedEntries);
                Collections.sort(removeList);
                if (removeList.size()!=0) {
                    for (int i = removeList.size()-1; i >=0; i = i - 1) {
                       titleArrayList.remove((int) removeList.get(i));
                       contentArrayList.remove(removeList.get(i));
                    }
                }
                noteArrayAdapter.notifyDataSetChanged();
                sharedPreferences= sharedPreferences = this.getSharedPreferences("com.example.nilehenry.notetaker", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.titleArrayList)).apply();
                    sharedPreferences.edit().putString("content",ObjectSerializer.serialize(MainActivity.contentArrayList)).apply();
                } catch (IOException e) {
                }
                Toast.makeText(MainActivity.this,"Changes Saved!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.addItem:
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                intent.putExtra("action", "add");
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteListView = (ListView) findViewById(R.id.noteListView);

        selectedEntries=new HashSet<Integer>();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.nilehenry.notetaker", Context.MODE_PRIVATE);
       //sharedPreferences.edit().clear().commit();


        try {
            titleArrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("title", ObjectSerializer.serialize(new ArrayList<String>())));
            contentArrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("content",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            titleArrayList = new ArrayList<String>();
            contentArrayList=new ArrayList<String>();
        }

        noteArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, titleArrayList);
        noteListView.setAdapter(noteArrayAdapter);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                intent.putExtra("action", "edit");
                indexChosen=position;
                startActivity(intent);
            }
        });
        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!selectedEntries.contains(position)){
                    selectedEntries.add(position);
                    view.setBackgroundColor(Color.GREEN);
                }
                else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                    selectedEntries.remove(position);
                }

                indexChosen=position;
                return true;
            }
        });



    }
}
