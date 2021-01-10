package com.jacelyn.ezyfood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FoodMenu extends AppCompatActivity {
    List<Integer> wallet = DataHolder.getInstance().wallet;
    private SQLiteDatabase db;
    private Cursor cursor;
    public static final String EXTRA_REST_ID="restID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_layout);

        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
        ListView listDrinks = (android.widget.ListView) findViewById(R.id.menuList);
        Bundle bundle = getIntent().getExtras();
        int restID = 0;
        TextView foodCategory = (TextView) findViewById(R.id.foodCategory);
        foodCategory.setText("Food");

        if (bundle != null)
        {
            restID = (Integer)getIntent().getExtras().get(EXTRA_REST_ID);
        }

        try {
            db = EzyFoodDatabaseHelper.getReadableDatabase();

            Cursor cursor = db.query ("MENU",
                    new String[] {"_id", "CATEGORY", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "STOCK"},
                    "RESTAURANT_ID = ? AND CATEGORY = ?",
                    new String[] {Integer.toString(restID), "Food"},
                    null, null,null);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    R.layout.activity_menu_list_view,
                    cursor,
                    new String[]{"NAME","IMG","PRICE"},
                    new int[]{R.id.itemName, R.id.itemImg, R.id.itemPrice},
                    0);
            listDrinks.setAdapter(listAdapter);
        }
        catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Create the listener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> listDrinks,
                                            View itemView,
                                            int position,
                                            long id) {
                        Intent intent = new Intent(FoodMenu.this,
                                FoodMenuDetails.class);
                        intent.putExtra(FoodMenuDetails.EXTRA_ID, (int) id);
                        startActivity(intent);
                    }
                };

        //Assign the listener to the list view
        listDrinks.setOnItemClickListener(itemClickListener);
        TextView walletView = (TextView)findViewById(R.id.wallet);
        if(wallet.size()==0){
            walletView.setText("Rp0");
        }
        else {
            walletView.setText("Rp" + wallet.get(0));
        }

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int restID = (Integer)getIntent().getExtras().get(EXTRA_REST_ID);

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
                intent.putExtra(MapsActivity.EXTRA_REST_ID, restID-1);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
//        cursor.close();
        db.close();
    }
}