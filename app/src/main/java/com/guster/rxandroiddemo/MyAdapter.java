package com.guster.rxandroiddemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gusterwoei on 7/21/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final Context context;
    private List<ListItem> data = new ArrayList<>();

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = getItem(position);

        holder.textView.setText(listItem.getText());

        switch(listItem.getType()) {
            case ListItem.TYPE_SYSTEM:
                holder.root.setGravity(Gravity.LEFT);
                holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_EEEEEE));
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.textView.setGravity(Gravity.LEFT);
                break;
            case ListItem.TYPE_USER:
                holder.root.setGravity(Gravity.RIGHT);
                holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                //holder.textView.setGravity(Gravity.RIGHT);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ListItem getItem(int position) {
        return data.get(position);
    }

    public int addItem(ListItem listItem) {
        data.add(listItem);
        return data.size() - 1;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.lyt_root);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
