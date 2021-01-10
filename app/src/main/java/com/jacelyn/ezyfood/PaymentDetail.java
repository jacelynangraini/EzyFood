package com.jacelyn.ezyfood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaymentDetail extends AppCompatActivity {
    List<String> nameList = DataHolder.getInstance().nameList;
    List<Integer> priceList = DataHolder.getInstance().priceList;
    List<Integer> idList = DataHolder.getInstance().idList;
    List<Integer> qtyList = DataHolder.getInstance().qtyList;
    int totalPrice = 0;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        //get views
        TextView total = (TextView)findViewById(R.id.totalPrice);
        TextView totalText = (TextView)findViewById(R.id.textView7);
        TextView orderDate = (TextView)findViewById(R.id.orderDatee);

        //read database
        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
        ListView listOrders = (android.widget.ListView) findViewById(R.id.list_orders);

        try {
            db = EzyFoodDatabaseHelper.getReadableDatabase();

            Cursor cursor = db.query ("CART",
                    new String[] {"_id", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "QTY", "ORDER_PRICE"},
                    null, null, null, null,null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                totalPrice = totalPrice+(cursor.getInt(3)*cursor.getInt(5));
                cursor.moveToNext();
            }
            total.setText(""+totalPrice);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    R.layout.activity_list_view,
                    cursor,
                    new String[]{"NAME","IMG","PRICE", "QTY","ORDER_PRICE"},
                    new int[]{R.id.itemName, R.id.itemImg, R.id.itemPrice, R.id.itemQty, R.id.itemSubtotal},
                    0);

            listOrders.setAdapter(listAdapter);
        }
        catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Cursor cursor = db.query ("CART",
                new String[] {"_id", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "QTY"},
                null, null, null, null,null);

        //get current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String curr = sdf.format(new Date());
        String address ="";
        orderDate.setText(""+ curr);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int menuid = cursor.getInt(0);
            String itemName = cursor.getString(1);
            int itemImg = cursor.getInt(2);
            int itemPrice = cursor.getInt(3);
            int itemRestId = cursor.getInt(4);
            int itemQty = cursor.getInt(5);

            ((EzyFoodDatabaseHelper) EzyFoodDatabaseHelper).insertHistory(db, curr, address, menuid, itemName, itemImg, itemPrice, itemRestId, itemQty);

            //delete cart record
            EzyFoodDatabaseHelper.getWritableDatabase().delete("CART",
                    "_id = ?",
                    new String[] {Integer.toString(menuid)});

            //update db
            Cursor cursor2 = db.query ("MENU",
                    new String[] {"NAME", "IMG", "PRICE", "RESTAURANT_ID", "STOCK"},
                    "NAME = ? AND RESTAURANT_ID = ?",
                    new String[] {itemName, Integer.toString(itemRestId)},
                    null, null,null);

            if(cursor2.moveToFirst()){
                ContentValues newValues = new ContentValues();
                newValues.put("STOCK", (cursor2.getInt(4)-itemQty));

                db.update("MENU", newValues,
                        "NAME = ? AND RESTAURANT_ID = ?",
                        new String[] {itemName, Integer.toString(itemRestId)});
            };
            cursor.moveToNext();
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
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onOK (View view){
        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(MainActivity.EXTRA_REST_ID, restId);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        nameList.clear();
        priceList.clear();
        qtyList.clear();
        idList.clear();
    }
}