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
    String name;
    String symbol;//skrot
    Double quantity;//posiadana liczba
    Double price;
    Date lastSync;//ostatnia aktualizacja
    String imageUrl;
    String unit;//symbol waluty na ktora jest przeliczane

    public Cryptocurrency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
        this.quantity = 0.0;
        this.price = null;
        this.lastSync = null;
        this.imageUrl=null;
        this.unit="";
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

    public void setPrice(double exchangeRate) {
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
}
