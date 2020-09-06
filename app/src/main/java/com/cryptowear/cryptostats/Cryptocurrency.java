package com.cryptowear.cryptostats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    ImageView imageV;
    Bitmap bitmap;

    public Cryptocurrency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
        this.quantity = 0.0;
        this.price = null;
        this.lastSync = null;
        this.imageUrl=null;
        this.imageV=null;
        this.bitmap=null;
    }

    public Cryptocurrency(String name, String symbol, String imageUrl) {
        this.name = name;
        this.symbol = symbol;
        this.quantity = 0.0;
        this.price = null;
        this.lastSync = null;
        this.imageUrl=imageUrl;
        this.imageV=null;
        this.bitmap=null;

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

    public Date getLastSync() {
        return lastSync;
    }

   public Double getOwnValue() {
       if(price==null)
           return null;
       else {
           final double val = price * quantity;
           return val;
       }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ImageView getImageV() {
        return imageV;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setImageV(ImageView imageV) {
        this.imageV = imageV;

        this.bitmap=imageV.getDrawingCache();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap=bitmap;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {

            //uncomment below line in image name have spaces.
            //src = src.replaceAll(" ", "%20");

            URL url = new URL(src);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            Bitmap myBitmap=null;
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            Log.d("Bitmapa",myBitmap.toString());
            return myBitmap;
        } catch (Exception e) {

            return null;
        }
    }



}
