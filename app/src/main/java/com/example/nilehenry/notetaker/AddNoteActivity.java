package com.example.nilehenry.notetaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class AddNoteActivity extends AppCompatActivity {


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.saveNote:
                if (mode.equals("add")){
                    if (noteEditText.getText().toString().equals("")){
                        Toast.makeText(AddNoteActivity.this,"You did not write a note.",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (titleEditText.getText().toString().equals("")){
                            MainActivity.titleArrayList.add("Untitled");
                            MainActivity.contentArrayList.add(noteEditText.getText().toString());
                            MainActivity.noteArrayAdapter.notifyDataSetChanged();
                            MainActivity.indexChosen=MainActivity.titleArrayList.size()-1;
                    }
                    else{
                        MainActivity.titleArrayList.add(titleEditText.getText().toString());
                        MainActivity.contentArrayList.add(noteEditText.getText().toString());
                        MainActivity.noteArrayAdapter.notifyDataSetChanged();
                        MainActivity.indexChosen=MainActivity.titleArrayList.size()-1; //sets index to last index in arraylist
                    }
                    SharedPreferences sharedPreferences= sharedPreferences = this.getSharedPreferences("com.example.nilehenry.notetaker", Context.MODE_PRIVATE);
                    try {
                        sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.titleArrayList)).apply();
                        sharedPreferences.edit().putString("content",ObjectSerializer.serialize(MainActivity.contentArrayList)).apply();
                    } catch (IOException e) {
                    }
                    Toast.makeText(AddNoteActivity.this,"Note Saved!",Toast.LENGTH_SHORT).show();
                    mode="edit";
                }
                else if (mode.equals("edit")){
                    MainActivity.titleArrayList.set(MainActivity.indexChosen,titleEditText.getText().toString());
                    MainActivity.contentArrayList.set(MainActivity.indexChosen,noteEditText.getText().toString());
                    MainActivity.noteArrayAdapter.notifyDataSetChanged();
                    SharedPreferences sharedPreferences= sharedPreferences = this.getSharedPreferences("com.example.nilehenry.notetaker", Context.MODE_PRIVATE);
                    try {
                        sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.titleArrayList)).apply();
                        sharedPreferences.edit().putString("content",ObjectSerializer.serialize(MainActivity.contentArrayList)).apply();
                    } catch (IOException e) {
                    }
                    Toast.makeText(AddNoteActivity.this,"Note Saved!",Toast.LENGTH_SHORT).show();
                    mode="edit";
                    Toast.makeText(AddNoteActivity.this,"Note Saved!",Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    String mode;  //either add or edit
    EditText titleEditText;
    EditText noteEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);



        titleEditText= (EditText) findViewById(R.id.titleEditText);
        noteEditText= (EditText) findViewById(R.id.noteEditText);
        Intent intent= getIntent();
        //noteEditText.setText(intent.getStringExtra("action"));

        if (intent.getStringExtra("action").equals("add")){
            mode="add";
        }
        else{  //edit previous note
            titleEditText.setText(MainActivity.titleArrayList.get(MainActivity.indexChosen));
            noteEditText.setText(MainActivity.contentArrayList.get(MainActivity.indexChosen));
            mode="edit";
        }
    }
}
