package com.example.contactmanager_room.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contactmanager_room.data.ContactDAO;
import com.example.contactmanager_room.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version =1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDAO contactDAO();
    public static final int NUMBER_OF_THREADS = 4;

    //volatile means easy to remove if needed
    private static volatile ContactRoomDatabase INSTANCE;
    public static final ExecutorService dataBaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //instantiating Room database
    public static ContactRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (ContactRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDatabase.class, "contact_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    dataBaseWriteExecutor.execute(()->{
                        ContactDAO contactDAO = INSTANCE.contactDAO();
                        contactDAO.deleteAll();

                        Contact contact = new Contact("Paulo","Teacher");
                        contactDAO.insert(contact);

                        contact = new Contact("James","Spy");
                        contactDAO.insert(contact);

                        contact = new Contact("Bond","Agent");
                        contactDAO.insert(contact);

                        contact = new Contact("Bruce","writer");
                        contactDAO.insert(contact);

                        contact = new Contact("Wong","Janitor");
                        contactDAO.insert(contact);
                    });
                }
            };
}
