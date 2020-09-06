// show The Image in a ImageView
package com.cryptowear.cryptostats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Bitmap bitmap;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        Log.d("pobrało ", urldisplay);
        Log.d("ikona", mIcon11.toString());
        this.bitmap=mIcon11;
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        this.bitmap=result;
        if(result!=null)
            try {
                bmImage.setImageBitmap(result);
                this.bitmap=result;
            }
        catch(java.lang.NullPointerException e)
        {
            Log.e("Error bmImage", e.getMessage());
        }
        else {
            bmImage = null;
            Log.d("bitmap is null", "");
        }
        bitmap=result;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}