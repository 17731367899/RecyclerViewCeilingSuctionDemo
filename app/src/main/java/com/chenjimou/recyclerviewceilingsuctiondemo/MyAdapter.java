package com.chenjimou.recyclerviewceilingsuctiondemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Model> models = new ArrayList<>();

    public MyAdapter(Context context, List<Model> models) {
        if (null != models && !models.isEmpty()){
            this.models.addAll(models);
        }
        mContext = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.tv.setText(models.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    /**
     * 判断当前position的item是否是组内第一个
     */
    public boolean isGroupFirstItem(int position) {
        // position为零肯定是组内第一个item
        if (position == 0){
            return true;
        } else {
            // 当前item的组名
            String currentGroupName = models.get(position).getGroupName();
            // 前一个item的组名
            String preGroupName = models.get(position - 1).getGroupName();
            return !preGroupName.equals(currentGroupName);
        }
    }

    /**
     * 获取当前position的item的组名
     */
    public String getGroupName(int position){
        return models.get(position).getGroupName();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_item);
        }
    }
}
