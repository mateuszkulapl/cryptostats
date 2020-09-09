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
    private TextView quantityInput;
    private int position;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_quantity);
        TextView symbolText = findViewById(R.id.symbolText);
        TextView nameText = findViewById(R.id.name);
        this.quantityInput=findViewById(R.id.quantityInput);
        Button backButton = (Button)findViewById(R.id.backButton);
        Button saveButton = (Button)findViewById(R.id.saveButton);


        Intent intent=getIntent();
        symbolText.setText(intent.getStringExtra("symbol"));
        nameText.setText(intent.getStringExtra("name"));
        quantityInput.setText(intent.getStringExtra("quantity"));
        position=intent.getIntExtra("position",-1);


        backButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }));



        //NIE DZIALA PRZYPISANIE NOWEJ WARTOSCI
        saveButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double quantity = Double.parseDouble(quantityInput.getText().toString());

                Intent resultIntent=new Intent();
                resultIntent.putExtra("newQuantity",quantity);
                resultIntent.putExtra("position",position);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        }));

        String symbol="Symbol not set";


    }
}
