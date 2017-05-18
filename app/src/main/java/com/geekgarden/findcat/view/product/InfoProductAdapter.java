package com.geekgarden.findcat.view.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekgarden.findcat.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by rioswarawan on 4/19/17.
 */

public class InfoProductAdapter extends RecyclerView.Adapter<InfoProductAdapter.InfoProductViewHolder> {

    private Context context;
    private List<Product> products;
    private LayoutInflater inflater;
    private OnAdapterListener adapterListener;

    public InfoProductAdapter(Context context, List<Product> products, OnAdapterListener adapterListener) {
        this.context = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.adapterListener = adapterListener;
    }

    @Override
    public InfoProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product_video, parent, false);
        return new InfoProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoProductViewHolder holder, int position) {
        Product product = products.get(position);
        ((TextView) holder.itemView.findViewById(R.id.text_description)).setText(
                Html.fromHtml(String.format(Locale.US, "<b>%s</b>", product.name) + ". " + product.description));
        ((ImageView) holder.itemView.findViewById(R.id.img_featured_image)).setImageDrawable(context.getResources().getDrawable(product.sample));

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