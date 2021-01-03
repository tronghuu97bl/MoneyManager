package com.tth.moneymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tth.moneymanager.Model.Item;
import com.tth.moneymanager.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> listItem = new ArrayList<>();
    private DialogFragment dialogFragment;
    private GetItem getItem;

    public interface GetItem {
        void OnGettingItemResult(Item item);
    }

    public ItemAdapter(DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
    }

    public void setListItem(List<Item> list) {
        this.listItem = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(listItem.get(position).getName());
        //holder.image
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem = (GetItem) dialogFragment;
                getItem.OnGettingItemResult(listItem.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listItem.size() > 0) {
            return listItem.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView name;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardviewItem);
            name = itemView.findViewById(R.id.item_item_name);
            imageView = itemView.findViewById(R.id.item_item_img);
        }
    }
}
