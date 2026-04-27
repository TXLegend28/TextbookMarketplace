package com.example.textbookmarketplace.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.utils.SettingsManager;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        RadioGroup rgTheme = findViewById(R.id.rg_theme);
        RadioButton rbLight = findViewById(R.id.rb_light);
        RadioButton rbDark = findViewById(R.id.rb_dark);
        RadioButton rbSystem = findViewById(R.id.rb_system);

        int current = SettingsManager.getThemeMode(this);
        if (current == SettingsManager.MODE_LIGHT) rbLight.setChecked(true);
        else if (current == SettingsManager.MODE_DARK) rbDark.setChecked(true);
        else rbSystem.setChecked(true);

        rgTheme.setOnCheckedChangeListener((group, id) -> {
            if (id == R.id.rb_light) SettingsManager.setThemeMode(this, SettingsManager.MODE_LIGHT);
            else if (id == R.id.rb_dark) SettingsManager.setThemeMode(this, SettingsManager.MODE_DARK);
            else SettingsManager.setThemeMode(this, SettingsManager.MODE_SYSTEM);
            Toast.makeText(this, "Theme updated", Toast.LENGTH_SHORT).show();
        });

        TextInputEditText etName = findViewById(R.id.et_def_name);
        TextInputEditText etEmail = findViewById(R.id.et_def_email);
        TextInputEditText etBank = findViewById(R.id.et_def_bank);
        TextInputEditText etAccount = findViewById(R.id.et_def_account);
        Button btnSave = findViewById(R.id.btn_save_profile);

        etName.setText(SettingsManager.getSellerName(this));
        etEmail.setText(SettingsManager.getSellerEmail(this));
        etBank.setText(SettingsManager.getBankName(this));
        etAccount.setText(SettingsManager.getAccountNumber(this));

        btnSave.setOnClickListener(v -> {
            SettingsManager.saveProfile(this,
                    etName.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etBank.getText().toString().trim(),
                    etAccount.getText().toString().trim());
            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_about).setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("Textbook Marketplace v1.1.1.0\n\nA platform for students to buy, sell, and read textbooks.\n\nCSC313 Assignment 2")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}