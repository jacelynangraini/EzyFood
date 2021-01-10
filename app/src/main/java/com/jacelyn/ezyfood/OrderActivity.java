package com.jacelyn.ezyfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    List<Integer> wallet = DataHolder.getInstance().wallet;
    int totalPrice = 0;
    private SQLiteDatabase db;
    private Cursor cursor;
    com.google.android.gms.location.FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        TextView total = (TextView)findViewById(R.id.totalPrice);
        TextView totalText = (TextView)findViewById(R.id.textView7);
        TextView rp = (TextView)findViewById(R.id.textView5);
        TextView clearCart = (TextView)findViewById(R.id.clearCart);
        ImageView clearCartBtn = (ImageView) findViewById(R.id.clearBtn);
        View payBtn = findViewById(R.id.payBtn);
        View topUpBtn = findViewById(R.id.topUpBtn);
        View border = findViewById(R.id.borderView);
        TextView test = (TextView)findViewById(R.id.wallet);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);

        // Refresh
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(OrderActivity.this, OrderActivity.class);
                startActivity(intent);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

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
            Toast toast2 = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast2.show();
        }

        Cursor cursor = db.query ("CART",
                new String[] {"_id", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "QTY"},
                null, null, null, null,null);

        if(wallet.size()!=0){
            test.setText("Rp"+wallet.get(0));
        }
        else{

            test.setText("Rp0");
        }

        TextView noOrder = (TextView)findViewById(R.id.noOrder);

        if(cursor.getCount()==0){
            noOrder.setText("No orders!\nAdd orders by adding items from menu to cart \uD83D\uDED2!");
            payBtn.setVisibility(View.GONE);
            topUpBtn.setVisibility(View.GONE);
            border.setVisibility(View.GONE);
            rp.setVisibility(View.GONE);
            clearCart.setVisibility(View.GONE);
            clearCartBtn.setVisibility(View.GONE);
            total.setText("");
            totalText.setText("");
        }
        else{
            noOrder.setVisibility(View.GONE);
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(OrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //WHEN GRANTED
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize location
                    Location location = task.getResult();
                    if(location!=null){
                        try {
                            //initialize geocoder
                            Geocoder geo = new Geocoder(OrderActivity.this, Locale.getDefault());
                            //initialize address list
                            List<Address> addresses = geo.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);

                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();

                            String address = addresses.get(0).getAddressLine(0);
                            TextView userAddress = (TextView) findViewById(R.id.userAddress);
                            userAddress.setText(Html.fromHtml(
                                    address
                            ));
                            userAddress.setMovementMethod(new ScrollingMovementMethod());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else {
            //WHEN DENIED
            ActivityCompat.requestPermissions(OrderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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

    public void onClickRefresh(View v){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pay, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pay_now:

                SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
                try {
                    db = EzyFoodDatabaseHelper.getReadableDatabase();
                    Cursor cursor = db.query ("CART",
                            new String[] {"_id", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "QTY"},
                            null, null, null, null,null);
                    if (cursor.getCount()==0){
                        Toast.makeText(this, "Please make an order before proceeding to payment!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (wallet.size() != 0) {
                            if (wallet.get(0) < totalPrice) {
                                Toast.makeText(this, "Wallet amount not enough. Please top up!", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                                builder.setTitle("Order Confirmation");
                                builder.setMessage("Are you ready to pay?");
                                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        wallet.set(0, wallet.get(0) - totalPrice);
                                        Toast.makeText(OrderActivity.this, "Order confirmed!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OrderActivity.this, PaymentDetail.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                        else{
                            Toast.makeText(this, "Wallet amount not enough. Please top up!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (SQLiteException e){
                    Toast toast2 = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
                    toast2.show();
                }
                return true;
            case R.id.action_refresh:
                Intent intentR = getIntent();
                finish();
                startActivity(intentR);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickTopUp(View view){
        Intent intent = new Intent(this, TopupMenu.class);
        startActivity(intent);
    }

    public void onConfirmPayment(View view) {
        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
        try {
            db = EzyFoodDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("CART",
                    new String[]{"_id", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "QTY"},
                    null, null, null, null, null);
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Please make an order before proceeding to payment!", Toast.LENGTH_SHORT).show();
            } else {
                if (wallet.size() != 0) {
                    if (wallet.get(0) < totalPrice) {
                        Toast.makeText(this, "Wallet amount not enough. Please top up!", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                        builder.setTitle("Order Confirmation");
                        builder.setMessage("Are you ready to pay?");
                        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                wallet.set(0, wallet.get(0) - totalPrice);
                                Toast.makeText(OrderActivity.this, "Order confirmed!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OrderActivity.this, PaymentDetail.class);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                } else {
                    Toast.makeText(this, "Wallet amount not enough. Please top up!", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (SQLiteException e) {
            Toast toast2 = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast2.show();
        }
    }

    public void onChooseLoc(View view){
        Toast toast2 = Toast.makeText(this, "Change location", Toast.LENGTH_SHORT);
        toast2.show();
        Intent intent = new Intent(this, MapsActivity.class);
//        intent.putExtra(MapsActivity.EXTRA_REST_ID, );
        startActivity(intent);
    }

    public void onDeleteItem(View view){
        SQLiteOpenHelper EzyFoodDatabaseHelper = new EzyFoodDatabaseHelper(this);
        db = EzyFoodDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.query ("CART",
                new String[] {"_id", "NAME", "IMG", "PRICE", "RESTAURANT_ID", "QTY", "ORDER_PRICE"},
                null, null, null, null,null);

        db.delete("CART", null, null);

        Toast toast2 = Toast.makeText(this, "Item(s) deleted!", Toast.LENGTH_SHORT);
        toast2.show();


        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }


}