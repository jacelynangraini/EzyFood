package com.jacelyn.ezyfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private SQLiteDatabase db;
    private Cursor cursor;
    public static final String EXTRA_REST_ID="restID";
    int restID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views and variables
        Bundle bundle = getIntent().getExtras();
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner
        spinner.setOnItemSelectedListener(this);

        // Add spinner data
        List<String> labels = new ArrayList<String>();
        labels.add("Central Park");
        labels.add("PIK Avenue");
        labels.add("Grand Indonesia");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if (bundle != null)
        {
            restID = (Integer)getIntent().getExtras().get(EXTRA_REST_ID);
            spinner.setSelection(restID);
        }

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        int restId = spinner.getSelectedItemPosition();
        switch (item.getItemId()) {
            case R.id.action_create_order:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                intent = new Intent(this, TransactionHistory.class);
                startActivity(intent);
                return true;
            case R.id.action_location:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(MapsActivity.EXTRA_REST_ID, restId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDrinksBtnClicked(View view){
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        int restId = spinner.getSelectedItemPosition();

        Intent intent = new Intent(this, DrinksMenu.class);
        intent.putExtra(DrinksMenu.EXTRA_REST_ID, restId+1);
        startActivity(intent);
    }

    public void onFoodBtnClicked(View view){
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        int restId = spinner.getSelectedItemPosition();

        Intent intent = new Intent(this, FoodMenu.class);
        intent.putExtra(FoodMenu.EXTRA_REST_ID, restId+1);
        startActivity(intent);
    }

    public void onSnacksBtnClicked(View view){
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        int restId = spinner.getSelectedItemPosition();

        Intent intent = new Intent(this, SnacksMenu.class);
        intent.putExtra(SnacksMenu.EXTRA_REST_ID, restId+1);
        startActivity(intent);
    }

    public void onTopupBtnClicked(View view){
        Intent intent = new Intent(this, TopupMenu.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            setContentView(R.layout.activity_home_screen);
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }

}