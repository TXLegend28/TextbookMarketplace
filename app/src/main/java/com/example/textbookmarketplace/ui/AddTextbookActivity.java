package com.example.textbookmarketplace.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.viewmodel.TextbookViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class AddTextbookActivity extends AppCompatActivity {
    private EditText etTitle, etAuthor, etIsbn, etEdition, etCourse;
    private EditText etCopies, etPrice, etDescription;
    private EditText etSellerName, etSellerEmail, etBankName, etAccountNumber;
    private RadioGroup rgCondition;
    private Button btnSubmit;
    private TextbookViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_textbook);

        setupToolbar();
        initializeViews();
        viewModel = new ViewModelProvider(this).get(TextbookViewModel.class);

        btnSubmit.setOnClickListener(v -> validateAndSubmit());

        // Setup back press handling
        setupBackPressHandling();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Add Textbook Listing");
            }
        }
    }

    private void initializeViews() {
        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etIsbn = findViewById(R.id.et_isbn);
        etEdition = findViewById(R.id.et_edition);
        etCourse = findViewById(R.id.et_course);
        etCopies = findViewById(R.id.et_copies);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        etSellerName = findViewById(R.id.et_seller_name);
        etSellerEmail = findViewById(R.id.et_seller_email);
        etBankName = findViewById(R.id.et_bank_name);
        etAccountNumber = findViewById(R.id.et_account_number);
        rgCondition = findViewById(R.id.rg_condition);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void setupBackPressHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Simply finish the activity when back is pressed
                finish();
            }
        });
    }

    private void validateAndSubmit() {
        // Get values
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String isbn = etIsbn.getText().toString().trim();

        // Validate required fields
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            Snackbar.make(btnSubmit, "Please fill in all required fields (*)",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Validate copies and price
        String copiesStr = etCopies.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (copiesStr.isEmpty() || priceStr.isEmpty()) {
            Snackbar.make(btnSubmit, "Please enter number of copies and price",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        int copies;
        double price;

        try {
            copies = Integer.parseInt(copiesStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Snackbar.make(btnSubmit, "Please enter valid numbers for copies and price",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (copies <= 0) {
            Snackbar.make(btnSubmit, "Number of copies must be at least 1",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (price <= 0) {
            Snackbar.make(btnSubmit, "Price must be greater than 0",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Validate seller info
        String sellerName = etSellerName.getText().toString().trim();
        String sellerEmail = etSellerEmail.getText().toString().trim();
        String bankName = etBankName.getText().toString().trim();
        String accountNumber = etAccountNumber.getText().toString().trim();

        if (sellerName.isEmpty() || sellerEmail.isEmpty() ||
                bankName.isEmpty() || accountNumber.isEmpty()) {
            Snackbar.make(btnSubmit, "Please complete seller and banking information",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(sellerEmail).matches()) {
            Snackbar.make(btnSubmit, "Please enter a valid email address",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for duplicate ISBN
        if (isDuplicateTextbook(isbn)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Duplicate Entry")
                    .setMessage("A textbook with ISBN " + isbn + " already exists. " +
                            "Duplicate entries are not allowed.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        // Get condition
        int selectedId = rgCondition.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String condition = radioButton != null ? radioButton.getText().toString() : "Good";

        // Get optional fields
        String edition = etEdition.getText().toString().trim();
        String course = etCourse.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Create textbook object
        Textbook textbook = new Textbook(
                title, author, isbn, edition, copies, price,
                sellerName, sellerEmail, bankName, accountNumber,
                course, condition, description, ""
        );

        // Save to database
        viewModel.insert(textbook);

        Toast.makeText(this, "Textbook listing submitted successfully!",
                Toast.LENGTH_SHORT).show();

        // Go back to main activity
        finish();
    }

    private boolean isDuplicateTextbook(String isbn) {
        return viewModel.isDuplicateTextbook(isbn);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}