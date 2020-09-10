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
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Cryptocurrency {
    private String name;
    private String symbol;//skrot
    private Double quantity;//posiadana liczba
    private Double price;
    private String lastSync;//ostatnia aktualizacja//todo: change to Date
    private String imageUrl;
    private String unit;//symbol waluty na ktora jest przeliczane
    private Double percentChange;
    private Integer id;

    public Cryptocurrency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
        this.quantity = 0.0;
        this.price = null;
        this.lastSync = null;
        this.imageUrl=null;
        this.unit="$";
        this.id=null;
    }

    public Cryptocurrency(String name, String symbol, String imageUrl) {
        this.name = name;
        this.symbol = symbol;
        this.quantity = 0.0;
        this.price = null;
        this.lastSync = null;
        this.imageUrl=imageUrl;
        this.unit="$";
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
        return imageUrl;
    }

    public void setPrice(Double exchangeRate) {
        this.price=exchangeRate;
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

    ////////////////
    public void setId(Integer id)
    {
        this.id=id;
    }
    public Integer getId(){return id;}
    /////////////////
}

