package com.example.textbookmarketplace.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.model.Textbook;
import com.squareup.picasso.Picasso;
import java.io.File;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Book Details");
        }

        Textbook t = (Textbook) getIntent().getSerializableExtra("book");
        if (t == null) { finish(); return; }

        ImageView ivCover = findViewById(R.id.iv_cover);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvAuthor = findViewById(R.id.tv_author);
        TextView tvIsbn = findViewById(R.id.tv_isbn);
        TextView tvEdition = findViewById(R.id.tv_edition);
        TextView tvCourse = findViewById(R.id.tv_course);
        TextView tvCondition = findViewById(R.id.tv_condition);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvCopies = findViewById(R.id.tv_copies);
        TextView tvSeller = findViewById(R.id.tv_seller);
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvBank = findViewById(R.id.tv_bank);
        TextView tvAccount = findViewById(R.id.tv_account);
        TextView tvDesc = findViewById(R.id.tv_description);
        Button btnContact = findViewById(R.id.btn_contact);
        Button btnRead = findViewById(R.id.btn_read);
        Button btnOpenDoc = findViewById(R.id.btn_open_doc);

        tvTitle.setText(t.getTitle());
        tvAuthor.setText("Author: " + t.getAuthor());
        tvIsbn.setText("ISBN: " + t.getIsbn());
        tvEdition.setText("Edition: " + t.getEdition());
        tvCourse.setText("Course: " + t.getCourse());
        tvCondition.setText("Condition: " + t.getCondition());
        tvPrice.setText(String.format("Price: $%.2f", t.getPrice()));
        tvCopies.setText("Copies: " + t.getCopies());
        tvSeller.setText("Seller: " + t.getSellerName());
        tvEmail.setText("Email: " + t.getSellerEmail());
        tvBank.setText("Bank: " + t.getBankName());
        tvAccount.setText("Account: " + t.getAccountNumber());
        tvDesc.setText(t.getDescription());

        if (t.getLocalImagePath() != null && !t.getLocalImagePath().isEmpty()) {
            Picasso.get().load(new File(t.getLocalImagePath())).into(ivCover);
        } else {
            ivCover.setImageResource(R.drawable.outline_book_24);
        }

        // PDF button
        if ("pdf".equals(t.getDigitalFileType())) {
            btnRead.setVisibility(View.VISIBLE);
            btnRead.setOnClickListener(v -> {
                Intent i = new Intent(this, PdfViewerActivity.class);
                i.putExtra("path", t.getDigitalFilePath());
                startActivity(i);
            });
        } else {
            btnRead.setVisibility(View.GONE);
        }

        // DOCX button
        if ("docx".equals(t.getDigitalFileType())) {
            btnOpenDoc.setVisibility(View.VISIBLE);
            btnOpenDoc.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse(t.getDigitalFilePath()),
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(i, "Open with"));
            });
        } else {
            btnOpenDoc.setVisibility(View.GONE);
        }

        btnContact.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + t.getSellerEmail() +
                    "?subject=" + Uri.encode("Interested in buying one of the available books")));
            startActivity(Intent.createChooser(intent, "Send email"));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}