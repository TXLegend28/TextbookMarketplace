package com.example.textbookmarketplace.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.utils.FileHelper;
import com.example.textbookmarketplace.viewmodel.TextbookViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import java.io.File;

public class EditTextbookActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "book_id";
    private TextbookViewModel vm;
    private Textbook current;
    private int bookId;

    private EditText etTitle, etAuthor, etIsbn, etEdition, etCourse, etCopies, etPrice, etDescription;
    private EditText etSellerName, etSellerEmail, etBankName, etAccountNumber;
    private RadioGroup rgCondition;
    private Button btnUpdate, btnPickImage, btnPickFile;
    private ImageView ivPreview;
    private TextView tvFileName;

    private String localImagePath = null;
    private String digitalFilePath = null;
    private String digitalFileType = null;

    private final ActivityResultLauncher<String> imagePicker = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    localImagePath = FileHelper.saveImage(this, uri);
                    Picasso.get().load(new File(localImagePath)).into(ivPreview);
                }
            });

    private final ActivityResultLauncher<String[]> docPicker = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    digitalFilePath = FileHelper.saveDocument(this, uri);
                    digitalFileType = FileHelper.getFileTypeFromPath(digitalFilePath);
                    tvFileName.setText("Attached: " + (digitalFileType != null ? digitalFileType.toUpperCase() : "File"));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_textbook); // re-use layout
        vm = new ViewModelProvider(this).get(TextbookViewModel.class);
        bookId = getIntent().getIntExtra(EXTRA_ID, -1);

        setupToolbar();
        initViews();
        loadBook();

        btnPickImage.setOnClickListener(v -> imagePicker.launch("image/*"));
        btnPickFile.setOnClickListener(v -> docPicker.launch(new String[]{"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}));
        btnUpdate.setOnClickListener(v -> update());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() { finish(); }
        });
    }

    private void setupToolbar() {
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Update Listing");
        }
        btnUpdate = findViewById(R.id.btn_submit);
        btnUpdate.setText("Update Listing");
    }

    private void initViews() {
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
        btnPickImage = findViewById(R.id.btn_pick_image);
        btnPickFile = findViewById(R.id.btn_pick_file);
        ivPreview = findViewById(R.id.iv_preview);
        tvFileName = findViewById(R.id.tv_file_name);
    }

    private void loadBook() {
        current = vm.getById(bookId);
        if (current == null) { finish(); return; }

        etTitle.setText(current.getTitle());
        etAuthor.setText(current.getAuthor());
        etIsbn.setText(current.getIsbn());
        etEdition.setText(current.getEdition());
        etCourse.setText(current.getCourse());
        etCopies.setText(String.valueOf(current.getCopies()));
        etPrice.setText(String.valueOf(current.getPrice()));
        etDescription.setText(current.getDescription());
        etSellerName.setText(current.getSellerName());
        etSellerEmail.setText(current.getSellerEmail());
        etBankName.setText(current.getBankName());
        etAccountNumber.setText(current.getAccountNumber());

        localImagePath = current.getLocalImagePath();
        digitalFilePath = current.getDigitalFilePath();
        digitalFileType = current.getDigitalFileType();

        if (localImagePath != null) Picasso.get().load(new File(localImagePath)).into(ivPreview);
        if (digitalFileType != null) tvFileName.setText("Attached: " + digitalFileType.toUpperCase());

        // select condition radio
        for (int i = 0; i < rgCondition.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rgCondition.getChildAt(i);
            if (rb.getText().toString().equalsIgnoreCase(current.getCondition())) {
                rgCondition.check(rb.getId());
                break;
            }
        }
    }

    private void update() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        if (title.isEmpty() || author.isEmpty()) {
            Snackbar.make(btnUpdate, "Title and Author required", Snackbar.LENGTH_SHORT).show();
            return;
        }

        current.setTitle(title);
        current.setAuthor(author);
        current.setEdition(etEdition.getText().toString().trim());
        current.setCourse(etCourse.getText().toString().trim());
        current.setDescription(etDescription.getText().toString().trim());
        current.setSellerName(etSellerName.getText().toString().trim());
        current.setSellerEmail(etSellerEmail.getText().toString().trim());
        current.setBankName(etBankName.getText().toString().trim());
        current.setAccountNumber(etAccountNumber.getText().toString().trim());
        current.setLocalImagePath(localImagePath);
        current.setDigitalFilePath(digitalFilePath);
        current.setDigitalFileType(digitalFileType);

        try {
            current.setCopies(Integer.parseInt(etCopies.getText().toString().trim()));
            current.setPrice(Double.parseDouble(etPrice.getText().toString().trim()));
        } catch (Exception e) {
            Snackbar.make(btnUpdate, "Invalid number format", Snackbar.LENGTH_SHORT).show();
            return;
        }

        int sel = rgCondition.getCheckedRadioButtonId();
        if (sel != -1) current.setCondition(((RadioButton)findViewById(sel)).getText().toString());

        vm.update(current);
        Toast.makeText(this, "Listing updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}