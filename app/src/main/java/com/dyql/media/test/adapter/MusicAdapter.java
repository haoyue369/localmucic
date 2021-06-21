package com.dyql.media.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dyql.media.test.R;
import com.dyql.media.test.bean.MusicBean;

import java.util.List;



public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{

    private Context context;
    private List<MusicBean> beans;
    //点击事件接口
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void ItemClickListener(View v, int position);
        void ItemViewClickListener(View v,int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public MusicAdapter(Context context, List<MusicBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music_list, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, final int position) {
        MusicBean bean = beans.get(position);
        holder.idTv.setText(bean.getId());
        holder.titleTv.setText(bean.getTitle());
        holder.singerMoreTv.setText(bean.getArtist()+" - "+bean.getAlbum());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.ItemClickListener(v,position);
            }
        });

        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.ItemViewClickListener(v,position);
            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        if (beans != null && beans.size() > 0) return beans.size();
        return 0;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (beans != null) {
            if (beans.size() > 0) beans.clear();
            beans = null;
        }
        if (context != null) context = null;
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        private TextView idTv,titleTv,singerMoreTv;
        private ImageView moreIv;
        private RelativeLayout mLayout;
        private MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.item_local_music_number);
            titleTv = itemView.findViewById(R.id.item_local_music_song);
            singerMoreTv = itemView.findViewById(R.id.item_local_music_singer_and_album);

            moreIv = itemView.findViewById(R.id.item_local_music_more);
            mLayout = itemView.findViewById(R.id.item_music_list_layout);
        }
    }
}
