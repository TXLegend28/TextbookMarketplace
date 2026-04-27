package com.example.textbookmarketplace.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.model.Textbook;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextbookAdapter extends RecyclerView.Adapter<TextbookAdapter.ViewHolder> implements Filterable {
    private List<Textbook> textbooks;
    private List<Textbook> fullList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Textbook t);
        void onDetailsClick(Textbook t);
    }

    public TextbookAdapter(List<Textbook> list, OnItemClickListener listener) {
        this.textbooks = new ArrayList<>(list);
        this.fullList = new ArrayList<>(list);
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textbook, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Textbook t = textbooks.get(pos);
        h.tvTitle.setText(t.getTitle());
        h.tvAuthor.setText(t.getAuthor());
        h.tvPrice.setText(String.format("$%.2f", t.getPrice()));
        h.tvCopies.setText(t.getCopies() + " copies");

        if (t.getLocalImagePath() != null && !t.getLocalImagePath().isEmpty()) {
            Picasso.get().load(new File(t.getLocalImagePath())).placeholder(R.drawable.outline_book_24).into(h.ivCover);
        } else {
            h.ivCover.setImageResource(R.drawable.outline_book_24);
        }

        if (t.getDigitalFileType() != null) {
            h.tvFileType.setVisibility(View.VISIBLE);
            h.tvFileType.setText(t.getDigitalFileType().toUpperCase());
        } else {
            h.tvFileType.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> listener.onItemClick(t));
        h.btnDetails.setOnClickListener(v -> listener.onDetailsClick(t));
    }

    @Override public int getItemCount() { return textbooks.size(); }

    public void updateList(List<Textbook> list) {
        this.textbooks = new ArrayList<>(list);
        this.fullList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @Override public Filter getFilter() { return filter; }

    private Filter filter = new Filter() {
        @Override protected FilterResults performFiltering(CharSequence c) {
            List<Textbook> filtered = new ArrayList<>();
            if (c == null || c.length() == 0) {
                filtered.addAll(fullList);
            } else {
                String p = c.toString().toLowerCase().trim();
                for (Textbook t : fullList) {
                    if (t.getTitle().toLowerCase().contains(p) ||
                            t.getAuthor().toLowerCase().contains(p) ||
                            t.getSellerName().toLowerCase().contains(p)) filtered.add(t);
                }
            }
            FilterResults r = new FilterResults();
            r.values = filtered;
            return r;
        }
        @Override protected void publishResults(CharSequence c, FilterResults r) {
            textbooks.clear();
            textbooks.addAll((List<Textbook>) r.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivCover;
        TextView tvTitle, tvAuthor, tvPrice, tvCopies, tvFileType;
        ImageButton btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_book_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCopies = itemView.findViewById(R.id.tv_copies);
            tvFileType = itemView.findViewById(R.id.tv_file_type);
            btnDetails = itemView.findViewById(R.id.btn_details);
        }
    }
}