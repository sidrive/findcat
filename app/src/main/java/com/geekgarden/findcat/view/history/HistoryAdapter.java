package com.geekgarden.findcat.view.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekgarden.findcat.R;
import com.geekgarden.findcat.utils.DateUtils;
import com.geekgarden.findcat.utils.ImageUtils;
import com.geekgarden.findcat.view.product.Product;

import java.util.List;

/**
 * Created by rioswarawan on 4/19/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.InfoProductViewHolder> {

    private Context context;
    private List<Product> products;
    private LayoutInflater inflater;
    private OnAdapterListener adapterListener;

    public HistoryAdapter(Context context, List<Product> products, OnAdapterListener adapterListener) {
        this.context = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.adapterListener = adapterListener;
    }

    @Override
    public InfoProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new InfoProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoProductViewHolder holder, int position) {
        Product product = products.get(position);
        ((TextView) holder.itemView.findViewById(R.id.text_name)).setText(product.name);
        ((TextView) holder.itemView.findViewById(R.id.text_date)).setText(DateUtils.getCurrentDate());

        ImageUtils.loadImage(context, product.image, (ImageView) holder.itemView.findViewById(R.id.img_featured_image));

        holder.itemView.findViewById(R.id.layout_item).setOnClickListener(view -> {
            adapterListener.onVideoClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class InfoProductViewHolder extends RecyclerView.ViewHolder {

        public InfoProductViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnAdapterListener {
        void onVideoClicked(int position);
    }
}