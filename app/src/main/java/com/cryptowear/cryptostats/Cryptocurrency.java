package com.cryptowear.cryptostats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Cryptocurrency {
    private String name;
    private String symbol;//skrot
    private Double quantity;//posiadana liczba
    private Double price;
    private String lastSync;//ostatnia aktualizacja
    private String imageUrl;
    private String unit;//symbol waluty na ktora jest przeliczane
    private Double percentChange;
    private Integer id;
    private Double marketCap;

    public Cryptocurrency(String name, String symbol, Double marketCap) {
        this.name = name;
        this.symbol = symbol;
        this.quantity = 0.0;
        this.price = null;
        this.lastSync = null;
        this.imageUrl=null;
        this.marketCap=marketCap;
        this.unit="$";
        this.id=null;
    }

    public String getName() {
        return name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getPrice() {
        return price;
    }

   public Double getOwnValue() {
       if(price==null)
           return null;
       else {
           return price * quantity;
       }
    }

    public String getImageUrl() {
        if(this.getId()!=null)
            return "https://s2.coinmarketcap.com/static/img/coins/64x64/" + this.getId() + ".png";
        else
            return null;
    }

    public String getUnit() {
        return unit;
    }

    public void setQuantity(Double quantity) {
        if(quantity>=0)
        {
            this.quantity = quantity;
        }
    }

    public void setPrice(Double exchangeRate, String lastSync, Double percentChange) {
        this.price=exchangeRate;
        this.lastSync=lastSync;
        this.percentChange=percentChange;

    }

    public Double getPercentChange() {
        return percentChange;
    }

    public void setId(Integer id)
    {
        this.id=id;
    }

    public Integer getId(){return id;}

    public void updateName(String name) {
        this.name=name;
    }

    public static Comparator<Cryptocurrency> quantityComparator = new Comparator<Cryptocurrency>() {
        @Override
        public int compare(Cryptocurrency c1, Cryptocurrency c2) {
            return c2.getQuantity().compareTo(c1.getQuantity());
        }
    };
    public static Comparator<Cryptocurrency> marketCapComparator = new Comparator<Cryptocurrency>() {
        @Override
        public int compare(Cryptocurrency c1, Cryptocurrency c2) {
            return c2.getMarketCap().compareTo(c1.getMarketCap());
        }
    };

    public static Comparator<Cryptocurrency> valueComparator = new Comparator<Cryptocurrency>() {
        @Override
        public int compare(Cryptocurrency c1, Cryptocurrency c2) {
            return (c2.getOwnValue()).compareTo(c1.getOwnValue());
        }
    };

    public static Comparator<Cryptocurrency> nameComparator = new Comparator<Cryptocurrency>() {
        @Override
        public int compare(Cryptocurrency c1, Cryptocurrency c2) {
            return (c1.getName().toLowerCase()).compareTo(c2.getName().toLowerCase());
        }
    };

    private Double getMarketCap() {
        return this.marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap=marketCap;
    }
}

