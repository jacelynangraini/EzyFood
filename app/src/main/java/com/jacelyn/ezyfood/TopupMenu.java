package com.jacelyn.ezyfood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TopupMenu extends AppCompatActivity {
    List<Integer> wallet = DataHolder.getInstance().wallet;
//    public int saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_menu);

        TextView test = (TextView)findViewById(R.id.textView9);
        if(wallet.size()!=0){
            test.setText("Rp"+wallet.get(0));
        }
        else{

            test.setText("Rp0");
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
        switch (item.getItemId()) {
            case R.id.action_create_order:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onTopUp(View view){
        EditText money = (EditText)findViewById(R.id.topupAmount);
        int moneyAmount = Integer.parseInt(money.getText().toString());
        if (moneyAmount < 10000 || moneyAmount > 2000001){
            Toast.makeText(getBaseContext(), "Invalid topup amount.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(wallet.size()==0){
                wallet.add(0, moneyAmount);
                Toast.makeText(getBaseContext(), "Top up successful!", Toast.LENGTH_SHORT).show();
            }
            else{
                if(wallet.get(0)+moneyAmount>2000000){
                    Toast.makeText(getBaseContext(), "The maximum wallet balance is Rp 2.000.000,-!", Toast.LENGTH_SHORT).show();
                }
                else{
                    wallet.set(0, wallet.get(0)+moneyAmount);
                    Toast.makeText(getBaseContext(), "Top up successful!", Toast.LENGTH_SHORT).show();
                }
            }
        finish();
        }
        money.setText("");



    }
}