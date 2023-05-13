package com.asmt.civiladvocacy.extend;

import android.view.View;
import android.widget.Adapter;

import androidx.recyclerview.widget.RecyclerView;

public class ListAdapterExtend {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static <VH extends RecyclerView.ViewHolder> VH addOnItemClickListener(
            VH holder, OnItemClickListener onItemClickListener) {
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
        return holder;
    }

}
