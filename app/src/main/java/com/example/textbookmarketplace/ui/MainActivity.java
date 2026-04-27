package com.example.textbookmarketplace.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.textbookmarketplace.utils.SettingsManager;
import com.example.textbookmarketplace.viewmodel.TextbookViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextbookViewModel vm;
    private TextbookAdapter adapter;
    private DrawerLayout drawer;
    private TextView tvEmpty;
    private EditText etSearch;
    private FloatingActionButton fab;
    private RecyclerView rv;
    private MaterialButton btnWebSearch, btnSuggestWeb;
    private View emptyContainer, suggestContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupDrawer();
        setupViews();
        setupViewModel();
        setupRecyclerView();
        setupSearch();
        setupBackPress();
    }

    private void setupToolbar() {
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        ImageView logo = findViewById(R.id.iv_logo);
        logo.setImageResource(R.drawable.ic_launcher_monochrome);
    }

    private void setupDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        Toolbar tb = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            return onNavItem(item);
        });
    }

    private void setupViews() {
        etSearch = findViewById(R.id.et_search);
        tvEmpty = findViewById(R.id.tv_empty);
        fab = findViewById(R.id.fab_add);
        rv = findViewById(R.id.recycler_view_textbooks);
        emptyContainer = findViewById(R.id.empty_container);
        suggestContainer = findViewById(R.id.suggest_container);
        btnWebSearch = findViewById(R.id.btn_web_search);
        btnSuggestWeb = findViewById(R.id.btn_suggest_web);

        fab.setOnClickListener(v -> startActivity(new Intent(this, AddTextbookActivity.class)));
        btnWebSearch.setOnClickListener(v -> openGoogleBooks(""));
        btnSuggestWeb.setOnClickListener(v -> openGoogleBooks(etSearch.getText().toString().trim()));
    }

    private void setupViewModel() {
        vm = new ViewModelProvider(this).get(TextbookViewModel.class);
        vm.getAllTextbooks().observe(this, list -> {
            if (adapter != null) {
                adapter.updateList(list != null ? list : new ArrayList<>());
                toggleEmpty(list == null || list.isEmpty());
            }
        });
    }

    private void setupRecyclerView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        adapter = new TextbookAdapter(new ArrayList<>(), new TextbookAdapter.OnItemClickListener() {
            @Override public void onItemClick(Textbook t) { showDetails(t); }
            @Override public void onDetailsClick(Textbook t) { showDetails(t); }
        });
        rv.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) adapter.getFilter().filter(s);
            }
            @Override public void afterTextChanged(Editable s) {
                boolean empty = adapter.getItemCount() == 0 && s.length() > 0;
                suggestContainer.setVisibility(empty ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void toggleEmpty(boolean empty) {
        if (empty && etSearch.getText().toString().isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
            suggestContainer.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    private void showDetails(Textbook t) {
        Intent i = new Intent(this, BookDetailActivity.class);
        i.putExtra("book", t);
        startActivity(i);
    }

    private void openGoogleBooks(String query) {
        String url = "https://books.google.com/books?q=" + Uri.encode(query.isEmpty() ? "textbooks" : query);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private boolean onNavItem(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) return true;
        if (id == R.id.nav_add_book) {
            startActivity(new Intent(this, AddTextbookActivity.class));
            return true;
        }
        if (id == R.id.nav_my_listings) {
            startActivity(new Intent(this, MyListingsActivity.class));
            return true;
        }
        if (id == R.id.nav_web_search) {
            openGoogleBooks("");
            return true;
        }
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return false;
    }

    private void setupBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
                else finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
            else drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}