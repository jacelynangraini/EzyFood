package com.jacelyn.ezyfood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SnacksMenuDetails extends AppCompatActivity {
    public static final String EXTRA_ID="snackID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail_layout);

        int snackID = (Integer) getIntent().getExtras().get(EXTRA_ID);

        //Create a cursor
        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
        try {
            SQLiteDatabase db = EzyFoodDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("MENU",
                    new String[] {"NAME", "IMG", "PRICE", "RESTAURANT_ID", "STOCK"},
                    "_id = ?",
                    new String[] {Integer.toString(snackID)},
                    null, null,null);

            int stockText=0;
            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                TextView qtyBox = (TextView)findViewById(R.id.qty);

                //Get the drink details from the cursor
                String nameText = cursor.getString(0);
                int imgId = cursor.getInt(1);
                int priceText = cursor.getInt(2);
//                int restId = cursor.getInt(5);
                stockText = cursor.getInt(4);

                //Populate the drink name
                TextView name = (TextView)findViewById(R.id.orderDate);
                name.setText(nameText);
//
                //Populate the drink price
                TextView price = (TextView)findViewById(R.id.orderConfirmed);
                price.setText(""+priceText);
//
                //Populate the drink image
                ImageView photo = (ImageView)findViewById(R.id.orderImg);
                photo.setImageResource(imgId);
                photo.setContentDescription(nameText);

                //Populate the drink stock
                TextView stock = (TextView)findViewById(R.id.itemStock);
                stock.setText(""+stockText+" available");
//
                if(stockText==0){
                    qtyBox.setText("0");
                    Button btn = (Button) findViewById(R.id.confirmBtn);
                    btn.setBackgroundResource(R.drawable.rounded_gray);


                    btn.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Toast toast = Toast.makeText(SnacksMenuDetails.this, "Sorry, the item is out of stock!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
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
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_order:
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                intent = new Intent(this, TransactionHistory.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onOrderBtnClicked(View view){
        final int drinkID = (Integer)getIntent().getExtras().get(EXTRA_ID);
        final int drinkQty;
        TextView qtyBox = (TextView)findViewById(R.id.qty);

        //Create a cursor
        final SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
        try {
            final SQLiteDatabase db = EzyFoodDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("MENU",
                    new String[] {"NAME", "IMG", "PRICE", "RESTAURANT_ID", "STOCK"},
                    "_id = ?",
                    new String[] {Integer.toString(drinkID)},
                    null, null,null);


            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //Get the drink details from the cursor
                final String nameText = cursor.getString(0);
                final int imgId = cursor.getInt(1);
                final int priceText = cursor.getInt(2);
                final int restId = cursor.getInt(3);
                int stockText = cursor.getInt(4);
                drinkQty = Integer.parseInt((String) qtyBox.getText());

                // Insert order to cart
                Cursor cursor2 = db.query("CART",
                        new String[]{"_id", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "QTY", "ORDER_PRICE"},
                        null, null, null, null, null);

                cursor2.moveToFirst();

                // If cart empty
                if (cursor2.getCount() == 0) {
                    ((EzyFoodDatabaseHelper) EzyFoodDatabaseHelper).insertOrder(db, drinkID, nameText, imgId, priceText, restId, drinkQty);
                    Intent intent = new Intent(this, SnacksMenu.class);
                    intent.putExtra(SnacksMenu.EXTRA_REST_ID, restId);
                    startActivity(intent);
                }
                // If cart is not empty
                else {
                    int restID_exists = cursor2.getInt(4);

                    // If restaurant changed
                    if (restId != restID_exists) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Want to change restaurants?");
                        builder.setMessage("No problem! But we have to clear your current cart first.");
                        builder.setPositiveButton("Okay, sure!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // empty cart
                                db.delete("CART", null, null);
                                // insert order
                                ((EzyFoodDatabaseHelper) EzyFoodDatabaseHelper).insertOrder(db, drinkID, nameText, imgId, priceText, restId, drinkQty);

                                Toast.makeText(SnacksMenuDetails.this, "Cart emptied", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SnacksMenuDetails.this, SnacksMenu.class);
                                intent.putExtra(SnacksMenu.EXTRA_REST_ID, restId);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    // If restaurant not changed
                    else {
                        ((EzyFoodDatabaseHelper) EzyFoodDatabaseHelper).insertOrder(db, drinkID, nameText, imgId, priceText, restId, drinkQty);

                        Intent intent = new Intent(this, SnacksMenu.class);
                        intent.putExtra(DrinksMenu.EXTRA_REST_ID, restId);
                        startActivity(intent);
                    }
                    cursor2.close();

                }
            }
            cursor.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onLess(View view){
        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);

        TextView qtyBox = (TextView)findViewById(R.id.qty);
        int drinkQty = Integer.parseInt((String) qtyBox.getText());
        int drinkID = (Integer)getIntent().getExtras().get(EXTRA_ID);
        final SQLiteDatabase db = EzyFoodDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.query ("MENU",
                new String[] {"NAME", "IMG", "PRICE", "RESTAURANT_ID", "STOCK"},
                "_id = ?",
                new String[] {Integer.toString(drinkID)},
                null, null,null);

        int stockText = 0;
        //Move to the first record in the Cursor
        if (cursor.moveToFirst()) {
            //Get the drink details from the cursor
            stockText = cursor.getInt(4);
        }

        if(stockText==0){
            qtyBox.setText("0");
        }
        else if(drinkQty>1){
            qtyBox.setText(""+(drinkQty-1));
        }
    }

    public void onMore(View view){
        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);

        TextView qtyBox = (TextView)findViewById(R.id.qty);
        int drinkQty = Integer.parseInt((String) qtyBox.getText());
        int drinkID = (Integer)getIntent().getExtras().get(EXTRA_ID);

        final SQLiteDatabase db = EzyFoodDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query ("MENU",
                new String[] {"NAME", "IMG", "PRICE", "RESTAURANT_ID", "STOCK"},
                "_id = ?",
                new String[] {Integer.toString(drinkID)},
                null, null,null);

        int stockText = 0;
        //Move to the first record in the Cursor
        if (cursor.moveToFirst()) {
            //Get the drink details from the cursor
            stockText = cursor.getInt(4);
        }

        if(drinkQty==stockText){
            qtyBox.setText(""+(drinkQty));

        }
        else if(stockText==0){
            qtyBox.setText("0");
        }
        else {
            qtyBox.setText(""+(drinkQty+1));
        }
    }

    public void onClickMore(View view){
        Intent intent = new Intent(this, SnacksMenu.class);
        startActivity(intent);
    }
}