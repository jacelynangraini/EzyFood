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
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionHistory extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
        ListView listOrders = (android.widget.ListView) findViewById(R.id.list_trans_headers);
        TextView noTrans = (TextView) findViewById(R.id.noTrans);

        try {
            db = EzyFoodDatabaseHelper.getReadableDatabase();

            Cursor cursor = db.query ("HISTORY",
                    new String[] {"_id", "TRANSACTION_DATE","ADDRESS", "MENU_ID", "NAME","IMG", "PRICE", "RESTAURANT_ID", "QTY","ORDER_PRICE"},
                    null, null, null, null,null);

            if(cursor.getCount()==0){
                noTrans.setText("No Transaction History!");
            }
            else{
                noTrans.setVisibility(View.GONE);

                SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                        R.layout.activity_transaction_header_list,
                        cursor,
                        new String[]{"TRANSACTION_DATE", "NAME", "IMG", "ORDER_PRICE", "QTY", "PRICE"},
                        new int[]{R.id.transactionDate, R.id.itemName, R.id.itemImg, R.id.itemSubtotal, R.id.itemQty, R.id.itemPrice},
                        0);
            listOrders.setAdapter(listAdapter);
            }
        }
        catch (SQLiteException e){
            Toast toast2 = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast2.show();
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
}