package com.cryptowear.cryptostats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChangeQuantity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_quantity);
        TextView SymbolText = findViewById(R.id.SymbolText);
        Button BackButton = (Button)findViewById(R.id.BackButton);
        Button SaveButton = (Button)findViewById(R.id.SaveButton);

        BackButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }));

        //NIE DZIALA PRZYPISANIE NOWEJ WARTOSCI
        SaveButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText t = findViewById(R.id.CryptoQuantity);
                String input = t.getText().toString();
                ((TextView)findViewById(R.id.own_number)).setText(input);
                Log.d("info",input);
                finish();
            }
        }));

        String symbol="Symbol not set";

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            symbol=extras.getString("symbol");
        }
        SymbolText.setText(symbol);
    }
}
