package com.example.contactmanager_room.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.contactmanager_room.model.Contact;
import com.example.contactmanager_room.util.ContactRoomDatabase;
import java.util.List;

public class ContactRepository {
    private ContactDAO contactDAO;
    private LiveData<List<Contact>> allContact;

    public ContactRepository(Application application) {
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDAO = db.contactDAO();

        allContact = contactDAO.getAllContact();
    }

    public LiveData<List<Contact>> getAllData(){ return allContact; }

    public void insert(Contact contact){
        //Executor service make this process run in the background thread
        ContactRoomDatabase.dataBaseWriteExecutor.execute(()->{
            contactDAO.insert(contact);
        });
    }

    public LiveData<Contact> get(int id){
        return contactDAO.get(id);
    }

    public void update(Contact contact){
        ContactRoomDatabase.dataBaseWriteExecutor.execute(() -> contactDAO.update(contact));
    }

    public void delete(Contact contact){
        ContactRoomDatabase.dataBaseWriteExecutor.execute(() -> contactDAO.delete(contact));
    }
}
