package com.example.textbookmarketplace.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.model.Textbook;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextbookAdapter extends RecyclerView.Adapter<TextbookAdapter.ViewHolder> implements Filterable {
    private List<Textbook> textbooks;
    private List<Textbook> textbooksFull;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Textbook textbook);
        void onDetailsClick(Textbook textbook);
    }

    public TextbookAdapter(List<Textbook> textbooks, OnItemClickListener listener) {
        this.textbooks = textbooks;
        this.textbooksFull = new ArrayList<>(textbooks);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_textbook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Textbook textbook = textbooks.get(position);
        holder.tvTitle.setText(textbook.getTitle());
        holder.tvAuthor.setText(textbook.getAuthor());
        holder.tvPrice.setText(String.format("$%.2f", textbook.getPrice()));
        holder.tvCopies.setText(textbook.getCopies() + " copies available");

        holder.itemView.setOnClickListener(v -> listener.onItemClick(textbook));
        holder.btnDetails.setOnClickListener(v -> listener.onDetailsClick(textbook));
    }

    @Override
    public int getItemCount() {
        return textbooks != null ? textbooks.size() : 0;
    }

    public void updateList(List<Textbook> newList) {
        this.textbooks = newList;
        this.textbooksFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return textbookFilter;
    }

    private Filter textbookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Textbook> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(textbooksFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Textbook textbook : textbooksFull) {
                    if (textbook.getTitle().toLowerCase().contains(filterPattern) ||
                            textbook.getSellerName().toLowerCase().contains(filterPattern) ||
                            textbook.getAuthor().toLowerCase().contains(filterPattern)) {
                        filteredList.add(textbook);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            textbooks.clear();
            textbooks.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivBookCover;
        TextView tvTitle, tvAuthor, tvPrice, tvCopies;
        ImageButton btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBookCover = itemView.findViewById(R.id.iv_book_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCopies = itemView.findViewById(R.id.tv_copies);
            btnDetails = itemView.findViewById(R.id.btn_details);
        }
    }
}