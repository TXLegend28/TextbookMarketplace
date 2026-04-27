package com.example.textbookmarketplace.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.textbookmarketplace.database.AppDatabase;
import com.example.textbookmarketplace.database.TextbookDao;
import com.example.textbookmarketplace.model.Textbook;
import java.util.List;

public class TextbookViewModel extends AndroidViewModel {
    private TextbookDao dao;
    private LiveData<List<Textbook>> allTextbooks;

    public TextbookViewModel(Application app) {
        super(app);
        dao = AppDatabase.getInstance(app).textbookDao();
        allTextbooks = dao.getAllTextbooks();
    }

    public void insert(Textbook t) { dao.insert(t); }
    public void update(Textbook t) { dao.update(t); }
    public void delete(Textbook t) { dao.delete(t); }

    public LiveData<List<Textbook>> getAllTextbooks() { return allTextbooks; }
    public LiveData<List<Textbook>> search(String q) { return dao.searchTextbooks(q); }
    public LiveData<List<Textbook>> getBySeller(String email) { return dao.getTextbooksBySeller(email); }

    public Textbook getByIsbn(String isbn) { return dao.getTextbookByIsbn(isbn); }
    public Textbook getById(int id) { return dao.getTextbookById(id); }
    public boolean isDuplicate(String isbn) { return dao.getTextbookByIsbn(isbn) != null; }
}