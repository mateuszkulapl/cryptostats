package com.cryptowear.cryptostats;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.ImageView;

        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URL;

public class BitmapFromUrl extends AsyncTask<String, Void, Bitmap> {

    Bitmap bitmap;
    public Bitmap BitmapFromUrl(String inURL) {
        try {
            URL url = new URL(inURL);
            Bitmap image = BitmapFactory.decodeStream(url.openStream());
            return image;
        } catch(IOException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return null;
    }
}