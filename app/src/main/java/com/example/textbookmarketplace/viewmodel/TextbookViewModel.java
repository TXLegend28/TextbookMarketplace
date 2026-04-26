package com.example.textbookmarketplace.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.textbookmarketplace.database.AppDatabase;
import com.example.textbookmarketplace.database.TextbookDao;
import com.example.textbookmarketplace.model.Textbook;
import java.util.List;


public class TextbookViewModel extends AndroidViewModel {
    private TextbookDao textbookDao;
    private LiveData<List<Textbook>> allTextbooks;

    public TextbookViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        textbookDao = database.textbookDao();
        allTextbooks = textbookDao.getAllTextbooks();
    }

    public void insert(Textbook textbook) {
        textbookDao.insert(textbook);
    }

    public void update(Textbook textbook) {
        textbookDao.update(textbook);
    }

    public void delete(Textbook textbook) {
        textbookDao.delete(textbook);
    }

    public LiveData<List<Textbook>> getAllTextbooks() {
        return allTextbooks;
    }

    public LiveData<List<Textbook>> searchTextbooks(String query) {
        return textbookDao.searchTextbooks(query);
    }

    public Textbook getTextbookByIsbn(String isbn) {
        return textbookDao.getTextbookByIsbn(isbn);
    }

    public boolean isDuplicateTextbook(String isbn) {
        return textbookDao.getTextbookByIsbn(isbn) != null;
    }
}