package com.cryptowear.cryptostats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Cryptocurrency> cryptocurrencies;
    String apiKey="";//coimarketcap api key
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cryptocurrencies = InitiateCrypto();



       /* for(int i = 0; i<cryptocurrencies.size(); i++)
        {
            SetExchangeRate(i, this.apiKey);
        }*/
        for(int i = 0; i<cryptocurrencies.size(); i++)
        {
            SetExchangeRate(i, this.apiKey);
            cryptocurrencies.get(i).setUnit("$");
        }


        RecyclerView recyclerView = findViewById(R.id.cryptoRV);

        recyclerView.setHasFixedSize(true);
        // ustawiamy LayoutManagera
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CryptoRecyclerViewAdapter adapter = new CryptoRecyclerViewAdapter(this, cryptocurrencies);
        //adapter.setClickListener(this);//not implemented
        recyclerView.setAdapter(adapter);


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

    List<Cryptocurrency> InitiateCrypto()
    {
        List<Cryptocurrency> cryptocurrencies= new ArrayList<>();
        Cryptocurrency tempCryptocurrency=new Cryptocurrency("Bitcoin","BTC","https://s2.coinmarketcap.com/static/img/coins/64x64/1.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Ethereum","ETH","https://s2.coinmarketcap.com/static/img/coins/64x64/1027.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Tether","USDT","https://s2.coinmarketcap.com/static/img/coins/64x64/825.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("XRP","XRP","https://s2.coinmarketcap.com/static/img/coins/64x64/52.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Chainlink","LINK","https://s2.coinmarketcap.com/static/img/coins/64x64/1975.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Polkadot","DOT","https://s2.coinmarketcap.com/static/img/coins/64x64/6636.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Binance Coin","BNB","https://s2.coinmarketcap.com/static/img/coins/64x64/1839.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Litecoin","LTC","https://s2.coinmarketcap.com/static/img/coins/64x64/2.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Bitcoin SV","BSV","https://s2.coinmarketcap.com/static/img/coins/64x64/3602.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Crypto.com Coin","CRO","https://s2.coinmarketcap.com/static/img/coins/64x64/3635.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("EOS","EOS","https://s2.coinmarketcap.com/static/img/coins/64x64/1765.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Cardano","ADA","https://s2.coinmarketcap.com/static/img/coins/64x64/2010.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("TRON","TRX","https://s2.coinmarketcap.com/static/img/coins/64x64/1958.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Tezos","XTZ","https://s2.coinmarketcap.com/static/img/coins/64x64/2011.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("USD Coin","USDC","https://s2.coinmarketcap.com/static/img/coins/64x64/3408.png"); cryptocurrencies.add(tempCryptocurrency);
        /*tempCryptocurrency=new Cryptocurrency("Stellar","XLM","https://s2.coinmarketcap.com/static/img/coins/64x64/512.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Monero","XMR","https://s2.coinmarketcap.com/static/img/coins/64x64/328.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("UNUS SED LEO","LEO","https://s2.coinmarketcap.com/static/img/coins/64x64/3957.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Neo","NEO","https://s2.coinmarketcap.com/static/img/coins/64x64/1376.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("NEM","XEM","https://s2.coinmarketcap.com/static/img/coins/64x64/873.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Cosmos","ATOM","https://s2.coinmarketcap.com/static/img/coins/64x64/3794.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Huobi Token","HT","https://s2.coinmarketcap.com/static/img/coins/64x64/2502.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("UMA","UMA","https://s2.coinmarketcap.com/static/img/coins/64x64/5617.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("IOTA","MIOTA","https://s2.coinmarketcap.com/static/img/coins/64x64/1720.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("yearn.finance","YFI","https://s2.coinmarketcap.com/static/img/coins/64x64/5864.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Aave","LEND","https://s2.coinmarketcap.com/static/img/coins/64x64/2239.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("VeChain","VET","https://s2.coinmarketcap.com/static/img/coins/64x64/3077.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Dash","DASH","https://s2.coinmarketcap.com/static/img/coins/64x64/131.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Ethereum Classic","ETC","https://s2.coinmarketcap.com/static/img/coins/64x64/1321.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Zcash","ZEC","https://s2.coinmarketcap.com/static/img/coins/64x64/1437.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("OMG Network","OMG","https://s2.coinmarketcap.com/static/img/coins/64x64/1808.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Maker","MKR","https://s2.coinmarketcap.com/static/img/coins/64x64/1518.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Synthetix Network Token","SNX","https://s2.coinmarketcap.com/static/img/coins/64x64/2586.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Ontology","ONT","https://s2.coinmarketcap.com/static/img/coins/64x64/2566.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Dai","DAI","https://s2.coinmarketcap.com/static/img/coins/64x64/4943.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Celo","CELO","https://s2.coinmarketcap.com/static/img/coins/64x64/5567.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Compound","COMP","https://s2.coinmarketcap.com/static/img/coins/64x64/5692.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("HedgeTrade","HEDG","https://s2.coinmarketcap.com/static/img/coins/64x64/3662.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("TrueUSD","TUSD","https://s2.coinmarketcap.com/static/img/coins/64x64/2563.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Algorand","ALGO","https://s2.coinmarketcap.com/static/img/coins/64x64/4030.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Basic Attention Token","BAT","https://s2.coinmarketcap.com/static/img/coins/64x64/1697.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("THETA","THETA","https://s2.coinmarketcap.com/static/img/coins/64x64/2416.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Dogecoin","DOGE","https://s2.coinmarketcap.com/static/img/coins/64x64/74.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("BitTorrent","BTT","https://s2.coinmarketcap.com/static/img/coins/64x64/3718.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("FTX Token","FTT","https://s2.coinmarketcap.com/static/img/coins/64x64/4195.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("0x","ZRX","https://s2.coinmarketcap.com/static/img/coins/64x64/1896.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("OKB","OKB","https://s2.coinmarketcap.com/static/img/coins/64x64/3897.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Kusama","KSM","https://s2.coinmarketcap.com/static/img/coins/64x64/5034.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("DigiByte","DGB","https://s2.coinmarketcap.com/static/img/coins/64x64/109.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Energy Web Token","EWT","https://s2.coinmarketcap.com/static/img/coins/64x64/5268.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Flexacoin","FXC","https://s2.coinmarketcap.com/static/img/coins/64x64/3812.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Elrond ERD","ERD","https://s2.coinmarketcap.com/static/img/coins/64x64/4086.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Kyber Network","KNC","https://s2.coinmarketcap.com/static/img/coins/64x64/1982.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Ren","REN","https://s2.coinmarketcap.com/static/img/coins/64x64/2539.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Waves","WAVES","https://s2.coinmarketcap.com/static/img/coins/64x64/1274.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("ICON","ICX","https://s2.coinmarketcap.com/static/img/coins/64x64/2099.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Paxos Standard","PAX","https://s2.coinmarketcap.com/static/img/coins/64x64/3330.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Qtum","QTUM","https://s2.coinmarketcap.com/static/img/coins/64x64/1684.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Hyperion","HYN","https://s2.coinmarketcap.com/static/img/coins/64x64/3695.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Loopring","LRC","https://s2.coinmarketcap.com/static/img/coins/64x64/1934.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Binance USD","BUSD","https://s2.coinmarketcap.com/static/img/coins/64x64/4687.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Hedera Hashgraph","HBAR","https://s2.coinmarketcap.com/static/img/coins/64x64/4642.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Band Protocol","BAND","https://s2.coinmarketcap.com/static/img/coins/64x64/4679.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Elrond","EGLD","https://s2.coinmarketcap.com/static/img/coins/64x64/6892.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("SushiSwap","SUSHI","https://s2.coinmarketcap.com/static/img/coins/64x64/6758.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Augur","REP","https://s2.coinmarketcap.com/static/img/coins/64x64/1104.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Lisk","LSK","https://s2.coinmarketcap.com/static/img/coins/64x64/1214.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Arweave","AR","https://s2.coinmarketcap.com/static/img/coins/64x64/5632.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Decred","DCR","https://s2.coinmarketcap.com/static/img/coins/64x64/1168.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Zilliqa","ZIL","https://s2.coinmarketcap.com/static/img/coins/64x64/2469.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Aragon","ANT","https://s2.coinmarketcap.com/static/img/coins/64x64/1680.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Ocean Protocol","OCEAN","https://s2.coinmarketcap.com/static/img/coins/64x64/3911.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("HUSD","HUSD","https://s2.coinmarketcap.com/static/img/coins/64x64/4779.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Ampleforth","AMPL","https://s2.coinmarketcap.com/static/img/coins/64x64/4056.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Bitcoin Gold","BTG","https://s2.coinmarketcap.com/static/img/coins/64x64/2083.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("DFI.Money","YFII","https://s2.coinmarketcap.com/static/img/coins/64x64/5957.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("BitShares","BTS","https://s2.coinmarketcap.com/static/img/coins/64x64/463.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Balancer","BAL","https://s2.coinmarketcap.com/static/img/coins/64x64/5728.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Siacoin","SC","https://s2.coinmarketcap.com/static/img/coins/64x64/1042.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("ZB Token","ZB","https://s2.coinmarketcap.com/static/img/coins/64x64/3351.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Swipe","SXP","https://s2.coinmarketcap.com/static/img/coins/64x64/4279.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Terra","LUNA","https://s2.coinmarketcap.com/static/img/coins/64x64/4172.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Enjin Coin","ENJ","https://s2.coinmarketcap.com/static/img/coins/64x64/2130.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Serum","SRM","https://s2.coinmarketcap.com/static/img/coins/64x64/6187.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("CyberVein","CVT","https://s2.coinmarketcap.com/static/img/coins/64x64/2642.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Reserve Rights","RSR","https://s2.coinmarketcap.com/static/img/coins/64x64/3964.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Ravencoin","RVN","https://s2.coinmarketcap.com/static/img/coins/64x64/2577.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("THORChain","RUNE","https://s2.coinmarketcap.com/static/img/coins/64x64/4157.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Numeraire","NMR","https://s2.coinmarketcap.com/static/img/coins/64x64/1732.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Decentraland","MANA","https://s2.coinmarketcap.com/static/img/coins/64x64/1966.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Bitcoin Diamond","BCD","https://s2.coinmarketcap.com/static/img/coins/64x64/2222.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Nano","NANO","https://s2.coinmarketcap.com/static/img/coins/64x64/1567.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Golem","GNT","https://s2.coinmarketcap.com/static/img/coins/64x64/1455.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Nervos Network","CKB","https://s2.coinmarketcap.com/static/img/coins/64x64/4948.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Storj","STORJ","https://s2.coinmarketcap.com/static/img/coins/64x64/1772.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Status","SNT","https://s2.coinmarketcap.com/static/img/coins/64x64/1759.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("MonaCoin","MONA","https://s2.coinmarketcap.com/static/img/coins/64x64/213.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Bytom","BTM","https://s2.coinmarketcap.com/static/img/coins/64x64/1866.png"); cryptocurrencies.add(tempCryptocurrency);
        tempCryptocurrency=new Cryptocurrency("Solana","SOL","https://s2.coinmarketcap.com/static/img/coins/64x64/5426.png"); cryptocurrencies.add(tempCryptocurrency);

    return cryptocurrencies;
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
            URL nbpEndpoint = new URL("https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=1&symbol="+symbol);
            Log.d(symbol, String.valueOf(nbpEndpoint));
            HttpURLConnection apiConnection = (HttpURLConnection) nbpEndpoint.openConnection();
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
                Log.d("price",Double. toString(exchangeRate));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

}