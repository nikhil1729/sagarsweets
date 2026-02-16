package com.sagarsweets.in.RoomDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.sagarsweets.in.Dao.CartDao;
import com.sagarsweets.in.Dao.WishlistDao;
import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.WishlistItem;

@Database(entities = {
        WishlistItem.class,
        CartItem.class
}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract WishlistDao wishlistDao();
    public abstract CartDao cartDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "sagar_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
