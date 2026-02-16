package com.sagarsweets.in.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sagarsweets.in.Session.CartItem;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Query("DELETE FROM cart_table WHERE productId = :productId AND userId = :userId")
    void deleteItem(int productId, int userId);

    @Query("SELECT * FROM cart_table WHERE userId = :userId")
    LiveData<List<CartItem>> getCartItems(int userId);

    @Query("DELETE FROM cart_table WHERE userId = :userId AND productId = :productId")
    void deleteCartByProductId(int userId,int productId);

    @Query("SELECT * FROM cart_table WHERE productId = :productId AND userId = :userId LIMIT 1")
    CartItem checkItem(int productId, int userId);

    @Query("SELECT SUM(quantity) FROM cart_table WHERE userId = :userId")
    LiveData<Integer> getCartCount(int userId);

    @Query("SELECT quantity FROM cart_table WHERE productId = :productId AND userId = :userId")
    LiveData<Integer> getProductQuantity(int productId, int userId);

    @Query("SELECT IFNULL(SUM(quantity), 0) FROM cart_table WHERE productId = :productId AND userId = :userId")
    int getProductQuantityDirect(int productId, int userId);

    @Query("DELETE FROM cart_table WHERE userId = :userId")
    void clearAllCartByUser(int userId);

}

