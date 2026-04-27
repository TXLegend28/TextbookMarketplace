package com.example.textbookmarketplace.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.MyListingsAdapter;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.utils.SettingsManager;
import com.example.textbookmarketplace.viewmodel.TextbookViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class MyListingsActivity extends AppCompatActivity {
    private TextbookViewModel vm;
    private MyListingsAdapter adapter;
    private RecyclerView rv;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Listings");
        }

        tvEmpty = findViewById(R.id.tv_empty);
        rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        adapter = new MyListingsAdapter(new MyListingsAdapter.Listener() {
            @Override public void onEdit(Textbook t) {
                Intent i = new Intent(MyListingsActivity.this, EditTextbookActivity.class);
                i.putExtra(EditTextbookActivity.EXTRA_ID, t.getId());
                startActivity(i);
            }
            @Override public void onDelete(Textbook t) {
                new MaterialAlertDialogBuilder(MyListingsActivity.this)
                        .setTitle("Delete Listing")
                        .setMessage("Remove \"" + t.getTitle() + "\"?")
                        .setPositiveButton("Delete", (d, w) -> {
                            vm.delete(t);
                            Snackbar.make(rv, "Deleted", Snackbar.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
            @Override public void onItemClick(Textbook t) {
                Intent i = new Intent(MyListingsActivity.this, BookDetailActivity.class);
                i.putExtra("book", t);
                startActivity(i);
            }
        });
        rv.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(TextbookViewModel.class);
        String email = SettingsManager.getSellerEmail(this);
        if (SettingsManager.hasProfile(this)) {
            vm.getBySeller(email).observe(this, list -> {
                adapter.setList(list != null ? list : new ArrayList<>());
                tvEmpty.setVisibility((list == null || list.isEmpty()) ? View.VISIBLE : View.GONE);
            });
        } else {
            vm.getAllTextbooks().observe(this, list -> {
                adapter.setList(list != null ? list : new ArrayList<>());
                tvEmpty.setVisibility((list == null || list.isEmpty()) ? View.VISIBLE : View.GONE);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}