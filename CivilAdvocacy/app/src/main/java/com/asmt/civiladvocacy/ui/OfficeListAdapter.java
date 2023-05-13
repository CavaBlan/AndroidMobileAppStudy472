package com.asmt.civiladvocacy.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.asmt.civiladvocacy.R;
import com.asmt.civiladvocacy.data.entity.Office;
import com.asmt.civiladvocacy.extend.ListAdapterExtend;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OfficeListAdapter extends ListAdapter<Office, OfficeListAdapter.OfficeViewHolder> {
    private static final String TAG = "CA.OfficialListAdapter";

    private ListAdapterExtend.OnItemClickListener onItemClickListener;
    public OfficeListAdapter() {
        super(new DiffUtil.ItemCallback<Office>() {
            @Override
            public boolean areItemsTheSame(@NonNull Office oldItem, @NonNull Office newItem) {
                return Objects.equals(oldItem.name, newItem.name);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Office oldItem,
                                              @NonNull Office newItem) {
                return Objects.equals(oldItem.name, newItem.name) &&
                        Objects.equals(oldItem.getOfficial().photoUrl, newItem.getOfficial().photoUrl) &&
                        Objects.equals(oldItem.getOfficial().name, newItem.getOfficial().name) &&
                        Objects.equals(oldItem.getOfficial().party, newItem.getOfficial().party);
            }
        });
    }

    public void setOnItemClickListener(ListAdapterExtend.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Office getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public OfficeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OfficeViewHolder holder = new OfficeViewHolder(
                inflater.inflate(R.layout.official_list_item, parent, false));
        return ListAdapterExtend.addOnItemClickListener(holder, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeViewHolder holder, int position) {
        Office item = getItem(position);
        if (!TextUtils.isEmpty(item.getOfficial().photoUrl)) {
            Picasso.get().load(item.getOfficial().photoUrl)
                    .error(R.drawable.missing)
                    .into(holder.ivPhoto);
        } else {
            holder.ivPhoto.setImageResource(R.drawable.missing);
        }
        holder.tvName.setText(item.name);
        holder.tvOfficial.setText(String.format("%s (%s)",
                item.getOfficial().name, item.getOfficial().party));
    }

    static class OfficeViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPhoto;
        final TextView tvName;
        final TextView tvOfficial;

        public OfficeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvName = itemView.findViewById(R.id.tv_address);
            tvOfficial = itemView.findViewById(R.id.tv_name_and_party);
        }
    }
}
