package com.cryptowear.cryptostats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    List<Cryptocurrency> cryptocurrencies=new ArrayList<>();
    String apiKey="36c74fa4-2214-41ee-8000-43b9a8af6237";//coimarketcap api key
    final int limit=100;//number of cryptocurrency to download from api, default sort by market capitaliaztion
    private CryptoRecyclerViewAdapter.RecyclerViewClickListener listener;
    RecyclerView recyclerView;
    CryptoRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setOnClickListener();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.adapter = new CryptoRecyclerViewAdapter(this, cryptocurrencies, listener);
        getCryptoMetadata(apiKey, limit);
        updateAllExchangeRates();

        this.recyclerView = findViewById(R.id.cryptoRV);

        this.recyclerView.setHasFixedSize(true);
        //ustawiamy LayoutManagera
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //adapter.setClickListener(this);//not implemented
        this.recyclerView.setAdapter(this.adapter);
    }

    private void updateAllExchangeRates() {
        if(this.cryptocurrencies!=null)
        for(int i = 0; i<this.cryptocurrencies.size(); i++)
        {
            SetExchangeRate(i, this.apiKey);
        }
    }

    private void setOnClickListener() {
        listener= new CryptoRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent changeQuantityIntent = new Intent(getApplicationContext(), ChangeQuantity.class);
                changeQuantityIntent.putExtra("position", position);
                changeQuantityIntent.putExtra("symbol", String.valueOf(cryptocurrencies.get(position).getSymbol()));
                changeQuantityIntent.putExtra("name", String.valueOf(cryptocurrencies.get(position).getName()));
                changeQuantityIntent.putExtra("quantity", String.valueOf(cryptocurrencies.get(position).getQuantity()));
                startActivityForResult(changeQuantityIntent,1);

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Double newQuantity = data.getDoubleExtra("newQuantity", 0);
                Toast.makeText(this,newQuantity.toString(), Toast.LENGTH_SHORT).show();
                int position=data.getIntExtra("position",-1);
                if(position>=0&&position<cryptocurrencies.size()) {
                    cryptocurrencies.get(position).setQuantity(newQuantity);
                    this.adapter.notifyDataSetChanged();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflanter = getMenuInflater();
        inflanter.inflate(R.menu.app_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                //todo wywołanie ustawień
                Toast.makeText(this,"Settings not implemented yet", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void SetExchangeRate(final int index, final String apiKey) {
        final String cryptoSymbol=cryptocurrencies.get(index).getSymbol();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                double exchangeRate = AsyncGetExhangeRate(cryptoSymbol, apiKey);
                cryptocurrencies.get(index).setPrice(exchangeRate);

            }
        });
    }
    private double AsyncGetExhangeRate(String symbol, String apiKey) {
        double exchangeRate = 0;
        try {
            URL apiEndpoint = new URL("https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=1&symbol="+symbol);

            HttpURLConnection apiConnection = (HttpURLConnection) apiEndpoint.openConnection();
            apiConnection.setRequestProperty("Accept", "application/json");
            apiConnection.setRequestProperty("X-CMC_PRO_API_KEY", apiKey);
            if (apiConnection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
                //cały obiekt jest w jednej linii
                JSONObject jsonObject = new JSONObject(br.readLine());
                JSONObject apiData = (JSONObject) jsonObject.getJSONObject("data");
                JSONObject apiDataQuote=(JSONObject) apiData.getJSONObject("quote");
                JSONObject apiDataQuoteUsd=(JSONObject) apiDataQuote.getJSONObject("USD");
                exchangeRate=apiDataQuoteUsd.getDouble("price");
                apiConnection.disconnect();
                this.adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRate;

    }

    void getCryptoMetadata(final String apiKey, final int limit) {

        if(limit>0) {
            HashMap<String, String> queries = new HashMap<String,String>();
            queries.put("sort", "market_cap");
            queries.put("sort_dir", "desc");
            queries.put("aux", "");
            queries.put("limit", String.valueOf(limit));
            final String queryParams=getQueryFromMap(queries);


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL apiEndpoint = new URL("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"+queryParams);

                        HttpURLConnection apiConnection = (HttpURLConnection) apiEndpoint.openConnection();
                        apiConnection.setRequestProperty("Accept", "application/json");
                        apiConnection.setRequestProperty("X-CMC_PRO_API_KEY", apiKey);
                        int apiCode=apiConnection.getResponseCode();
                        if (apiCode == 200) {

                            BufferedReader br = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));

                            //cały obiekt jest w jednej linii
                            JSONObject jsonObject = new JSONObject(br.readLine());

                            JSONObject status = (JSONObject) jsonObject.getJSONObject("status");
                            Log.d("status len", String.valueOf(status.length()));
                            apiConnection.disconnect();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                String name = data.getJSONObject(i).getString("name");
                                String symbol = data.getJSONObject(i).getString("symbol");
                                Double price = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").getDouble("price");
                                String lastSync = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").getString("last_updated");
                                Double percentChange = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h");
                                Integer id = data.getJSONObject(i).getInt("id");

                                Cryptocurrency tempCryptocurrency = new Cryptocurrency(name, symbol);

                                tempCryptocurrency.setPrice(price, lastSync, percentChange);
                                tempCryptocurrency.setId(id);
                                cryptocurrencies.add(tempCryptocurrency);


                                // aktualizacja listy
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                        else
                        {
                            Log.d("error:",getResponseFromResponseCode(apiCode));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public String getQueryFromMap(HashMap<String, String> queries) {
        String urlParams="";
        if(queries.size()>0)
            urlParams+="?";
        Iterator<Map.Entry<String, String>> it = queries.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry <String, String> pair = it.next();
            urlParams+=pair.getKey() + "=" + pair.getValue();
            if(it.hasNext())
            {
                urlParams += "&";
            }
        }
        return urlParams;

    }

    private String getResponseFromResponseCode(int code){
        HashMap<Integer, String> responses = new HashMap<Integer,String>();
        responses.put(400, "Bad Request");
        responses.put(401, "Unauthorized");
        responses.put(403, "Forbidden");
        responses.put(429, "Too Many Requests");
        responses.put(500, "Internal Server Error");
        return responses.getOrDefault(code,"another error");
    }

}