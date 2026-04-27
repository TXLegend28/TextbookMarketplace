package com.example.textbookmarketplace.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class MyListingsAdapter extends RecyclerView.Adapter<MyListingsAdapter.VH> {
    private List<Textbook> list = new ArrayList<>();
    private Listener listener;

    public interface Listener {
        void onEdit(Textbook t);
        void onDelete(Textbook t);
        void onItemClick(Textbook t);
    }

    public MyListingsAdapter(Listener listener) { this.listener = listener; }

    public void setList(List<Textbook> list) {
        this.list = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_my_listing, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Textbook t = list.get(pos);
        h.tvTitle.setText(t.getTitle());
        h.tvMeta.setText(t.getAuthor() + " • " + String.format("$%.2f", t.getPrice()));
        if (t.getLocalImagePath() != null && !t.getLocalImagePath().isEmpty()) {
            Picasso.get().load(new File(t.getLocalImagePath())).placeholder(R.drawable.outline_book_24).into(h.iv);
        } else {
            h.iv.setImageResource(R.drawable.outline_book_24);
        }
        h.btnEdit.setOnClickListener(v -> listener.onEdit(t));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(t));
        h.itemView.setOnClickListener(v -> listener.onItemClick(t));
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        CircleImageView iv;
        TextView tvTitle, tvMeta;
        ImageButton btnEdit, btnDelete;
        VH(View v) {
            super(v);
            iv = v.findViewById(R.id.iv_cover);
            tvTitle = v.findViewById(R.id.tv_title);
            tvMeta = v.findViewById(R.id.tv_meta);
            btnEdit = v.findViewById(R.id.btn_edit);
            btnDelete = v.findViewById(R.id.btn_delete);
        }
    }
}