package com.example.textbookmarketplace.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.textbookmarketplace.model.Textbook;
import java.util.List;

@Dao
public interface TextbookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Textbook textbook);

    @Update
    void update(Textbook textbook);

    @Delete
    void delete(Textbook textbook);

    @Query("SELECT * FROM textbooks ORDER BY date_added DESC")
    LiveData<List<Textbook>> getAllTextbooks();

    @Query("SELECT * FROM textbooks WHERE title LIKE '%' || :searchQuery || '%' OR seller_name LIKE '%' || :searchQuery || '%'")
    LiveData<List<Textbook>> searchTextbooks(String searchQuery);

    @Query("SELECT * FROM textbooks WHERE isbn = :isbn LIMIT 1")
    Textbook getTextbookByIsbn(String isbn);

    @Query("SELECT * FROM textbooks WHERE seller_email = :email ORDER BY date_added DESC")
    LiveData<List<Textbook>> getTextbooksBySeller(String email);

    @Query("DELETE FROM textbooks")
    void deleteAll();
}