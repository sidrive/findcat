package id.findcat_store.app.view.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.findcat_store.app.R;
import id.findcat_store.app.api.Video;
import id.findcat_store.app.utils.DateUtils;
import id.findcat_store.app.utils.ImageUtils;

import java.util.List;

/**
 * Created by rioswarawan on 4/19/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.InfoProductViewHolder> {

    private Context context;
    private List<Video.Data> videos;
    private LayoutInflater inflater;
    private OnAdapterListener adapterListener;

    public VideoAdapter(Context context, List<Video.Data> videos, OnAdapterListener adapterListener) {
        this.context = context;
        this.videos = videos;
        this.inflater = LayoutInflater.from(context);
        this.adapterListener = adapterListener;
    }

    @Override
    public InfoProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video, parent, false);
        return new InfoProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoProductViewHolder holder, int position) {
        Video.Data video = videos.get(position);
        ((TextView) holder.itemView.findViewById(R.id.text_name)).setText(video.title);
        ((TextView) holder.itemView.findViewById(R.id.text_date)).setText(DateUtils.getDateTimeStr(video.uploadedAt, "yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy hh:mm:ss"));

        ImageUtils.loadImage(context, video.thumbnails, (ImageView) holder.itemView.findViewById(R.id.img_video));

        holder.itemView.findViewById(R.id.layout_item).setOnClickListener(view -> {
            adapterListener.onVideoClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
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