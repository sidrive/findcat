package id.findcat.app.view.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.findcat.app.R;
import id.findcat.app.database.entity.ProductHistory;
import id.findcat.app.utils.ImageUtils;

import java.util.List;

/**
 * Created by rioswarawan on 4/19/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.InfoProductViewHolder> {

    private Context context;
    private List<ProductHistory> products;
    private LayoutInflater inflater;
    private OnAdapterListener adapterListener;

    public HistoryAdapter(Context context, List<ProductHistory> products, OnAdapterListener adapterListener) {
        this.context = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.adapterListener = adapterListener;
    }

    @Override
    public InfoProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new InfoProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoProductViewHolder holder, int position) {
        ProductHistory product = products.get(position);
        /*((TextView) holder.itemView.findViewById(R.id.text_name)).setText(product.name);
        ((TextView) holder.itemView.findViewById(R.id.text_date)).setText(product.createdAt);

        ImageUtils.loadImageRotate(context, product.image, (ImageView) holder.itemView.findViewById(R.id.img_featured_image));*/
        ((TextView) holder.itemView.findViewById(R.id.text_name)).setText(product.name);
        ((TextView) holder.itemView.findViewById(R.id.text_description)).setText(Html.fromHtml(product.description));

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