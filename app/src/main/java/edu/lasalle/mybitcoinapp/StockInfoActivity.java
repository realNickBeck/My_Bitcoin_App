package edu.lasalle.mybitcoinapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockInfoActivity extends AppCompatActivity {
    

    @Override
    public void onCreate (@Nullable Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.stock_info_activity);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
               finish();
            }
        });
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("stock_symbol") && getIntent().hasExtra("stock_name")){
            String symbol = getIntent().getStringExtra("stock_symbol");
            String name = getIntent().getStringExtra("stock_name");
        }
    }


    private void setInfo(String symbol) {

        TextView symbol1 = findViewById(R.id.stockSymbol);
        symbol1.setText(symbol);
    }

}
