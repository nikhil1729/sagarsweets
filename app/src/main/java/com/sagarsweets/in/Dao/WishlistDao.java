package com.sagarsweets.in.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sagarsweets.in.Session.WishlistItem;

import java.util.List;

@Dao
public interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WishlistItem item);

    @Query("DELETE FROM wishlist WHERE productId = :id")
    void deleteById(int id);

    @Query("SELECT * FROM wishlist")
    List<WishlistItem> getAllItems();

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE productId = :id)")
    boolean isExists(int id);
    @Query("DELETE FROM wishlist")
    void deleteAll();
}

