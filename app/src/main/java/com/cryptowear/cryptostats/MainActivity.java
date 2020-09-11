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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {
    List<Cryptocurrency> cryptocurrencies = new ArrayList<>();
    String apiKey = "";//coimarketcap api key
    final int limit = 100;//number of cryptocurrency to download from api, default sort by market capitaliaztion
    private CryptoRecyclerViewAdapter.RecyclerViewClickListener listener;
    RecyclerView recyclerView;
    CryptoRecyclerViewAdapter adapter;
    TextView summaryValue;
    Boolean isUpdatingSummary=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setOnClickListener();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.adapter = new CryptoRecyclerViewAdapter(this, cryptocurrencies, listener);
        getCryptoMetadata(apiKey, limit);

        this.recyclerView = findViewById(R.id.cryptoRV);
        this.summaryValue = findViewById(R.id.walletValue);
        this.recyclerView.setHasFixedSize(true);
        //ustawiamy LayoutManagera
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter.setClickListener(this);//not implemented
        this.recyclerView.setAdapter(this.adapter);
    }


    private void setOnClickListener() {
        listener = new CryptoRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent changeQuantityIntent = new Intent(getApplicationContext(), ChangeQuantity.class);
                changeQuantityIntent.putExtra("position", position);
                changeQuantityIntent.putExtra("symbol", valueOf(cryptocurrencies.get(position).getSymbol()));
                changeQuantityIntent.putExtra("name", valueOf(cryptocurrencies.get(position).getName()));
                changeQuantityIntent.putExtra("quantity", valueOf(cryptocurrencies.get(position).getQuantity()));
                startActivityForResult(changeQuantityIntent, 1);
            }
        };
    }

    private void updateSummary(double totalValue) {
        if(isUpdatingSummary==false) {//pritection against ConcurrentModificationException
            this.isUpdatingSummary=true;
            String newValueText = "$" +String.format("%.2f", totalValue);
            this.summaryValue.setText(newValueText);
            this.isUpdatingSummary=false;
        }
    }
    private Double getTotalVelue(){
        double totalValue = 0.0;
        for (Cryptocurrency crypto : this.cryptocurrencies) {
            totalValue += crypto.getOwnValue();
        }
        return totalValue;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Double newQuantity = data.getDoubleExtra("newQuantity", 0);
                Toast.makeText(this, newQuantity.toString(), Toast.LENGTH_SHORT).show();
                int position = data.getIntExtra("position", -1);
                if (position >= 0 && position < cryptocurrencies.size()) {
                    cryptocurrencies.get(position).setQuantity(newQuantity);
                    this.adapter.notifyDataSetChanged();
                    updateSummary(getTotalVelue());
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
        inflanter.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
                refreshExchangeRate();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            case R.id.sortByMarketCap:
                sortData(Cryptocurrency.marketCapComparator);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            case R.id.sortByQuantity:
                sortData(Cryptocurrency.quantityComparator);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            case R.id.sortByName:
                sortData(Cryptocurrency.nameComparator);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            case R.id.sortByValue:
                sortData(Cryptocurrency.valueComparator);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void refreshExchangeRate() {
        getCryptoMetadata(apiKey, limit);

    }

    private void getCryptoMetadata(final String apiKey, final int limit) {
        Log.d("getCryptoMetadata", "getCryptoMetadata");
        if (limit > 0) {
            HashMap<String, String> queries = new HashMap<String, String>();
            queries.put("sort", "market_cap");
            queries.put("sort_dir", "desc");
            queries.put("aux", "");
            queries.put("limit", valueOf(limit));
            final String queryParams = getQueryFromMap(queries);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL apiEndpoint = new URL("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest" + queryParams);
                        HttpURLConnection apiConnection = (HttpURLConnection) apiEndpoint.openConnection();
                        apiConnection.setRequestProperty("Accept", "application/json");
                        apiConnection.setRequestProperty("X-CMC_PRO_API_KEY", apiKey);
                        int apiCode = apiConnection.getResponseCode();
                        if (apiCode == 200) {

                            BufferedReader br = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));

                            //cały obiekt jest w jednej linii
                            JSONObject jsonObject = new JSONObject(br.readLine());

                            JSONObject status = jsonObject.getJSONObject("status");
                            Log.d("status len", valueOf(status.length()));
                            apiConnection.disconnect();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                String name = data.getJSONObject(i).getString("name");
                                String symbol = data.getJSONObject(i).getString("symbol");
                                Double price = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").getDouble("price");
                                String lastSync = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").getString("last_updated");
                                Double percentChange = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h");
                                Double marketCap = data.getJSONObject(i).getJSONObject("quote").getJSONObject("USD").getDouble("market_cap");
                                Integer id = data.getJSONObject(i).getInt("id");

                                Cryptocurrency tempCryptocurrency = new Cryptocurrency(name, symbol,marketCap);
                                int indexOfNewInArray = getIndex(tempCryptocurrency);
                                if (indexOfNewInArray == -1) {//dodanie kryptowaluty do listy
                                    tempCryptocurrency.setPrice(price, lastSync, percentChange);
                                    tempCryptocurrency.setId(id);
                                    cryptocurrencies.add(tempCryptocurrency);
                                    // aktualizacja listy
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged(); updateSummary(getTotalVelue());

                                        }
                                    });

                                } else {//akyualizacja danych
                                    if (indexOfNewInArray >= 0) {
                                        cryptocurrencies.get(indexOfNewInArray).setPrice(price, lastSync, percentChange);
                                        cryptocurrencies.get(indexOfNewInArray).setMarketCap(marketCap);
                                        // aktualizacja listy
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                                updateSummary(getTotalVelue());
                                            }
                                        });
                                        Log.d("update", cryptocurrencies.get(indexOfNewInArray).getName());
                                    } else {
                                        Log.d("error", "not found");
                                    }
                                }
                            }
                        } else {
                            Log.d("error:", getResponseFromResponseCode(apiCode));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private String getQueryFromMap(HashMap<String, String> queries) {
        String urlParams = "";
        if (queries.size() > 0)
            urlParams += "?";
        Iterator<Map.Entry<String, String>> it = queries.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            urlParams += pair.getKey() + "=" + pair.getValue();
            if (it.hasNext()) {
                urlParams += "&";
            }
        }
        return urlParams;

    }

    private String getResponseFromResponseCode(int code) {
        HashMap<Integer, String> responses = new HashMap<Integer, String>();
        responses.put(400, "Bad Request");
        responses.put(401, "Unauthorized");
        responses.put(403, "Forbidden");
        responses.put(429, "Too Many Requests");
        responses.put(500, "Internal Server Error");
        return responses.getOrDefault(code, "another error");
    }

    private int getIndex(Cryptocurrency crypto) {
        String name = crypto.getName();
        String symbol = crypto.getSymbol();
        int i = 0;
        for (Cryptocurrency cryptoFromArr : this.cryptocurrencies) {
            if (cryptoFromArr.getSymbol().equals(symbol)) {
                if (!(cryptoFromArr.getName().equals(name)))//updating name of crypto based on symbol
                {
                    cryptoFromArr.updateName(name);
                }
                return i;
            }
            i += 1;
        }
        return -1;
    }

    private void sortData(Comparator comparator) {
        Collections.sort(cryptocurrencies, comparator);
    }

}