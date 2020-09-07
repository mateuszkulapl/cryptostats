package com.cryptowear.cryptostats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static java.lang.String.valueOf;

public class CryptoRecyclerViewAdapter extends RecyclerView.Adapter<CryptoRecyclerViewAdapter.ViewHolder> {
    List<Cryptocurrency> CryptocurrencyList;
    private LayoutInflater inflater;

    CryptoRecyclerViewAdapter(Context context, List<Cryptocurrency> CryptocurrencyList) {
        this.inflater = LayoutInflater.from(context);
        this.CryptocurrencyList = CryptocurrencyList;
    }
    //tworzenie nowego elementu dla recyclerview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.crypto_element, parent, false);

        return new ViewHolder(view);
    }

    //wiązanie elementu z widokiem
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cryptocurrency Cryptocurrency = CryptocurrencyList.get(position);
        holder.icon.setImageResource(android.R.color.transparent);
        holder.name.setText(Cryptocurrency.getName());
        Double actualPrice=Cryptocurrency.getPrice();
        String actualPriceOutput = actualPrice!=null ? String.format("%.14f",actualPrice) : holder.itemView.getContext().getString(R.string.no_data_yet);//sprawdzanie
        // poprawnosci danych
        // (konstruktor
        // przypisuje null)
        holder.actual_price.setText(Cryptocurrency.getUnit()+" "+actualPriceOutput);

        holder.own_number.setText(Cryptocurrency.getQuantity().toString());//delete
        Double ownValue=Cryptocurrency.getOwnValue();
        String ownValueOutput = ownValue!=null ? ownValue.toString() : holder.itemView.getContext().getString(R.string.no_data_yet);//sprawdzanie poprawnosci danych (wynosi null, gdy nie pobrano danych o kursie)
        holder.own_value.setText(ownValueOutput);

        if(Cryptocurrency.getImageUrl()!=null)//pobieranie zdjecia z url, todo:zapisanie do bazy (picasso)
        {
             holder.icon.setDrawingCacheEnabled(true);
             DownloadImageTask temp= new DownloadImageTask((ImageView) holder.icon);
             temp.execute(Cryptocurrency.getImageUrl());
        }

    }
    //liczba elementów listy
    @Override
    public int getItemCount() {
        return CryptocurrencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView actual_price;
        TextView own_number;
        TextView own_value;
        ImageView icon;
        Cryptocurrency Cryptocurrency;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            actual_price = itemView.findViewById(R.id.actual_price);
            own_number = itemView.findViewById(R.id.own_number);
            own_value = itemView.findViewById(R.id.own_value);
            icon = itemView.findViewById(R.id.icon);
        }
    }


}