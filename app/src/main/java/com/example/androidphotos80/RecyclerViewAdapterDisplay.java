package com.example.androidphotos80;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidphotos80.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterDisplay extends RecyclerView.Adapter<RecyclerViewAdapterDisplay.ViewHolder>{

    private Context context;
    private ArrayList<Tag> tagList = new ArrayList<Tag>();
    private OnTagItemClickListener tListener;
    private int selected_position = 0;
    private int position;

    public RecyclerViewAdapterDisplay(Context context, ArrayList<Tag> tagList){
        this.context = context;
        this.tagList = tagList;
        //will need to figure out tag passing
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_taglistitem, parent, false);
        RecyclerViewAdapterDisplay.ViewHolder holder = new RecyclerViewAdapterDisplay.ViewHolder(view,tListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.textView.setText(tag.toString());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag;

        public ViewHolder(@NonNull View itemView, OnTagItemClickListener tListener) {
            super(itemView);
        }
    }

    public interface OnTagItemClickListener{
        void onItemClick(int position);
    }
    public void setOnTagItemClickListener(OnTagItemClickListener listener){
        tListener = listener;
    }
}
