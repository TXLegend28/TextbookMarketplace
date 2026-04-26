package com.example.textbookmarketplace.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.TextbookAdapter;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.viewmodel.TextbookViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextbookViewModel textbookViewModel;
    private TextbookAdapter adapter;
    private DrawerLayout drawerLayout;
    private TextView tvEmpty;
    private EditText etSearch;
    private FloatingActionButton fabAdd;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupNavigationDrawer();
        setupViews();
        setupViewModel();
        setupRecyclerView();
        setupSearch();
        setupBackPressHandling();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Textbook Marketplace");
        }
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            return handleNavigationItemSelected(item);
        });
    }

    private void setupViews() {
        etSearch = findViewById(R.id.et_search);
        tvEmpty = findViewById(R.id.tv_empty);
        fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTextbookActivity.class);
            startActivity(intent);
        });
    }

    private void setupViewModel() {
        textbookViewModel = new ViewModelProvider(this).get(TextbookViewModel.class);
        textbookViewModel.getAllTextbooks().observe(this, textbooks -> {
            if (textbooks != null && adapter != null) {
                adapter.updateList(textbooks);
                toggleEmptyView(textbooks.isEmpty());
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_textbooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TextbookAdapter(new ArrayList<>(), new TextbookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Textbook textbook) {
                showTextbookDetails(textbook);
            }

            @Override
            public void onDetailsClick(Textbook textbook) {
                showTextbookDetails(textbook);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Modern way to handle back press using OnBackPressedCallback
     */
    private void setupBackPressHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // If drawer is closed, proceed with normal back press behavior
                    this.setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    this.setEnabled(true);
                }
            }
        });
    }

    private void toggleEmptyView(boolean isEmpty) {
        if (isEmpty) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showTextbookDetails(Textbook textbook) {
        if (textbook == null) return;

        new MaterialAlertDialogBuilder(this)
                .setTitle(textbook.getTitle())
                .setMessage(
                        "Author: " + textbook.getAuthor() + "\n" +
                                "ISBN: " + textbook.getIsbn() + "\n" +
                                "Edition: " + textbook.getEdition() + "\n" +
                                "Course: " + textbook.getCourse() + "\n" +
                                "Condition: " + textbook.getCondition() + "\n" +
                                "Price: $" + String.format("%.2f", textbook.getPrice()) + "\n" +
                                "Available Copies: " + textbook.getCopies() + "\n\n" +
                                "Seller: " + textbook.getSellerName() + "\n" +
                                "Email: " + textbook.getSellerEmail() + "\n" +
                                "Bank: " + textbook.getBankName() + "\n" +
                                "Account: " + textbook.getAccountNumber() + "\n\n" +
                                "Description: " + textbook.getDescription()
                )
                .setPositiveButton("Close", null)
                .setNegativeButton("Contact Seller", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{textbook.getSellerEmail()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding: " + textbook.getTitle());
                    startActivity(Intent.createChooser(intent, "Send Email"));
                })
                .show();
    }

    private boolean handleNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            return true;
        } else if (id == R.id.nav_add_book) {
            startActivity(new Intent(this, AddTextbookActivity.class));
            return true;
        } else if (id == R.id.nav_my_listings) {
            Snackbar.make(findViewById(android.R.id.content),
                    "My Listings feature coming soon!",
                    Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.nav_settings) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("About")
                    .setMessage("Textbook Marketplace v1.0\n\n" +
                            "A platform for students to buy and sell textbooks.\n\n" +
                            "Developed for CSC313 Assignment 2")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}